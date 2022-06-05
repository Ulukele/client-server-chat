package requests;

import common.User;
import server.Server;

public class LoginRequest implements IRequest {
    private final User user;
    private final int sessionId;

    public LoginRequest(int sessionId, User user) {
        this.sessionId = sessionId;
        this.user = user;
    }

    @Override
    public void react(Server server) {
        server.addUser(sessionId, user);
    }
}
