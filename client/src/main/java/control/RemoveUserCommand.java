package control;

import common.User;
import model.Participant;

public class RemoveUserCommand implements ICommand {
    private final User user;

    public RemoveUserCommand(User user) {
        this.user = user;
    }

    @Override
    public void execute(Participant participant) {
        participant.removeUser(user); // TODO remove equal by name
    }
}
