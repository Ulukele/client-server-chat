package utils;

import common.Message;
import common.User;

import java.util.List;

public class XMLEventBuilder implements IEventBuilder {

    private String wrapEvent(String eventName, String content) {
        return "<event name=\"" + eventName + "\">" + content + "</event>";
    }

    @Override
    public byte[] buildAddUser(User user) {
        String event = wrapEvent("user-login", "<name>" + user.getName() + "</name>");
        return event.getBytes();
    }

    @Override
    public byte[] buildRemoveUser(User user) {
        String event = wrapEvent("user-logout", "<name>" + user.getName() + "</name>");
        return event.getBytes();
    }

    @Override
    public byte[] buildAddMessage(Message message) {
        String event = wrapEvent(
                "message",
                "<name>" + message.getSender().getName() + "</name>" +
                "<message>" + message.getText() + "</message>"
                );
        return event.getBytes();
    }

    @Override
    public byte[] buildListUsers(List<User> users) {
        StringBuilder usersList = new StringBuilder();
        for (final User user : users) {
            usersList.append("<user><name>");
            usersList.append(user.getName());
            usersList.append("</name></user>");
        }
        String event = "<success><list-users>" + usersList + "</list-users></success>";
        return event.getBytes();
    }

    @Override
    public byte[] buildSuccess(String message) {
        String event = "<success><message>" + message + "</message></success>";
        return event.getBytes();
    }

    @Override
    public byte[] buildError(String message) {
        String event = "<error><message>" + message + "</message></error>";
        return event.getBytes();
    }

    @Override
    public byte[] buildSuccessLogin(int sessionId) {
        String event = "<success><session>" + sessionId + "</session></success>";
        return event.getBytes();
    }
}
