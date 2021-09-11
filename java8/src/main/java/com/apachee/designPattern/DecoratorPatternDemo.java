package com.apachee.designPattern;

// 装饰器模式

interface Shape4 {
    void draw();
}

class Rectangle4 implements Shape4 {

    @Override
    public void draw() {
        System.out.println("Shape: Rectangle");
    }
}

class Circle4 implements Shape4 {

    @Override
    public void draw() {
        System.out.println("Shape: Circle");
    }
}

abstract class ShapeDecorator implements Shape4 {
    protected Shape4 decoratedShape;

    public ShapeDecorator(Shape4 decoratedShape){
        this.decoratedShape = decoratedShape;
    }

    public void draw(){
        decoratedShape.draw();
    }
}

class RedShapeDecorator extends ShapeDecorator {

    public RedShapeDecorator(Shape4 decoratedShape) {
        super(decoratedShape);
    }

    @Override
    public void draw() {
        decoratedShape.draw();
        setRedBorder(decoratedShape);
    }

    private void setRedBorder(Shape4 decoratedShape){
        System.out.println("Border Color: Red");
    }
}


public class DecoratorPatternDemo {

    public static void main(String[] args) {

        Shape circle = new Circle();
        ShapeDecorator redCircle = new RedShapeDecorator(new Circle4());
        ShapeDecorator redRectangle = new RedShapeDecorator(new Rectangle4());
        //Shape redCircle = new RedShapeDecorator(new Circle());
        //Shape redRectangle = new RedShapeDecorator(new Rectangle());
        System.out.println("Circle with normal border");
        circle.draw();

        System.out.println("\nCircle of red border");
        redCircle.draw();

        System.out.println("\nRectangle of red border");
        redRectangle.draw();
    }
}
