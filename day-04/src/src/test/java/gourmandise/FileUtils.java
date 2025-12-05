package gourmandise;

import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

@UtilityClass
public class FileUtils {
    @SneakyThrows
    public static String getInputAsString(String input) {
        return Files.readString(Path.of(uriFrom(input)));
    }

    private static URI uriFrom(String input) throws URISyntaxException {
        return Objects.requireNonNull(urlFrom(input)).toURI();
    }

    private static URL urlFrom(String input) {
        return FileUtils.class.getClassLoader().getResource(input);
    }
}