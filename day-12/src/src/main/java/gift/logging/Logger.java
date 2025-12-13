package gift.logging;

public interface Logger {
    void log(String message);

    void error(Exception e);
}