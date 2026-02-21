package com.assignment.chatstorage.exception;

public class SessionNotFoundException extends RuntimeException{
    public SessionNotFoundException(String message){
        super(message);
    }
}
