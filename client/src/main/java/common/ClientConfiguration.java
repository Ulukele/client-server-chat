package common;

import exceptions.LoadConfigurationException;

import java.awt.*;
import java.io.IOException;

public class ClientConfiguration {
    private Color appMessageColor;
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
        appBgColor = new Color(70, 70, 120);
        appMessageColor = new Color(255, 200, 200);
    }

    private void configureFromProperties() throws LoadConfigurationException {
        try {

            int appBgColorR = propertiesParser.getInteger("APP_BG_COLOR_R");
            int appBgColorG = propertiesParser.getInteger("APP_BG_COLOR_G");
            int appBgColorB = propertiesParser.getInteger("APP_BG_COLOR_B");
            appBgColor = new Color(appBgColorR, appBgColorG, appBgColorB);

            int appMessageColorR = propertiesParser.getInteger("APP_MESSAGE_COLOR_R");
            int appMessageColorG = propertiesParser.getInteger("APP_MESSAGE_COLOR_G");
            int appMessageColorB = propertiesParser.getInteger("APP_MESSAGE_COLOR_B");
            appMessageColor = new Color(appMessageColorR, appMessageColorG, appMessageColorB);
        } catch (NullPointerException | NumberFormatException exception) {
            configureFromDefaults();
            throw new LoadConfigurationException("Unable to parse configuration file, use defaults");
        }
    }

    public Color getAppBgColor() {
        return appBgColor;
    }

    public Color getAppMessageColor() {
        return appMessageColor;
    }
}
