package sample;

import javafx.animation.AnimationTimer;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    @FXML private Pane field;

    @FXML private RadioButton peaceful;
    @FXML private RadioButton easy;
    @FXML private RadioButton medium;
    @FXML private RadioButton hard;
    private final ToggleGroup difficulty = new ToggleGroup();
    private int mode = 0;

    private final int k = 100000000;   //constant required for long range
    @Override
    public void initialize(URL url, ResourceBundle resource){

        Electron ball = new Electron( 7 );  //creating ball
        field.getChildren().add( ball );
        ArrayList<Source> sources = new ArrayList<>();  //creating array for sources

        peaceful.setToggleGroup(difficulty);
        easy.setToggleGroup(difficulty);
        medium.setToggleGroup(difficulty);
        hard.setToggleGroup(difficulty);

        ArrayList<Rectangle> er = new ArrayList<>();
        ArrayList<Rectangle> mr = new ArrayList<>();
        ArrayList<Rectangle> hr = new ArrayList<>();
        addRectangles(er, mr, hr);
        for(Rectangle rect: er){field.getChildren().add(rect); rect.setFill(Color.TRANSPARENT);}
        for(Rectangle rect: mr){field.getChildren().add(rect); rect.setFill(Color.TRANSPARENT);}
        for(Rectangle rect: hr){field.getChildren().add(rect); rect.setFill(Color.TRANSPARENT);}

        field.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent me) -> {      //creating sources with mouseclicks
            if(me.getButton().equals(MouseButton.PRIMARY)) {
                Source source = new Source(me.getX(), me.getY(), 7,  true);
                sources.add(source);
                //addEventHandler(root, source);
                field.getChildren().add(source);
            }
            if(me.getButton().equals(MouseButton.SECONDARY)) {
                Source source = new Source(me.getX(), me.getY(), 7, false);
                sources.add(source);
                //addEventHandler(root, source);
                field.getChildren().add(source);
            }
        });

        difficulty.selectedToggleProperty().addListener(new ChangeListener<Toggle>(){
            public void changed(ObservableValue<? extends Toggle> ov,
                                Toggle old_toggle, Toggle new_toggle) {

                if (difficulty.getSelectedToggle() == peaceful) {
                    for(Rectangle rect: er){rect.setFill(Color.TRANSPARENT);}
                    for(Rectangle rect: mr){rect.setFill(Color.TRANSPARENT);}
                    for(Rectangle rect: hr){rect.setFill(Color.TRANSPARENT);}
                    mode = 0;
                }
                if (difficulty.getSelectedToggle() == easy) {
                    for(Rectangle rect: er){rect.setFill(Color.BLACK);}
                    for(Rectangle rect: mr){rect.setFill(Color.TRANSPARENT);}
                    for(Rectangle rect: hr){rect.setFill(Color.TRANSPARENT);}
                    mode = 1;
                }
                else if (difficulty.getSelectedToggle() == medium) {
                    for(Rectangle rect: er){rect.setFill(Color.BLACK);}
                    for(Rectangle rect: mr){rect.setFill(Color.BLACK);}
                    for(Rectangle rect: hr){rect.setFill(Color.TRANSPARENT);}
                    mode = 2;
                }
                else if (difficulty.getSelectedToggle() == hard) {
                    for(Rectangle rect: er){rect.setFill(Color.BLACK);}
                    for(Rectangle rect: mr){rect.setFill(Color.BLACK);}
                    for(Rectangle rect: hr){rect.setFill(Color.BLACK);}
                    mode = 3;
                }

            }
        });

        new AnimationTimer()       //Main animation loop
        {
            public void handle(long currentNanoTime)
            {
                DetermineForce(ball, sources);
                ball.change();
                ball.setCenterX(ball.locX);
                ball.setCenterY(ball.locY);
                collisionChecker(er,mr,hr,ball,mode);
            }
        }.start();

    }

    private void DetermineForce(Electron elec, ArrayList<Source> sources){
        for(Source source: sources){
            double r = Math.sqrt(Math.pow(elec.locX - source.getCenterX(),2)+Math.pow(elec.locY - source.getCenterY(),2))/1.0;
            if(r<14){   //collision check
                System.exit(0);
            }
            double sinus = (source.getCenterX() - elec.locX)/r;
            double cosin = (source.getCenterY() - elec.locY)/r;
            if(source.sign == elec.sign) {
                elec.accX -= (sinus / Math.pow(r, 2)) * k;
                elec.accY -= (cosin / Math.pow(r, 2)) * k;
            }
            else{
                elec.accX += (sinus / Math.pow(r, 2)) * k;
                elec.accY += (cosin / Math.pow(r, 2)) * k;
            }
        }
    }

    private void addRectangles(ArrayList<Rectangle> er ,ArrayList<Rectangle> mr,ArrayList<Rectangle> hr){
        Rectangle rec1 = new Rectangle(300,100,3,700);
        Rectangle rec2 = new Rectangle(500,100,3,700);
        Rectangle rec3 = new Rectangle(700,100,3,700);
        er.add(rec3);
        mr.add(rec2);
        hr.add(rec1);
    }

    private void collisionChecker(ArrayList<Rectangle> er ,ArrayList<Rectangle> mr,ArrayList<Rectangle> hr, Electron ball, int mode){
        if (mode > 0){
            for(Rectangle rect: er){
                Shape intersect = Shape.intersect(rect, ball);
                if (intersect.getBoundsInLocal().getWidth() != -1) {
                    System.exit(0);
                }
            }
        }
        if (mode > 1){
            for(Rectangle rect: mr){
                Shape intersect = Shape.intersect(rect, ball);
                if (intersect.getBoundsInLocal().getWidth() != -1) {
                    System.exit(0);
                }
            }
        }
        if (mode == 3){
            for(Rectangle rect: hr){
                Shape intersect = Shape.intersect(rect, ball);
                if (intersect.getBoundsInLocal().getWidth() != -1) {
                    System.exit(0);
                }
            }
        }
    }
}
