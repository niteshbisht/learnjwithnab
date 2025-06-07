package n.io.learn.mq.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(basePackages = "n.io.learn.mq.db.repository")
@EntityScan(basePackages = "n.io.learn.mq.db.entities")
public class AppJpaConfiguration {}
