package utils;

import control.ICommand;

import java.io.DataInputStream;
import java.io.IOException;

public class DatagramReader {

    private DataInputStream inputStream;

    public DatagramReader() {
    }

    synchronized public void setInputStream(DataInputStream inputStream) {
        this.inputStream = inputStream;
    }

    synchronized public byte[] readOne() throws IOException {
        if (inputStream == null) throw new IOException("No input stream");
        int datagramLength = inputStream.readInt();
        byte[] datagram = new byte[datagramLength];
        int readLength = 0;

        while (readLength < datagramLength) {
            int readSuccessfully = inputStream.read(datagram, readLength, datagramLength - readLength);
            readLength += readSuccessfully;
        }
        return datagram;
    }
}
