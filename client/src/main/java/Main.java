import UI.MainView;
import common.ClientConfiguration;
import control.Control;
import exceptions.LoadConfigurationException;
import model.NotificationsManager;
import model.Participant;
import model.ParticipantStateManager;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {

        ParticipantStateManager participantStateManager = new ParticipantStateManager();
        NotificationsManager notificationsManager = new NotificationsManager();
        Participant participant = new Participant(
                participantStateManager,
                notificationsManager
        );
        Control control = new Control(participant);

        ClientConfiguration configuration = new ClientConfiguration("config.properties");
        try {
            configuration.configure();
        } catch (LoadConfigurationException e) {
            e.printStackTrace();
        }

        MainView app = new MainView(configuration);
        SwingUtilities.invokeLater(() -> {
            app.setVisible(true);
            app.connectControl(control);
            app.connectModels(participant, participantStateManager, notificationsManager);
        });
    }
}
