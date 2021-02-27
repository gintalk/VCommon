package com.vgu.cs.common.util;

/*
 * Copyright (c) 2012-2016 by Zalo Group.
 * All Rights Reserved.
 *
 * @author namnh16 on 27/02/2021
 */

import com.vgu.cs.common.concurrent.IdGenerator;

public class VUtils {

    public static final IdGenerator ID_GENERATOR = new IdGenerator();

    public static final long nextGID(){
        return ID_GENERATOR.nextId();
    }
}
