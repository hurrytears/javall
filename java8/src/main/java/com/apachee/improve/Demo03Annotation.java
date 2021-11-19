package com.apachee.improve;

enum People{
    P1, P2;
}

@interface MyAnnotation{

}

public @interface Demo03Annotation {

    /*
    属性：接口中的抽象方法
    返回值：
        基本类型4类八种
        String
        枚举
        注解
        以上类型的数据
    默认值： default
    属性名如果是value且仅有一个，则可以省略属性名


    元注解：
        @Target
            Type:只能作用于类上
            Method:方法上
            Field:成员变量上
        @Retention
            Source, Class, Runtime
        @Documented
        @Inherited 是否被子类继承
     */
    int age();
    String name() default "张三";
    People per();
    MyAnnotation myAnnotation();
    String[] strs();

}

@Demo03Annotation(age=12, per=People.P1, myAnnotation = @MyAnnotation, strs = {"aaa","bbb"})
class Worker{

}
