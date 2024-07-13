package com.sparklecow.book.exceptions;

public class OperationNotPermittedException extends Exception {
    public OperationNotPermittedException(String message) {
        super(message);
    }
}
