package XXLChess;
import processing.core.PImage;
import processing.core.PApplet;


public abstract class Piece {

    private String colour;
    private PImage sprite;
    protected int x;
    protected int y;

    public Piece(String colour, int x, int y){
        this.colour = colour;
        this.x = x;
        this.y = y;
    }

    // public abstract void tick();

    public String getColour(){
        return this.colour;
    }

    public int getX(){
        return this.x;
    }

    public int getY(){
        return this.y;
    }

    public void setSprite(PImage sprite){
        this.sprite = sprite;
    }

    public void draw(PApplet app){
        app.image(this.sprite, this.x, this.y, 48, 48);
    }

}