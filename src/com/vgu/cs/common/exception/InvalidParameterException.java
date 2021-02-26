package com.vgu.cs.common.exception;

/*
 * Copyright (c) 2012-2016 by Zalo Group.
 * All Rights Reserved.
 *
 * @author namnh16 on 26/02/2021
 */

public class InvalidParameterException extends VException{

    public InvalidParameterException(){

    }

    public InvalidParameterException(String message){
        super(message);
    }
}
