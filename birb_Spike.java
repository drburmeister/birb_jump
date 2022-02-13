package BirbSpike;

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
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Random;

/**
 * birb_Spike.java
 *
 * Single player game where you try to collect coins while avoiding the wall spikes.
 * *NOTE* Collisions with spikes are very finicky in current state
 * 	 	  as collision detection with triangles was not great with this implementation
 *
 * @author Dennis Burmeister (Marquette University)
 */
public class birb_Spike extends Application {
	//Variables used throughout the program
	final String appName = "Spike Jump";
	final int FPS = 30; // frames per second
	final static int WIDTH = 600;
	final static int HEIGHT = 500;
	final static int EDGE = 30;
	final static int X_SPEED = 10;
	final static int LEFTEDGE = WIDTH/5;
	final static int RIGHTEDGE = (4*WIDTH)/5;
	final static int SPIKE_SIZE = 50;//size of spikes
	public static final int BIRB = 0;
	public static final int COIN = 1;
	boolean pause = true;
	int currScore = 0;
	static int highScore = 0;
	int[] deadZones_x = new int[2*(HEIGHT/SPIKE_SIZE)];
	int[] deadZones_y = new int[2*(HEIGHT/SPIKE_SIZE)];
	int mod1 = 2;
	int mod2 = 2;
	
	//Objects used in the program
	Birb birb;
	Coin coin;
	Sprite[] sprites = new Sprite[2];
	//RNG
	Random rand = new Random();
	//Method to (re)set everything to a default state (besides high score)
	void initialize()
	{
		//Player initial position and motion
		sprites[BIRB] = birb = new Birb();
		birb.setPosition(HEIGHT/2, WIDTH/2);
		birb.setVelocity(X_SPEED, Sprite.JUMP);
		birb.resume();
		
		//Coin/Target initial posistion
		sprites[COIN] = coin = new Coin();
		coin.setPosition((3*WIDTH/5), HEIGHT/3);
		coin.resume();
		
		//reset score
		currScore = 0;
		//Set pause flag to true
		pause = true;
	}
	//Method to handle user inputs
	void setHandlers(Scene scene)
	{
		scene.setOnKeyPressed(
			e -> { 
				//When spacebar is pressed the game should start if its paused and the player should "jump"
					if (e.getCode() == KeyCode.SPACE) {
						birb.birbBounce();
						pause = false;
					}
					//When 'p' is pressed the game should pause
					if (e.getCode() == KeyCode.P) pause = true;
				}
		);
	}

