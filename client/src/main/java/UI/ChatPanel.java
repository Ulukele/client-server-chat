package UI;

import common.*;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.util.List;

public class ChatPanel extends JScrollPane implements ISubscriber {

    private final ClientConfiguration configuration;

    private Model<Chat> chatModel;
    private final JTextPane chatPane;
    private final StyledDocument chatDocument;

    private SimpleAttributeSet senderSet;
    private SimpleAttributeSet textSet;

    public ChatPanel(ClientConfiguration configuration) {
        this.configuration = configuration;
        this.chatPane = new JTextPane();
        chatPane.setEditable(false);
        this.chatDocument = chatPane.getStyledDocument();
        setViewportView(chatPane);
    }

    public void setupLayout() {
        setBackground(configuration.getAppBgColor());
        chatPane.setBackground(configuration.getAppBgColor());

        senderSet = new SimpleAttributeSet();
        StyleConstants.setFontSize(senderSet, 17);
        StyleConstants.setForeground(senderSet, configuration.getAppMessageColor().brighter());
        textSet = new SimpleAttributeSet();
        StyleConstants.setFontSize(textSet, 17);
        StyleConstants.setForeground(textSet, configuration.getAppMessageColor());
    }

    public void addModel(Model<Chat> chatModel) {
        this.chatModel = chatModel;
        chatModel.addSubscriber(this);
    }

    public void addMessage(Message message) {
        try {
            chatDocument.insertString(chatDocument.getLength(), message.getSender().getName() + "\n", senderSet);
            chatDocument.insertString(chatDocument.getLength(), message.getText() + "\n\n", textSet);
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void reactOnNotify() {
        Chat chat = chatModel.getData();
        List<Message> messages = chat.getMessages();
        if (messages.size() <= 0) return;
        addMessage(messages.get(messages.size() - 1));
        updateUI();
    }
}
