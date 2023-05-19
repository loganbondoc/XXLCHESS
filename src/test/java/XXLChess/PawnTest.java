package XXLChess;


import processing.core.PApplet;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

//gradle test jacocoTestReport

public class PawnTest {

    // testing that pawn can move two squares ahead on first turn
    @Test
    public void validMoveTest() {
        App app = new App();
        app.setup();
        
        Piece pawn = app.boardArray.get(13).get(1);
        assertFalse(pawn.isValidMove(11, 1, app.boardArray));
    }
}
