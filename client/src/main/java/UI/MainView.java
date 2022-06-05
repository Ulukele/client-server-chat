package UI;

import common.Chat;
import common.ClientConfiguration;
import common.ISubscriber;
import common.Model;
import control.Control;
import model.NotificationsManager;
import model.Participant;
import model.ParticipantState;
import model.ParticipantStateManager;

import javax.swing.*;
import java.awt.*;

public class MainView extends JFrame implements ISubscriber {

    private final ChatPanel chatPanel;
    private Control control;

    private Model<String> infoManager;

    private final ClientConfiguration configuration;
    private final ChatTextField chatTextField;
    private final SendMessageButton sendMessageButton;
    private final StateLabel stateLabel;


    public MainView(ClientConfiguration configuration) {
        super("Super-common.Chat");
        this.configuration = configuration;

        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        chatPanel = new ChatPanel(configuration);
        chatTextField = new ChatTextField(configuration);
        sendMessageButton = new SendMessageButton(chatTextField);
        stateLabel = new StateLabel();

        setupLayout();
    }

    private void setupLayout() {
        Color appColor = configuration.getAppBgColor();
        Container container = getContentPane();
        container.setBackground(appColor.darker());
        GroupLayout layout = new GroupLayout(container);
        container.setLayout(layout);

        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);

        chatPanel.setMinimumSize(new Dimension(500, 700));
        chatPanel.setupLayout();

        chatTextField.setMinimumSize(new Dimension(400, 30));
        sendMessageButton.setMinimumSize(new Dimension(50, 30));

        stateLabel.setForeground(appColor.brighter());
        stateLabel.setMinimumSize(new Dimension(400, 20));

        layout.setHorizontalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                        .addComponent(chatPanel)
                        .addGroup(layout.createSequentialGroup()
                                .addComponent(chatTextField)
                                .addComponent(sendMessageButton)
                        )
                        .addComponent(stateLabel)
        );
        layout.setVerticalGroup(
                layout.createSequentialGroup()
                        .addComponent(chatPanel)
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(chatTextField)
                                .addComponent(sendMessageButton)
                        )
                        .addComponent(stateLabel)
        );

        pack();
    }

    public void connectControl(Control control) {
        this.control = control;
        sendMessageButton.connectControl(control);
        stateLabel.connectControl(control);

        addWindowListener(new UIWindowListener(control));
    }

    private void addModel(Model<String> stringModel) {
        infoManager = stringModel;
        infoManager.addSubscriber(this);
    }

    public void connectModels(Model<Chat> chatModel, Model<ParticipantState> participantStateModel, Model<String> stringModel) {
        chatPanel.addModel(chatModel);
        stateLabel.addModel(participantStateModel);
        addModel(stringModel);
    }

    @Override
    public void reactOnNotify() {
        if (infoManager == null) return;
        JOptionPane.showMessageDialog(this, infoManager.getData());
    }
}
