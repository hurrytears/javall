package com.apachee.designPattern;

// 享元模式

import java.util.HashMap;

interface Shape6 {
    void draw();
}

class Circle6 implements Shape6 {
    private String color;
    private int x;
    private int y;
    private int radius;

    public Circle6(String color){
        this.color = color;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }

    @Override
    public void draw() {
        System.out.println("Circle: Draw() [Color : " + color
                +", x : " + x +", y :" + y +", radius :" + radius);
    }
}

class ShapeFactory6 {
    private static final HashMap<String, Shape6> circleMap = new HashMap<>();

    public static Shape6 getCircle(String color) {
        Circle6 circle = (Circle6)circleMap.get(color);

        if(circle == null) {
            circle = new Circle6(color);
            circleMap.put(color, circle);
            System.out.println("Creating circle of color : " + color);
        }
        return circle;
    }
}


public class FlyweightPatternDemo {

    private static final String colors[] =
            { "Red", "Green", "Blue", "White", "Black" };
    public static void main(String[] args) {

        for(int i=0; i < 20; ++i) {
            Circle6 circle =
                    (Circle6)ShapeFactory6.getCircle(getRandomColor());
            circle.setX(getRandomX());
            circle.setY(getRandomY());
            circle.setRadius(100);
            circle.draw();
        }
    }
    private static String getRandomColor() {
        return colors[(int)(Math.random()*colors.length)];
    }
    private static int getRandomX() {
        return (int)(Math.random()*100 );
    }
    private static int getRandomY() {
        return (int)(Math.random()*100);
    }
}
