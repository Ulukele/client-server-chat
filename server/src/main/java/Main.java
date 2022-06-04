import common.ServerConfiguration;
import exceptions.LoadConfigurationException;
import server.Server;
import utils.IEventBuilder;
import utils.XMLEventBuilder;

public class Main {
    public static void main(String[] args) {
        ServerConfiguration serverConfiguration = new ServerConfiguration("config.properties");
        try {
            serverConfiguration.configure();
        } catch (LoadConfigurationException e) {
            e.printStackTrace();
        }
        IEventBuilder eventBuilder = new XMLEventBuilder();
        Server server = new Server(serverConfiguration, eventBuilder);
        try {
            server.start();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
}
