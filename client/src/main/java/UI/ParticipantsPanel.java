package UI;

import common.*;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.util.List;

public class ParticipantsPanel extends JScrollPane implements ISubscriber {
    private Model<Chat> chatModel;
    private final JTextPane usersPane;
    private final StyledDocument usersDocument;
    private final ClientConfiguration configuration;

    private SimpleAttributeSet attributeSet;

    public ParticipantsPanel(ClientConfiguration configuration) {
        this.configuration = configuration;
        this.usersPane = new JTextPane();
        usersPane.setEditable(false);
        this.usersDocument = usersPane.getStyledDocument();
        setViewportView(usersPane);
    }

    public void setupLayout() {
        setBackground(configuration.getAppBgColor());
        usersPane.setBackground(configuration.getAppBgColor());

        attributeSet = new SimpleAttributeSet();
        StyleConstants.setFontSize(attributeSet, 17);
        StyleConstants.setForeground(attributeSet, configuration.getAppMessageColor().brighter());
    }

    public void addModel(Model<Chat> chatModel) {
        this.chatModel = chatModel;
        chatModel.addSubscriber(this);
    }

    private void addUser(User user) {
        try {
            usersDocument.insertString(usersDocument.getLength(), user.getName() + "\n", attributeSet);
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void reactOnNotify() {
        if (chatModel == null) return;
        usersPane.setText("");
        Chat chat = chatModel.getData();
        List<User> users = chat.getUsers();
        for (final User user : users) {
            addUser(user);
        }
    }
}
