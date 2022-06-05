package requests;

import common.Message;
import server.Server;

public class MessageRequest implements IRequest {
    private final int sessionId;
    private final String text;
    public MessageRequest(int sessionId, String text) {
        this.sessionId = sessionId;
        this.text = text;
    }

    @Override
    public void react(Server server) {
        server.addMessageFromSession(sessionId, text);
    }
}
