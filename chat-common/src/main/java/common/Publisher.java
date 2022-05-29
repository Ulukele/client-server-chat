package common;

import java.util.concurrent.CopyOnWriteArrayList;

public class Publisher implements IPublisher {
    private final CopyOnWriteArrayList<ISubscriber> subscribers = new CopyOnWriteArrayList<>();

    @Override
    public void addSubscriber(ISubscriber subscriber) {
        subscribers.add(subscriber);
    }

    @Override
    public boolean removeSubscriber(ISubscriber subscriber) {
        return subscribers.remove(subscriber);
    }

    @Override
    public void publishNotify() {
        for (final ISubscriber subscriber : subscribers) {
            subscriber.reactOnNotify();
        }
    }
}
