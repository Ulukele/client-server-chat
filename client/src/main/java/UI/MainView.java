package UI;

import common.ClientConfiguration;
import control.Control;

import javax.swing.*;
import java.awt.*;

public class MainView extends JFrame {

    private final ChatPanel chatPanel;
    private Control control;

    private final ClientConfiguration configuration;
    private final ChatTextField chatTextField;
    private final SendMessageButton sendMessageButton;

    public MainView(ClientConfiguration configuration) {
        super("Super-common.Chat");
        this.configuration = configuration;

        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        chatPanel = new ChatPanel(configuration);
        chatTextField = new ChatTextField(configuration);
        sendMessageButton = new SendMessageButton(chatTextField);

        setupLayout();
    }

    private void setupLayout() {
        Container container = getContentPane();
        container.setBackground(configuration.getAppBgColor().darker());
        GroupLayout layout = new GroupLayout(container);
        container.setLayout(layout);

        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);

        chatPanel.setMinimumSize(new Dimension(500, 700));
        chatPanel.setupLayout();

        chatTextField.setMinimumSize(new Dimension(400, 30));
        sendMessageButton.setMinimumSize(new Dimension(50, 30));

        layout.setHorizontalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                        .addComponent(chatPanel)
                        .addGroup(layout.createSequentialGroup()
                                .addComponent(chatTextField)
                                .addComponent(sendMessageButton)
                        )
        );
        layout.setVerticalGroup(
                layout.createSequentialGroup()
                        .addComponent(chatPanel)
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(chatTextField)
                                .addComponent(sendMessageButton)
                        )
        );

        pack();
    }

    public void connectControl(Control control) {
        this.control = control;
        sendMessageButton.connectControl(control);
    }

    public void connectModels() {

    }
}
