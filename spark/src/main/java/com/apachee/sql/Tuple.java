package com.apachee.sql;

import java.io.Serializable;

public class Tuple<U, V> implements Serializable {

    private U key;
    private V value;

    public Tuple() {
    }

    public Tuple(U key, V value) {
        this.key = key;
        this.value = value;
    }

    public U getKey() {
        return key;
    }

    public void setKey(U key) {
        this.key = key;
    }

    public V getValue() {
        return value;
    }

    public void setValue(V value) {
        this.value = value;
    }
}
