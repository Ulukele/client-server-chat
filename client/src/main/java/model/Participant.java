package model;

import common.Address;
import common.Chat;
import common.User;

public class Participant {
    private User user;
    private Chat chat;
    private Client client;

    public Participant() {}

    public void makeConnection(Address address) {
        client = new Client(address);
    }
}
