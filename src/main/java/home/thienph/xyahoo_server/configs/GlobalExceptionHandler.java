package home.thienph.xyahoo_server.configs;

import home.thienph.xyahoo_server.anotations.ControllerAdvice;
import home.thienph.xyahoo_server.anotations.ExceptionHandler;
import home.thienph.xyahoo_server.data.base.Packet;
import home.thienph.xyahoo_server.data.exceptions.UnauthorizedException;
import home.thienph.xyahoo_server.data.users.UserContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration(proxyBeanMethods = false)
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Throwable.class)
    public void handleAllExceptions(UserContext userContext, Packet packet, Throwable ex) {
        log.error("handleAllExceptions packet commandId={}, typeId={}", packet.getCommandId(), packet.getTypeId(), ex);
    }

    @ExceptionHandler(UnauthorizedException.class)
    public void handleUnauthorized(UserContext userContext, Packet packet, Throwable ex) {
        log.error("UnauthorizedException packet commandId={}, typeId={}", packet.getCommandId(), packet.getTypeId(), ex);
    }
}
