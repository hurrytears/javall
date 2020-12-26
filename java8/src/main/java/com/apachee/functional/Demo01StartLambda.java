package com.apachee.functional;

public class Demo01StartLambda {

    public static void show(MyFuncitonInterface myInter){
        myInter.method();
    }

    public static void main(String[] args) {
        show(new MyFunctionalInterfaceImpl());

        show(new MyFuncitonInterface() {
            @Override
            public void method() {
                System.out.println("使用匿名内部类重写接口中的抽象方法");
            }
        });

        show(()-> System.out.println("使用lambda重写接口中的方法"));
    }
}
