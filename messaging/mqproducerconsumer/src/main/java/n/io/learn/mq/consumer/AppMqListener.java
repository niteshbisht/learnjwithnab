package n.io.learn.mq.consumer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@Slf4j
public class AppMqListener {

    @JmsListener(destination = "${destination.queue.name}")
    public void receiveMessage(String message, @Headers Map<String, Object> messageHeaders) {
        log.info("Received: message = {}",  message);
        log.info("Headers: {}", messageHeaders);
    }
}
