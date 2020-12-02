package imterfaceample;

/*
java7 可以包含：常量，抽象方法
java8 额外包含：默认方法，静态方法
java9 私有方法
 */
public interface Demo01Interface {

    String name = "张三";

    default String getName(){
        return Demo01Interface.name;
    }

    static void getName4(){

    }

//    void getName2(){
//
//    }

    static void getName3(){

    }

}

/*
常量：[public] [static] [final] 常量必须赋值且不能改变
抽象方法：[public] [abstract]
默认方法：[public] default
静态方法：static
 */
