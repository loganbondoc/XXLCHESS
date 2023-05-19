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

    // Drawing Elements
    public static int[] lastMoves = new int[4]; // storing last moves {oldX, oldY, newX, newY}
    

    // Game Elements
    public static boolean firstMove = true;
    public static ArrayList<ArrayList<Piece>> boardArray; // nested ArrayList for 2D board
    public static boolean choosingPiece = true; // if choosing or moving a piece
    public static Piece selectedPiece; // piece that is chosen to move
    public static King bKing;
    public static King wKing;
    public static Opponent opponent;
    
    public static boolean resignation = false; // if player resigns
    public static boolean yourTurn = true;
    public static boolean checkmated = false; // only change if game has ended

    public static String restartMessage = "Press 'r' to restart the game";

    // timers for players and increments
    public int wIncrement;
    public int bIncrement;
    public int wLastTime = 0;
    public int wElapsedTime = 0;
    public int wRemainingTime; 
    public int bLastTime = 0;
    public int bElapsedTime = 0;
    public int bRemainingTime; 

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
        
        // load config
        JSONObject conf = loadJSONObject(new File(this.configPath));
        wIncrement = conf.getJSONObject("time_controls").getJSONObject("player").getInt("increment");
        bIncrement = conf.getJSONObject("time_controls").getJSONObject("cpu").getInt("increment");
        wRemainingTime = conf.getJSONObject("time_controls").getJSONObject("player").getInt("seconds");
        bRemainingTime = conf.getJSONObject("time_controls").getJSONObject("cpu").getInt("seconds");
        String difficulty = conf.getString("difficulty");

        // building the board based on file in config
        try {
            File f = new File(conf.getString("layout"));
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
                    }
                    boardArray.add(emptyRow);
                    continue;
                }

                ArrayList<Piece> boardRow = new ArrayList<>();
                for (int x = 0; x < 14; x++){ // reading horizontally
                    char c = s.charAt(x);
                        // initiates new pieces, assigns sprites to each piece and adds them to row
                        switch (c){
                            case 'R': // Black Rook
                                Piece bRook = new Rook("black", (x * CELLSIZE), (y * CELLSIZE));
                                bRook.setSprite(loadImage("src/main/resources/XXLChess/b-rook.png"));
                                boardRow.add(bRook);
                                break;
                            case 'r': // White Rook
                                Piece wRook = new Rook("white", (x * CELLSIZE), (y * CELLSIZE));
                                wRook.setSprite(loadImage("src/main/resources/XXLChess/w-rook.png"));
                                boardRow.add(wRook);
                                break;
                            case 'P': // Black Pawn
                                Piece bPawn = new Pawn("black", (x * CELLSIZE), (y * CELLSIZE));
                                bPawn.setSprite(loadImage("src/main/resources/XXLChess/b-pawn.png"));
                                boardRow.add(bPawn);
                                break;
                            case 'p': // White Pawn
                                Piece wPawn = new Pawn("white", (x * CELLSIZE), (y * CELLSIZE));
                                wPawn.setSprite(loadImage("src/main/resources/XXLChess/w-pawn.png"));
                                boardRow.add(wPawn);
                                break;
                            case 'B': // Black Bishop
                                Piece bBishop = new Bishop("black", (x * CELLSIZE), (y * CELLSIZE));
                                bBishop.setSprite(loadImage("src/main/resources/XXLChess/b-bishop.png"));
                                boardRow.add(bBishop);
                                break;
                            case 'b': // White Bishop
                                Piece wBishop = new Bishop("white", (x * CELLSIZE), (y * CELLSIZE));
                                wBishop.setSprite(loadImage("src/main/resources/XXLChess/w-bishop.png"));
                                boardRow.add(wBishop);
                                break;
                            case 'Q': // Black Queen
                                Piece bQueen = new Queen("black", (x * CELLSIZE), (y * CELLSIZE));
                                bQueen.setSprite(loadImage("src/main/resources/XXLChess/b-queen.png"));
                                boardRow.add(bQueen);
                                break;
                            case 'q': // White Queen
                                Piece wQueen = new Queen("white", (x * CELLSIZE), (y * CELLSIZE));
                                wQueen.setSprite(loadImage("src/main/resources/XXLChess/w-queen.png"));
                                boardRow.add(wQueen);
                                break;
                            case 'N': // Black Knight
                                Piece bKnight = new Knight("black", (x * CELLSIZE), (y * CELLSIZE));
                                bKnight.setSprite(loadImage("src/main/resources/XXLChess/b-knight.png"));
                                boardRow.add(bKnight);
                                break;
                            case 'n': // White Knight
                                Piece wKnight = new Knight("white", (x * CELLSIZE), (y * CELLSIZE));
                                wKnight.setSprite(loadImage("src/main/resources/XXLChess/w-knight.png"));
                                boardRow.add(wKnight);
                                break;
                            case 'H': // Black Archbishop
                                Piece bArchbishop = new Archbishop("black", (x * CELLSIZE), (y * CELLSIZE));
                                bArchbishop.setSprite(loadImage("src/main/resources/XXLChess/b-archbishop.png"));
                                boardRow.add(bArchbishop);
                                break;
                            case 'h': // White Archbishop
                                Piece wArchbishop = new Archbishop("white", (x * CELLSIZE), (y * CELLSIZE));
                                wArchbishop.setSprite(loadImage("src/main/resources/XXLChess/w-archbishop.png"));
                                boardRow.add(wArchbishop);
                                break;
                            case 'A': // Black Amazon
                                Piece bAmazon = new Amazon("black", (x * CELLSIZE), (y * CELLSIZE));
                                bAmazon.setSprite(loadImage("src/main/resources/XXLChess/b-amazon.png"));
                                boardRow.add(bAmazon);
                                break;
                            case 'a': // White Amazon
                                Piece wAmazon = new Amazon("white", (x * CELLSIZE), (y * CELLSIZE));
                                wAmazon.setSprite(loadImage("src/main/resources/XXLChess/w-amazon.png"));
                                boardRow.add(wAmazon);
                                break;
                            case 'E': // Black Chancellor
                                Piece bChancellor = new Chancellor("black", (x * CELLSIZE), (y * CELLSIZE));
                                bChancellor.setSprite(loadImage("src/main/resources/XXLChess/b-chancellor.png"));
                                boardRow.add(bChancellor);
                                break;
                            case 'e': // White Chancellor
                                Piece wChancellor = new Chancellor("white", (x * CELLSIZE), (y * CELLSIZE));
                                wChancellor.setSprite(loadImage("src/main/resources/XXLChess/w-chancellor.png"));
                                boardRow.add(wChancellor);
                                break;
                            case 'C': // Black Camel
                                Piece bCamel = new Camel("black", (x * CELLSIZE), (y * CELLSIZE));
                                bCamel.setSprite(loadImage("src/main/resources/XXLChess/b-camel.png"));
                                boardRow.add(bCamel);
                                break;
                            case 'c': // White Camel
                                Piece wCamel = new Camel("white", (x * CELLSIZE), (y * CELLSIZE));
                                wCamel.setSprite(loadImage("src/main/resources/XXLChess/w-camel.png"));
                                boardRow.add(wCamel);
                                break;
                            case 'K': // Black King
                                bKing = new King("black", (x * CELLSIZE), (y * CELLSIZE));
                                bKing.setSprite(loadImage("src/main/resources/XXLChess/b-king.png"));
                                boardRow.add(bKing);
                                break;
                            case 'k': // White King
                                wKing = new King("white", (x * CELLSIZE), (y * CELLSIZE));
                                wKing.setSprite(loadImage("src/main/resources/XXLChess/w-king.png"));
                                boardRow.add(wKing);
                                break;
                            case 'G': // Black Guard
                                Piece bGuard = new Guard("black", (x * CELLSIZE), (y * CELLSIZE));
                                bGuard.setSprite(loadImage("src/main/resources/XXLChess/b-knight-king.png"));
                                boardRow.add(bGuard);
                                break;
                            case 'g': // White Guard
                                Piece wGuard = new Guard("white", (x * CELLSIZE), (y * CELLSIZE));
                                wGuard.setSprite(loadImage("src/main/resources/XXLChess/w-knight-king.png"));
                                boardRow.add(wGuard);
                                break;
                            default: // empty cell
                                boardRow.add(null);
                                break;
                        }
                }
                boardArray.add(boardRow); // row is added to board
            }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

        // initialising opponent
        opponent = new Opponent("black", difficulty);

        // clean reset for game
        firstMove = true;
        choosingPiece = true;
        selectedPiece = null;
        yourTurn = true;
        checkmated = false;
        resignation = false;

        // reset for timer
        wLastTime = 0;
        wElapsedTime = 0;
        bLastTime = 0;
        bElapsedTime = 0;

        System.out.println("Game Loaded Successfully");
    }

    /**
     * Receive key pressed signal from the keyboard.
    */
    public void keyPressed(){
        // if player presses 'e' to resign
        if (key == 'e'){
            checkmated = true;
            resignation = true;
        }
        
        // if player presses 'r' to restart game
        if (checkmated == true && key == 'r'){
            setup();
            loop();
        }
    }
    
    /**
     * Receive key released signal from the keyboard.
    */
    public void keyReleased(){

    }

    /**
     * Player select a piece to move or a square to move selected piece to
     * CPU then selects move
    */
    @Override
    public void mousePressed(MouseEvent e) {
        
        if(bKing.isCheckmate(boardArray) == true || wKing.isCheckmate(boardArray) == true){
            checkmated = true;
        }

        // ensure click is in the bounds of board
        if (mouseX < (CELLSIZE * 14) && mouseY < (CELLSIZE * 14)){
            int xPos = mouseX / CELLSIZE;
            int yPos = mouseY / CELLSIZE;
        
        
            // Accessing selected piece
            // indexes between the drawn boardtiles and boardArray are different, need to +1 for tile
            System.out.println("Selected square is: " + (xPos + 1) + ", "+ (yPos + 1));

            // if a piece is chosen and choosing a tile to move to
            if (choosingPiece == false) {
                int oldSpotX = (selectedPiece.getX()/CELLSIZE);
                int oldSpotY = (selectedPiece.getY()/CELLSIZE);
                Piece newSelect = boardArray.get(yPos).get(xPos);
                
                // if they selected an empty tile
                if (boardArray.get(yPos).get(xPos) == null){
                    
                    // check if any piece is in the way
                    if(selectedPiece.isValidMove(xPos, yPos, boardArray) == true){
                        
                        // replace selectedPiece with null and move to new spot in boardArray
                        boardArray.get(oldSpotY).set(oldSpotX, null);
                        boardArray.get(yPos).set(xPos, selectedPiece);
                        System.out.println(selectedPiece + " is now at " + (xPos + 1) + "," + (yPos + 1));


                        // set coords for highlighs
                        lastMoves[0] = oldSpotX * 48;
                        lastMoves[1] = oldSpotY * 48;
                        lastMoves[2] = xPos * 48;
                        lastMoves[3] = yPos * 48;


                        //set new coords
                        selectedPiece.setX(xPos * CELLSIZE);
                        selectedPiece.setY(yPos * CELLSIZE);
                        selectedPiece.setFirstMove(false);
                        choosingPiece = true;
                        selectedPiece = null;
                        firstMove = false;
                        yourTurn = false;
                        wRemainingTime += wIncrement;
                    
                    } else {
                        System.out.println("not a valid move :(");
                        choosingPiece = true;
                        selectedPiece = null;
                        yourTurn = true;
                    }
                    
                // if they selected a tile that another piece is on
                // if its your piece, cannot move
                } else if (newSelect.getColour() == "white"){
                    System.out.println("blocked");
                    choosingPiece = true;
                    selectedPiece = null;
                    yourTurn = true;

                // if its their piece... KILL
                } else if (newSelect.getColour() == "black"){
                    System.out.println("KILL");
                    if(selectedPiece.isValidMove(xPos, yPos, boardArray) == true){
                        // replace selectedPiece with null and move to new spot in boardArray
                        boardArray.get(oldSpotY).set(oldSpotX, null);
                        boardArray.get(yPos).set(xPos, selectedPiece);
                        System.out.println(selectedPiece + "is now at " + (xPos + 1) + "," + (yPos + 1));

                        // set coords for highlighs
                        lastMoves[0] = oldSpotX * 48;
                        lastMoves[1] = oldSpotY * 48;
                        lastMoves[2] = xPos * 48;
                        lastMoves[3] = yPos * 48;

                        //set new coords
                        selectedPiece.setX(xPos * CELLSIZE);
                        selectedPiece.setY(yPos * CELLSIZE);

                        selectedPiece.setFirstMove(false);
                        choosingPiece = true;
                        selectedPiece = null;
                        firstMove = false;
                        yourTurn = false;
                        wRemainingTime += wIncrement;
                    }

                } else {
                    System.out.println("HOW DID YOU EVEN GET HERE");
                }
                
                if(bKing.isCheckmate(boardArray) == true || wKing.isCheckmate(boardArray) == true){
                    checkmated = true;
                }

                // opponent makes move
                if(yourTurn == false){
                    System.out.println("making a move");
                
                    // if its checkmate and king is under threat, CPU loses
                    if(bKing.isCheck(boardArray) == true && bKing.isCheckmate(boardArray) == true){
                        checkmated = true;
                    // if bKing cannot move anywhere but it is not under check, stalemate
                    } else if(bKing.isCheck(boardArray) == false && bKing.isCheckmate(boardArray) == true){
                        checkmated = true;
                    // if in check and not checkmate, or neither, make a normal move
                    } else {
                        OpponentMove move = opponent.calcMove(boardArray, bKing);
                        
                        int oldX = (move.getPiece().getX())/CELLSIZE;
                        int oldY = (move.getPiece().getY())/CELLSIZE;
                        int newX = move.getX();
                        int newY = move.getY();

                        boardArray.get(oldY).set(oldX, null);
                        boardArray.get(newY).set(newX, move.getPiece());

                        // set coords for highlighs
                        lastMoves[0] = oldX * 48;
                        lastMoves[1] = oldY * 48;
                        lastMoves[2] = newX * 48;
                        lastMoves[3] = newY * 48;
                        
                        move.getPiece().setX(newX * CELLSIZE);
                        move.getPiece().setY(newY * CELLSIZE);
                        move.getPiece().setFirstMove(false);
                        System.out.println("Opponent made move");
                        yourTurn = true;
                        bRemainingTime += bIncrement;
                    }
                }
                
            
            // if choosing a piece
            } else {
                selectedPiece = boardArray.get(yPos).get(xPos);
                
                // if they chose an empty tile
                if(selectedPiece == null){
                    choosingPiece = true;
                    yourTurn = true;
                
                // if you choose a tile with your thing on it
                } else if(selectedPiece.getColour() == "white") {
                    choosingPiece = false;
                    yourTurn = true;
                
                // if you choose a tile with someone elses piece on it
                } else {
                    choosingPiece = true;
                    selectedPiece = null;
                    System.out.println("That's not your piece!");
                    yourTurn = true;
                }
            }
        }
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
                
                // to get checkerboard pattern
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

        // last move highlight
        if (firstMove == false){
            fill(170, 162, 58);
            rect(lastMoves[0], lastMoves[1], CELLSIZE, CELLSIZE);
            rect(lastMoves[2], lastMoves[3], CELLSIZE, CELLSIZE);
        }

        // piece highlights
        if (choosingPiece == false && selectedPiece.getColour() == "white"){
            // of selected piece, check on board all available moves
            for (int i = 0; i < 14; i++){
                for (int j = 0; j < 14; j++){
                    if(selectedPiece.isValidMove(j, i, boardArray) == true){ 
                        
                        // if its empty make it blue
                        if (boardArray.get(i).get(j) == null){
                            fill(137, 207, 240, 155);
                            rect(j * CELLSIZE, i * CELLSIZE, CELLSIZE, CELLSIZE);
                        }
                        
                        // if piece is white dont fill
                        if (boardArray.get(i).get(j) != null && boardArray.get(i).get(j).getColour() == "white"){
                            continue;
                        }
        
                        // if piece is black fill red
                        if (boardArray.get(i).get(j) != null && boardArray.get(i).get(j).getColour() == "black"){
                            fill(215, 0, 0, 90);
                            rect(j * CELLSIZE, i * CELLSIZE, CELLSIZE, CELLSIZE);
                        } 
                    } 
                }
            }
            // drawing green highlight of selected piece
            fill(105, 138, 76);
            rect(selectedPiece.getX(), selectedPiece.getY(), CELLSIZE, CELLSIZE);
        }

        // drawing all pieces to board
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

        // drawing timer
        int currentTime = millis();
        
        // for white timer
        if (yourTurn == true) { 
            wElapsedTime += currentTime - wLastTime;
            wLastTime = currentTime;
            wRemainingTime = 180 - (wElapsedTime / 1000);
        }

        // for black timer
        if (yourTurn == false) { 
            bElapsedTime += currentTime - bLastTime;
            bLastTime = currentTime;
            bRemainingTime = 180 - (bElapsedTime / 1000);
        }
        
        // for white's timer
        fill(204,204,204);
        rect((BOARD_WIDTH * CELLSIZE) + 10, 576, 110, 96);
        fill(0, 0, 0);
        textSize(30);
        String wTimeString = String.format("%02d:%02d", wRemainingTime / 60, wRemainingTime % 60);
        text(wTimeString, (BOARD_WIDTH * CELLSIZE) + 10, 624);

        // for black's timer
        fill(204,204,204);
        rect((BOARD_WIDTH * CELLSIZE) + 10, 48, 110, 96);
        fill(0, 0, 0);
        textSize(30);
        String bTimeString = String.format("%02d:%02d", bRemainingTime / 60, bRemainingTime % 60);
        text(bTimeString, (BOARD_WIDTH * CELLSIZE) + 10, 96);

        // end of game messages
        if (checkmated == true){
            noLoop();
            textSize(17);
            fill(0, 0, 0);
            String endMessage = "";
            // if black wins
            if(wKing.isCheckmate(boardArray) == true){
                endMessage = "You lost by checkmate!";
            // if white wins
            } else if (wKing.isCheckmate(boardArray) == true){
                endMessage = "You won by checkmate!";

            } else if (resignation == true){
                endMessage = "You lost by resignation";
            // in the case of a stalemate
            } else {
                endMessage = "You won by checkmate!";
            }
            text(endMessage, 682, 156, 96, 96); 
            text(restartMessage, 682, 400, 96, 96);
        } else {
            fill(204,204,204);
            rect(682, 156, 400, 400);
        }
    }

    public static void main(String[] args) {
        PApplet.main("XXLChess.App");
    }
}