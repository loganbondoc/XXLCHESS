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

    public void setColour(String colour){
        this.colour = colour;
    }
    
    public String getColour(){
        return this.colour;
    }

    public void setDifficulty(String difficulty){
        this.difficulty = difficulty;
    }
    
    public String getDifficulty(){
        return this.difficulty;
    }

    /**
     * Calculates the move the opponent will make, based on if they are in check and difficulty setting.
     * Returns an OpponentMove
    */
    public OpponentMove calcMove(ArrayList<ArrayList<Piece>> boardArray, King king){  
        
        // if king is in check
        if (king.isCheck(boardArray) == true && king.isCheckmate(boardArray) == false){
            ArrayList<OpponentMove> possibleMoves = new ArrayList<>();
            
            // check for moves where it is not in check
            for (int i = 0; i < 14; i++){
                for (int j = 0; j < 14; j++){
                    
                    // prevent king from moving to place it is already in
                    if (j == (king.getX()/48) && i == (king.getY()/48)){
                        continue;
                    }
                    
                    if (king.isValidMove(j, i, boardArray) == true){
                        // prevent from taking own piece
                        if(boardArray.get(i).get(j) == null || boardArray.get(i).get(j).getColour() != this.colour){
                            OpponentMove move = new OpponentMove(j, i, king);
                            possibleMoves.add(move);
                        } else {
                            continue;
                        }
                    }
                }
            }
            
            // check for other pieces moves where it is not in check
            for(int i = 0; i < 14; i++){
                for(int j = 0; j < 14; j++){
                    if (boardArray.get(i).get(j) == null || boardArray.get(i).get(j).getColour() != this.colour){
                        continue;
                    } else {
                        Piece chosenPiece = boardArray.get(i).get(j);
                        for(int a = 0; a < 14; a++){
                            for(int b = 0; b < 14; b++){
                                
                                if (chosenPiece.isValidMove(b, a, boardArray) == true){
                                    // temporarily moving piece to see if check is affected
                                    int oldX = chosenPiece.getX()/48;
                                    int oldY = chosenPiece.getY()/48;
                                    Piece replacedPiece = boardArray.get(a).get(b);
                                    boardArray.get(oldY).set(oldX, null);
                                    boardArray.get(a).set(b, chosenPiece);

                                    if (king.isCheck(boardArray) == false){
                                        OpponentMove move = new OpponentMove(b, a, chosenPiece);
                                        possibleMoves.add(move);
                                    }
                                    
                                    // moving piece back
                                    boardArray.get(oldY).set(oldX, chosenPiece);
                                    boardArray.get(a).set(b, replacedPiece);
                                }
                            }
                        }
                    }

                }
            }

            // pick randomly from possibleMoves
            OpponentMove randomMove = possibleMoves.get((int)(Math.random() * possibleMoves.size()));
            return randomMove;
        }

        // normal moves
        // easy difficulty
        if (this.difficulty.equals("easy")){
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

            // make an array of possible moves for that piece then pick randomly
            boolean choseMove = false;
            while (choseMove == false){
                
                // if no piece can move
                if (myPieces.size() == 0){
                    System.out.println("uhoh");
                    return null;
                }

                int selectedIndex = (int)(Math.random() * myPieces.size());
                Piece selectedPiece = myPieces.get(selectedIndex);
                ArrayList<int[]> possibleMoves = new ArrayList<>();
                for (int i = 0; i < 14; i++){
                    for (int j = 0; j < 14; j++){
                        if(j == (selectedPiece.getX()/48) && i == (selectedPiece.getY()/48)){
                            continue;
                        }
                        
                        if (selectedPiece.isValidMove(j, i, boardArray) == true){
                            // preventing from taking own pieces
                            if (boardArray.get(i).get(j) == null || boardArray.get(i).get(j).getColour() != this.colour){
                                int[] move = new int[2];
                                move[0] = j;
                                move[1] = i;
                                possibleMoves.add(move);
                            } else {
                                continue;
                            }
                            
                        }
                    }
                }

                // if there are no possible moves, remove that piece from the array and pick again
                if (possibleMoves.size() == 0){
                    choseMove = false;
                    myPieces.remove(selectedIndex);
                    continue;
                } else {
                    int[] randomMove = possibleMoves.get((int)(Math.random() * possibleMoves.size()));
                    OpponentMove selectedMove = new OpponentMove(randomMove[0], randomMove[1], selectedPiece);
                    return selectedMove;
                }
            }

        // hard difficulty
        } else if (this.difficulty.equals("hard")){
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
            
            // for every piece find all valid moves
            // valid moves are sorted into 4 different categories
            ArrayList<OpponentMove> attackingMoves = new ArrayList<>();
            ArrayList<OpponentMove> tradingMoves = new ArrayList<>();
            ArrayList<OpponentMove> neutralMoves = new ArrayList<>();
            ArrayList<OpponentMove> badMoves = new ArrayList<>();
            
            int amount = myPieces.size();
            int iterations = 0;
            while(iterations < amount){
                Piece selectedPiece = myPieces.get(iterations);
                for (int i = 0; i < 14; i++){
                    for (int j = 0; j < 14; j++){
                        if(j == (selectedPiece.getX()/48) && i == (selectedPiece.getY()/48)){
                            continue;
                        }
                        
                        if (selectedPiece.isValidMove(j, i, boardArray) == true){
                            
                            OpponentMove move = new OpponentMove(j, i, selectedPiece);
                            
                            boolean inDanger = false;
                            // if empty square
                            if (boardArray.get(i).get(j) == null){
                                
                                // check if it is the valid move of players pieces
                                for(int k = 0; k < 14; k++){
                                    for(int l = 0; l < 14; l++){
                                        Piece p = boardArray.get(k).get(l);
                                        if (p == null || p.getColour() == this.colour){
                                            continue;
                                        }
                                        
                                        if(p.isValidMove(j, i, boardArray) == true){
                                            inDanger = true;
                                        }
                                    }
                                }
                                
                                // if piece was in danger add it to bad moves, if not add to neutral
                                if (inDanger == true){
                                    badMoves.add(move);
                                } else {
                                    neutralMoves.add(move);
                                }
                            
                            // if theres a piece on it that's not yours
                            } else if (boardArray.get(i).get(j).getColour() != this.colour){
                                // check if it is the valid move of players pieces
                                for(int k = 0; k < 14; k++){
                                    for(int l = 0; l < 14; l++){
                                        Piece p = boardArray.get(k).get(l);
                                        if (p == null || p.getColour() == this.colour){
                                            continue;
                                        }
                                        
                                        if(p.isValidMove(j, i, boardArray) == true){
                                            inDanger = true;
                                        }
                                    }
                                }

                                // if piece was in danger add it to trade moves, if not add to attacking
                                if (inDanger == true){
                                    tradingMoves.add(move);
                                } else {
                                    attackingMoves.add(move);
                                }
                            }
                        }
                    }
                }
                iterations++;
            }

            // choose the move to do
            // if there are attacking moves
            if (attackingMoves.size() != 0){
                
                // sorting moves highest to lowest according to piece value
                for (int i = 0; i < attackingMoves.size() - 1; i++) {
                    for (int j = 0; j < attackingMoves.size() - i - 1; j++) {
                        double pieceValue = attackingMoves.get(j).getPiece().getValue();
                        double nextPieceValue = attackingMoves.get(j + 1).getPiece().getValue();
                        if (pieceValue < nextPieceValue) {
                            // swap them
                            OpponentMove temp = attackingMoves.get(j);
                            attackingMoves.set(j, attackingMoves.get(j + 1));
                            attackingMoves.set(j + 1, temp);
                        }
                    }
                }
                // returns highest value move
                return attackingMoves.get(0);
            
            // if there are no attacking moves, choose a trading move
            } else if (attackingMoves.size() == 0 && tradingMoves.size() != 0){
                
                // sort moves from the pieces of the lowest value to the highest
                for (int i = 0; i < tradingMoves.size() - 1; i++) {
                    for (int j = 0; j < tradingMoves.size() - i - 1; j++) {
                        // minus the value of the players piece that is taken from the CPU piece that is in danger
                        // sort in ascending order
                        double theirPieceValue = boardArray.get(tradingMoves.get(j).getY()).get(tradingMoves.get(j).getX()).getValue();
                        double yourPieceValue = tradingMoves.get(j).getPiece().getValue();
                        double tradeValue = yourPieceValue - theirPieceValue;

                        double nTheirPieceValue = boardArray.get(tradingMoves.get(j + 1).getY()).get(tradingMoves.get(j + 1).getX()).getValue();
                        double nYourPieceValue = tradingMoves.get(j + 1).getPiece().getValue();
                        double nTradeValue = nYourPieceValue - nTheirPieceValue;

                        if (tradeValue > nTradeValue) {
                            // swap them
                            OpponentMove temp = tradingMoves.get(j);
                            tradingMoves.set(j, tradingMoves.get(j + 1));
                            tradingMoves.set(j + 1, temp);
                        }
                    }
                }
                // returns highest value trade move
                return tradingMoves.get(0);
            
            // if there are no attacking or trading moves, make a neutral move
            } else if (attackingMoves.size() == 0 && tradingMoves.size() == 0){
                OpponentMove neutralMove = neutralMoves.get((int)(Math.random() * neutralMoves.size()));
                return neutralMove;
            
            // if no other options, choose a bad move
            } else {
                OpponentMove badMove = badMoves.get((int)(Math.random() * badMoves.size()));
                return badMove;
            }
        }
        System.out.println("didn't return a move D:");
        return null;
    }
}