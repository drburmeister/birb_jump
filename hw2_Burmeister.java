package HW2_Burmeister;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * Pong.java
 *
 * Pong game for two human players.
 *
 * @author mike slattery
 * @version jan 2016
 */
public class hw2_Burmeister extends Application {
	final String appName = "Spike Jump";
	final int FPS = 30; // frames per second
	final static int WIDTH = 600;
	final static int HEIGHT = 500;
	final static int EDGE = 30;
	final static int X_SPEED = 3;
	final static int LEFTEDGE = WIDTH/5;
	final static int RIGHTEDGE = (4*WIDTH)/5;
	int SPIKE_SIZE = 25; //size of spikes
	
	public static final int BIRB = 0;
	/**
	 * Set up initial data structures/values
	 */	
	int[] deadZones_x = new int[2*(HEIGHT/SPIKE_SIZE)];
	int[] deadZones_y = new int[2*(HEIGHT/SPIKE_SIZE)];
	Birb birb;
	
	Sprite[] sprites = new Sprite[3];
	
	void initialize()
	{
		sprites[BIRB] = birb = new Birb();
		birb.setPosition(HEIGHT/2, WIDTH/2);
		birb.setVelocity(X_SPEED, Sprite.JUMP);
		birb.resume();
	}
	
	void setHandlers(Scene scene)
	{
		scene.setOnKeyPressed(
			e -> { 
					if (e.getCode() == KeyCode.SPACE) birb.birbBounce();
				}
		);
		
		scene.setOnKeyReleased(
				e -> { }
			);
	}

	/**
	 *  Update variables for one time step
	 */
	public void update()
	{
		birb.updateSprite();
	}
	
	/**
	 *  Draw the game world
	 */
	void render(GraphicsContext gc) {
		// fill background and set play area

		gc.setFill(Color.WHITE);
		gc.fillRect(0, 0, WIDTH, HEIGHT);
		gc.setFill(Color.BLACK);
		gc.fillRect(0, 0, LEFTEDGE, HEIGHT);
		gc.fillRect(RIGHTEDGE, 0, LEFTEDGE, HEIGHT);
		//generate spike fields
		int dead_n = 0;
		for (int i = 0; (i*SPIKE_SIZE) < HEIGHT; i++) {
			if(i%2 == 0 || i%5 == 0) {
				fillEqTriangle(LEFTEDGE, (i*SPIKE_SIZE), SPIKE_SIZE, 0, gc);
				deadZones_x[dead_n] = LEFTEDGE;
				deadZones_y[dead_n] = (i*SPIKE_SIZE);
				dead_n++;
			}
			if(i%2 == 0 || i%3 == 0) {
				fillEqTriangle(RIGHTEDGE, (i*SPIKE_SIZE), SPIKE_SIZE, 180, gc);
				deadZones_x[dead_n] = LEFTEDGE;
				deadZones_y[dead_n] = (i*SPIKE_SIZE);
				dead_n++;
			}
		}
		birb.render(gc);
	}

	/*
	 * Begin boiler-plate code...
	 * [Animation and events with initialization]
	 */
	public static void main(String[] args) {
		launch(args);
	}
	
	@Override
	public void start(Stage theStage) {
		theStage.setTitle(appName);

		Group root = new Group();
		Scene theScene = new Scene(root);
		theStage.setScene(theScene);

		Canvas canvas = new Canvas(WIDTH, HEIGHT);
		root.getChildren().add(canvas);

		GraphicsContext gc = canvas.getGraphicsContext2D();

		// Initial setup
		initialize();
		setHandlers(theScene);
		
		// Setup and start animation loop (Timeline)
		KeyFrame kf = new KeyFrame(Duration.millis(1000 / FPS),
				e -> {
					// update position
					update();
					// draw frame
					render(gc);
				}
			);
		Timeline mainLoop = new Timeline(kf);
		mainLoop.setCycleCount(Animation.INDEFINITE);
		mainLoop.play();

		theStage.show();
	}
	/*
	 * ... End boiler-plate code
	 */
	
	
	public static void fillEqTriangle(int x, int y, int size, int angle, GraphicsContext gc) {
		if(angle == 0) gc.fillPolygon(new double[] {x, x, x+size}, new double[] {y,y+size,y+size/2}, 3);
		else if(angle == 90)  gc.fillPolygon(new double[] {x, x+size, x+size/2}, new double[] {y,y,y-size}, 3);
		if(angle == 180) gc.fillPolygon(new double[] {x, x, x-size}, new double[] {y,y+size,y+size/2}, 3);
		else if(angle == 270)  gc.fillPolygon(new double[] {x, x+size, x+size/2}, new double[] {y,y,y+size}, 3);
	}
}