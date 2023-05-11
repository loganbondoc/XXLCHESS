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

public interface RookMovement {
    default boolean isValidRookMove(int xPos, int yPos, ArrayList<ArrayList<Piece>> boardArray, int x, int y) {
        
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
        return true;
    }
}
