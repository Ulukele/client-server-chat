package control;

import model.Participant;

public class SendMessageCommand implements ICommand {

    private final String messageText;

    public SendMessageCommand(String messageText) {
        this.messageText = messageText;
    }

    @Override
    public void execute(Participant participant) {
        participant.sendMessage(messageText);
    }
}
