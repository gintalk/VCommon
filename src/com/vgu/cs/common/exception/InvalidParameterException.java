package com.vgu.cs.common.exception;

public class InvalidParameterException extends VException{

    public InvalidParameterException(){

    }

    public InvalidParameterException(String message){
        super(message);
    }
}
