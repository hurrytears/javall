package com.apachee.functionRef;

@FunctionalInterface
interface Richable{
    void buy();
}

class Husband{
    public void buyHouse(){
        System.out.println("北京二环内买一套四合院");
    }

    public void marry(Richable r){
        r.buy();
    }

    public void soHappy(){
//        marry(()->{
//            this.buyHouse();
//        });

        marry(this::buyHouse);
    }

    public static void main(String[] args) {
        new Husband().soHappy();
    }
}

public class Demo04ThisMethod {
}
