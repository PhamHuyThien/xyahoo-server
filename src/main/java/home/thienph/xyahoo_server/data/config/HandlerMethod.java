package home.thienph.xyahoo_server.data.config;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.lang.reflect.Method;

@Data
@AllArgsConstructor
public class HandlerMethod {
    private final Object bean;
    private final Method method;

    public Object getBean() {
        return bean;
    }

    public Method getMethod() {
        return method;
    }

    public Object invoke(Object... args) throws Exception {
        return method.invoke(bean, args);
    }
}
