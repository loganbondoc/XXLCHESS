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

public class OpponentMove{
    
    private int x;
    private int y;
    private Piece piece;

    public OpponentMove(int x, int y, Piece piece){
        this.x = x;
        this.y = y;
        this.piece = piece;
    }
    
    // setters
    public void setX(int x){
        this.x = x;
    }

    public void setY(int y){
        this.y = y;
    }

    public void setPiece(Piece piece){
        this.piece = piece;
    }

    // getters
    public int getX(){
        return this.x;
    }

    public int getY(){
        return this.y;
    }

    public Piece getPiece(){
        return this.piece;
    }
}