	/**
	 *  Update variables for one time step
	 */
	public void update()
	{	
		//The game should not update if the pause flag is true
		if (!pause) {
			//enact gravity on player
			birb.updatePosition();
			
			//collision detection for player and spikes, if there is a collision restart the game
			for (int i = 0; i < 2*(HEIGHT/SPIKE_SIZE); i++) {
				if (((i%2 == 0) && (birb.x) >= deadZones_x[i]) && ((birb.x) <= (deadZones_x[i]+SPIKE_SIZE)) &&
						(((birb.y) >= deadZones_y[i]) && ((birb.y) <= (deadZones_y[i]+SPIKE_SIZE)))) {
					initialize();
				}
				else if (((i%2 == 1) && (birb.x) <= deadZones_x[i]) && ((birb.x) >= (deadZones_x[i]-SPIKE_SIZE)) &&
						(((birb.y) >= deadZones_y[i]) && ((birb.y) <= (deadZones_y[i]+SPIKE_SIZE)))) {
					initialize();
				}
			}
			//collision detection for the player and the coin
			if (birb.isCloserThan(coin, Coin.RADIUS+Birb.RADIUS)) {
				//if there the player hits a coin the score should increase and a new coin should randomly spawn
				coin.setPosition((rand.nextInt(RIGHTEDGE - (2*LEFTEDGE + SPIKE_SIZE)) + LEFTEDGE + 2*SPIKE_SIZE),
						((rand.nextInt((3*HEIGHT)/5)) + HEIGHT/5));
				currScore++;
				//Check if a high score has been hit and if so update the text file
				if(currScore > highScore) {
					highScore++;
				    // append the last score to the end of the file
				    try {
				        BufferedWriter out = new BufferedWriter(new FileWriter("src/BirbSpike/HighScore.txt", true));
				        out.newLine();
				        out.append("" + currScore);
				        out.close();
	
				    } catch (IOException ex1) {
				        System.out.printf("ERROR writing score to file: %s\n", ex1);
				    }
				}
			}
		}
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
		//two flags to randomly generate spike locations on the opposite wall every tim a player hits a wall
		if (Sprite.r_bound) mod1 = (rand.nextInt(3)+2);
		if (Sprite.l_bound) mod2 = (rand.nextInt(3)+2);
		//generate spike fields
		int dead_n = 0;
		for (int i = 0; (i*SPIKE_SIZE) < HEIGHT; i++) {
			//left wall
			if(i%mod1 == 0 || i%3 == 0) { //always have mod3 so the corners always have spikes (balance change)
				fillEqTriangle(LEFTEDGE, (i*SPIKE_SIZE), SPIKE_SIZE, 0, gc);
				//save the locations of the spikes for collision detection
				deadZones_x[dead_n] = LEFTEDGE;
				deadZones_y[dead_n] = (i*SPIKE_SIZE);
				dead_n++;
				//reset player/wall collision flag so spikes aren't constantly changing
				Sprite.r_bound = false;
			}
			//right wall
			if(i%mod2 == 0 || i%3 == 0) {
				fillEqTriangle(RIGHTEDGE, (i*SPIKE_SIZE), SPIKE_SIZE, 180, gc);
				deadZones_x[dead_n] = RIGHTEDGE;
				deadZones_y[dead_n] = (i*SPIKE_SIZE);
				dead_n++;
				Sprite.l_bound = false;
			}
		}
		//draw sprites
		coin.render(gc);
		birb.render(gc);
		//put "tutorial" on screen whenever game is paused
		if (pause) {
			gc.setFill(Color.BLACK);
			gc.fillText("Press [SPACE] to Jump/Play", WIDTH/2 - 75, 15);
		}
		//Draw scores and author name
		gc.setFill(Color.WHITE);
		gc.fillText("Current Score: " + currScore, 5, 15);
		gc.fillText("High Score: " + highScore, RIGHTEDGE+45, 15);
		gc.fillText("Made By:\nDennis Burmeister " , RIGHTEDGE+15, HEIGHT-25);

	}
	/*
	 * Begin boiler-plate code...
	 * [Animation and events with initialization]
	 */
	public static void main(String[] args) {
	    // load the high score from the .txt file
	    try {
	        BufferedReader rdr = new BufferedReader(new FileReader("src/BirbSpike/HighScore.txt"));
	        String ln = rdr.readLine();
	        while (ln != null) // go line by line
	        {
	            try {
	                highScore = Integer.parseInt(ln.trim());   // parse each line as an int
	            } catch (NumberFormatException e1) {
	            }
	            ln = rdr.readLine();
	        }
	        rdr.close();

	    } catch (IOException ex) {
	        System.err.println("ERROR reading scores from file"); //catch errors
	    }
		launch(args); //run game
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

	//Method to draw equilateral triangles with given xy_location, size, and angle
	public static void fillEqTriangle(int x, int y, int size, int angle, GraphicsContext gc) {
		if(angle == 0) gc.fillPolygon(new double[] {x, x, x+size}, new double[] {y,y+size,y+size/2}, 3);
		else if(angle == 90)  gc.fillPolygon(new double[] {x, x+size, x+size/2}, new double[] {y,y,y-size}, 3);
		if(angle == 180) gc.fillPolygon(new double[] {x, x, x-size}, new double[] {y,y+size,y+size/2}, 3);
		else if(angle == 270)  gc.fillPolygon(new double[] {x, x+size, x+size/2}, new double[] {y,y,y+size}, 3);
	}
}