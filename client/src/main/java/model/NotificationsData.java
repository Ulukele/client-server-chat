package model;

import common.Model;
import common.Publisher;

import java.util.ArrayList;
import java.util.List;

public class NotificationsData extends Publisher implements Model<String> {
    private final List<String> notifications = new ArrayList<>();

    public void addNotification(String messageToDisplay) {
        notifications.add(messageToDisplay);
        publishNotify();
    }

    @Override
    public String getData() {
        if (notifications.size() <= 0) return null;
        return notifications.get(notifications.size() - 1);
    }
}
