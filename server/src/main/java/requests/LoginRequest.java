package requests;

import common.User;
import requests.IRequest;
import server.Server;

public class LoginRequest implements IRequest {
    private User user;

    public LoginRequest(User user) {
        this.user = user;
    }

    @Override
    public void react(Server server) {
        // TODO react
    }
}
