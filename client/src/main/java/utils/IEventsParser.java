package utils;

import control.ICommand;
import exceptions.DataParsingException;

public interface IEventsParser {
    ICommand parseEvent(byte[] eventData) throws DataParsingException;
}
