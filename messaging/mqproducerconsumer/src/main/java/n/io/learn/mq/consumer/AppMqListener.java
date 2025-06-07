package n.io.learn.mq.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import n.io.learn.mq.db.entities.OrderEntity;
import n.io.learn.mq.db.repository.OrderEntityRepository;
import n.io.learn.mq.pojo.Order;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@Slf4j
public class AppMqListener {

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private OrderEntityRepository orderEntityRepository;

    @JmsListener(destination = "${destination.queue.name}")
    public void receiveMessage(String message, @Headers Map<String, Object> messageHeaders) throws JsonProcessingException {
        log.info("Received: message = {}",  message);
        log.info("Headers: {}", messageHeaders);
        final Order order = objectMapper.readValue(message, Order.class);
        if(orderEntityRepository.existsByOrderId(order.getOrderId())) {
            log.warn("Order with orderId={} already exists", order.getOrderId());
            return;
        }
        OrderEntity orderEntity = new OrderEntity();
        BeanUtils.copyProperties(order, orderEntity);
        OrderEntity savedOrder = orderEntityRepository.save(orderEntity);
        log.info("Saved order by rowId={} and orderId={}", savedOrder.getId(), savedOrder.getOrderId());
    }
}
