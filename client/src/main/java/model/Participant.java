package model;

import common.*;
import control.Control;
import exceptions.ConnectionException;
import utils.*;

import java.io.IOException;
import java.util.List;

public class Participant extends Publisher implements Model<Chat> {
    private User user;
    private Chat chat;
    private Client client;
    private int sessionId;

    private final ParticipantStateManager stateManager;
    private final NotificationsManager notificationsManager;
    private final IRequestBuilder requestBuilder;
    private final EventsManager eventsManager;

    public Participant(
            ParticipantStateManager stateManager,
            NotificationsManager notificationsManager,
            IRequestBuilder requestBuilder,
            IEventsParser eventsParser
    ) {
        this.stateManager = stateManager;
        stateManager.setParticipantState(ParticipantState.WAITS_ADDRESS);
        this.notificationsManager = notificationsManager;
        this.requestBuilder = requestBuilder;
        eventsManager = new EventsManager(eventsParser, new Control(this));
    }

    public void closeCurrentConnection() {
        if (stateManager.getParticipantState() != ParticipantState.CONNECTED) return;
        try {
            client.send(requestBuilder.buildDisconnect());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void makeConnection(Address address, String userName) throws ConnectionException {
        user = new User(userName);
        chat = new Chat();
        try {
            client = new Client(eventsManager);
            client.connect(address);
            client.send(requestBuilder.buildConnect(user));
            client.send(requestBuilder.buildRequestUsers());
        } catch (IOException ioException) {
            throw new ConnectionException("Unable to connect to such host");
        }
    }

    public void receiveMessage(Message message) {
        if (chat == null || user == null) return;
        chat.addMessage(message);
        publishNotify();
    }

    public void sendMessage(String message) {
        if (chat == null || user == null) return;
        Message messageObj = new Message(user, message);
        try {
            client.send(requestBuilder.buildSendMessage(messageObj));
        } catch (IOException exception) {
            notificationsManager.addNotification("Error while sending message");
        }
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
        stateManager.setParticipantState(ParticipantState.CONNECTED);
        requestBuilder.setSessionId(sessionId);
    }

    @Override
    public Chat getData() {
        return chat;
    }
}
