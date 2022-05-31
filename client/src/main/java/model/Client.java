package model;

import common.Address;

import java.io.*;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Client {

    private Socket socket;
    private DataInputStream inputStream;

    private ExecutorService eventLoop;
    private final EventsManager eventsManager;
    private final DatagramReader datagramReader;

    public Client(EventsManager eventsManager) {
        this.eventsManager = eventsManager;
        eventLoop = Executors.newSingleThreadExecutor();
        datagramReader = new DatagramReader();

        // Processing events from server
        eventLoop.submit(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        byte[] datagram = datagramReader.readOne();
                        eventsManager.handle(datagram);
                    } catch (IOException ioException) {
                        ioException.printStackTrace(); // TODO Logging
                    }
                }
            }
        });
    }

    public void connect(Address address) throws IOException {
        socket = new Socket(address.getAddress(), address.getPort());
        // TODO create output stream
        inputStream = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
        datagramReader.setInputStream(inputStream);
    }

    public void send(String data) throws IOException {
        // TODO sending messages
    }
}
