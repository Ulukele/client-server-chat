package common;

import java.util.List;

public class Chat {
    private List<User> users;

    public List<Message> getMessages() {
        return messages;
    }

    private List<Message> messages;

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
        users.remove(user);
    }
}
