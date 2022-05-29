package control;

import model.Participant;

public class Control {
    private final Participant participant;

    public Control(Participant participant) {
        this.participant = participant;
    }

    public void execute(ICommand command) {
        command.execute(participant);
    }

    public void executeUnsafe(IUnsafeCommand command) throws Exception {
        command.execute(participant);
    }
}
