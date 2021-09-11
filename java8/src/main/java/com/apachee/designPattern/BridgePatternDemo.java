package com.apachee.designPattern;

// 桥接模式
interface DrawAPI {
    public void drawCircle(int radius, int x, int y);
}

class RedCircle implements DrawAPI {
    @Override
    public void drawCircle(int radius, int x, int y) {
        System.out.println("Drawing Circle[ color: red, radius: "
                + radius +", x: " +x+", "+ y +"]");
    }
}

class GreenCircle implements DrawAPI {
    @Override
    public void drawCircle(int radius, int x, int y) {
        System.out.println("Drawing Circle[ color: green, radius: "
                + radius +", x: " +x+", "+ y +"]");
    }
}

abstract class Shape3 {
    protected DrawAPI drawAPI;
    protected Shape3(DrawAPI drawAPI){
        this.drawAPI = drawAPI;
    }
    public abstract void draw();
}

class Circle1 extends Shape3 {
    private int x, y, radius;

    public Circle1(int x, int y, int radius, DrawAPI drawAPI) {
        super(drawAPI);
        this.x = x;
        this.y = y;
        this.radius = radius;
    }

    public void draw() {
        drawAPI.drawCircle(radius,x,y);
    }
}

public class BridgePatternDemo {
    public static void main(String[] args) {
        Shape3 redCircle = new Circle1(100,100, 10, new RedCircle());
        Shape3 greenCircle = new Circle1(100,100, 10, new GreenCircle());

        redCircle.draw();
        greenCircle.draw();
    }
}
