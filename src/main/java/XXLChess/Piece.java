package XXLChess;

public abstract class Piece {

    private String colour;

    public Piece(String colour){
        this.colour = colour;
    }

    public void setColour(){
        this.colour = colour;
    }

    public String getColour(){
        return colour;
    }

}