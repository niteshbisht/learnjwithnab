package io.temporal.samples.springboot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.actuate.autoconfigure.security.servlet.ManagementWebSecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(
        exclude = {SecurityAutoConfiguration.class,
        ManagementWebSecurityAutoConfiguration.class}
)
public class TemporalWorker {
  public static void main(String[] args) {
    SpringApplication.run(TemporalWorker.class, args).start();
  }
}
