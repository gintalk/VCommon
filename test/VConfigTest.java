/*
 * Copyright (c) 2012-2016 by Zalo Group.
 * All Rights Reserved.
 *
 * @author namnh16 on 26/02/2021
 */

import com.vgu.cs.common.config.VConfig;

public class VConfigTest {
    public static void main(String[] args) {
//        System.out.println(VConfig.INSTANCE.getString(VConfigTest.class, "abc", "def"));
//        System.out.println(VConfig.INSTANCE.getInt(VConfigTest.class, "integer", 999));
        System.out.println(VConfig.INSTANCE.getBoolean(VConfigTest.class, "boolean"));
    }
}
