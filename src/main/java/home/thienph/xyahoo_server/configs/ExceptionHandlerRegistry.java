package home.thienph.xyahoo_server.configs;

import home.thienph.xyahoo_server.anotations.ControllerAdvice;
import home.thienph.xyahoo_server.anotations.ExceptionHandler;
import home.thienph.xyahoo_server.data.base.Packet;
import home.thienph.xyahoo_server.data.config.HandlerMethod;
import home.thienph.xyahoo_server.data.users.UserContext;
import io.netty.channel.Channel;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Configuration
public class ExceptionHandlerRegistry {
    private final Map<Class<?>, HandlerMethod> exceptionHandlers = new HashMap<>();
    @Autowired
    private ApplicationContext context;

    @PostConstruct
    public void init() {
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

    public void handleException(UserContext userContext, Packet packet, Throwable ex) {
        for (Class<?> clazz = ex.getClass(); clazz != null; clazz = clazz.getSuperclass()) {
            HandlerMethod handler = exceptionHandlers.get(clazz);
            if (handler == null) handler = exceptionHandlers.get(Throwable.class);
            if (handler != null) {
                try {
                    handler.getMethod().invoke(handler.getBean(), userContext, packet, ex);
                    return;
                } catch (Exception e) {
                    log.error("ControllerAdvice / Error invoking exception handler", e);
                }
            }
        }
        ex.printStackTrace();
    }
}
