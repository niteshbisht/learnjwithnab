package n.io.learn.mq;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
public class MqApplication {

	public static void main(String[] args) {
		SpringApplication.run(MqApplication.class, args);
	}

}
