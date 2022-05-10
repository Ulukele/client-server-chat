package control;

public class Control {
    void execute(ICommand command) {
        command.execute();
    }
}
