package home.thienph.xyahoo_server.configs;

import home.thienph.xyahoo_server.anotations.CommandMapping;
import home.thienph.xyahoo_server.anotations.UIComponentController;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import jakarta.annotation.PostConstruct;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Configuration
public class UIComponentHandlerRegistry {

    private final Map<Integer, Method> handlerMap = new HashMap<>();
    private final Map<Integer, Object> beanMap = new HashMap<>();

    @Autowired
    private ApplicationContext context;
    @Autowired
    private ExceptionHandlerRegistry exceptionHandlerRegistry;

    @PostConstruct
    public void init() {
        Map<String, Object> beans = context.getBeansWithAnnotation(UIComponentController.class);
        for (Object bean : beans.values()) {
            for (Method method : bean.getClass().getDeclaredMethods()) {
                if (method.isAnnotationPresent(CommandMapping.class)) {
                    CommandMapping packetMapping = method.getAnnotation(CommandMapping.class);
                    int commandId = packetMapping.commandId();
                    handlerMap.put(commandId, method);
                    beanMap.put(commandId, bean);
                }
            }
        }
    }

    @SneakyThrows
    public void handle(Channel ctx, Integer commandId, ByteBuf payload) {
        Method method = handlerMap.get(commandId);
        Object bean = beanMap.get(commandId);
        if (method != null) {
            try {
                method.invoke(bean, ctx, payload);
            } catch (InvocationTargetException e) {
                exceptionHandlerRegistry.handleException(ctx, null, e.getCause());
            }
        } else {
            log.warn("CommandMapping / No handler found for commandId={}", commandId);
        }
    }
}
