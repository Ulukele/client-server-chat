package server;

import common.User;
import exceptions.RequestParsingException;
import requests.IRequest;
import utils.DatagramReader;
import utils.IRequestParser;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Queue;
import java.util.concurrent.SynchronousQueue;

public class RequestProcessor {

    private User user;

    private final int sessionId;
    private final Socket clientSocket;
    private final IRequestParser requestParser;
    private final Queue<byte[]> eventsQueue = new SynchronousQueue<>();
    private final ResponseExecutor responseExecutor;

    private Thread requestThread;

    public RequestProcessor(
            int sessionId,
            Socket clientSocket,
            IRequestParser requestParser,
            ResponseExecutor responseExecutor
    ) {
        this.sessionId = sessionId;
        this.clientSocket = clientSocket;
        this.requestParser = requestParser;
        this.responseExecutor = responseExecutor;
    }

    public void startResponse() {
        DataInputStream inputStream;
        DataOutputStream outputStream;
        try {
            inputStream = new DataInputStream(new BufferedInputStream(clientSocket.getInputStream()));
            outputStream = new DataOutputStream(clientSocket.getOutputStream());
        } catch (IOException exception) {
            exception.printStackTrace();
            return;
        }
        DatagramReader datagramReader = new DatagramReader();
        datagramReader.setInputStream(inputStream);

        requestThread = new Thread(() -> {
            while (true) {
                try {
                    if (!eventsQueue.isEmpty()) {
                        byte[] event = eventsQueue.poll();
                        outputStream.write(event);
                    }
                    if (!datagramReader.haveData()) continue;
                    byte[] datagram = datagramReader.readOne();
                    IRequest request = requestParser.parseRequest(datagram);
                    responseExecutor.execute(request);
                } catch (IOException | RequestParsingException e) {
                    e.printStackTrace();
                }
            }
        });
        requestThread.start();
    }

    public void addEvent(byte[] event) {
        eventsQueue.add(event);
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }
}
