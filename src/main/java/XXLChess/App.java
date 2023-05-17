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

    // Arrays for storing last moves {oldX, oldY, newX, newY}
    public static int[] lastMoves = new int[4];
    public static boolean firstMove = true;
    // nested ArrayList for 2D board
    public static ArrayList<ArrayList<Piece>> boardArray;
    // boolean for if your choosing a piece to move or if you are moving a piece
    public static boolean choosingPiece = true;
    public static Piece selectedPiece;

    // boolean for whose turn it is
    public static boolean yourTurn = true;
    public static boolean checkmated = false;
    public static King bKing;
    public static King wKing;
    public static Opponent opponent;

    // timers for players and increments
    public int wTotalTime;
    public int bTotalTime;
    public int wRemainingTime;
    public int bRemainingTime;
    public int wElapsedTime;
    public int bElapsedTime;
    public int wIncrement;
    public int bIncrement;

    
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
                            case 'R': // Black Rook
                                Piece bRook = new Rook("black", (x * CELLSIZE), (y * CELLSIZE));
                                bRook.setSprite(loadImage("src/main/resources/XXLChess/b-rook.png"));
                                boardRow.add(bRook);
                                System.out.println("added black rook");
                                break;
                            case 'r': // White Rook
                                Piece wRook = new Rook("white", (x * CELLSIZE), (y * CELLSIZE));
                                wRook.setSprite(loadImage("src/main/resources/XXLChess/w-rook.png"));
                                boardRow.add(wRook);
                                System.out.println("added white rook");
                                break;
                            case 'P': // Black Pawn
                                Piece bPawn = new Pawn("black", (x * CELLSIZE), (y * CELLSIZE));
                                bPawn.setSprite(loadImage("src/main/resources/XXLChess/b-pawn.png"));
                                boardRow.add(bPawn);
                                System.out.println("added black pawn");
                                break;
                            case 'p': // White Pawn
                                Piece wPawn = new Pawn("white", (x * CELLSIZE), (y * CELLSIZE));
                                wPawn.setSprite(loadImage("src/main/resources/XXLChess/w-pawn.png"));
                                boardRow.add(wPawn);
                                System.out.println("added white pawn");
                                break;
                            case 'B': // Black Bishop
                                Piece bBishop = new Bishop("black", (x * CELLSIZE), (y * CELLSIZE));
                                bBishop.setSprite(loadImage("src/main/resources/XXLChess/b-bishop.png"));
                                boardRow.add(bBishop);
                                System.out.println("added black bishop");
                                break;
                            case 'b': // White Bishop
                                Piece wBishop = new Bishop("white", (x * CELLSIZE), (y * CELLSIZE));
                                wBishop.setSprite(loadImage("src/main/resources/XXLChess/w-bishop.png"));
                                boardRow.add(wBishop);
                                System.out.println("added white bishop");
                                break;
                            case 'Q': // Black Queen
                                Piece bQueen = new Queen("black", (x * CELLSIZE), (y * CELLSIZE));
                                bQueen.setSprite(loadImage("src/main/resources/XXLChess/b-queen.png"));
                                boardRow.add(bQueen);
                                System.out.println("added black queen");
                                break;
                            case 'q': // White Queen
                                Piece wQueen = new Queen("white", (x * CELLSIZE), (y * CELLSIZE));
                                wQueen.setSprite(loadImage("src/main/resources/XXLChess/w-queen.png"));
                                boardRow.add(wQueen);
                                System.out.println("added white queen");
                                break;
                            case 'N': // Black Knight
                                Piece bKnight = new Knight("black", (x * CELLSIZE), (y * CELLSIZE));
                                bKnight.setSprite(loadImage("src/main/resources/XXLChess/b-knight.png"));
                                boardRow.add(bKnight);
                                System.out.println("added black knight");
                                break;
                            case 'n': // White Knight
                                Piece wKnight = new Knight("white", (x * CELLSIZE), (y * CELLSIZE));
                                wKnight.setSprite(loadImage("src/main/resources/XXLChess/w-knight.png"));
                                boardRow.add(wKnight);
                                System.out.println("added white knight");
                                break;
                            case 'H': // Black Archbishop
                                Piece bArchbishop = new Archbishop("black", (x * CELLSIZE), (y * CELLSIZE));
                                bArchbishop.setSprite(loadImage("src/main/resources/XXLChess/b-archbishop.png"));
                                boardRow.add(bArchbishop);
                                System.out.println("added black archbishop");
                                break;
                            case 'h': // White Archbishop
                                Piece wArchbishop = new Archbishop("white", (x * CELLSIZE), (y * CELLSIZE));
                                wArchbishop.setSprite(loadImage("src/main/resources/XXLChess/w-archbishop.png"));
                                boardRow.add(wArchbishop);
                                System.out.println("added white archbishop");
                                break;
                            case 'A': // Black Amazon
                                Piece bAmazon = new Amazon("black", (x * CELLSIZE), (y * CELLSIZE));
                                bAmazon.setSprite(loadImage("src/main/resources/XXLChess/b-amazon.png"));
                                boardRow.add(bAmazon);
                                System.out.println("added black amazon");
                                break;
                            case 'a': // White Amazon
                                Piece wAmazon = new Amazon("white", (x * CELLSIZE), (y * CELLSIZE));
                                wAmazon.setSprite(loadImage("src/main/resources/XXLChess/w-amazon.png"));
                                boardRow.add(wAmazon);
                                System.out.println("added white amazon");
                                break;
                            case 'E': // Black Chancellor
                                Piece bChancellor = new Chancellor("black", (x * CELLSIZE), (y * CELLSIZE));
                                bChancellor.setSprite(loadImage("src/main/resources/XXLChess/b-chancellor.png"));
                                boardRow.add(bChancellor);
                                System.out.println("added black chancellor");
                                break;
                            case 'e': // White Chancellor
                                Piece wChancellor = new Chancellor("white", (x * CELLSIZE), (y * CELLSIZE));
                                wChancellor.setSprite(loadImage("src/main/resources/XXLChess/w-chancellor.png"));
                                boardRow.add(wChancellor);
                                System.out.println("added white chancellor");
                                break;
                            case 'C': // Black Camel
                                Piece bCamel = new Camel("black", (x * CELLSIZE), (y * CELLSIZE));
                                bCamel.setSprite(loadImage("src/main/resources/XXLChess/b-camel.png"));
                                boardRow.add(bCamel);
                                System.out.println("added black camel");
                                break;
                            case 'c': // White Camel
                                Piece wCamel = new Camel("white", (x * CELLSIZE), (y * CELLSIZE));
                                wCamel.setSprite(loadImage("src/main/resources/XXLChess/w-camel.png"));
                                boardRow.add(wCamel);
                                System.out.println("added white camel");
                                break;
                            case 'K': // Black King
                                bKing = new King("black", (x * CELLSIZE), (y * CELLSIZE));
                                bKing.setSprite(loadImage("src/main/resources/XXLChess/b-king.png"));
                                boardRow.add(bKing);
                                System.out.println("added black king");
                                break;
                            case 'k': // White King
                                wKing = new King("white", (x * CELLSIZE), (y * CELLSIZE));
                                wKing.setSprite(loadImage("src/main/resources/XXLChess/w-king.png"));
                                boardRow.add(wKing);
                                System.out.println("added white king");
                                break;
                            case 'G': // Black Guard
                                Piece bGuard = new Guard("black", (x * CELLSIZE), (y * CELLSIZE));
                                bGuard.setSprite(loadImage("src/main/resources/XXLChess/b-knight-king.png"));
                                boardRow.add(bGuard);
                                System.out.println("added black guard");
                                break;
                            case 'g': // White Guard
                                Piece wGuard = new Guard("white", (x * CELLSIZE), (y * CELLSIZE));
                                wGuard.setSprite(loadImage("src/main/resources/XXLChess/w-knight-king.png"));
                                boardRow.add(wGuard);
                                System.out.println("added white guard");
                                break;
                            default: // empty cell
                                boardRow.add(null);
                                System.out.println("added empty piece");
                                break;
                        }
                }
                boardArray.add(boardRow);
                System.out.println("onto the next part!");
                System.out.println(boardRow);
                System.out.println(boardRow.size());
            }
                // if is a certain type of piece, create that piece
                // place in a certain point on the board
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

		// load config
        JSONObject conf = loadJSONObject(new File(this.configPath));
        wTotalTime = 180;
        bTotalTime = 180;
        wIncrement = 2;
        bIncrement = 2;

        // initialising opponent
        opponent = new Opponent("black", "easy");
    }

    /**
     * Receive key pressed signal from the keyboard.
    */
    public void keyPressed(){
        // if key == 'e'
            // checkmated == true
        // if checkmated == true and key == 'r'
            // call setup again
    }
    
    /**
     * Receive key released signal from the keyboard.
    */
    public void keyReleased(){

    }

    @Override
    public void mousePressed(MouseEvent e) {
        
        // ensure click is in the bounds of board
        if (mouseX < (CELLSIZE * 14) && mouseY < (CELLSIZE * 14)){
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
                Piece newSelect = boardArray.get(yPos).get(xPos);
                
                // if they selected an empty tile
                if (boardArray.get(yPos).get(xPos) == null){
                    
                    // check if any piece is in the way
                    if(selectedPiece.isValidMove(xPos, yPos, boardArray) == true){
                        // replace selectedPiece with null and move to new spot in boardArray
                        boardArray.get(oldSpotY).set(oldSpotX, null);
                        boardArray.get(yPos).set(xPos, selectedPiece);
                        
                        System.out.println((oldSpotX + 1) + "," + (oldSpotY + 1) + " is now null");
                        System.out.println(selectedPiece + "is now at " + (xPos + 1) + "," + (yPos + 1));


                        // set coords for highlighs
                        lastMoves[0] = oldSpotX * 48;
                        lastMoves[1] = oldSpotY * 48;
                        lastMoves[2] = xPos * 48;
                        lastMoves[3] = yPos * 48;


                        //set new coords
                        selectedPiece.setX(xPos * CELLSIZE);
                        selectedPiece.setY(yPos * CELLSIZE);
                        System.out.println("was moved!");
                        selectedPiece.setFirstMove(false);
                        choosingPiece = true;
                        selectedPiece = null;
                        firstMove = false;
                        yourTurn = false;
                    
                    } else {
                        System.out.println("doesnt work :(");
                        choosingPiece = true;
                        selectedPiece = null;
                    }
                    
                // if they selected a tile that another piece is on
                // if its your piece, cannot move
                } else if (newSelect.getColour() == "white"){
                    System.out.println("ur blocked");
                    choosingPiece = true;
                    selectedPiece = null;

                // if its their piece... KILL
                } else if (newSelect.getColour() == "black"){
                    System.out.println("KILL");
                    if(selectedPiece.isValidMove(xPos, yPos, boardArray) == true){
                        // replace selectedPiece with null and move to new spot in boardArray
                        boardArray.get(oldSpotY).set(oldSpotX, null);
                        boardArray.get(yPos).set(xPos, selectedPiece);
                        
                        System.out.println((oldSpotX + 1) + "," + (oldSpotY + 1) + " is now null");
                        System.out.println(selectedPiece + "is now at " + (xPos + 1) + "," + (yPos + 1));

                        // set coords for highlighs
                        lastMoves[0] = oldSpotX * 48;
                        lastMoves[1] = oldSpotY * 48;
                        lastMoves[2] = xPos * 48;
                        lastMoves[3] = yPos * 48;

                        //set new coords
                        selectedPiece.setX(xPos * CELLSIZE);
                        selectedPiece.setY(yPos * CELLSIZE);
                        System.out.println("was moved!");
                    }
                    selectedPiece.setFirstMove(false);
                    choosingPiece = true;
                    selectedPiece = null;
                    firstMove = false;
                    yourTurn = false;

                } else {
                    System.out.println("HOW DID YOU EVEN GET HERE");
                }
                
                // opponent makes move
                System.out.println("making a move");
                
                // if its checkmate and king is under threat, CPU loses
                if(bKing.isCheck(boardArray) == true && bKing.isCheckmate(boardArray) == true){
                    ;
                // if bKing cannot move anywhere but it is not under check, stalemate
                } else if(bKing.isCheck(boardArray) == false && bKing.isCheckmate(boardArray) == true){
                    ;
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
                    System.out.println("was moved!");
                    move.getPiece().setFirstMove(false);
                    System.out.println("made the move");
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
                    System.out.println("piece was chosen");
                    choosingPiece = false;
                
                // if you choose a tile with someone elses piece on it
                } else {
                    choosingPiece = true;
                    selectedPiece = null;
                    System.out.println("not ur piece");
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
            // drawing green rect of selected piece
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

        // drawing timer
        if (yourTurn == true){
            // int currentTime = millis()/1000;
            wRemainingTime = wTotalTime - (millis()/1000);
        } else {
            bRemainingTime = bTotalTime - (millis()/1000);
        }
        
        // for white's timer
        fill(255,255,255);
        rect((BOARD_WIDTH * CELLSIZE) + 10, 576, 110, 96);
        fill(0, 0, 0);
        textSize(30);
        String wTimeString = String.format("%02d:%02d", wRemainingTime / 60, wRemainingTime % 60);
        text(wTimeString, (BOARD_WIDTH * CELLSIZE) + 10, 624);

        // for black's timer
        fill(255,255,255);
        rect((BOARD_WIDTH * CELLSIZE) + 10, 48, 110, 96);
        fill(0, 0, 0);
        textSize(30);
        String bTimeString = String.format("%02d:%02d", bRemainingTime / 60, bRemainingTime % 60);
        text(bTimeString, (BOARD_WIDTH * CELLSIZE) + 10, 96);
    }

    // while checkmate is false, players turn, then computers turn

    // method for is check, at the end of each turn loop through board and check if anyones in check
        // if in check, check for checkmate
    // if in check at the start of turn, cannot be in check at the end
    // if not in check at start of turn, cannot be in check at the end

    public static void main(String[] args) {
        PApplet.main("XXLChess.App");

        
        // if (yourTurn == false){
        //     System.out.println("LMAO");
        // }

        // while (checkmated == false){
        //     if (yourTurn == false){
        //         System.out.println("making a move");
        //         // if its checkmate and king is under threat, CPU loses
        //         if(bKing.isCheck(boardArray) == true && bKing.isCheckmate(boardArray) == true){
        //             ;
        //         // if bKing cannot move anywhere but it is not under check, stalemate
        //         } else if(bKing.isCheck(boardArray) == false && bKing.isCheckmate(boardArray) == true){
        //             ;
        //         } else {
                    
        //             OpponentMove move = opponent.calcMove(boardArray, bKing);
                    
        //             int oldX = (move.getPiece().getX())/CELLSIZE;
        //             int oldY = (move.getPiece().getY())/CELLSIZE;
        //             int newX = move.getX();
        //             int newY = move.getY();

        //             boardArray.get(oldY).set(oldX, null);
        //             boardArray.get(newY).set(newX, move.getPiece());
                    
        //             move.getPiece().setX(newX * CELLSIZE);
        //             move.getPiece().setY(newY * CELLSIZE);
        //             System.out.println("was moved!");
        //             move.getPiece().setFirstMove(false);
        //             System.out.println("made the move");
        //         }
        //         yourTurn = true;
        //     }
        // }
    }

}