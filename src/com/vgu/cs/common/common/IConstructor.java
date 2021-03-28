package com.vgu.cs.common.common;

/*
 * Copyright (c) 2012-2016 by Zalo Group.
 * All Rights Reserved.
 *
 * @author namnh16 on 25/03/2021
 */

public interface IConstructor<T> {
    T ctor();

    class StringConstructor implements IConstructor<String> {
        public StringConstructor() {

        }

        public String ctor() {
            return "";
        }
    }

    class DoubleConstructor implements IConstructor<Double> {
        public DoubleConstructor() {

        }

        public Double ctor() {
            return 0.0D;
        }
    }

    class FloatConstructor implements IConstructor<Float> {
        public FloatConstructor() {

        }

        public Float ctor() {
            return 0.0F;
        }
    }
}
