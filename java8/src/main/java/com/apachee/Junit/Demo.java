package com.apachee.Junit;

import org.junit.Assert;
import org.junit.Test;

public class Demo {

    @Test
    public void add(){
//        System.out.println( 1 + 1 );
        Assert.assertEquals(2, 3);
    }

    @Test
    public void sub(){
        System.out.println(3 - 4);
    }
}
