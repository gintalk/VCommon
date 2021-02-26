package com.vgu.cs.common.exception;

/*
 * Copyright (c) 2012-2016 by Zalo Group.
 * All Rights Reserved.
 *
 * @author namnh16 on 26/02/2021
 */

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
