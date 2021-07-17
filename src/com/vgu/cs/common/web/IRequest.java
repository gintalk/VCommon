package com.vgu.cs.common.web;

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
