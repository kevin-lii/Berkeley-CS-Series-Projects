package amazons;

import static amazons.Piece.*;

import org.junit.Test;
import static org.junit.Assert.*;
import ucb.junit.textui;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;
import java.util.HashSet;

/** The suite of all JUnit tests for the enigma package.
 *  @author Kevin Li
 */
public class UnitTest {

    /**
     * Run the JUnit tests in this package. Add xxxTest.class entries to
     * the arguments of runClasses to run other JUnit tests.
     */
    public static void main(String[] ignored) {
        textui.runClasses(UnitTest.class);
    }

    /**
     * Tests basic correctness of put and get on the initialized board.
     */
    @Test
    public void testBasicPutGet() {
        Board b = new Board();
        b.put(BLACK, Square.sq(3, 5));
        assertEquals(b.get(3, 5), BLACK);
        b.put(WHITE, Square.sq(9, 9));
        assertEquals(b.get(9, 9), WHITE);
        b.put(EMPTY, Square.sq(3, 5));
        assertEquals(b.get(3, 5), EMPTY);
    }

    /**
     * Tests proper identification of legal/illegal queen moves.
     */
    @Test
    public void testIsQueenMove() {
        assertFalse(Square.sq(1, 5).isQueenMove(Square.sq(1, 5)));
        assertFalse(Square.sq(1, 5).isQueenMove(Square.sq(2, 7)));
        assertFalse(Square.sq(0, 0).isQueenMove(Square.sq(5, 1)));
        assertTrue(Square.sq(1, 9).isQueenMove(Square.sq(9, 1)));
        assertTrue(Square.sq(9, 1).isQueenMove(Square.sq(1, 9)));
        assertTrue(Square.sq(1, 1).isQueenMove(Square.sq(9, 9)));
        assertTrue(Square.sq(2, 7).isQueenMove(Square.sq(8, 7)));
        assertTrue(Square.sq(3, 0).isQueenMove(Square.sq(3, 4)));
        assertTrue(Square.sq(7, 9).isQueenMove(Square.sq(0, 2)));
        assertTrue(Square.sq(7, 5).isQueenMove(Square.sq(9, 3)));
        assertTrue(Square.sq(7, 5).isQueenMove(Square.sq(5, 3)));
    }

    /**
     * Tests toString for initial board state and a smiling board state. :)
     */
    @Test
    public void testToString() {
        Board b = new Board();
        assertEquals(INIT_BOARD_STATE, b.toString());
        makeSmile(b);
        assertEquals(SMILE, b.toString());
    }

    @Test
    public void testDirection() {
        assertEquals(0, Square.sq(2, 5).direction(Square.sq(2, 8)));
        assertEquals(1, Square.sq(2, 5).direction(Square.sq(5, 8)));
        assertEquals(2, Square.sq(2, 5).direction(Square.sq(4, 5)));
        assertEquals(3, Square.sq(2, 5).direction(Square.sq(4, 3)));
        assertEquals(4, Square.sq(2, 5).direction(Square.sq(2, 4)));
        assertEquals(5, Square.sq(2, 5).direction(Square.sq(1, 4)));
        assertEquals(6, Square.sq(2, 5).direction(Square.sq(0, 5)));
        assertEquals(7, Square.sq(2, 5).direction(Square.sq(0, 7)));
    }
    @Test
    public void isLegalTest() {
        Board b = new Board();
        assertTrue(b.isLegal(Square.sq(3, 0)));
        assertTrue(b.isLegal(Square.sq(0, 3)));
        assertFalse(b.isLegal(Square.sq(3, 9)));
        assertTrue(b.isLegal(Square.sq(0, 3), Square.sq(1, 4)));
        assertTrue(b.isLegal(Square.sq(3, 0), Square.sq(5, 0)));
        assertFalse(b.isLegal(Square.sq(3, 0), Square.sq(0, 9)));
        assertFalse(b.isLegal(Square.sq(3, 0), Square.sq(9, 0)));
        assertTrue(b.isLegal(Square.sq(3, 0), Square.sq(3, 1),
                Square.sq(3, 2)));
        assertTrue(b.isLegal(Square.sq(3, 0), Square.sq(3, 1),
                Square.sq(3, 0)));
        assertFalse(b.isLegal(Square.sq(3, 0), Square.sq(4, 0),
                Square.sq(9, 0)));
        assertFalse(b.isLegal(Square.sq(3, 0), Square.sq(3, 1),
                Square.sq(3, 9)));
        assertFalse(b.isLegal(Square.sq(3, 0), Square.sq(3, 9),
                Square.sq(3, 8)));
        b.makeMove(Move.mv(Square.sq(3, 0), Square.sq(3, 1),
                Square.sq(3, 0)));
        assertFalse(b.isLegal(Square.sq(3, 0)));
        assertFalse(b.isLegal(Square.sq(0, 3)));
        assertTrue(b.isLegal(Square.sq(3, 9)));
        assertFalse(b.isLegal(Square.sq(0, 3), Square.sq(1, 4)));
        assertFalse(b.isLegal(Square.sq(3, 0), Square.sq(5, 0)));
        assertFalse(b.isLegal(Square.sq(3, 0), Square.sq(0, 9)));
        assertTrue(b.isLegal(Square.sq(3, 9), Square.sq(3, 8),
                Square.sq(3, 7)));
        assertFalse(b.isLegal(Square.sq(3, 9), Square.sq(3, 8),
                Square.sq(3, 1)));
        assertFalse(b.isLegal(Square.sq(3, 9), Square.sq(3, 1),
                Square.sq(3, 0)));
        assertFalse(b.isLegal(Square.sq(3, 9), Square.sq(3, 1),
                Square.sq(3, 1)));
    }

