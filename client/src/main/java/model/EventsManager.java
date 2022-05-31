package model;

import control.Control;
import control.ICommand;
import exceptions.DataParsingException;
import utils.IEventsParser;

import java.io.IOException;

public class EventsManager {
    private final IEventsParser eventsParser;
    private final Control control;

    public EventsManager(IEventsParser eventsParser, Control control) {
        this.eventsParser = eventsParser;
        this.control = control;
    }


    public void handle(byte[] event) {
        try{
            ICommand command = eventsParser.parseEvent(event);
            control.execute(command);
        } catch (DataParsingException exception) {
            exception.printStackTrace(); // TODO Logging
        }
    }
}
