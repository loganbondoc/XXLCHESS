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
                if (this.isValidMove(j, i, boardArray) == true && this.isCheck(boardArray) == false){
                    return false;
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
                // System.out.println("it's been done");
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