package utils;

import common.Message;
import common.User;
import exceptions.RequestParsingException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import requests.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.ByteArrayInputStream;
import java.io.IOException;

public class XMLRequestParser implements IRequestParser {

    private int sessionId;
    private final DocumentBuilder documentBuilder;

    public XMLRequestParser() throws ParserConfigurationException {
        documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
    }

    @Override
    public void setSessionId(int sessionId) {
        this.sessionId = sessionId;
    }

    private String parseStrFromTag(Element root, String tagName) {
        NodeList nList = root.getElementsByTagName(tagName);
        if (nList.getLength() != 1) return null;
        Node oneNode = nList.item(0);
        if (oneNode.getNodeType() != Node.ELEMENT_NODE) return null;
        Element element = (Element) oneNode;
        return element.getTextContent();
    }

    private Integer parseSessionId(Element root) {
        NodeList sessionsList = root.getElementsByTagName("session");
        if (sessionsList.getLength() != 1) return null;
        Node sessionNode = sessionsList.item(0);
        if (sessionNode.getNodeType() != Node.ELEMENT_NODE) return null;
        Element session = (Element) sessionNode;
        try {
            return Integer.parseInt(session.getTextContent());
        } catch (NumberFormatException exception) {
            return null;
        }
    }

    private String parseUserName(Element root) {
        return parseStrFromTag(root, "name");
    }

    private String parseMessageText(Element root) {
        return parseStrFromTag(root, "message");
    }

    private IRequest parseCommand(Element root) throws RequestParsingException {
        String name = root.getAttribute("name");
        if (name.equals("login")) {
            String userName = parseUserName(root);
            if (userName == null) throw new RequestParsingException();
            return new LoginRequest(sessionId, new User(userName));
        } else if (name.equals("logout")) {
            Integer sessionId = parseSessionId(root);
            if (sessionId == null) throw new RequestParsingException();
            return new LogoutRequest(sessionId);
        } else if (name.equals("message")) {
            Integer sessionId = parseSessionId(root);
            String messageText = parseMessageText(root);
            if (sessionId == null || messageText == null) throw new RequestParsingException();
            return new MessageRequest(sessionId, messageText);
        } else if (name.equals("list")) {
            return new UsersListRequest(sessionId);
        } else {
            throw new RequestParsingException();
        }
    }

    @Override
    public IRequest parseRequest(byte[] requestData) throws RequestParsingException {
        try {
            Document document = documentBuilder.parse(new ByteArrayInputStream(requestData));
            Element root = document.getDocumentElement();

            String requestType = root.getNodeName();
            if (requestType.equals("command")) {
                return parseCommand(root);
            } else {
                throw new RequestParsingException();
            }
        } catch (SAXException | IOException exception) {
            throw new RequestParsingException();
        }
    }
}
