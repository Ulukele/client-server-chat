package control;

import model.Participant;

public interface IUnsafeCommand {
    void execute(Participant participant) throws Exception;
}
