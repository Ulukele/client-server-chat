package utils;

import control.ICommand;

import java.io.IOException;

public interface IEventsParser {
    ICommand parseEvent(byte[] eventData) throws IOException;
}
