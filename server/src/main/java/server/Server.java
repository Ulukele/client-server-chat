package server;

import utils.XMLRequestParser;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
    private final int port;
    private final ExecutorService processorsPool;

    public Server(int port) {
        this.port = port;
        processorsPool = Executors.newFixedThreadPool(1);
    }

    public void start() throws IOException, ParserConfigurationException {
        ServerSocket serverSocket = new ServerSocket(port);
        while (true) {
            Socket clientSocket = serverSocket.accept();
            processorsPool.submit(new RequestProcessor(0, clientSocket, new XMLRequestParser()));
        }
    }
}
