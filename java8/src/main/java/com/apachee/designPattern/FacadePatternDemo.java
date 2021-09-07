package com.apachee.designPattern;

// 外观模式

interface Shape5 {
    void draw();
}

class Rectangle5 implements Shape5 {

    @Override
    public void draw() {
        System.out.println("Rectangle::draw()");
    }
}

class Square5 implements Shape5 {

    @Override
    public void draw() {
        System.out.println("Square::draw()");
    }
}

class Circle5 implements Shape5 {

    @Override
    public void draw() {
        System.out.println("Circle::draw()");
    }
}

class ShapeMaker {
    private Shape circle;
    private Shape rectangle;
    private Shape square;

    public ShapeMaker() {
        circle = new Circle();
        rectangle = new Rectangle();
        square = new Square();
    }

    public void drawCircle(){
        circle.draw();
    }
    public void drawRectangle(){
        rectangle.draw();
    }
    public void drawSquare(){
        square.draw();
    }
}

public class FacadePatternDemo {
    public static void main(String[] args) {
        ShapeMaker shapeMaker = new ShapeMaker();

        shapeMaker.drawCircle();
        shapeMaker.drawRectangle();
        shapeMaker.drawSquare();
    }
}
