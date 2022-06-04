package requests;

import server.Server;

public class LogoutRequest implements IRequest {
    private int sessionId;

    public LogoutRequest(int sessionId) {
        this.sessionId = sessionId;
    }

    @Override
    public void react(Server server) {
        server.RemoveUser(sessionId);
    }
}
