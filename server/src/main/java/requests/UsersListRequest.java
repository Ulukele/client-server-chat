package requests;

import server.Server;

public class UsersListRequest implements IRequest {
    private final int sessionId;

    public UsersListRequest(int sessionId) {
        this.sessionId = sessionId;
    }

    @Override
    public void react(Server server) {
        server.sendUsersList(sessionId);
    }
}
