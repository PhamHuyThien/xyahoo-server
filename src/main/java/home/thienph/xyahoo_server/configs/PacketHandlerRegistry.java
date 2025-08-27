package home.thienph.xyahoo_server.configs;

import home.thienph.xyahoo_server.anotations.HasRole;
import home.thienph.xyahoo_server.anotations.PacketMapping;
import home.thienph.xyahoo_server.data.base.Packet;
import home.thienph.xyahoo_server.data.exceptions.UnauthorizedException;
import home.thienph.xyahoo_server.data.users.UserContext;
import home.thienph.xyahoo_server.managers.GameManager;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import jakarta.annotation.PostConstruct;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Configuration
@DependsOn("gameManager")
public class PacketHandlerRegistry {

    private final Map<String, Method> handlerPacketMap = new HashMap<>();
    private final Map<String, Object> beanMap = new HashMap<>();
    private final Map<String, String[]> handlerRoleMap = new HashMap<>();

    @Autowired
    private ApplicationContext context;
    @Autowired
    GameManager gameManager;
    @Autowired
    private ExceptionHandlerRegistry exceptionHandlerRegistry;

    @PostConstruct
    public void init() {
        Map<String, Object> beans = context.getBeansWithAnnotation(Controller.class);
        for (Object bean : beans.values()) {
            for (Method method : bean.getClass().getDeclaredMethods()) {
                if (method.isAnnotationPresent(PacketMapping.class)) {
                    PacketMapping packetMapping = method.getAnnotation(PacketMapping.class);
                    int commandId = packetMapping.commandId();
                    int typeId = packetMapping.typeId();
                    handlerPacketMap.put(buildKey(commandId, typeId), method);
                    beanMap.put(buildKey(commandId, typeId), bean);

                    if (method.isAnnotationPresent(HasRole.class)) {
                        HasRole roleMapping = method.getAnnotation(HasRole.class);
                        String[] roles = roleMapping.value();
                        handlerRoleMap.put(buildKey(commandId, typeId), roles);
                    }
                }
            }
        }
    }

    @SneakyThrows
    public void handle(ChannelHandlerContext ctx, Packet packet) {
        int commandId = packet.getCommandId();
        int typeId = packet.getTypeId();

        String key = buildKey(commandId, typeId);
        Method method = handlerPacketMap.get(key);
        Object bean = beanMap.get(key);
        if (method == null) {
            key = buildKey(commandId, -999);
            method = handlerPacketMap.get(key);
            bean = beanMap.get(key);
        }
        if (method != null && bean != null) {
            try {
                checkRole(ctx.channel(), key);
                method.invoke(bean, ctx.channel(), packet);
            } catch (InvocationTargetException e) {
                exceptionHandlerRegistry.handleException(ctx.channel(), packet, e.getCause());
            }
        } else {
            log.warn("PacketMapping / No handler found for commandId={}, typeId={}", commandId, typeId);
        }
    }


    private String buildKey(int commandId, int typeId) {
        return commandId + ":" + typeId;
    }

    private void checkRole(Channel channel, String key) {
        String[] roles = handlerRoleMap.get(key);
        if (roles != null && roles.length > 0) {
            UserContext userContext = gameManager.getUserContext(channel);
            if (userContext != null && userContext.isLogin() && userContext.getUser() != null) {
                for (String role : roles) {
                    if (userContext.getUser().getRole().equals(role)) return;
                }
            }
            throw new UnauthorizedException();
        }
    }
}
