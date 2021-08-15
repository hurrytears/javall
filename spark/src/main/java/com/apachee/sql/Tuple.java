package com.apachee.sql;

import java.io.Serializable;

public class Tuple implements Serializable {

    private long key;
    private int value;

    public Tuple() {
    }

    public Tuple(long key, int value) {
        this.key = key;
        this.value = value;
    }

    public long getKey() {
        return key;
    }

    public void setKey(long key) {
        this.key = key;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
