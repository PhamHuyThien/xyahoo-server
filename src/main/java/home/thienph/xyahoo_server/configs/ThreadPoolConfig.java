package home.thienph.xyahoo_server.configs;

import lombok.Getter;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Getter
@Configuration
public class ThreadPoolConfig   {
    ExecutorService threadPool10 = Executors.newFixedThreadPool(10);
}
