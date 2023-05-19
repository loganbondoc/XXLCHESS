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

public interface KnightMovement {
    default boolean isValidKnightMove(int xPos, int yPos, ArrayList<ArrayList<Piece>> boardArray, int x, int y) {
        
        int xCell = x / 48;
        int yCell = y / 48;

        int xDistance = Math.abs(xPos - xCell);
        int yDistance =  Math.abs(yPos - yCell);

        // move must have an xDistance of 2 and yDistance of 1 or vice versa
        if (xDistance == 2 && yDistance == 1) {
            return true;
        } else if (xDistance == 1 && yDistance == 2) {
            return true;
        } else {
            return false;
        }
    }
}
