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
    int mass = 25;
    Electron (int radius){
        super(radius);
        setCenterX(locX);
        setCenterY(locY);
    }
    private void changeLoc(){
        locX += veloX/40000.0;   //dzielnik 2500x mniejszy niż stała k z klasy main
        locY += veloY/40000.0;
    }
    void change(){
        veloX += accX;
        veloY += accY;
        changeLoc();
        accX = 0;
        accY = 0;
    }
    void reset(){
        veloX = 0;
        veloY = 0;
        accX = 0;
        accY = 0;
        locX = 100;
        locY = 400;
    }
    void bounce(){
        if (locX < 1100 || locX >1150) veloX = veloX * (-1);
        else veloY = veloY*(-1);
    }

    void change_sign(){
        if (sign == true) sign =false;
        else sign = true;
    }

    void pounce(){
        veloX = veloX * (-1);
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
