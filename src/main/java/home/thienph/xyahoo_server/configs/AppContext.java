package home.thienph.xyahoo_server.configs;

import home.thienph.xyahoo_server.managers.GameManager;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
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

    public static GameManager getGameManager() {
        return context.getBean(GameManager.class);
    }

}
