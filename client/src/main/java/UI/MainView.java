package UI;

import javax.swing.*;

public class MainView extends JFrame {

    private ChatPanel chatPanel;

    public MainView() {
        super("Super-Chat");


        chatPanel = new ChatPanel();
    }
}
