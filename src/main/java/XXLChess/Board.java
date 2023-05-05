package XXLChess;

import java.util.Scanner;
import java.util.ArrayList;
import java.io.*;
import processing.core.PApplet;

public class Board{

    public ArrayList<ArrayList<Piece>> boardArray;
    public String black;
    public String white;
    public Rook bRook;
    public Rook wRook;

    public Board(){
        this.boardArray = boardArray;
    }

    // public void makeBoard(){
    //     try {
    //         // int success = 0;
    //         File f = new File("level1.txt");
    //         Scanner scan = new Scanner(f);

    //         ArrayList<ArrayList<Piece>> boardArray = new ArrayList<ArrayList<Piece>>();

    //         for(int y = 0; y < 14; y++) { // reading vertically
    //             String s = scan.nextLine();
    //             System.out.println(s);

    //             // if an empty line, skip to next line and add empty row to array
    //             if (s.isEmpty()){
    //                 ArrayList<Piece> emptyRow = new ArrayList<Piece>();
    //                 for (int i = 0; i < 14; i++){
    //                     emptyRow.add(null);
    //                     System.out.println("added empty cell!");
    //                 }
    //                 boardArray.add(emptyRow);
    //                 System.out.println("emptyRow!");
    //                 continue;
    //             }

    //             ArrayList<Piece> boardRow = new ArrayList<>();
    //             for (int x = 0; x < 14; x++){ // reading horizontally
    //                 char c = s.charAt(x);
    //                     switch (c){
    //                         case 'R':
    //                             Piece bRook = new Rook(black, (x * 48), (y * 48));
    //                             boardRow.add(bRook);
    //                             System.out.println("added black rook");
    //                             break;
    //                         case 'r':
    //                             Piece wRook = new Rook(white, (x * 48), (y * 48));
    //                             // this.Rook.setSprite(this.loadImage("src/main/resources/XXLChess/"+"w-rook.png"));
    //                             boardRow.add(wRook);
    //                             System.out.println("added white rook");
    //                             break;
    //                         default:
    //                             boardRow.add(null);
    //                             System.out.println("added empty piece");
    //                             break;
    //                     }
    //             }
    //             // pieceNum++;
    //             boardArray.add(boardRow);
    //             System.out.println("onto the next part!");
    //             // success++;
    //             System.out.println(boardRow);
    //             System.out.println(boardRow.size());
    //         }
    //             // if is a certain type of piece, create that piece
    //             // place in a certain point on the board
    //         } catch (FileNotFoundException e) {
    //             e.printStackTrace();
    //         }
    // }
    
    // public void displayPieces() {
        
    // }
}

// case "N":
//     Piece bKnight = new Knight(black);
// case "n":
//     Piece wKnight = new Knight(white);
// case "B":
//     Piece bBishop = new Bishop(black);
// case "b":
//     Piece wBishop = new Bishop(white);
// case "H":
//     Piece bArchbishop = new Archbishop(black);
// case "h":
//     Piece wArchbishop = new Archbishop(white);
// case "C":
//     Piece bCamel = new Camel(black);
// case "c":
//     Piece wCamel = new Camel(white);
// case "G":
//     Piece bGuard = new Guard(black);
// case "g":
//     Piece wGuard = new Guard(white);
// case "A":
//     Piece bAmazon = new Amazon(black);
// case "a":
//     Piece wAmazon = new Amazon(white);
// case "K":
//     Piece bKing = new King(black);
// case "k":
//     Piece wKing = new King(white);
// case "E":
//     Piece bEmperor = new Emperor(black);
// case "e":
//     Piece wEmperor = new Emperor(white);
// case "Q":
//     Piece bQueen = new Queen(black);
// case "q":
//     Piece wQueen = new Queen(white);
// case "P":
//     Piece bPawn = new Pawn(black);
// case "p":
//     Piece wPawn = new Pawn(white);