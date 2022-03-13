package tr.com.nihatalim.yt.downloader.exception;

public class KafkaProduceFailedException extends RuntimeException {
    public KafkaProduceFailedException(String message) {
        super(message);
    }
}
