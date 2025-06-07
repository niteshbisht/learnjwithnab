package n.io.learn.mq.error;

public class AppRtimeException extends RuntimeException {
    public AppRtimeException(String message) {
        super(message);
    }
}
