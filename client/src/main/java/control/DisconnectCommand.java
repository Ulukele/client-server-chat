package control;

import model.Participant;

public class DisconnectCommand implements ICommand {
    @Override
    public void execute(Participant participant) {
        participant.closeCurrentConnection();
    }
}
