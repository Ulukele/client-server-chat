package model;

import common.*;
import control.Control;
import exceptions.ConnectionException;
import utils.Client;
import utils.EventsManager;
import utils.XMLEventsParser;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.List;

public class Participant extends Publisher implements Model<Chat> {
    private User user;
    private Chat chat;
    private Client client;
    private int sessionId;

    private final ParticipantStateManager stateManager;
    private final NotificationsManager notificationsManager;

    public Participant(ParticipantStateManager stateManager, NotificationsManager notificationsManager) {
        this.stateManager = stateManager;
        stateManager.setParticipantState(ParticipantState.WAITS_ADDRESS);
        this.notificationsManager = notificationsManager;
    }

    public void makeConnection(Address address, String userName) throws ConnectionException {
        EventsManager eventsManager;
        try {
            eventsManager = new EventsManager(new XMLEventsParser(), new Control(this));
        } catch (ParserConfigurationException exception) {
            throw new ConnectionException("Unable to configure XML parser");
        }

        try {
            client = new Client(eventsManager);
            client.connect(address);
        } catch (IOException ioException) {
            throw new ConnectionException("Unable to connect to such host");
        }

        chat = new Chat();
        user = new User(userName);
    }

    public void receiveMessage(Message message) {
        if (chat == null || user == null) return;
        chat.addMessage(message);
        publishNotify();
    }

    public void addSelfMessage(String message) {
        if (chat == null || user == null) return;
        Message messageObj = new Message(user, message);
        chat.addMessage(messageObj);
        publishNotify();
    }

    public void addUser(User newUser) {
        if (chat == null || user == null) return;
        chat.addUser(newUser);
        publishNotify();
    }

    public void addUsers(List<User> users) {
        if (chat == null || user == null) return;
        chat.addUsers(users);
        publishNotify();
    }

    public void removeUser(User userToRemove) {
        if (chat == null || user == null) return;
        chat.removeUser(userToRemove);
        publishNotify();
    }

    public void notifyClient(String messageToDisplay) {
        notificationsManager.addNotification(messageToDisplay);
    }

    public void setSessionId(int sessionId) {
        this.sessionId = sessionId;
        publishNotify();
    }

    public NotificationsManager getNotificationsData() {
        return notificationsManager;
    }

    @Override
    public Chat getData() {
        return chat;
    }
}
