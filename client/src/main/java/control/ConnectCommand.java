package control;

import common.Address;
import model.Participant;

public class ConnectCommand implements ICommand {
    private final Address address;

    public ConnectCommand(Address address) {
        this.address = address;
    }

    @Override
    public void execute(Participant participant) {
        participant.makeConnection(address);
    }
}
