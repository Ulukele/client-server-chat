package utils;

import exceptions.RequestParsingException;
import requests.IRequest;

public interface IRequestParser {
    IRequest parseRequest(byte[] requestData) throws RequestParsingException;
    void setSessionId(int sessionId);
}
