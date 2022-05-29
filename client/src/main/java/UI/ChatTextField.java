package UI;

import common.ClientConfiguration;

import javax.swing.*;

public class ChatTextField extends JTextField {

    private final ClientConfiguration configuration;

    public ChatTextField(ClientConfiguration configuration) {
        this.configuration = configuration;
    }
}
