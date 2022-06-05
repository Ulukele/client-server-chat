package common;

import java.util.ArrayList;
import java.util.List;

public class Chat {
    private final List<User> users = new ArrayList<>();

    private final List<Message> messages = new ArrayList<>();

    public List<Message> getMessages() {
        return messages;
    }

    public void addMessage(Message message) {
        messages.add(message);
    }

    public void addUser(User user) {
        users.add(user);
    }

    public void addUsers(List<User> users) {
        this.users.addAll(users);
    }

    public void removeUser(User user) {
        for (final User oneUser : users) {
            if (oneUser.getName().equals(user.getName())) users.remove(user);
        }
    }
}
