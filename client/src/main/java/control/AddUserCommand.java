package control;

import common.User;
import model.Participant;

public class AddUserCommand implements ICommand {
    private final User user;

    public AddUserCommand(User user) {
        this.user = user;
    }

    @Override
    public void execute(Participant participant) {
        participant.addUser(user);
    }
}
