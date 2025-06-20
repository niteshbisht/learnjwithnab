package n.io.learn.mq.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import n.io.learn.mq.pojo.Order;
import n.io.learn.mq.service.OrderCreationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@Slf4j
public class OrdersQueueListener {

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private OrderCreationService orderCreationService;

    @JmsListener(destination = "${destination.queue.name}")
    public void receiveMessage(String message, @Headers Map<String, Object> messageHeaders) throws JsonProcessingException {
        try{
            log.info("Received: message = {}",  message);
            log.info("Headers: {}", messageHeaders);
            final Order order = objectMapper.readValue(message, Order.class);
            orderCreationService.createOrder(order);
        } catch (Exception e) {
            log.error("Failed to create error sending to dead letter queue", e);
        }
    }
}
