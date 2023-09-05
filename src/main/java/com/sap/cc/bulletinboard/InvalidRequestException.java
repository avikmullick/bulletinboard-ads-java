package com.sap.cc.bulletinboard;

public class InvalidRequestException extends RuntimeException {
    public InvalidRequestException(String message, Throwable cause) {
        super(message, cause);
    }
}
