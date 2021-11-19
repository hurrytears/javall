package com.apachee.improve;

import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Properties;

/*
反射：
    源代码阶段：Person.java -> javac编译 -> Person.class
    类对象阶段（类加载器（ClassLoader)）：Class类对象（成员变量Field[] fileds，构造方法Constructor[] cons，成员方法Method[] methods）
    运行时阶段：new Person

 */
class Person {
    private String name;

    public void eat(String food){
        System.out.println("吃屎啦");
    }

    public void eat(){
        System.out.println("开吃了");
    }

    public Person() {
    }

    @Override
    public String toString() {
        return "Person{" +
                "name='" + name + '\'' +
                ", age=" + age +
                '}';
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    private int age;

    public Person(String name, int age) {
        this.name = name;
        this.age = age;
    }


}

class Student{
    public void sleep(){
        System.out.println("sleep...");
    }
}

public class Demo02Reflect {
    public static void main(String[] args) throws Exception {
//        show01();
//        show02();
        show03();
    }

    //小框架
    private static void show03() throws Exception {
        Properties prop = new Properties();
//        prop.loadFromXML();
        InputStream is = Demo02Reflect.class.getClassLoader().getResourceAsStream("prop.properties");
        prop.load(is);
        String className = prop.getProperty("className");
        String methodName = prop.getProperty("methodName");
        Class cls = Class.forName(className);
        Method method = cls.getMethod(methodName);
        Object o = cls.newInstance();
        method.invoke(o);
    }

    /*
    获取功能：
        1.获取成员变量
            Field[] getFields()
            Field getField(String name)
            Field[] getDeclaredFields()
            Field getDeclaredField(String name)
        2.获取构造方法
            Constructor[] getConstructors()
            Constructor getConstructor(类<?>... parammeterTypes)
            Constructor[] getDeclaredConstructors()
            Constructor getDeclaredConstructor(类<?>... parammeterTypes)
        3.获取成员方法
            Method[] getMethods()
            Method getMethod(String name, 类<?>... parammeterTypes)
            Method[] getDeclaredMethods()
            Method getDeclaredMethod(String name, 类<?>... parammeterTypes)
        4.获取类名
            String getName();
     */
    private static void show02() throws Exception {
        Class<Person> personClass = Person.class;
        //获取所有pulic修饰的成员变量
        Field[] fields1 = personClass.getFields();
        Field[] fields2 = personClass.getDeclaredFields();
        for (Field field : fields2) {
            System.out.println(field);
        }
//        Field field1 = personClass.getField("name");
        Field field2 = personClass.getDeclaredField("name");
        Person p = new Person("李四", 12);
        field2.setAccessible(true);
        Object value = field2.get(p);
        System.out.println(value);
        field2.set(p, "张三");
        System.out.println(p);
        System.out.println("---------------获取构造方法--------------------------");
        Constructor<Person> constructor = personClass.getConstructor(String.class, int.class);
        //用来创建对象
        Person wangwu = constructor.newInstance("wangwu", 23);
        Person person = personClass.newInstance(); //空参快捷创建对象
        System.out.println(wangwu);
        System.out.println("---------------获取成员方法--------------------------");
        Method eatMethon = personClass.getMethod("eat");
        eatMethon.invoke(p);
        Method eatMethod2 = personClass.getMethod("eat", String.class);
        eatMethod2.invoke(p, "fan");
        System.out.println(eatMethon.getName() + "\n获取类名" + personClass.getName());
    }

    /*
    获取类对象的三种方式:
        Class.forName("com.util.net") 将字节码加载进内存，返回Class对象
            多用于配置文件中，将类名配置到配置文件中，读取文件，加载类
        类名.class
            多用于参数的传递
        对象.getClass,多有对象都有这个方法
            多用于对象的获取字节码的方式
     */
    static void show01() throws Exception {
        Class cls1 = Class.forName("com.apachee.improve.Person");
        System.out.println(cls1);
        Class cls2 = Person.class;
        System.out.println(cls2);
        Person p = new Person();
        System.out.println(p.getClass());
        // 同一个字节码文件在一次程序中只会被加载一次
    }

}
