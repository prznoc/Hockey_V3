package sample;

import javafx.animation.AnimationTimer;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.text.Text;

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
    private int diff = 0;  //current difficulty

    @FXML private Text messages;
    @FXML private Text mass_counter;
    @FXML private Slider mass_changer;

    @FXML private Button start;
    @FXML private Button reset;
    @FXML private Button pause;
    private int mode = 0; // 0 - stop, 1 - work, 2 - win/lost

    private ArrayList<Rectangle> er = new ArrayList<>();
    private ArrayList<Rectangle> mr = new ArrayList<>();
    private ArrayList<Rectangle> hr = new ArrayList<>();

    @FXML private Rectangle barrier1;
    @FXML private Rectangle barrier21;
    @FXML private Rectangle barrier22;
    @FXML private Rectangle barrier31;
    @FXML private Rectangle barrier32;
    @FXML private Rectangle barrier33;
    @FXML private Rectangle barrier34;
    @FXML private Rectangle barrier35;
    @FXML private Rectangle barrier36;

    @FXML private ToggleButton ball_sign_toggle;
    private Electron ball = new Electron( 7 );

    @Override
    public void initialize(URL url, ResourceBundle resource){

         //creating ball
        field.getChildren().add( ball );
        ArrayList<Source> sources = new ArrayList<>();  //creating array for sources

        peaceful.setToggleGroup(difficulty);
        easy.setToggleGroup(difficulty);
        medium.setToggleGroup(difficulty);
        hard.setToggleGroup(difficulty);

        Rectangle post1 = new Rectangle(1100,340,50,5);
        Rectangle post2 = new Rectangle(1100,455,50,5);
        Rectangle backpost = new Rectangle (1145, 345, 5, 110);
        field.getChildren().add(post1);
        field.getChildren().add(post2);
        field.getChildren().add(backpost);

        addRectangles();
        for(Rectangle rect: er){rect.setFill(Color.TRANSPARENT);}
        for(Rectangle rect: mr){rect.setFill(Color.TRANSPARENT);}
        for(Rectangle rect: hr){rect.setFill(Color.TRANSPARENT);}

        field.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent me) -> {      //creating sources with mouseclicks
            if (! (me.getPickResult().getIntersectedNode() instanceof Source)) {
                if (me.getButton().equals(MouseButton.PRIMARY)) {
                    Source source = new Source(me.getX(), me.getY(), 7, true);
                    sources.add(source);
                    addEventHandler(source);
                    field.getChildren().add(source);


                }
                if (me.getButton().equals(MouseButton.SECONDARY)) {
                    Source source = new Source(me.getX(), me.getY(), 7, false);
                    sources.add(source);
                    addEventHandler(source);
                    field.getChildren().add(source);
                }
            }
        });



        difficulty.selectedToggleProperty().addListener(new ChangeListener<Toggle>(){  //for changing difficulty
            public void changed(ObservableValue<? extends Toggle> ov,
                                Toggle old_toggle, Toggle new_toggle) {

                if (difficulty.getSelectedToggle() == peaceful) {
                    for(Rectangle rect: er){rect.setFill(Color.TRANSPARENT);}
                    for(Rectangle rect: mr){rect.setFill(Color.TRANSPARENT);}
                    for(Rectangle rect: hr){rect.setFill(Color.TRANSPARENT);}
                    diff = 0;
                }
                if (difficulty.getSelectedToggle() == easy) {
                    for(Rectangle rect: er){rect.setFill(Color.BLACK);}
                    for(Rectangle rect: mr){rect.setFill(Color.TRANSPARENT);}
                    for(Rectangle rect: hr){rect.setFill(Color.TRANSPARENT);}
                    diff = 1;
                }
                else if (difficulty.getSelectedToggle() == medium) {
                    for(Rectangle rect: er){rect.setFill(Color.TRANSPARENT);}
                    for(Rectangle rect: mr){rect.setFill(Color.BLACK);}
                    for(Rectangle rect: hr){rect.setFill(Color.TRANSPARENT);}
                    diff = 2;
                }
                else if (difficulty.getSelectedToggle() == hard) {
                    for(Rectangle rect: er){rect.setFill(Color.TRANSPARENT);}
                    for(Rectangle rect: mr){rect.setFill(Color.TRANSPARENT);}
                    for(Rectangle rect: hr){rect.setFill(Color.BLACK);}
                    diff = 3;
                }

            }
        });


        mass_changer.valueProperty().addListener(new ChangeListener<Number>() {   //for changing mass, do ewentualnej poprawy
            public void changed(ObservableValue<? extends Number> ov,
                                Number old_val, Number new_val) {
                ball.mass = new_val.intValue();   //cast new_val to int
                mass_counter.setText(String.valueOf(ball.mass));
            }
        });
        AnimationTimer timer = new AnimationTimer()       //Main animation loop
        {
            public void handle(long currentNanoTime)
            {
                double[] results = DetermineForce(sources, this);
                change_electron(results);
                ball.change();
                ball.setCenterX(ball.locX);
                ball.setCenterY(ball.locY);
                boundary_checker(this);
                collision_checker(this);
                goal_check(post1,post2,backpost,this);
            }
        };


        start.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                if(mode != 2) {
                    timer.start();
                    mode = 1;
                }
            }
        });

        pause.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                if (mode != 2) {
                    timer.stop();
                    mode = 0;
                }
            }
        });

        reset.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                ball.reset();
                ball.setCenterX(ball.locX);
                ball.setCenterY(ball.locY);
                for(Source source: sources){
                    field.getChildren().remove(source);
                }
                sources.clear();
                messages.setText("");
                mode = 0;
                timer.stop();
            }
        });
        ball_sign_toggle.setOnAction(e -> {
            ball.change_sign();
        });

    }

    private void addEventHandler(Node node) {
        node.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent me) -> {
            field.getChildren().remove(node);
        });
    }

    private void display_arrow(){
        ;
    }

    private void change_electron(double[] results){
        ball.accX = results[0];
        ball.accY = results[1];

    }

    private double[] DetermineForce(ArrayList<Source> sources, AnimationTimer timer){
        double[] results = {0,0};
        for(Source source: sources){
            final int k = 100000000;   //constant required for long range
            double r = Math.sqrt(Math.pow(ball.locX - source.getCenterX(),2)+Math.pow(ball.locY - source.getCenterY(),2))/1.0;
            if(r<14){   //collision check
                timer.stop();
                messages.setText("You Lost");
                mode = 2;
            }
            double sinus = (source.getCenterX() - ball.locX)/r;
            double cosin = (source.getCenterY() - ball.locY)/r;

            if(source.sign == ball.sign) {
                results[0] -= (sinus / (Math.pow(r, 2)*ball.mass)) * k;
                results[1] -= (cosin / (Math.pow(r, 2)*ball.mass)) * k;
            }
            else{
                results[0] += (sinus / (Math.pow(r, 2)*ball.mass)) * k;
                results[1] += (cosin / (Math.pow(r, 2)*ball.mass)) * k;
            }
        }
        return results;
    }

    private void addRectangles(){
        er.add(barrier1);
        mr.add(barrier21);
        mr.add(barrier22);
        hr.add(barrier31);
        hr.add(barrier32);
        hr.add(barrier33);
        hr.add(barrier34);
        hr.add(barrier35);
        hr.add(barrier36);
    }

    private void collision_checker(AnimationTimer timer){
        if (diff == 1){
            for(Rectangle rect: er){
                Shape intersect = Shape.intersect(rect, ball);
                if (intersect.getBoundsInLocal().getWidth() != -1) {
                    timer.stop();
                    messages.setText("You Lost");
                    mode = 2;
                }
            }
        }
        else if (diff == 2){
            for(Rectangle rect: mr){
                Shape intersect = Shape.intersect(rect, ball);
                if (intersect.getBoundsInLocal().getWidth() != -1) {
                    timer.stop();
                    messages.setText("You Lost");
                    mode = 2;
                }
            }
        }
        else if (diff == 3){
            for(Rectangle rect: hr){
                Shape intersect = Shape.intersect(rect, ball);
                if (intersect.getBoundsInLocal().getWidth() != -1) {
                    timer.stop();
                    messages.setText("You Lost");
                    mode = 2;
                }
            }
        }
    }

    private void goal_check(Rectangle post1, Rectangle post2, Rectangle backpost, AnimationTimer timer){
        Shape intersect1 = Shape.intersect(post1, ball);
        Shape intersect2 = Shape.intersect(post2, ball);
        Shape intersect_back = Shape.intersect(backpost, ball);
        if ((intersect1.getBoundsInLocal().getWidth() != -1) || (intersect2.getBoundsInLocal().getWidth() != -1)) {
            ball.bounce();
        }
        else if (intersect_back.getBoundsInLocal().getWidth() != -1) {
            if (ball.locX > 1150) ball.pounce();
            else win(timer);
        }
    }

    private void win(AnimationTimer timer){
        timer.stop();
        messages.setText("You Win");
        mode = 2;
    }

    private void boundary_checker(AnimationTimer timer){
        if(ball.locX < 0 || ball.locX > 1200 || ball.locY < 0 || ball.locY > 800){
            timer.stop();
            messages.setText("You lost");
            mode = 2;
        }

    }


}
