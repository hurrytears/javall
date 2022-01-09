package com.apachee.core;

import java.io.Serializable;

public class Person implements Serializable {
    private String name;
    private long age;

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getAge() {
        return this.age;
    }

    public void setAge(long age) {
        this.age = age;
    }
}
