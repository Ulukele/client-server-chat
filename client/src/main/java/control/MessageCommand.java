package control;

import common.Message;
import model.Participant;

public class MessageCommand implements ICommand {

    private final Message message;

    public MessageCommand(Message message) {
        this.message = message;
    }

    @Override
    public void execute(Participant participant) {
        participant.receiveMessage(message);
    }
}
