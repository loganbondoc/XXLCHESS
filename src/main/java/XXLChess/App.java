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

    public ArrayList<ArrayList<Piece>> boardArray; 

    // boolean for if your choosing a piece to move or if you are moving a piece
    public boolean choosingPiece = true;
    public Piece selectedPiece;
    public boolean yourTurn = true;
    // public int xPos;
    // public int yPos;

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
        // building the board based on "level1.txt"
        try {
            // int success = 0;
            File f = new File("level1.txt");
            Scanner scan = new Scanner(f);

            boardArray = new ArrayList<ArrayList<Piece>>();

            for(int y = 0; y < 14; y++) { // reading vertically
                String s = scan.nextLine();
                System.out.println(s);

                // if an empty line, skip to next line and add empty row to array
                if (s.isEmpty()){
                    ArrayList<Piece> emptyRow = new ArrayList<Piece>();
                    for (int i = 0; i < 14; i++){
                        emptyRow.add(null);
                        System.out.println("added empty cell!");
                    }
                    boardArray.add(emptyRow);
                    System.out.println("emptyRow!");
                    continue;
                }

                ArrayList<Piece> boardRow = new ArrayList<>();
                for (int x = 0; x < 14; x++){ // reading horizontally
                    char c = s.charAt(x);
                        // initiates new pieces, assigns sprites to each piece and adds them to array
                        switch (c){
                            case 'R':
                                Piece bRook = new Rook("black", (x * CELLSIZE), (y * CELLSIZE));
                                bRook.setSprite(loadImage("src/main/resources/XXLChess/b-rook.png"));
                                boardRow.add(bRook);
                                System.out.println("added black rook");
                                break;
                            case 'r':
                                Piece wRook = new Rook("white", (x * CELLSIZE), (y * CELLSIZE));
                                wRook.setSprite(loadImage("src/main/resources/XXLChess/w-rook.png"));
                                boardRow.add(wRook);
                                System.out.println("added white rook");
                                break;
                            default:
                                boardRow.add(null);
                                System.out.println("added empty piece");
                                break;
                        }
                }
                // pieceNum++;
                boardArray.add(boardRow);
                System.out.println("onto the next part!");
                // success++;
                System.out.println(boardRow);
                System.out.println(boardRow.size());
            }
                // if is a certain type of piece, create that piece
                // place in a certain point on the board
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }



        // // Load images during setup
        
        // for(int i = 0; i < 14; i++){
        //     for(int j = 0; j < 14; j++){
        //         Piece piece = board.boardArray.get(i).get(j);
        //         System.out.println(piece.getClass());
        //         // switch(piece.getClass()){
        //         //     case("class Rook"):
        //         //         this.piece.setSprite(this.loadImage("src/main/resources/XXLChess/"+"b-rook.png"));
        //         //         break;
        //         // }
        //     }
        // }
        
        
        // PImage spr = loadImage("src/main/resources/XXLChess/"+"b-rook.png")

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
    public void mousePressed(MouseEvent e) {
        
        System.out.println("Real mouse position is" + (mouseX) + " , " + (mouseY));
        int xPos = mouseX / CELLSIZE;
        int yPos = mouseY / CELLSIZE;
       
    
        // Accessing selected piece
        // indexes between the drawn boardtiles and boardArray are different, need to +1 for tile
        System.out.println("Selected square is: " + (xPos + 1) + ", "+ (yPos + 1));

        // if a piece is chosen and choosing a tile to move to
        if (choosingPiece == false) {
            System.out.println("moving a piece");
            int oldSpotX = (selectedPiece.getX()/CELLSIZE);
            int oldSpotY = (selectedPiece.getY()/CELLSIZE);
            
            // if they selected an empty tile
            if (boardArray.get(yPos).get(xPos) == null){
                // replace selectedPiece with null and move to new spot in boardArray
                boardArray.get(oldSpotY).set(oldSpotX, null);
                boardArray.get(yPos).set(xPos, selectedPiece);
                
                System.out.println((oldSpotX + 1) + "," + (oldSpotY + 1) + " is now null");
                System.out.println(selectedPiece + "is now at " + (xPos + 1) + "," + (yPos + 1));

                //set new coords
                selectedPiece.setX(xPos * CELLSIZE);
                selectedPiece.setY(yPos * CELLSIZE);
                System.out.println("was moved!");
                choosingPiece = true;

            // if they selected a tile that another piece is on
            // if its your piece, cannot move
            } else if (selectedPiece.getColour() == "white"){
                System.out.println("ur blocked");
                choosingPiece = true;

            // if its their piece... KILL
            } else if (selectedPiece.getColour() == "black"){
                System.out.println("KILL");
                choosingPiece = true;

            } else {
                System.out.println("HOW DID YOU EVEN GET HERE");
            }
        
        // if choosing a piece
        } else {
            selectedPiece = boardArray.get(yPos).get(xPos);
            System.out.println(selectedPiece);
            
            // if they chose an empty tile
            if(selectedPiece == null){
                choosingPiece = true;
            
            // if you choose a tile with your thing on it
            } else if(selectedPiece.getColour() == "white") {
                // figure out how to colour tile green
                System.out.println("piece was chosen");
                choosingPiece = false;
            
            // if you choose a tile with someone elses piece on it
            } else {
                choosingPiece = true;
                System.out.println("not ur piece");
            }
        }




        // find mouse coordinates of where clicked happened
            // click that same piece again and it'll cancel
        // need to average it out between a certain coordinate
        // take the piece that was clicked
            // if there is no piece that was clicked change boolean do nothing

    }

    @Override
    public void mouseDragged(MouseEvent e) {
        
    }

    /**
     * Draw all elements in the game by current frame. 
    */
    public void draw() {
       
        // drawing board
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
        
        // drawing green rect of selected piece
        if (choosingPiece == false && selectedPiece.getColour() == "white"){
            fill(105, 138, 76);
            rect(selectedPiece.getX(), selectedPiece.getY(), CELLSIZE, CELLSIZE);
        }

        // drawing pieces to board
        for(int i = 0; i < 14; i++){
            for(int j = 0; j < 14; j++){
                Piece cell = boardArray.get(i).get(j);
                if(cell == null){
                    continue;
                }
                cell.tick();
                cell.draw(this);
            }
        }
    }
	
	// Add any additional methods or attributes you want. Please put classes in different files.

    public static void main(String[] args) {
        PApplet.main("XXLChess.App");
    }

}
