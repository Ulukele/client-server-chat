package utils;

import common.Address;

import java.io.*;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Client {

    private Socket socket;
    private DataInputStream inputStream;
    protected DataOutputStream outputStream;

    private ExecutorService eventLoop;
    private final EventsManager eventsManager;
    private final DatagramReader datagramReader;

    public Client(EventsManager eventsManager) {
        this.eventsManager = eventsManager;
        eventLoop = Executors.newSingleThreadExecutor();
        datagramReader = new DatagramReader();
    }

    public void connect(Address address) throws IOException {
        socket = new Socket(address.getAddress(), address.getPort());
        inputStream = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
        datagramReader.setInputStream(inputStream);
        outputStream = new DataOutputStream(socket.getOutputStream());

        // Processing events from server
        eventLoop.shutdown();
        eventLoop.submit(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        if (!datagramReader.haveData()) continue;
                        byte[] datagram = datagramReader.readOne();
                        eventsManager.handle(datagram);
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    }
                }
            }
        });
    }

    public void send(byte[] data) throws IOException {
        int dataLen = data.length;

        outputStream.writeInt(dataLen);
        outputStream.write(data);
    }
}
