package control;

import model.Participant;

public class SuccessCommand implements ICommand {

    private int sessionId;

    public SuccessCommand(int sessionId) {
        this.sessionId = sessionId;
    }

    @Override
    public void execute(Participant participant) {
        participant.setSessionId(sessionId);
    }
}
