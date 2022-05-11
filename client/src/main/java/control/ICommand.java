package control;

import model.Participant;

public interface ICommand {
    void execute(Participant participant);
}
