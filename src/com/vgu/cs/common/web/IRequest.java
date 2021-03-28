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

    String getAppVersion();

    int getAppVersionNum();

    String getClientIp();

    long getClientTime();

    int getCountryCode();

    long getCurTime();

    String getDeviceId();

    String getLanguageCode();

    String getOs();

    String getOsVersion();

    VPath getPath();

    String getSession();

    void setSession(String session);

    int getUserId();

    void setUserId(int userId);

    HttpServletRequest getHRequest();

    HttpServletResponse getHResponse();

    String getDeviceInfo();

    String getPublicKey();
}
