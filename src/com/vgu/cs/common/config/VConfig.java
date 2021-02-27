package com.vgu.cs.common.config;

/*
 * Copyright (c) 2012-2016 by Zalo Group.
 * All Rights Reserved.
 *
 * @author namnh16 on 26/02/2021
 */

import com.vgu.cs.common.exception.InvalidParameterException;
import com.vgu.cs.common.exception.NotExistException;
import com.vgu.cs.common.system.VSystemProperty;
import org.apache.commons.configuration2.INIConfiguration;
import org.apache.commons.configuration2.SubnodeConfiguration;
import org.apache.commons.configuration2.builder.FileBasedConfigurationBuilder;
import org.apache.commons.configuration2.builder.fluent.FileBasedBuilderParameters;
import org.apache.commons.configuration2.builder.fluent.Parameters;
import org.apache.commons.configuration2.convert.DefaultListDelimiterHandler;
import org.apache.commons.configuration2.ex.ConfigurationException;

import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class VConfig {

    public static final VConfig INSTANCE = new VConfig();
    private final ConcurrentMap<String, String> CONFIG_MAP = new ConcurrentHashMap<>();
    private final String KEY_SEPARATOR = ".";

    private VConfig() {
        _init();
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Init
    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    private void _init() {
        String configPathPrefix;
        String appProfile = VSystemProperty.getAppProfile();
        String configDir = VSystemProperty.getConfigDir();
        if (configDir.endsWith("/")) {
            configPathPrefix = configDir + appProfile + ".";
        } else {
            configPathPrefix = configDir + "/" + appProfile + ".";
        }

        String configFileName = VSystemProperty.getProperty("configfile", "config.ini");
        if (configFileName == null || configFileName.isEmpty()) {
            System.err.println("No configuration file has been specified");
            return;
        }

        String configFilePath = configPathPrefix + configFileName;
        FileBasedBuilderParameters parameters = new Parameters()
                .fileBased()
                .setFileName(configFilePath)
                .setListDelimiterHandler(new DefaultListDelimiterHandler(','))
                .setThrowExceptionOnMissing(false);
        FileBasedConfigurationBuilder<INIConfiguration> configBuilder = new FileBasedConfigurationBuilder<>(INIConfiguration.class)
                .configure(parameters);

        INIConfiguration config = null;
        try {
            config = configBuilder.getConfiguration();
        } catch (ConfigurationException e) {
            e.printStackTrace(System.err);
        }

        if (config == null) {
            return;
        }

        _configToMap(config);
    }

    private void _configToMap(INIConfiguration config) {
        Set<String> sections = config.getSections();
        for (String section : sections) {
            SubnodeConfiguration subConfig = config.getSection(section);
            Iterator<String> iterator = subConfig.getKeys();
            while (iterator.hasNext()) {
                String key = iterator.next();
                String value = subConfig.getString(key);
                if (value == null) {
                    continue;
                }

                String mapKey = _buildMapKey(section, key);
                CONFIG_MAP.put(mapKey, value);
            }
        }
    }

    private String _buildMapKey(String sectionName, String key) {
        return String.format("%s%s%s", sectionName.trim(), KEY_SEPARATOR, key.trim());
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Public
    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    public int getInt(Class<?> clazz, String key, int def) {
        try {
            return Integer.parseInt(_getProperty(_buildMapKey(_classToSectionName(clazz), key)));
        } catch (NumberFormatException | NotExistException | InvalidParameterException e) {
            return def;
        }
    }

    public short getShort(Class<?> clazz, String key, short def) {
        try {
            return Short.parseShort(_getProperty(_buildMapKey(_classToSectionName(clazz), key)));
        } catch (NumberFormatException | NotExistException | InvalidParameterException e) {
            return def;
        }
    }

    public String getString(Class<?> clazz, String key, String def) {
        try {
            return _getProperty(_buildMapKey(_classToSectionName(clazz), key));
        } catch (NotExistException | InvalidParameterException e) {
            return def;
        }
    }

    public boolean getBoolean(Class<?> clazz, String key) {
        try {
            return Boolean.parseBoolean(_getProperty(_buildMapKey(_classToSectionName(clazz), key)));
        } catch (NotExistException | InvalidParameterException e) {
            return false;
        }
    }

    public <T extends Enum<T>> T getEnum(Class<?> clazz, Class<T> enumType, String key, T def) {
        try {
            String value = _getProperty(_getProperty(_buildMapKey(_classToSectionName(clazz), key)));
            return Enum.valueOf(enumType, value);
        } catch (NotExistException | InvalidParameterException e) {
            return def;
        }
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Private
    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    private String _classToSectionName(Class<?> clazz) {
        return clazz.getSimpleName();
    }

    private String _getProperty(String key) throws NotExistException, InvalidParameterException {
        if (key == null || key.isEmpty()) {
            throw new InvalidParameterException("Key is either null or empty. Try another key");
        }

        String value = CONFIG_MAP.get(key);
        if (value == null || value.isEmpty()) {
            throw new NotExistException("Value for this key does not exist");
        }

        return value;
    }
}
