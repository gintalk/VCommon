/*
 * Copyright (c) 2012-2016 by Zalo Group.
 * All Rights Reserved.
 *
 * @author namnh16 on 26/02/2021
 */

import com.vgu.cs.common.system.VSystemProperty;
import org.apache.commons.configuration2.INIConfiguration;
import org.apache.commons.configuration2.builder.FileBasedConfigurationBuilder;
import org.apache.commons.configuration2.builder.fluent.Configurations;
import org.apache.commons.configuration2.builder.fluent.FileBasedBuilderParameters;
import org.apache.commons.configuration2.builder.fluent.Parameters;
import org.apache.commons.configuration2.convert.DefaultListDelimiterHandler;
import org.apache.commons.configuration2.ex.ConfigurationException;

public class ApacheConfiguration {
    public static void main(String[] args) {
        try {
            FileBasedBuilderParameters parameters = new Parameters().fileBased()
                    .setBasePath(VSystemProperty.getWorkingDirectory())
                    .setFileName("conf/myconfig.ini")
                    .setThrowExceptionOnMissing(false)
                    .setListDelimiterHandler(new DefaultListDelimiterHandler(','));
            FileBasedConfigurationBuilder<INIConfiguration> configBuilder = new FileBasedConfigurationBuilder<>(INIConfiguration.class)
                    .configure(parameters);

            Configurations configs = new Configurations();
            INIConfiguration config = configs.fileBased(INIConfiguration.class, "conf/myconfig.ini");
            System.out.println(config.getSection("Config"));
        } catch (ConfigurationException e) {
            e.printStackTrace(System.err);
        }
    }
}
