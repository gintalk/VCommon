package com.vgu.cs.common.exception;

public abstract class VException extends Exception {

    protected VException() {

    }

    protected VException(String message){
        super(message);
    }

    protected VException(Throwable cause){
        super(cause);
    }

    protected VException(String message, Throwable cause){
        super(message, cause);
    }
}
