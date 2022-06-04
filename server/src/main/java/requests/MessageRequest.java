package requests;

import common.Message;
import requests.IRequest;
import server.Server;

public class MessageRequest implements IRequest {
    private final Message message;
    public MessageRequest(Message message) {
        this.message = message;
    }

    @Override
    public void react(Server server) {
        // TODO react
    }
}
