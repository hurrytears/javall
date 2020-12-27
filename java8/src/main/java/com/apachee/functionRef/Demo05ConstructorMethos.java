package com.apachee.functionRef;

class Person{
    private String name;
    public Person() {
    }

    public Person(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Person{" +
                "name='" + name + '\'' +
                '}';
    }
}

@FunctionalInterface
interface PersonBuilder{
    Person build(String name);
}

public class Demo05ConstructorMethos {
    static void printName(String name, PersonBuilder p){
        Person build = p.build(name);
        System.out.println(build.toString());
    }

    public static void main(String[] args) {
        printName("Newton", name -> new Person(name));

        //优化
        printName("迪丽热巴", Person::new);
    }
}
