package com.vgu.cs.common.util;

/*
 * Copyright (c) 2012-2016 by Zalo Group.
 * All Rights Reserved.
 *
 * @author namnh16 on 28/03/2021
 */

import com.vgu.cs.common.logger.VLogger;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

public class ServletUtils {

    private static final Logger LOGGER = VLogger.getLogger(ServletUtils.class);

    public static void prepareResponseHeader(HttpServletResponse res) {
        res.setContentType("application/json; charset=utf-8");
    }

    public static boolean printString(HttpServletRequest req, HttpServletResponse res, String message) {
        res.setHeader("Cache-control", "private");
        res.setCharacterEncoding("utf-8");

        try (PrintWriter writer = res.getWriter()) {
            writer.write(message);
            return true;
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage(), req.getRequestURI(), req.getQueryString(), ex);
            return false;
        }
    }

    public static int getInt(HttpServletRequest req, String key){
        return getInt(req, key, 0);
    }

    public static int getInt(HttpServletRequest req, String key, int def){
        String value = req.getParameter(key);
        if(StringUtils.isNullOrEmpty(value)){
            return def;
        }

        try{
            return Integer.parseInt(value);
        } catch (NumberFormatException ex){
            return def;
        }
    }

    public static String getString(HttpServletRequest req, String key){
        return getString(req, key, "");
    }

    public static String getString(HttpServletRequest req, String key, String def){
        String value = req.getParameter(key);
        if(value == null){
            return def;
        }

        return value.trim();
    }
}
