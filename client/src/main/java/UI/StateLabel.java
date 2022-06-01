package UI;

import common.Address;
import common.ISubscriber;
import common.Model;
import control.ConnectCommand;
import control.Control;
import control.ICommand;
import model.ParticipantState;

import javax.swing.*;

public class StateLabel extends JLabel implements ISubscriber {
    private Model<ParticipantState> participantStateModel;
    private Control control;

    public StateLabel() {
        super();
    }

    public void addModel(Model<ParticipantState> participantStateModel) {
        this.participantStateModel = participantStateModel;
        participantStateModel.addSubscriber(this);
        reactOnNotify();
    }

    public void connectControl(Control control) {
        this.control = control;
    }

    private void requestAddress() throws Exception {
        if (control == null) return;
        Address address = null;
        while (address == null) {
            try {
                String addressStr = JOptionPane.showInputDialog("Enter IP address:");
                int port = Integer.parseInt(JOptionPane.showInputDialog("Enter port number:"));
                address = new Address(addressStr, port);
            } catch (NumberFormatException exception) {
                JOptionPane.showMessageDialog(this, "Specify correct number");
            }
        }

        String userName = JOptionPane.showInputDialog("Enter your name:");

        ConnectCommand command = new ConnectCommand(address, userName);
        control.executeUnsafe(command);
    }

    @Override
    public void reactOnNotify() {
        if (participantStateModel == null) return;
        ParticipantState state = participantStateModel.getData();
        if (state == ParticipantState.CONNECTED) {
            setText("Connected");
        } else if (state == ParticipantState.WAITS_ADDRESS) {
            setText("Awaiting host address to connect with");
            while (true) {
                try {
                    requestAddress();
                    break;
                } catch (Exception exception) {
                    JOptionPane.showMessageDialog(this, "Unable to connect");
                }
            }
        }
    }
}
