package model;

import common.Address;

import java.io.*;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Client {
    private final Address address;

    private Socket socket;
    private DataInputStream inputStream;
    private ExecutorService eventLoop;

    public Client(Address address) {
        this.address = address;
        eventLoop = Executors.newSingleThreadExecutor();
    }

    public void connect() throws IOException {
        socket = new Socket(address.getAddress(), address.getPort());
        // TODO create output stream
        inputStream = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
    }

    public void send(String data) throws IOException {
        // TODO sending messages
    }

    public void addListener() {
        eventLoop.submit(new Runnable() {
            @Override
            public void run() {

            }
        });
    }
}
