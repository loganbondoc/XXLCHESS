package XXLChess;
import processing.core.PImage;
import processing.core.PApplet;


public abstract class Piece {

    private String colour;
    private PImage sprite;
    private double value;
    protected int x;
    protected int y;

    public Piece(String colour, int x, int y, double value){
        this.colour = colour;
        this.x = x;
        this.y = y;
        this.value = value;
    }

    // setter
    public void setX(int x){
        this.x = x;
    }

    public void setY(int y){
        this.y = y;
    }
    

    // getter
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


    // methods
    public abstract void tick();

    public void setSprite(PImage sprite){
        this.sprite = sprite;
    }

    public void draw(PApplet app){
        app.image(this.sprite, this.x, this.y, 48, 48);
    }

}