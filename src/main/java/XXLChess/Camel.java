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

public class Camel extends Piece {
    
    public Camel(String colour, int x, int y){
        super(colour, x, y, 2);
    }

    public void tick(){
        
    }

    public boolean isValidMove(int xPos, int yPos, ArrayList<ArrayList<Piece>> boardArray){
        int xCell = x / 48;
        int yCell = y / 48;

        int xDistance = Math.abs(xPos - xCell);
        int yDistance =  Math.abs(yPos - yCell);

        // move must have an xDistance of 3 and yDistance of 1 or vice versa
        if (xDistance == 3 && yDistance == 1) {
            // System.out.println("not it chief");
            return true;
        } else if (xDistance == 1 && yDistance == 3) {
            return true;
        } else {
            return false;
        }
    }
}