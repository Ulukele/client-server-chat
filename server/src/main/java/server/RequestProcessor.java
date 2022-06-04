package server;

import exceptions.RequestParsingException;
import requests.IRequest;
import utils.DatagramReader;
import utils.IRequestParser;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class RequestProcessor implements Runnable {

    private final int sessionId;
    private final Socket clientSocket;
    private final IRequestParser requestParser;

    private DataInputStream inputStream;
    private DataOutputStream outputStream;

    public RequestProcessor(int sessionId, Socket clientSocket, IRequestParser requestParser) {
        this.sessionId = sessionId;
        this.clientSocket = clientSocket;
        this.requestParser = requestParser;
    }

    @Override
    public void run() {
        try {
            inputStream = new DataInputStream(new BufferedInputStream(clientSocket.getInputStream()));
            outputStream = new DataOutputStream(clientSocket.getOutputStream());
        } catch (IOException exception) {
            exception.printStackTrace();
            return;
        }
        DatagramReader datagramReader = new DatagramReader();
        datagramReader.setInputStream(inputStream);
        while (true) {
            try {
                if (!datagramReader.haveData()) continue;
                byte[] datagram = datagramReader.readOne();
                IRequest request = requestParser.parseRequest(datagram);
            } catch (IOException | RequestParsingException e) {
                e.printStackTrace();
            }
        }
    }
}
