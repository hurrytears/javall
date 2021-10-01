package com.apachee.file;

/*
Person p = new Person("小美女", 18)
把对象以流的方式，写入到文件中保存，叫写对象，也叫对象的序列化
对象中包含的不仅仅是字符，使用字节流
ObjectOutputStream: 对象的序列化流

注意，静态成员不能被序列化
transient修饰的也不能被序列化
 */

import java.io.*;

class Person implements Serializable {
    String name;
    int age;

    @Override
    public String toString() {
        return "Person{" +
                "name='" + name + '\'' +
                ", age=" + age +
                '}';
    }

    public Person() {
    }

    public Person(String name, int age) {
        this.name = name;
        this.age = age;
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
}

public class Demo07Seriallize {

    static String path = "localdata/java/serial.txt";

    public static void main(String[] args) throws IOException, ClassNotFoundException {
//        show01();
        show02();
    }

    private static void show02() throws IOException, ClassNotFoundException {
        ObjectInputStream ois = new ObjectInputStream(new FileInputStream(path));
        Person o = (Person)ois.readObject();
        System.out.println(o);
        ois.close();
    }

    private static void show01() throws IOException {
        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(path));
        Person p = new Person("小美女", 18);
        oos.writeObject(p);
        oos.close();
    }

}
