package server;

import requests.IRequest;

public class ResponseExecutor {
    private final Server server;

    public ResponseExecutor(Server server) {
        this.server = server;
    }

    public void execute(IRequest request) {
        request.react(server);
    }
}
