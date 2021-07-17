package com.vgu.cs.common.common;

public interface IObjectBuilder<T> {

    T build(T obj);
}
