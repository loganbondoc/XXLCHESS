package XXLChess;
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

public class King extends Piece implements KingMovement {
    
    public King(String colour, int x, int y){
        super(colour, x, y, 100);
    }

    public void tick(){
        
    }

    public boolean isCheck(ArrayList<ArrayList<Piece>> boardArray){
        // method for is check, at the end of each turn loop through board and check if anyones in check
        
        int xPos = this.getX()/48;
        int yPos = this.getY()/48;

        for (int i = 0; i < 14; i++){
            for(int j = 0; j < 14; j++){
                // if looking at the same king piece
                Piece piece = boardArray.get(i).get(j);
                if (piece == null){
                    continue;
                }

                if(piece instanceof King == true){
                    continue;
                }
                if (piece.isValidMove(xPos, yPos, boardArray) == true && piece.getColour() != this.getColour()){
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isCheckmate(ArrayList<ArrayList<Piece>> boardArray){
        // check the entire board for moves king can make, if none, then return true
        for (int i = 0; i < 14; i++){
            for(int j = 0; j < 14; j++){
                if (this.isValidMove(j, i, boardArray) == true){
                    return false;
                }
            }
        }

        // check for other pieces moves where it is not in check
        for(int i = 0; i < 14; i++){
            for(int j = 0; j < 14; j++){
                if (boardArray.get(i).get(j) == null || boardArray.get(i).get(j).getColour() != this.getColour()){
                    continue;
                } else {
                    Piece chosenPiece = boardArray.get(i).get(j);
                    // checking every available move for this piece
                    for(int a = 0; a < 14; a++){
                        for(int b = 0; b < 14; b++){
                            
                            if (chosenPiece.isValidMove(b, a, boardArray) == true){
                                // temporarily moving piece to see if check is affected
                                int oldX = chosenPiece.getX()/48;
                                int oldY = chosenPiece.getY()/48;
                                Piece replacedPiece = boardArray.get(a).get(b);
                                boardArray.get(oldY).set(oldX, null);
                                boardArray.get(a).set(b, chosenPiece);

                                if (this.isCheck(boardArray) == false){
                                    // moving piece back
                                    boardArray.get(oldY).set(oldX, chosenPiece);
                                    boardArray.get(a).set(b, replacedPiece);
                                    return false;
                                }
                                
                                // moving piece back
                                boardArray.get(oldY).set(oldX, chosenPiece);
                                boardArray.get(a).set(b, replacedPiece);
                            }
                        }
                    }
                }

            }
        }
        return true;
    }

    public boolean isValidMove(int xPos, int yPos, ArrayList<ArrayList<Piece>> boardArray){

        // check if moving into check
        // loop through each piece on board and check valid moves against square being moved to
        for (int i = 0; i < 14; i++){
            for(int j = 0; j < 14; j++){
                Piece piece = boardArray.get(i).get(j);
                
                // if king is selected
                if(piece instanceof King == true){
                    if (piece.getColour() != this.getColour()){
                        
                        // if the king is in attacking distance of other king, return false;
                        if (isValidKingMove(xPos, yPos, boardArray, j, i) == true){
                            return false;
                        }
                    }
                    continue;
                
                } else if (piece != null){
                    if (piece.isValidMove(xPos, yPos, boardArray) == true && piece.getColour() != this.getColour()){
                        return false;
                    }
                } else {
                    continue;
                }
            }
        }
        
        return isValidKingMove(xPos, yPos, boardArray, x, y);
    }
}