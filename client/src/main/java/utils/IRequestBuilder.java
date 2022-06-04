package utils;

import common.Message;
import common.User;

public interface IRequestBuilder {
    void setSessionId(int sessionId);
    byte[] buildConnect(User user);
    byte[] buildDisconnect();
    byte[] buildRequestUsers();
    byte[] buildSendMessage(Message message);
}
