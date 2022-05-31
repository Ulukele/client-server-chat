import UI.MainView;
import common.Address;
import common.ClientConfiguration;
import control.Control;
import exceptions.ConnectionException;
import exceptions.LoadConfigurationException;
import model.NotificationsData;
import model.Participant;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        Participant participant = new Participant();
        NotificationsData notificationsData = participant.getNotificationsData();
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
            app.connectModels(participant, notificationsData);
            app.setVisible(true);
        });
    }
}
