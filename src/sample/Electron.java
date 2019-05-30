package sample;

import javafx.scene.shape.Circle;
import javafx.scene.paint.Color;

class Electron extends Circle {
    private double veloX = 0;
    private double veloY = 0;
    double accX = 0;
    double accY = 0;
    double locX = 100;
    double locY = 400;
    boolean sign = true; //true +, false -
    Electron (int radius){
        super(radius);
        setCenterX(locX);
        setCenterY(locY);
    }
    private void changeLoc(){
        locX += veloX/1000000.0;   //dzielnik 100x mniejszy niż stała k z klasy main
        locY += veloY/1000000.0;
    }
    void change(){
        veloX += accX;
        veloY += accY;
        changeLoc();
        accX = 0;
        accY = 0;
    }
}

class Source extends Circle {
    boolean sign;  //true +, false -
    Source(double x, double y, int r, boolean s){
        super(x,y,r);
        Color color = Color.RED;
        if(!s) color = Color.BLUE;
        setFill(color);
        sign = s;
    }
}
