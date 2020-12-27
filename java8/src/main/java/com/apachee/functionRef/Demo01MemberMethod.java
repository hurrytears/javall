package com.apachee.functionRef;

class MethodRedObject{
    public void printUpperCaseString(String str){
        System.out.println(str.toUpperCase());
    }
}

public class Demo01MemberMethod {

    @FunctionalInterface
    interface Printable{
        public abstract void print(String s);
    }

    static void printString(Printable p){
        p.print("Hello");
    }

    public static void main(String[] args) {
//        printString(s -> {
//            MethodRedObject obj = new MethodRedObject();
//            obj.printUpperCaseString(s);
//        });

        /**
         * 方法引用优化lambda
         */
        MethodRedObject obj = new MethodRedObject();
        printString(obj::printUpperCaseString);
    }
}
