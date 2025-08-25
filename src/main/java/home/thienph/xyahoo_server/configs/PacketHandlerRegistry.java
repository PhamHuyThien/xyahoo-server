package home.thienph.xyahoo_server.configs;

import home.thienph.xyahoo_server.anotations.ControllerAdvice;
import home.thienph.xyahoo_server.anotations.ExceptionHandler;
import home.thienph.xyahoo_server.anotations.PacketMapping;
import home.thienph.xyahoo_server.data.base.Packet;
import home.thienph.xyahoo_server.data.config.HandlerMethod;
import io.netty.channel.ChannelHandlerContext;
import jakarta.annotation.PostConstruct;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Controller;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Configuration
public class PacketHandlerRegistry {

    private final Map<String, Method> handlerMap = new HashMap<>();
    private final Map<String, Object> beanMap = new HashMap<>();
    private final Map<Class<?>, HandlerMethod> exceptionHandlers = new HashMap<>();

    @Autowired
    private ApplicationContext context;

    @PostConstruct
    public void init() {
        Map<String, Object> beans = context.getBeansWithAnnotation(Controller.class);
        for (Object bean : beans.values()) {
            for (Method method : bean.getClass().getDeclaredMethods()) {
                if (method.isAnnotationPresent(PacketMapping.class)) {
                    PacketMapping packetMapping = method.getAnnotation(PacketMapping.class);
                    int commandId = packetMapping.commandId();
                    int typeId = packetMapping.typeId();
                    handlerMap.put(buildKey(commandId, typeId), method);
                    beanMap.put(buildKey(commandId, typeId), bean);
                }
            }
        }
        for (Object bean : context.getBeansWithAnnotation(ControllerAdvice.class).values()) {
            Class<?> targetClass = org.springframework.aop.support.AopUtils.getTargetClass(bean);
            for (Method m : targetClass.getDeclaredMethods()) {
                if (m.isAnnotationPresent(ExceptionHandler.class)) {
                    Class<? extends Throwable>[] exTypes = m.getAnnotation(ExceptionHandler.class).value();
                    for (Class<? extends Throwable> exType : exTypes) {
                        exceptionHandlers.put(exType, new HandlerMethod(bean, m));
                    }
                }
            }
        }
    }

    @SneakyThrows
    public void handle(ChannelHandlerContext ctx, Packet packet) {
        int commandId = packet.getCommandId();
        int typeId = packet.getTypeId();

        Method method = handlerMap.get(buildKey(commandId, typeId));
        Object bean = beanMap.get(buildKey(commandId, typeId));
        if (method == null) {
            method = handlerMap.get(buildKey(commandId, -999));
            bean = beanMap.get(buildKey(commandId, -999));
        }
        if (method != null) {
            try {
                method.invoke(bean, ctx.channel(), packet);
            } catch (InvocationTargetException e) {
                handleException(ctx, packet, e.getCause());
            }
        } else {
            log.warn("No handler found for commandId={}, typeId={}", commandId, typeId);
        }
    }

    private void handleException(ChannelHandlerContext ctx, Packet packet, Throwable ex) {
        for (Class<?> clazz = ex.getClass(); clazz != null; clazz = clazz.getSuperclass()) {
            HandlerMethod handler = exceptionHandlers.get(clazz);
            if (handler == null) handler = exceptionHandlers.get(Throwable.class);
            if (handler != null) {
                try {
                    handler.getMethod().invoke(handler.getBean(), ctx.channel(), packet, ex);
                    return;
                } catch (Exception e) {
                    log.error("Error invoking exception handler", e);
                }
            }
        }
        ex.printStackTrace();
    }


    private String buildKey(int commandId, int typeId) {
        return commandId + ":" + typeId;
    }
}
