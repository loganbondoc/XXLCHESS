package XXLChess;

//import org.reflections.Reflections;
//import org.reflections.scanners.Scanners;
import processing.core.PApplet;
import processing.core.PImage;
import processing.data.JSONObject;
import processing.data.JSONArray;
import processing.core.PFont;
import processing.event.MouseEvent;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.awt.Font;
import java.io.*;
import java.util.*;

public class App extends PApplet {

    public static final int SPRITESIZE = 480;
    public static final int CELLSIZE = 48;
    public static final int SIDEBAR = 120;
    public static final int BOARD_WIDTH = 14;

    public static int WIDTH = CELLSIZE*BOARD_WIDTH+SIDEBAR;
    public static int HEIGHT = BOARD_WIDTH*CELLSIZE;

    public static final int FPS = 60;
	
    public String configPath;

    // App happens before main
    
    public App() {
        this.configPath = "config.json";
    }

    /**
     * Initialise the setting of the window size.
    */
    public void settings() {
        size(WIDTH, HEIGHT);
    }

    /**
     * Load all resources such as images. Initialise the elements such as the player, enemies and map elements.
    */
    public void setup() {
        frameRate(FPS);

        // Load images during setup

        PImage spr = loadImage("src/main/resources/XXLChess/"+"b-rook.png");

		// load config
        JSONObject conf = loadJSONObject(new File(this.configPath));
        
    }

    /**
     * Receive key pressed signal from the keyboard.
    */
    public void keyPressed(){


    }
    
    /**
     * Receive key released signal from the keyboard.
    */
    public void keyReleased(){

    }

    @Override
    public void mouseClicked(MouseEvent e) {
        
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        
    }

    /**
     * Draw all elements in the game by current frame. 
    */
    public void draw() {
        boolean white = true;
        noStroke();

        for (int y = 0; y < CELLSIZE * BOARD_WIDTH; y += CELLSIZE){
            for (int x = 0; x < CELLSIZE * BOARD_WIDTH; x += CELLSIZE){
                
                if (white == false){
                    fill(181, 136, 99);
                    white = true;
                } else {
                    fill(240, 217, 181);
                    white = false;
                }

                rect(x, y, CELLSIZE, CELLSIZE);
            }
            if (white == false){
                white = true;
            } else {
                white = false; 
            }
        }
    }
	
	// Add any additional methods or attributes you want. Please put classes in different files.


    public static void main(String[] args) {
        PApplet.main("XXLChess.App");
        // create board
        Board board = new Board();
        board.makeBoard();
        board.displayPieces();
        // System.out.println(board.boardArray);
    }

}
