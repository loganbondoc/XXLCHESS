// package XXLChess;

// import java.util.Scanner;
// import java.util.ArrayList;
// import java.io.*;
// import processing.core.PApplet;

// public class Board{

//     public ArrayList<ArrayList<Piece>> boardArray;
//     public String black;
//     public String white;
//     public Rook bRook;
//     public Rook wRook;

//     public Board(){
//         this.boardArray = boardArray;
//     }

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
// }

