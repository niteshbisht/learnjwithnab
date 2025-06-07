package n.io.learn.mq.consumer;

import jakarta.jms.JMSException;
import jakarta.jms.Message;
import jakarta.jms.TextMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import n.io.learn.mq.error.AppRtimeException;
import n.io.learn.mq.processor.MessageProcessor;
import org.apache.activemq.command.ActiveMQTextMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class AppMqConsumerWithRetry {

    @Value("${destination.dlq.name}")
    private String destinationDlqName;
    private final MessageProcessor messageProcessor;
    private final JmsTemplate jmsTemplate;
    @Retryable(
            retryFor = {AppRtimeException.class, RuntimeException.class},
            maxAttempts = 3,
            backoff = @Backoff(delay = 5000) // Fixed 5-second delay
    )
    @JmsListener(destination = "${destination.queue.name}")
    public void receiveMessage(Message messageJms, @Headers Map<String, Object> messageHeaders) throws JMSException {
        log.info("Received: message = {}",  messageJms);
        log.info("Headers: {}", messageHeaders);
        try{
            messageProcessor.processMessage(((TextMessage)messageJms).getText());
        } catch (Exception e) {
            log.error("Failed to process message: {}", messageJms, e);
            int retryCount = 0;
            retryCount =  ((ActiveMQTextMessage) messageJms).getRedeliveryCounter();
            if(retryCount> 2) {
                // send to dead letter queue
                sendToDeadLetterQueue(messageJms);
                return;
            }
            throw e;
        }
    }

    private void sendToDeadLetterQueue(Message message) {
        jmsTemplate.convertAndSend(destinationDlqName, message, (message1)-> {
            log.info("sent to dead letter queue: {}", message1);
            return message1;
        });
    }
}
