package control;

import common.User;
import model.Participant;

import java.util.List;

public class AddUsersCommand implements ICommand {
    private User user;
    private List<User> users;

    public AddUsersCommand(User user) {
        this.user = user;
    }

    public AddUsersCommand(List<User> users) {
        this.users = users;
    }

    @Override
    public void execute(Participant participant) {
        if (users != null) {
            participant.addUsers(users);
        }
        if (user != null) {
            participant.addUser(user);
        }
    }
}
