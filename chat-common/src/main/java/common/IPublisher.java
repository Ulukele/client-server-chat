package common;

public interface IPublisher {
    void addSubscriber(ISubscriber subscriber);
    boolean removeSubscriber(ISubscriber subscriber);
    void publishNotify();
}
