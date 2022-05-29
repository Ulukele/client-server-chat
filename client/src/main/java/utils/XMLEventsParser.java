package utils;

import control.ICommand;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.ByteArrayInputStream;
import java.io.IOException;

public class XMLEventsParser implements IEventsParser {

    private final DocumentBuilder documentBuilder;

    public XMLEventsParser() throws ParserConfigurationException {
        documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
    }

    @Override
    public ICommand parseEvent(byte[] eventData) throws IOException {
        try {
         Document document = documentBuilder.parse(new ByteArrayInputStream(eventData));


        } catch (SAXException exception) {
            throw new IOException();
        }
        return null;
    }
}
