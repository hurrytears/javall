package com.apachee.designPattern;

import java.util.Hashtable;

// 原型模式

// 这里涉及到深拷贝和浅拷贝
abstract class Shape2 implements Cloneable {

    private String id;
    protected String type;

    abstract void draw();

    public String getType(){
        return type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Object clone() {
        Object clone = null;
        try {
            clone = super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return clone;
    }
}

class Rectangle2 extends Shape2 {

    public Rectangle2(){
        type = "Rectangle";
    }

    @Override
    public void draw() {
        System.out.println("Inside Rectangle::draw() method.");
    }
}

class Square2 extends Shape2 {

    public Square2(){
        type = "Square";
    }

    @Override
    public void draw() {
        System.out.println("Inside Square::draw() method.");
    }
}

class Circle2 extends Shape2 {

    public Circle2(){
        type = "Circle";
    }

    @Override
    public void draw() {
        System.out.println("Inside Circle::draw() method.");
    }
}

class ShapeCache {

    private static Hashtable<String, Shape2> shapeMap
            = new Hashtable<String, Shape2>();

    public static Shape2 getShape(String shapeId) {
        Shape2 cachedShape = shapeMap.get(shapeId);
        return (Shape2) cachedShape.clone();
    }

    // 对每种形状都运行数据库查询，并创建该形状
    // shapeMap.put(shapeKey, shape);
    // 例如，我们要添加三种形状
    public static void loadCache() {
        Circle2 circle = new Circle2();
        circle.setId("1");
        shapeMap.put(circle.getId(),circle);

        Square2 square = new Square2();
        square.setId("2");
        shapeMap.put(square.getId(),square);

        Rectangle2 rectangle = new Rectangle2();
        rectangle.setId("3");
        shapeMap.put(rectangle.getId(),rectangle);
    }
}

public class PrototypePatternDemo {
    public static void main(String[] args) {
        ShapeCache.loadCache();

        Shape2 clonedShape = (Shape2) ShapeCache.getShape("1");
        System.out.println("Shape : " + clonedShape.getType());

        Shape2 clonedShape2 = (Shape2) ShapeCache.getShape("2");
        System.out.println("Shape : " + clonedShape2.getType());

        Shape2 clonedShape3 = (Shape2) ShapeCache.getShape("3");
        System.out.println("Shape : " + clonedShape3.getType());
    }
}
