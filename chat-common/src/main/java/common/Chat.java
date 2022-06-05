package common;

import java.util.ArrayList;
import java.util.List;

public class Chat {
    private List<User> users = new ArrayList<>();

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
        this.users = users;
    }

    public List<User> getUsers() {
        return users;
    }

    public void removeUser(User user) {
        users.removeIf(oneUser -> oneUser.getName().equals(user.getName()));
    }
}
