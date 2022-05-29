import UI.MainView;
import common.ClientConfiguration;
import control.Control;
import exceptions.LoadConfigurationException;
import model.Participant;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        Participant participant = new Participant();
        Control control = new Control(participant);

        ClientConfiguration configuration = new ClientConfiguration("config.properties");
        try {
            configuration.configure();
        } catch (LoadConfigurationException e) {
            e.printStackTrace();
        }

        MainView app = new MainView(configuration);
        SwingUtilities.invokeLater(() -> {
            app.connectControl(control);
            app.connectModels();
            app.setVisible(true);
        });
    }
}
