package n.io.learn.mq.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.jms.JMSException;
import jakarta.jms.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.jms.core.MessagePostProcessor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/jms")
public class MqProducerRestApi {

    @Value("${destination.queue.name}")
    private String destinationQueueName;

    @Autowired
    private JmsTemplate jmsTemplate;

    @Autowired
    ObjectMapper objectMapper;

    @PostMapping("/drop")
    ResponseEntity<String> dropMessageToQueue(@RequestBody String payload) {
        jmsTemplate.convertAndSend(destinationQueueName, payload);
        return ResponseEntity.ok("Sent Message to Queue");
    }

    @PostMapping("/drop/v2")
    ResponseEntity<String> dropMessageWithHeaders(@RequestBody String payload) throws JsonProcessingException {
        Map<String, String> msgMap = new HashMap<>();
        msgMap.put("value", payload);
        jmsTemplate.convertAndSend(destinationQueueName, objectMapper.writeValueAsString(msgMap), new MessagePostProcessor() {
            @Override
            public Message postProcessMessage(Message message) throws JMSException {
                message.setJMSMessageID(UUID.randomUUID().toString());
                message.setIntProperty("TEST", 1);
                return message;
            }
        });
        return ResponseEntity.ok("Created");
    }

}
