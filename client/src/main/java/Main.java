import UI.MainView;
import common.ClientConfiguration;
import control.Control;
import exceptions.LoadConfigurationException;
import model.NotificationsManager;
import model.Participant;
import model.ParticipantStateManager;
import utils.IEventsParser;
import utils.XMLEventsParser;
import utils.XMLRequestsBuilder;

import javax.swing.*;
import javax.xml.parsers.ParserConfigurationException;

public class Main {
    public static void main(String[] args) {
        ParticipantStateManager participantStateManager = new ParticipantStateManager();
        NotificationsManager notificationsManager = new NotificationsManager();
        XMLRequestsBuilder requestsBuilder = new XMLRequestsBuilder();
        IEventsParser eventsParser;
        try {
            eventsParser = new XMLEventsParser();
        } catch (ParserConfigurationException exception) {
            exception.printStackTrace();
            return;
        }
        Participant participant = new Participant(
                participantStateManager,
                notificationsManager,
                requestsBuilder,
                eventsParser
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
