package com.vgu.cs.common.system;

public class VSystemProperty {

    public static String getAppProfile(){
        String ret = null;

        try{
            ret = System.getProperty("appprofile");
        } catch(Exception e){
            e.printStackTrace(System.err);
        }

        if(ret == null){
            System.err.println("Missing required property \"appprofile\"");
            System.err.println("Please rerun the application with additional parameter \"-Dappprofile=your_app_profile\"");
            Runtime.getRuntime().exit(-1);
        }

        return ret;
    }

    public static String getConfigDir(){
        return System.getProperty("configdir", "conf");
    }

    public static String getWorkingDirectory(){
        return System.getProperty("user.dir");
    }

    public static String getProperty(String key, String def){
        return System.getProperty(key, def);
    }
}
