package n.io.learn.mq.processor;

import lombok.extern.slf4j.Slf4j;
import n.io.learn.mq.error.AppRtimeException;
import org.springframework.stereotype.Service;

import java.util.Random;

@Slf4j
@Service
public class MessageProcessor {
    private static final Random random = new Random();

    public void processMessage(String message) {
        log.info("Processing message: {}", message);
        if (true) {
            throw new AppRtimeException("Failed to process message: " + message);
        }
        log.info("Message processed successfully: {}", message);
    }
}
