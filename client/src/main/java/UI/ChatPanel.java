package UI;

import common.*;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayDeque;
import java.util.List;
import java.util.Queue;

public class ChatPanel extends JPanel implements ISubscriber {

    private final ClientConfiguration configuration;

//    private final Queue<MessagePanel> messagePanels = new ArrayDeque<>();
    private final int maxMessages;
    private Model<Chat> chatModel;

    public ChatPanel(ClientConfiguration configuration) {
        this.configuration = configuration;
        this.maxMessages = configuration.getMaxMessagesToDisplay();
    }

    public void setupLayout() {
        setBackground(configuration.getAppBgColor());
        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
    }

    private void addModel(Model<Chat> chatModel) {
        this.chatModel = chatModel;
    }

    public void addMessage(MessagePanel messagePanel) {
        Color appColor = configuration.getAppBgColor();
        messagePanel.setBackground(appColor.brighter());
        messagePanel.getUser().setForeground(appColor.darker().darker());
        messagePanel.getText().setForeground(appColor.darker());

        add(messagePanel);
//        if (messagePanels.size() >= maxMessages) {
//            MessagePanel lastMessage = messagePanels.poll();
//            remove(lastMessage);
//        }
//        messagePanels.add(messagePanel);
    }

    @Override
    public void reactOnNotify() {
        Chat chat = chatModel.getData();
        removeAll();
        List<Message> messages = chat.getMessages().subList(0, maxMessages);

        for (final Message message : messages) {
            addMessage(new MessagePanel(message.getText(), message.getSender().getName()));
        }
    }
}
