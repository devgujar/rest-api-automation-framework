package api.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigManager {
    private static final Properties properties = new Properties();

    static {
        try (InputStream input = ConfigManager.class.getClassLoader().getResourceAsStream("baseConfig.properties")) {
            if (input != null) {
                properties.load(input);
            } else {
                throw new RuntimeException("baseConfig.properties not found in resources");
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to load baseConfig.properties", e);
        }
    }

    public static String getProperty(String key) {
        return properties.getProperty(key);
    }

    public static String get(String key) {
        return properties.getProperty(key);
    }

    public static String get(String key, String defaultValue) {
        return get(key)==null ? defaultValue : get(key);
    }
}

