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


public abstract class Piece {

    private String colour;
    private PImage sprite;
    private double value;
    private boolean firstMove = true;
    protected int x;
    protected int y;

    public Piece(String colour, int x, int y, double value){
        this.colour = colour;
        this.x = x;
        this.y = y;
        this.value = value;
    }

    // setters
    public void setX(int x){
        this.x = x;
    }

    public void setY(int y){
        this.y = y;
    }

    public void setFirstMove(boolean firstMove){
        this.firstMove = firstMove;
    }

    // getters
    public String getColour(){
        return this.colour;
    }

    public int getX(){
        return this.x;
    }

    public int getY(){
        return this.y;
    }

    public double getValue(){
        return this.value;
    }

    public boolean getFirstMove(){
        return this.firstMove;
    }


    // methods
    public abstract void tick();

    public abstract boolean isValidMove(int xPos, int yPos, ArrayList<ArrayList<Piece>> boardArray);

    public void setSprite(PImage sprite){
        this.sprite = sprite;
    }

    public void draw(PApplet app){
        app.image(this.sprite, this.x, this.y, 48, 48);
    }

}