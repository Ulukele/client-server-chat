package control;

import common.Message;
import model.Participant;

public class MessageCommand implements ICommand {

    private final String message;

    public MessageCommand(String message) {
        this.message = message;
    }

    @Override
    public void execute(Participant participant) {
        participant.addMessage(message);
    }
}
