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
import java.util.*;
import java.util.logging.Logger;

public class Server {
    private final int port;
    private final boolean logging;
    private final int maxClients;
    private final int maxMessages;
    private int lastSessionId;
    private final IEventBuilder eventBuilder;
    private final Map<Integer, RequestProcessor> requestProcessorMap = new HashMap<>();
    private final Logger logger = Logger.getLogger(Server.class.getName());

    private final Queue<Message> messageQueue;

    private void logInfo(String message) {
        if (!logging) return;
        logger.info(message);
    }

    public Server(ServerConfiguration configuration, IEventBuilder eventBuilder) {
        this.port = configuration.getPort();
        this.logging = configuration.isLog();
        this.maxClients = configuration.getMaxClients();
        this.maxMessages = configuration.getMaxMessages();
        this.lastSessionId = 0;
        this.eventBuilder = eventBuilder;
        messageQueue = new ArrayDeque<>(maxMessages);

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                checkProcessors();
            }
        }, 0, 6000);
    }

    public void start() throws IOException, ParserConfigurationException {
        ServerSocket serverSocket = new ServerSocket(port, maxClients);

        logInfo(
                "Start server on " +
                serverSocket.getInetAddress().getHostAddress() +
                ":" +
                serverSocket.getLocalPort()
        );

        while (true) {
            Socket clientSocket = serverSocket.accept();
            logInfo("Accept");
            int sessionId = lastSessionId;
            lastSessionId++;
            IRequestParser requestParser = new XMLRequestParser();
            requestParser.setSessionId(sessionId);
            RequestProcessor requestProcessor = new RequestProcessor(
                    sessionId,
                    clientSocket,
                    requestParser,
                    new ResponseExecutor(this),
                    600L
            );
            requestProcessorMap.put(sessionId, requestProcessor);
            requestProcessor.startResponse();
        }
    }

    private void checkProcessors() {
        for (final int sessionId : requestProcessorMap.keySet()) {
            requestProcessorMap.get(sessionId).checkAlive();
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

    public void sendUsersList(int sessionId) {
        List<User> userList = new ArrayList<>();
        for (final int session : requestProcessorMap.keySet()) {
            userList.add(requestProcessorMap.get(session).getUser());
        }
        byte[] event = eventBuilder.buildListUsers(userList);
        sendForSession(sessionId, event);
        logInfo("Send users list to sessionID=" + sessionId);
    }

    public void addUser(int sessionId, User user) {
        requestProcessorMap.get(sessionId).setUser(user);
        sendForSession(sessionId, eventBuilder.buildSuccessLogin(sessionId));
        for (final Message message : messageQueue) {
            sendForSession(sessionId, eventBuilder.buildAddMessage(message));
        }
        broadcastData(eventBuilder.buildAddUser(user));
        logInfo("Add " + user + " with sessionID=" + sessionId);
    }

    public void removeUser(int sessionId) {
        RequestProcessor requestProcessor = requestProcessorMap.get(sessionId);
        User user = requestProcessor.getUser();
        requestProcessorMap.remove(sessionId);
        requestProcessor.closeConnection();
        broadcastData(eventBuilder.buildRemoveUser(user));
        logInfo("Remove " + user + " with sessionID=" + sessionId);
    }

    public void addMessage(Message message) {
        if (message.isEmpty()) return;
        if (messageQueue.size() >= maxMessages) {
            messageQueue.poll();
        }
        messageQueue.add(message);
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
