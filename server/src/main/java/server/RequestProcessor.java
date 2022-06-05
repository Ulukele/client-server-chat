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
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.SynchronousQueue;

public class RequestProcessor {

    private User user;

    private final int sessionId;
    private final Socket clientSocket;
    private final IRequestParser requestParser;
    private final SynchronousQueue<byte[]> eventsQueue = new SynchronousQueue<>();
    private final ResponseExecutor responseExecutor;

    private DataOutputStream outputStream;

    private final ExecutorService pool = Executors.newFixedThreadPool(2);

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
        try {
            inputStream = new DataInputStream(new BufferedInputStream(clientSocket.getInputStream()));
            outputStream = new DataOutputStream(clientSocket.getOutputStream());
        } catch (IOException exception) {
            exception.printStackTrace();
            return;
        }
        DatagramReader datagramReader = new DatagramReader();
        datagramReader.setInputStream(inputStream);

        pool.submit(() -> {
            while (true) {
                try {
                    if (!datagramReader.haveData()) continue;
                    byte[] datagram = datagramReader.readOne();
                    IRequest request = requestParser.parseRequest(datagram);
                    responseExecutor.execute(request);
                } catch (IOException e) {
                    closeConnection();
                } catch (RequestParsingException e) {
                    e.printStackTrace();
                }
            }
        });

        pool.submit(() -> {
           while (true) {
               try {
                   byte[] event = eventsQueue.take();
                   send(event);
               } catch (IOException e) {
                   e.printStackTrace();
               } catch (InterruptedException e) {
                    break;
               }
           }
        });
    }

    public void closeConnection() {
        pool.shutdown();
    }

    private void send(byte[] data) throws IOException {
        outputStream.writeInt(data.length);
        outputStream.write(data);
    }

    public void addEvent(byte[] event) throws InterruptedException {
        eventsQueue.put(event);
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }
}
