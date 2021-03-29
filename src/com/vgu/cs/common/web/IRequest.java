package com.vgu.cs.common.web;

/*
 * Copyright (c) 2012-2016 by Zalo Group.
 * All Rights Reserved.
 *
 * @author namnh16 on 26/03/2021
 */

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface IRequest {

    void doRequest();

    VResponse getApiResponse();

    long getCurrentTime();

    VPath getPath();

    HttpServletRequest getHRequest();

    HttpServletResponse getHResponse();
}
