package model;

import common.Address;
import common.Chat;
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
}
