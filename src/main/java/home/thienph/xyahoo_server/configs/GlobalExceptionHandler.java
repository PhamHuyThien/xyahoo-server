package home.thienph.xyahoo_server.configs;

import home.thienph.xyahoo_server.anotations.ControllerAdvice;
import home.thienph.xyahoo_server.anotations.ExceptionHandler;
import home.thienph.xyahoo_server.data.base.Packet;
import home.thienph.xyahoo_server.data.users.UserContext;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Throwable.class)
    public void handleAllExceptions(UserContext userContext, Packet packet, Throwable ex) {
        ex.printStackTrace();
    }

}
