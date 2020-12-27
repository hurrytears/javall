package com.apachee.functionRef;

interface Greetable{
    public abstract void greet();
}

class Human{
    public void sayHello(){
        System.out.println("Hello, I'm Human");
    }
}

class Man extends Human{
    @Override
    public void sayHello(){
        System.out.println("Hello, I'm man");
    }

    public void method(Greetable g){
        g.greet();
    }

    public void show(){
//        method(() -> {
//            Human h = new Human();
//            h.sayHello();
//        });
//        method(()->{
//            super.sayHello();
//        });
        method(super::sayHello);
    }

    public static void main(String[] args) {
        new Man().show();

    }
}

public class Demo03SuperMethod {
}
