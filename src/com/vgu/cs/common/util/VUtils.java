package com.vgu.cs.common.util;

import com.vgu.cs.common.concurrent.IdGenerator;

public class VUtils {

    public static final IdGenerator ID_GENERATOR = new IdGenerator();

    public static final long nextGID(){
        return ID_GENERATOR.nextId();
    }
}
