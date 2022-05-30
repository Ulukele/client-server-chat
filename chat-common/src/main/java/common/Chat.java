package common;

import java.util.List;

public class Chat {
    private List<User> users;
    private List<Message> messages;

    public void addMessage(Message message) {
        messages.add(message);
    }

    public void addUser(User user) {
        users.add(user);
    }

    public void removeUser(User user) {
        users.remove(user);
    }
}
