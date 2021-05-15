package com.vgu.cs.common.util;

/*
 * Copyright (c) 2012-2016 by Zalo Group.
 * All Rights Reserved.
 *
 * @author namnh16 on 13/05/2021
 */

import com.vgu.cs.common.logger.VLogger;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

public class HttpUtils {

    private static final Logger LOGGER = VLogger.getLogger(HttpUtils.class);

    public static String sendGet(String url, Map<String, String> headers) {
        try {
            URL URL = new URL(url);
            StringBuilder response = new StringBuilder();
            HttpURLConnection con = (HttpURLConnection) URL.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("User-Agent", "");

            headers.forEach(con::setRequestProperty);
            int responseCode = con.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(
                        con.getInputStream()));
                String inputLine;

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();
            }

            return response.toString();
        } catch (IOException ex) {
            LOGGER.error(ex.getMessage(), ex);
        }

        return "";
    }
}