    @Test
    public void testisUnblockedMove() {
        Board a = new Board();
        assertFalse(a.isUnblockedMove(Square.sq(1, 5),
                Square.sq(2, 7), null));
        assertFalse(a.isUnblockedMove(Square.sq(0, 5),
                Square.sq(0, 9), null));
        assertFalse(a.isUnblockedMove(Square.sq(0, 5),
                Square.sq(0, 6), null));
        assertTrue(a.isUnblockedMove(Square.sq(1, 1),
                Square.sq(9, 9), null));
        assertTrue(a.isUnblockedMove(Square.sq(0, 4),
                Square.sq(0, 9), Square.sq(0, 6)));
    }

    @Test
    public void testBoard() {
        Board a = new Board();
        a.makeMove(Square.sq(6, 0), Square.sq(8, 0),
                Square.sq(8, 9));
        assertFalse(a.isLegal(Square.sq(6, 0), Square.sq(8, 0),
                Square.sq(8, 9)));
        assertFalse(a.isLegal(Square.sq(6, 9), Square.sq(8, 9),
                Square.sq(6, 9)));
        assertTrue(a.isLegal(Square.sq(6, 9), Square.sq(7, 9),
                Square.sq(6, 9)));
        a.undo();
        assertFalse(a.isLegal(Square.sq(6, 9), Square.sq(8, 9),
                Square.sq(6, 9)));
        assertTrue(a.isLegal(Square.sq(6, 0), Square.sq(8, 0),
                Square.sq(8, 9)));
    }

    @Test
    public void testIterators() {
        Board a = new Board();
        ArrayList<Square> b = new ArrayList<>();
        ArrayList<Square> b1 = new ArrayList<>();
        Iterator<Square> s = a.reachableFrom(Square.sq(6, 0), Square.sq(3, 0));
        while (s.hasNext()) {
            b.add(s.next());
        }
        Iterator<Square> s1 = a.reachableFrom(Square.sq(6, 0), null);
        while (s1.hasNext()) {
            b1.add(s1.next());
        }
        assertEquals(b1.size(), b.size() - 4);
        assertTrue(b.contains(Square.sq("e3")));
        assertTrue(b1.contains(Square.sq("e3")));
        assertTrue(b.contains(Square.sq("a1")));
        assertFalse(b1.contains(Square.sq("a1")));
        assertFalse(b.contains(Square.sq("a7")));
        assertFalse(b1.contains(Square.sq("a7")));
        ArrayList<Move> b2 = new ArrayList<>();
        Iterator<Move> c = a.legalMoves();
        while (c.hasNext()) {
            b2.add(c.next());
        }
        assertEquals(b2.size(), 2176);
    }

    @Test
    public void testReachableFrom() {
        Board b = new Board();
        buildBoard(b, REACHABLEFROMTESTBOARD);
        int numSquares = 0;
        Set<Square> squares = new HashSet<>();
        Iterator<Square> reachableFrom = b.reachableFrom(Square.sq(5, 4), null);
        while (reachableFrom.hasNext()) {
            Square s = reachableFrom.next();
            assertTrue(REACHABLEFROMTESTSQUARE.contains(s));
            numSquares += 1;
            squares.add(s);
        }
        assertEquals(REACHABLEFROMTESTSQUARE.size(), numSquares);
        assertEquals(REACHABLEFROMTESTSQUARE.size(), squares.size());
    }
    @Test
    public void testLegalMoves() {
        Board b = new Board();
        int numMoves = 0;
        Set<Move> moves = new HashSet<>();
        Iterator<Move> legalMoves = b.legalMoves(Piece.WHITE);
        while (legalMoves.hasNext()) {
            Move m = legalMoves.next();
            numMoves += 1;
            moves.add(m);
        }
        assertEquals(2176, numMoves);
        assertEquals(2176, moves.size());
    }

