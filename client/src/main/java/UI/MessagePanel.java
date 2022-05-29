package UI;

import javax.swing.*;
import java.awt.*;

public class MessagePanel extends JPanel {
    private final JLabel text;
    private final JLabel user;

    public MessagePanel(String text, String user) {
        this.text = new JLabel(text);
        this.user = new JLabel(user);


        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        add(this.user);
        add(this.text);
    }

    public JLabel getText() {
        return text;
    }

    public JLabel getUser() {
        return user;
    }
}
