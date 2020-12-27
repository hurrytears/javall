package com.apachee.functionRef;

@FunctionalInterface
interface Calcable{
    public abstract int calAbs(int number);
}

public class Demo02StaticMethod {

    static int method(int number, Calcable c){
        return c.calAbs(number);
    }

    public static void main(String[] args) {
        int res = method(-10, i -> Math.abs(i));
        System.out.println(res);
        System.out.println("--------------------方法引用优化");
        System.out.println(method(-100, Math::abs));
    }
}
