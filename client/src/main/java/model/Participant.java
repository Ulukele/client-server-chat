package model;

import common.Address;
import common.Chat;
import common.Message;
import common.User;

import java.io.IOException;

public class Participant {
    private User user;
    private Chat chat;
    private Client client;

    public Participant() {
    }

    public void makeConnection(Address address, String userName) throws IOException {
        client = new Client(address);
        client.connect();

        chat = new Chat();
        user = new User(userName);
    }

    public void addMessage(String message) {
        if (chat == null || user == null) return;
        Message messageObj = new Message(user, message);
        chat.addMessage(messageObj);
    }

    public void addUser(User newUser) {
        if (chat == null || user == null) return;
        chat.addUser(newUser);
    }

    public void removeUser(User userToRemove) {
        if (chat == null || user == null) return;
        chat.removeUser(userToRemove);
    }
}