    private void buildBoard(Board b, Piece[][] target) {
        for (int col = 0; col < Board.SIZE; col++) {
            for (int row = Board.SIZE - 1; row >= 0; row--) {
                Piece piece = target[Board.SIZE - row - 1][col];
                b.put(piece, Square.sq(col, row));
            }
        }
    }

    static final Piece E = Piece.EMPTY;

    static final Piece W = Piece.WHITE;

    static final Piece B = Piece.BLACK;

    static final Piece S = Piece.SPEAR;

    static final Piece[][] REACHABLEFROMTESTBOARD =
    {
        { E, E, E, E, E, E, E, E, E, E },
        { E, E, E, E, E, E, E, E, W, W },
        { E, E, E, E, E, E, E, S, E, S },
        { E, E, E, S, S, S, S, E, E, S },
        { E, E, E, S, E, E, E, E, B, E },
        { E, E, E, S, E, W, E, E, B, E },
        { E, E, E, S, S, S, B, W, B, E },
        { E, E, E, E, E, E, E, E, E, E },
        { E, E, E, E, E, E, E, E, E, E },
        { E, E, E, E, E, E, E, E, E, E },
    };
    static final Piece[][] LEGALBOARD1 =
    {
        { S, S, S, S, S, S, S, E, S, W },
        { S, S, S, S, S, S, S, S, S, E },
        { S, S, E, S, S, S, S, S, E, E },
        { E, E, S, S, E, S, S, S, S, S },
        { S, S, S, S, S, W, S, S, E, E },
        { S, E, S, S, S, S, E, S, S, S },
        { S, B, S, E, B, S, B, B, S, S },
        { S, E, S, S, S, S, S, S, S, S },
        { S, S, S, S, S, E, S, S, S, S },
        { S, S, S, S, S, S, S, S, S, S },
    };
    static final Piece[][] LEGALBOARD2 =
    {
        { S, S, S, S, S, S, S, E, S, W },
        { S, S, S, S, S, S, S, S, S, E },
        { S, S, E, S, S, S, S, S, E, E },
        { E, E, S, S, E, S, S, S, S, S },
        { S, S, W, S, S, W, S, S, E, E },
        { S, E, S, S, S, S, E, S, S, S },
        { S, B, S, E, B, E, W, B, S, S },
        { S, E, S, S, S, S, S, S, S, S },
        { S, W, S, S, S, E, S, S, S, S },
        { E, S, E, S, S, S, S, S, S, S },
    };
    static final Piece[][] TESTWINNER =
    {
        { E, E, E, E, E, E, E, S, S, S },
        { E, E, E, E, E, E, E, S, W, W },
        { E, E, E, E, E, E, E, S, S, S },
        { E, E, E, S, S, S, S, E, E, S },
        { E, E, E, S, S, S, S, E, B, E },
        { E, E, E, S, S, W, S, S, B, E },
        { E, E, E, S, S, S, B, E, B, E },
        { E, E, E, E, E, E, S, W, S, E },
        { E, E, E, E, E, E, E, E, E, E },
        { E, E, E, E, E, E, E, E, E, E },
    };
    static final Piece[][] TESTWINNER1 =
    {
        { E, E, E, E, E, E, E, E, E, E },
        { E, E, E, E, E, E, E, E, W, W },
        { E, E, E, E, E, E, E, S, E, S },
        { E, E, E, S, S, S, S, S, S, S },
        { E, E, E, S, E, E, S, S, B, S },
        { E, E, E, S, E, W, S, S, B, S },
        { E, E, E, S, S, S, B, W, B, S },
        { E, E, E, E, E, S, S, S, S, S },
        { E, E, E, E, E, E, E, E, E, E },
        { E, E, E, E, E, E, E, E, E, E },
    };
    @Test
    public void testWinner() {
        Board b = new Board();
        assertEquals(b.winner(), null);
        buildBoard(b, TESTWINNER);
        assertNotEquals(b.winner(), BLACK);
        b.makeMove(Move.mv("h3-h4(h3)"));
        assertNotEquals(b.winner(), BLACK);
        b.makeMove(Move.mv("i4-j4(i4)"));
        assertEquals(b.winner(), BLACK);
        assertNotEquals(b.winner(), WHITE);

        Board a = new Board();
        assertEquals(a.winner(), null);
        buildBoard(a, TESTWINNER1);
        assertNotEquals(a.winner(), WHITE);
        assertNotEquals(a.winner(), BLACK);
        a.makeMove(Move.mv("f5-e5(f5)"));
        assertNotEquals(a.winner(), null);
        assertNotEquals(a.winner(), BLACK);
        assertEquals(a.winner(), WHITE);
    }

