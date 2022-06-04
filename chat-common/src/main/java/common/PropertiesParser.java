package common;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

class PropertiesParser {
    private final Properties properties;

    public PropertiesParser(String filename) throws IOException {
        properties = new Properties();
        InputStream stream = ClassLoader.getSystemClassLoader().getResourceAsStream(filename);
        properties.load(stream);
    }

    public PropertiesParser(Properties properties) {
        this.properties = properties;
    }

    public String getString(String key) throws NullPointerException {
        return properties.get(key).toString();
    }

    public int getInteger(String key) throws NullPointerException, NumberFormatException {
        return Integer.parseInt(getString(key));
    }

    public boolean getBoolean(String key) throws NullPointerException {
        return Boolean.parseBoolean(getString(key));
    }
}
