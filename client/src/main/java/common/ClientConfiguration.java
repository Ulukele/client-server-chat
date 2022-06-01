package common;

import exceptions.LoadConfigurationException;

import java.awt.*;
import java.io.IOException;

public class ClientConfiguration {
    private int maxMessagesToDisplay;
    private Color appBgColor;

    private final String filename;
    private PropertiesParser propertiesParser;

    public ClientConfiguration(String filename) {
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
        maxMessagesToDisplay = 10;
        appBgColor = new Color(70, 70, 120);
    }

    private void configureFromProperties() throws LoadConfigurationException {
        try {
            maxMessagesToDisplay = propertiesParser.getInteger("MAX_MESSAGES_TO_DISPLAY");

            int appBgColorR = propertiesParser.getInteger("APP_BG_COLOR_R");
            int appBgColorG = propertiesParser.getInteger("APP_BG_COLOR_G");
            int appBgColorB = propertiesParser.getInteger("APP_BG_COLOR_B");
            appBgColor = new Color(appBgColorR, appBgColorG, appBgColorB);
        } catch (NullPointerException | NumberFormatException exception) {
            configureFromDefaults();
            throw new LoadConfigurationException("Unable to parse configuration file, use defaults");
        }
    }

    public int getMaxMessagesToDisplay() {
        return maxMessagesToDisplay;
    }

    public Color getAppBgColor() {
        return appBgColor;
    }
}
