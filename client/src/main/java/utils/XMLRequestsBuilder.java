package utils;

import common.Message;
import common.User;

public class XMLRequestsBuilder implements IRequestBuilder {

    private int sessionId;

    @Override
    public void setSessionId(int sessionId) {
        this.sessionId = sessionId;
    }

    private String wrapCommand(String commandName, String content) {
        return "<command name=\"" + commandName + "\">" + content + "</command>";
    }

    @Override
    public byte[] buildConnect(User user) {
        String request = wrapCommand("login", "<name>" + user.getName() + "</name>");
        return request.getBytes();
    }

    @Override
    public byte[] buildDisconnect() {
        String request = wrapCommand("logout", "<session>" + sessionId + "</session>");
        return request.getBytes();
    }

    @Override
    public byte[] buildRequestUsers() {
        String request = wrapCommand("list", "<session>" + sessionId + "</session>");
        return request.getBytes();
    }

    @Override
    public byte[] buildSendMessage(Message message) {
        String request = wrapCommand(
                "message",
                "<message>" + message.getText() + "</message>" +
                        "<session>" + sessionId + "</session>"
        );
        return request.getBytes();
    }
}
