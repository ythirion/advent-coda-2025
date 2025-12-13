package gift.logging;

import java.time.format.DateTimeFormatter;

import static java.lang.System.out;
import static java.time.LocalTime.now;

public class Console implements Logger {
    private static String time() {
        return now().format(
                DateTimeFormatter.ofPattern("HH:mm:ss")
        );
    }

    @Override
    public void log(String message) {
        out.println("[" + time() + "] " + message);
    }

    @Override
    public void error(Exception e) {
        log("🚨 ERREUR CRITIQUE 🚨");
        log("❌ " + e.getMessage());
    }
}