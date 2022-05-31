package control;

import model.Participant;

public class ErrorCommand implements ICommand {
    private final String errorMessage;

    public ErrorCommand(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    @Override
    public void execute(Participant participant) {
        participant.notifyClient(errorMessage);
    }
}
