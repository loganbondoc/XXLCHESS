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

public class Opponent {

    private String colour;
    private String difficulty;

    public Opponent(String colour, String difficulty){
        this.colour = colour;
        this.difficulty = difficulty;
    }

    // calculate move to make
    public OpponentMove calcMove(ArrayList<ArrayList<Piece>> boardArray, King king){  
        // if king is in check
        // if (king.isCheck(boardArray) == true && king.isCheckmate(boardArray) == false){
        //     // move it to where it is not in check
        //     // if it can't move anywhere, see if you can move pieces in the way
        // }

        // easy difficulty
        if (this.difficulty == "easy"){
            // make an array of all available pieces
            ArrayList<Piece> myPieces = new ArrayList<>();
            for (int i = 0; i < 14; i++){
                for (int j = 0; j < 14; j++){
                    if (boardArray.get(i).get(j) == null){
                        continue;
                    }

                    if (boardArray.get(i).get(j).getColour() == this.colour){
                        myPieces.add(boardArray.get(i).get(j));
                    }
                }
            }
            System.out.println("found avail pieces");

            // make an array of possible moves for that piece then pick randomly
            boolean choseMove = false;
            while (choseMove == false){
                System.out.println("got here");
                // if no piece can move
                if (myPieces.size() == 0){
                    System.out.println("uhoh");
                    return null;
                }

                int selectedIndex = (int)(Math.random() * myPieces.size());
                Piece selectedPiece = myPieces.get(selectedIndex);
                System.out.println(selectedPiece);
                ArrayList<int[]> possibleMoves = new ArrayList<>();
                for (int i = 0; i < 14; i++){
                    for (int j = 0; j < 14; j++){
                        if(j == (selectedPiece.getX()/48) && i == (selectedPiece.getY()/48)){
                            continue;
                        }
                        
                        if (selectedPiece.isValidMove(j, i, boardArray) == true){
                            int[] move = new int[2];
                            move[0] = j;
                            move[1] = i;
                            possibleMoves.add(move);
                        }
                    }
                }
                System.out.println("found avail moves");

                // if there are no possible moves, remove that piece from the array and pick again
                if (possibleMoves.size() == 0){
                    choseMove = false;
                    myPieces.remove(selectedIndex);
                    continue;
                } else {
                    int[] randomMove = possibleMoves.get((int)(Math.random() * possibleMoves.size()));
                    OpponentMove selectedMove = new OpponentMove(randomMove[0], randomMove[1], selectedPiece);
                    System.out.println("found yo move!");
                    return selectedMove;
                }
            }
        } else {
            System.out.println("uh oh");
            return null;
        }
        System.out.println("uuoh-");
        return null;

    }
}