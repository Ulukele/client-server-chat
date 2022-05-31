package control;

import common.Address;
import exceptions.ConnectionException;
import model.Participant;

import java.io.IOException;

public class ConnectCommand implements IUnsafeCommand {
    private final Address address;
    private final String userName;

    public ConnectCommand(Address address, String userName) {
        this.address = address;
        this.userName = userName;
    }

    @Override
    public void execute(Participant participant) throws ConnectionException {
        participant.makeConnection(address, userName);
    }
}
