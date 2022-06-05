package utils;

import common.Message;
import common.User;

import java.util.List;

public interface IEventBuilder {
    byte[] buildAddUser(User user);
    byte[] buildRemoveUser(User user);
    byte[] buildAddMessage(Message message);
    byte[] buildListUsers(List<User> users);
    byte[] buildSuccess(String message);
    byte[] buildError(String message);
    byte[] buildSuccessLogin(int sessionId);
}
