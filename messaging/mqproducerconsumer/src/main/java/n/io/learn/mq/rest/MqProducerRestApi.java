package n.io.learn.mq.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import n.io.learn.mq.pojo.Order;
import n.io.learn.mq.service.OrderCreationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class MqProducerRestApi {

    @Value("${destination.queue.name}")
    private String destinationQueueName;

    @Autowired
    private final JmsTemplate jmsTemplate;

    @Autowired
    private final ObjectMapper objectMapper;

    @Autowired
    private final OrderCreationService orderCreationService;

    @PostMapping(path = "/create", consumes = "application/json", produces = "application/json")
    ResponseEntity<Order> create(@RequestBody Order payload) throws JsonProcessingException {
        try{
            orderCreationService.createOrder(payload);
            return ResponseEntity.ok(payload);
        } catch (Exception e) {
            log.error("Failed to create order", e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping(path = "/create-async", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<Order> createOrderAsync(@RequestBody Order payload) {
        try{
            jmsTemplate.convertAndSend(destinationQueueName, objectMapper.writeValueAsString(payload));
            return ResponseEntity.ok(payload);
        } catch (Exception e){
            log.error("Error delivering message to jms", e);
            return ResponseEntity.internalServerError().build();
        }
    }

}
