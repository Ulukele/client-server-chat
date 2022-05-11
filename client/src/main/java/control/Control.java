package control;

import model.Participant;

public class Control {
    void execute(ICommand command, Participant participant) {
        command.execute(participant);
    }
}
