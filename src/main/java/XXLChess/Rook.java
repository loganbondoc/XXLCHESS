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

public class Rook extends Piece {
    
    public Rook(String colour, int x, int y){
        super(colour, x, y, 5.25);
    }

    public void tick(){
        
    }

    public boolean isValidMove(int xPos, int yPos, ArrayList<ArrayList<Piece>> boardArray){
        
        int xCell = x / 48;
        int yCell = y / 48;

        // Check that move is directly horizontal or vertical
        if (xCell != xPos && yCell != yPos) {
            // System.out.println("not hori or verti");
            return false;
        }

        // Check if any pieces between move and current location
        int tempX = Integer.signum(xPos - xCell);
        int tempY = Integer.signum(yPos - yCell);
        int checkedX = xCell + tempX;
        int checkedY = yCell + tempY;
        while (checkedX != xPos || checkedY != yPos) {
            if (boardArray.get(checkedY).get(checkedX) != null) {
                // System.out.println("AHA A PIECE!");
                return false;
            }
            checkedX += tempX;
            checkedY += tempY;
        }

        // // if selected tile is directly horizontal
        // if (yPos == (y/48)){
            
        //     // moving left
        //     if (x > xPos){
        //         int spacesBetween = x - xPos;
        //         int temp = xPos;
        //         for (int i = 0; i < spacesBetween; i++){
        //             System.out.println(boardArray.get(yPos).get(temp));
        //             if (boardArray.get(yPos).get(temp) != null){
        //                 System.out.println("can't go left");
        //                 return false;
        //             }
        //             temp -= 1;
        //         }
        //         return true;
            
        //     // moving right
        //     } else if (xPos > x){
        //         int spacesBetween = xPos - x;
        //         int temp = xPos;
        //         for (int i = 0; i < spacesBetween; i++){
        //             if (boardArray.get(yPos).get(temp) != null){
        //                 System.out.println("can't go right");
        //                 return false;
        //             }
        //             temp += 1;
        //         }
        //         return true;
        //     }

        // // if selected tile is directly vertical
        // } else if (xPos == (x/48)){
        //     // moving down
        //     if (y > yPos){
        //         int spacesBetween = y - yPos;
        //         int temp = yPos;
        //         for (int i = 0; i < spacesBetween; i++){
        //             if (boardArray.get(temp).get(xPos) != null){
        //                 System.out.println("can't go up");
        //                 return false;
        //             }
        //             temp -= 1;
        //         }
        //         return true;
            
        //     // moving down
        //     } else if (yPos > y){
        //         int spacesBetween = yPos - y;
        //         int temp = yPos;
        //         for (int i = 0; i < spacesBetween; i++){
        //             if (boardArray.get(temp).get(xPos) != null){
        //                 System.out.println("can't go down");
        //                 return false;
        //             }
        //             temp += 1;
        //         }
        //         return true;
        //     }
        
        // } else {
        //     return false;
        // }
        return true;
    }
}