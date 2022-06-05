package server;

import common.Message;
import common.ServerConfiguration;
import common.User;
import utils.IEventBuilder;
import utils.IRequestParser;
import utils.XMLRequestParser;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public class Server {
    private final int port;
    private final boolean logging;
    private final int maxClients;
    private int clientsCount;
    private final IEventBuilder eventBuilder;
    private final Map<Integer, RequestProcessor> requestProcessorMap = new HashMap<>();
    private final Logger logger = Logger.getLogger(Server.class.getName());

    private void logInfo(String message) {
        if (!logging) return;
        logger.info(message);
    }

    public Server(ServerConfiguration configuration, IEventBuilder eventBuilder) {
        this.port = configuration.getPort();
        this.logging = configuration.isLog();
        this.maxClients = configuration.getMaxClients();
        this.clientsCount = 0;
        this.eventBuilder = eventBuilder;
    }

    public void start() throws IOException, ParserConfigurationException {
        ServerSocket serverSocket = new ServerSocket(port);

        logInfo(
                "Start server on " +
                serverSocket.getInetAddress().getHostAddress() +
                ":" +
                serverSocket.getLocalPort()
        );

        while (true) {
            Socket clientSocket = serverSocket.accept();
            logInfo("Accept");
            clientsCount++;
            int sessionId = clientsCount;
            IRequestParser requestParser = new XMLRequestParser();
            requestParser.setSessionId(sessionId);
            RequestProcessor requestProcessor = new RequestProcessor(
                    sessionId,
                    clientSocket,
                    requestParser,
                    new ResponseExecutor(this)
            );
            requestProcessorMap.put(sessionId, requestProcessor);
            requestProcessor.startResponse();
        }
    }

    private void broadcastData(byte[] data) {
        for (final int sessionId : requestProcessorMap.keySet()) {
            sendForSession(sessionId, data);
        }
    }

    private void sendForSession(int sessionId, byte[] data) {
        RequestProcessor requestProcessor = requestProcessorMap.get(sessionId);
        if (requestProcessor == null) return;
        logInfo("Send " +  data.length + " bytes to sessionID="+sessionId);
        try {
            requestProcessor.addEvent(data);
        } catch (InterruptedException e) {
            logInfo(e.getMessage());
        }
    }

    public void AddUser(int sessionId, User user) {
        requestProcessorMap.get(sessionId).setUser(user);
        sendForSession(sessionId, eventBuilder.buildSuccessLogin(sessionId));
        broadcastData(eventBuilder.buildAddUser(user));
        logInfo("Add " + user + " with sessionID=" + sessionId);
    }

    public void RemoveUser(int sessionId) {
        RequestProcessor requestProcessor = requestProcessorMap.get(sessionId);
        User user = requestProcessor.getUser();
        requestProcessorMap.remove(sessionId);
        requestProcessor.closeConnection();
        broadcastData(eventBuilder.buildRemoveUser(user));
        logInfo("Remove " + user + " with sessionID=" + sessionId);
    }

    public void addMessage(Message message) {
        if (message.isEmpty()) return;
        broadcastData(eventBuilder.buildAddMessage(message));
        logInfo("Add message " + message);
    }

    public void addMessageFromSession(int sessionId, String text) {
        RequestProcessor requestProcessor = requestProcessorMap.get(sessionId);
        if (requestProcessor == null) return;
        User user = requestProcessor.getUser();
        addMessage(new Message(user, text));
    }
}
