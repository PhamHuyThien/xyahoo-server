package home.thienph.xyahoo_server.configs;

import jakarta.annotation.PostConstruct;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppContext {
    @Getter
    private static ApplicationContext context;

    @Autowired
    ApplicationContext applicationContext;

    @PostConstruct
    public void init() {
        context = applicationContext;
    }

}
