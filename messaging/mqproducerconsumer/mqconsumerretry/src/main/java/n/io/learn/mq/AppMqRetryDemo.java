package n.io.learn.mq;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.retry.annotation.EnableRetry;

@EnableRetry
@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
public class AppMqRetryDemo {

    public static void main(String[] args) {
        SpringApplication.run(AppMqRetryDemo.class, args);
    }
}