package io.github.adr.eadlsync.util.common;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

public class BuildInfo {

    private final String version;
    private final String authors;
    private final String year;


    public BuildInfo() {
        this("/build.properties");
    }

    private BuildInfo(String path) {
        Properties properties = new Properties();

        try (InputStream stream = BuildInfo.class.getResourceAsStream(path)) {
            if (stream != null) {
                try (InputStreamReader reader = new InputStreamReader(stream, StandardCharsets.UTF_8)) {
                    properties.load(reader);
                }
            }
        } catch (IOException ignored) {
            // nothing to do -> default already set
        }

        version = properties.getProperty("version");
        authors = properties.getProperty("authors", "");
        year = properties.getProperty("year", "");
    }

    public String getVersion() {
        return version;
    }

    public String getAuthors() {
        return authors;
    }

    public String getYear() {
        return year;
    }

}
