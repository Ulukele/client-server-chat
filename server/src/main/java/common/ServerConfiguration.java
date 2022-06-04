package common;

import exceptions.LoadConfigurationException;

import java.awt.*;
import java.io.IOException;

public class ServerConfiguration {
    private int maxClients;
    private boolean log;
    private int port;

    private final String filename;
    private PropertiesParser propertiesParser;

    public ServerConfiguration(String filename) {
        this.filename = filename;
    }

    public void configure() throws LoadConfigurationException {
        // Configure parameters
        try {
            propertiesParser = new PropertiesParser(filename);
            configureFromProperties();
        } catch (IOException ioException) {
            configureFromDefaults();
            throw new LoadConfigurationException(
                    "Unable to load configuration file " + '\"'+ filename +'\"' + ", use defaults"
            );
        }
    }

    private void configureFromDefaults() {
        maxClients = 2;
        log = true;
        port = 8080;
    }

    private void configureFromProperties() throws LoadConfigurationException {
        try {
            maxClients = propertiesParser.getInteger("MAX_CLIENTS");
            log = propertiesParser.getBoolean("LOG");
            port = propertiesParser.getInteger("PORT");
        } catch (NullPointerException | NumberFormatException exception) {
            configureFromDefaults();
            throw new LoadConfigurationException("Unable to parse configuration file, use defaults");
        }
    }

    public int getMaxClients() {
        return maxClients;
    }

    public boolean isLog() {
        return log;
    }

    public int getPort() {
        return port;
    }
}
