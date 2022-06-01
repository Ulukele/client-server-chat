package utils;

import common.Message;
import common.User;
import control.*;
import exceptions.DataParsingException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class XMLEventsParser implements IEventsParser {

    private final DocumentBuilder documentBuilder;

    public XMLEventsParser() throws ParserConfigurationException {
        documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
    }

    public User parseUserFromEvent(Element eventElement) {
        NodeList namesList = eventElement.getElementsByTagName("name");
        if (namesList.getLength() != 1) return null;

        Node nameNode = namesList.item(0);
        if (nameNode.getNodeType() != Node.ELEMENT_NODE) return null;

        Element nameElement = (Element) nameNode;

        String name = nameElement.getTextContent();
        return new User(name);
    }

    private Message parseMessageFromEvent(Element eventElement) {
        NodeList messagesList =  eventElement.getElementsByTagName("message");
        NodeList namesList = eventElement.getElementsByTagName("name");
        if (messagesList.getLength() != 1 || namesList.getLength() != 1) return null;

        Node messageNode = messagesList.item(0);
        Node nameNode = namesList.item(0);
        if (messageNode.getNodeType() != Node.ELEMENT_NODE || nameNode.getNodeType() != Node.ELEMENT_NODE) return null;

        Element messageElement = (Element) messageNode;
        Element nameElement = (Element) nameNode;

        String message = messageElement.getTextContent();
        String name = nameElement.getTextContent();
        return new Message(new User(name), message);
    }

    private ICommand parseServerEvent(Element root) throws DataParsingException {
        NodeList eventList = root.getElementsByTagName("event");
        if (eventList.getLength() != 1) throw new DataParsingException();

        Node eventNode = eventList.item(0);
        if (eventNode.getNodeType() != Node.ELEMENT_NODE) throw new DataParsingException();

        Element eventElement = (Element) eventNode;
        String name = eventElement.getAttribute("name");

        ICommand command = null;
        if (name.equals("user-login")) {
            User user = parseUserFromEvent(eventElement);
            if (user == null) throw new DataParsingException();
            command = new AddUsersCommand(user);
        } else if (name.equals("user-logout")) {
            User user = parseUserFromEvent(eventElement);
            if (user == null) throw new DataParsingException();
            command = new RemoveUserCommand(user);
        } else if (name.equals("message")) {
            Message message = parseMessageFromEvent(eventElement);
            if (message == null) throw new DataParsingException();
            command = new MessageCommand(message);
        } else {
            throw new DataParsingException();
        }

        return command;
    }

    private List<User> getUsersFromServerMessage(Element root) {
        NodeList listUsers = root.getElementsByTagName("list-users");
        if (listUsers.getLength() != 1) return null;
        Element listUsersElement = (Element) listUsers.item(0);
        NodeList users = listUsersElement.getElementsByTagName("user");
        int usersCount = users.getLength();
        if (usersCount == 0) return null;
        List<User> usersList = new ArrayList<>();
        for (int i = 0; i < usersCount; ++i) {
            Node userNode = users.item(i);
            if (userNode.getNodeType() == Node.ELEMENT_NODE) {
                Element userElement = (Element) userNode;
                NodeList names = userElement.getElementsByTagName("name");
                if (names.getLength() != 1) return null;
                String userName = names.item(0).getTextContent();
                usersList.add(
                        new User(userName)
                );
            }
        }
        return usersList;
    }

    private Integer getSessionIdFromServerMessage(Element root) {
        NodeList sessions = root.getElementsByTagName("session");
        if (sessions.getLength() != 1) return null;
        try {
            return Integer.parseInt(sessions.item(0).getTextContent());
        } catch (NumberFormatException exception) {
            return null;
        }
    }

    private ICommand parseServerMessage(Element root) throws DataParsingException {
        ICommand command = null;
        String status = root.getNodeName();
        if (status.equals("success")) {
            List<User> users = getUsersFromServerMessage(root);
            Integer sessionId = getSessionIdFromServerMessage(root);

            if (users == null && sessionId == null) throw new DataParsingException();

            if (sessionId != null) {
                command = new SuccessCommand(sessionId);
            } else {
                command = new AddUsersCommand(users);
            }
        } else if (status.equals("error")) {
            NodeList sessions = root.getElementsByTagName("message");
            if (sessions.getLength() != 1) throw new DataParsingException();
            command = new ErrorCommand(sessions.item(0).getTextContent());
        }
        return command;
    }

    @Override
    public ICommand parseEvent(byte[] eventData) throws DataParsingException {
        try {
            Document document = documentBuilder.parse(new ByteArrayInputStream(eventData));
            Element root = document.getDocumentElement();

            String eventType = root.getNodeName();
            if (eventType.equals("success") || eventType.equals("error")) {
                return parseServerMessage(root);
            } else if (eventType.equals("event")) {
                return parseServerEvent(root);
            } else {
                throw new DataParsingException();
            }

        } catch (SAXException | IOException exception) {
            throw new DataParsingException();
        }
    }
}
