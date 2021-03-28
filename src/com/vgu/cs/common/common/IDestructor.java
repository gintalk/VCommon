package com.vgu.cs.common.common;

/*
 * Copyright (c) 2012-2016 by Zalo Group.
 * All Rights Reserved.
 *
 * @author namnh16 on 26/03/2021
 */

public interface IDestructor<T> {

    T dtor(T obj);
}
