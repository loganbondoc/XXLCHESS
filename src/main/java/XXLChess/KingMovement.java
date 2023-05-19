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

public interface KingMovement {
    default boolean isValidKingMove(int xPos, int yPos, ArrayList<ArrayList<Piece>> boardArray, int x, int y) {
        
        int xCell = x / 48;
        int yCell = y / 48;

        int xDistance = Math.abs(xPos - xCell);
        int yDistance =  Math.abs(yPos - yCell);

        // move must have a distance of 1 all around
        if (xDistance <= 1 && yDistance <= 1) {
            return true;
        } else {
            return false;
        }
    }
}