    @Test
    public void testSmallerLegal() {
        Board b = new Board();
        buildBoard(b, LEGALBOARD1);
        int numMoves = 0;
        Set<Move> moves = new HashSet<>();
        Iterator<Move> legalMoves = b.legalMoves(Piece.WHITE);
        while (legalMoves.hasNext()) {
            Move m = legalMoves.next();
            numMoves += 1;
            moves.add(m);
        }
        assertEquals(10, numMoves);
        assertEquals(10, moves.size());
    }
    @Test
    public void testLegalBoard2() {
        Board b = new Board();
        buildBoard(b, LEGALBOARD2);
        int numMoves = 0;
        Set<Move> moves = new HashSet<>();
        Iterator<Move> legalMoves = b.legalMoves(Piece.WHITE);
        while (legalMoves.hasNext()) {
            Move m = legalMoves.next();
            LEGALMOVEMOVES.contains(m);
            numMoves += 1;
            moves.add(m);
        }
        assertEquals(22, numMoves);
        assertEquals(22, moves.size());
    }

    static final Set<Square> REACHABLEFROMTESTSQUARE =
            new HashSet<>(Arrays.asList(
                    Square.sq(5, 5),
                    Square.sq(4, 5),
                    Square.sq(4, 4),
                    Square.sq(6, 4),
                    Square.sq(7, 4),
                    Square.sq(6, 5),
                    Square.sq(7, 6),
                    Square.sq(8, 7)));

    static final Set<Move> LEGALMOVEMOVES =
            new HashSet<Move>(Arrays.asList(
                    Move.mv("b2-b3(b2)"),
                    Move.mv("j10-j8(i8)"),
                    Move.mv("b2-c1(b2)"),
                    Move.mv("j10-j8(j10)"),
                    Move.mv("b2-a1(b2)"),
                    Move.mv("j10-j8(j9)"),
                    Move.mv("g4-g5(g4)"),
                    Move.mv("j10-j9(i8)"),
                    Move.mv("g4-g5(f4)"),
                    Move.mv("j10-j9(j8)"),
                    Move.mv("g4-f4(g5)"),
                    Move.mv("j10-j9(j10)"),
                    Move.mv("g4-f4(g4)"),
                    Move.mv("f6-e7(g5)"),
                    Move.mv("c6-b5(c6)"),
                    Move.mv("f6-e7(f6)"),
                    Move.mv("c6-b7(c8)"),
                    Move.mv("f6-g5(e7)"),
                    Move.mv("c6-b7(c6)"),
                    Move.mv("f6-g5(f6)"),
                    Move.mv("c6-b7(a7)"),
                    Move.mv("f6-g5(f4)")));


    private void makeSmile(Board b) {
        b.put(EMPTY, Square.sq(0, 3));
        b.put(EMPTY, Square.sq(0, 6));
        b.put(EMPTY, Square.sq(9, 3));
        b.put(EMPTY, Square.sq(9, 6));
        b.put(EMPTY, Square.sq(3, 0));
        b.put(EMPTY, Square.sq(3, 9));
        b.put(EMPTY, Square.sq(6, 0));
        b.put(EMPTY, Square.sq(6, 9));
        for (int col = 1; col < 4; col += 1) {
            for (int row = 6; row < 9; row += 1) {
                b.put(SPEAR, Square.sq(col, row));
            }
        }
        b.put(EMPTY, Square.sq(2, 7));
        for (int col = 6; col < 9; col += 1) {
            for (int row = 6; row < 9; row += 1) {
                b.put(SPEAR, Square.sq(col, row));
            }
        }
        b.put(EMPTY, Square.sq(7, 7));
        for (int lip = 3; lip < 7; lip += 1) {
            b.put(WHITE, Square.sq(lip, 2));
        }
        b.put(WHITE, Square.sq(2, 3));
        b.put(WHITE, Square.sq(7, 3));
    }


    static final String INIT_BOARD_STATE =
            "   - - - B - - B - - -\n"
                    + "   - - - - - - - - - -\n"
                    + "   - - - - - - - - - -\n"
                    + "   B - - - - - - - - B\n"
                    + "   - - - - - - - - - -\n"
                    + "   - - - - - - - - - -\n"
                    + "   W - - - - - - - - W\n"
                    + "   - - - - - - - - - -\n"
                    + "   - - - - - - - - - -\n"
                    + "   - - - W - - W - - -\n";

    static final String SMILE =
            "   - - - - - - - - - -\n"
                    + "   - S S S - - S S S -\n"
                    + "   - S - S - - S - S -\n"
                    + "   - S S S - - S S S -\n"
                    + "   - - - - - - - - - -\n"
                    + "   - - - - - - - - - -\n"
                    + "   - - W - - - - W - -\n"
                    + "   - - - W W W W - - -\n"
                    + "   - - - - - - - - - -\n"
                    + "   - - - - - - - - - -\n";

}





