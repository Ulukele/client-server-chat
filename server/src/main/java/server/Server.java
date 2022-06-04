package server;

import common.Message;
import common.ServerConfiguration;
import common.User;
import utils.IEventBuilder;
import utils.XMLRequestParser;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class Server {
    private final int port;
    private final boolean logging;
    private final int maxClients;
    private int clientsCount;
    private final IEventBuilder eventBuilder;
    private final Map<Integer, RequestProcessor> requestProcessorMap = new HashMap<>();

    public Server(ServerConfiguration configuration, IEventBuilder eventBuilder) {
        this.port = configuration.getPort();
        this.logging = configuration.isLog();
        this.maxClients = configuration.getMaxClients();
        this.clientsCount = 0;
        this.eventBuilder = eventBuilder;
    }

    public void start() throws IOException, ParserConfigurationException {
        ServerSocket serverSocket = new ServerSocket(port);
        while (true) {
            Socket clientSocket = serverSocket.accept();
            clientsCount++;
            int sessionId = 0;
            RequestProcessor requestProcessor = new RequestProcessor(
                    sessionId,
                    clientSocket,
                    new XMLRequestParser(),
                    new ResponseExecutor(this)
            );
            requestProcessor.startResponse();
            requestProcessorMap.put(sessionId, requestProcessor);
        }
    }

    private void broadcastData(byte[] data) {
//        TODO broadcast
    }

    public void AddUser(int sessionId, User user) {
        requestProcessorMap.get(sessionId).setUser(user);
        broadcastData(eventBuilder.buildAddUser(user));
    }

    public void RemoveUser(int sessionId) {
        User user = requestProcessorMap.get(sessionId).getUser();
        broadcastData(eventBuilder.buildRemoveUser(user));
    }

    public void addMessage(Message message) {
        broadcastData(eventBuilder.buildAddMessage(message));
    }
}
