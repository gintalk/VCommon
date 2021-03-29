package com.vgu.cs.common.web;

/*
 * Copyright (c) 2012-2016 by Zalo Group.
 * All Rights Reserved.
 *
 * @author namnh16 on 26/03/2021
 */

import com.vgu.cs.common.common.StringBuilderPool;
import com.vgu.cs.common.logger.VLogger;
import com.vgu.cs.common.util.StringUtils;
import org.apache.logging.log4j.Logger;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class VPath {

    private static final Logger LOGGER = VLogger.getLogger(VPath.class);
    private static final Pattern PATTERN = Pattern.compile("^/(v\\d)/(.+)/(.+)/(.+)/(.+)$");
    private static final Map<String, Detail> DETAIL_MAP = new ConcurrentHashMap<>();
    public String path;
    public Detail detail;

    public VPath(String path) {
        if (!StringUtils.isNullOrEmpty(path)) {
            this.path = path;

            Detail detail = DETAIL_MAP.get(path);
            if (detail == null) {
                Matcher matcher = PATTERN.matcher(path);
                if (matcher.find()) {
                    detail = new Detail(
                            matcher.group(1),
                            matcher.group(2),
                            matcher.group(3),
                            matcher.group(4),
                            matcher.group(5)
                    );
                    DETAIL_MAP.put(path, detail);
                }
            }
            this.detail = detail;
        }
    }

    public boolean isValid() {
        return path != null && detail != null;
    }

    @Override
    public String toString() {
        return "VPath{" +
                "path='" + path + '\'' +
                ", detail=" + detail +
                '}';
    }

    public static class Detail {

        private final String _originVersion;
        private final String _container;
        private final String _group;
        private final String _action;
        private final String _name;
        private final String _methodName;
        private String _version;

        public Detail(String originVersion, String container, String group, String action, String name) {
            _originVersion = originVersion;
            _container = container.replaceAll("\\-", "_");
            _group = group.replaceAll("\\-", "_");
            _action = action;
            _name = name.replace("\\-", "_");
            _methodName = _buildMethodName();
        }

        public String getOriginVersion() {
            return _originVersion;
        }

        public String getContainer() {
            return _container;
        }

        public String getGroup() {
            return _group;
        }

        public String getAction() {
            return _action;
        }

        public String getName() {
            return _name;
        }

        public String getMethodName() {
            return _methodName;
        }

        public String getVersion() {
            return _version;
        }

        private String _buildMethodName() {
            StringBuilder builder = StringBuilderPool.INSTANCE.borrowObject(100);
            builder.append(_action);

            boolean upCase = true;
            String name = _name.toLowerCase();
            for (int i = 0; i < name.length(); i++) {
                char c = name.charAt(i);
                if ('_' == c) {
                    upCase = true;
                    continue;
                }
                if (upCase) {
                    c = Character.toUpperCase(c);
                    upCase = false;
                }

                builder.append(c);
            }

            return builder.toString();
        }

        @Override
        public String toString() {
            return "Detail{" +
                    "_originVersion='" + _originVersion + '\'' +
                    ", _container='" + _container + '\'' +
                    ", _group='" + _group + '\'' +
                    ", _action='" + _action + '\'' +
                    ", _name='" + _name + '\'' +
                    ", _methodName='" + _methodName + '\'' +
                    ", _version='" + _version + '\'' +
                    '}';
        }
    }
}
