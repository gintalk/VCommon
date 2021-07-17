package com.vgu.cs.common.common;

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
