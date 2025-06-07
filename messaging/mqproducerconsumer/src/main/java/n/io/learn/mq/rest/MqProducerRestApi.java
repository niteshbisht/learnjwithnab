package n.io.learn.mq.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import n.io.learn.mq.pojo.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @PostMapping(path = "/create", consumes = "application/json", produces = "application/json")
    ResponseEntity<Order> dropMessageToQueue(@RequestBody Order payload) throws JsonProcessingException {
        jmsTemplate.convertAndSend(destinationQueueName, objectMapper.writeValueAsString(payload));
        return ResponseEntity.ok(payload);
    }

}
