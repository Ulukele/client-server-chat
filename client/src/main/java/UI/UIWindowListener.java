package UI;

import control.Control;
import control.DisconnectCommand;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

public class UIWindowListener extends WindowAdapter implements WindowListener {
    private final Control control;

    UIWindowListener(Control control) {
        this.control = control;
    }

    @Override
    public void windowClosing(WindowEvent e) {
        control.execute(new DisconnectCommand());
    }
}
