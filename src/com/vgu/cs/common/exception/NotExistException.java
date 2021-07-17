package com.vgu.cs.common.exception;

public class NotExistException extends VException{

    public NotExistException(){

    }

    public NotExistException(String message){
        super(message);
    }
}
