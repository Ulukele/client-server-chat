package UI;

import common.ClientConfiguration;
import control.Control;
import control.SendMessageCommand;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SendMessageButton extends JButton {
    private Control control;

    public SendMessageButton(JTextField textField) {
        super("Send");
        addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (control == null) return;
                control.execute(new SendMessageCommand(textField.getText()));
            }
        });
    }

    public void connectControl(Control control) {
        this.control = control;
    }
}
