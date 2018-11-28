package amazons;

/*
NOTICE:
This file is a SUGGESTED skeleton.  NOTHING here or in any other source
file is sacred.  If any of it confuses you, throw it out and do it your way.
*/


import java.util.HashSet;
import java.util.Set;
import java.util.Iterator;

import static amazons.Piece.*;

/** A Player that automatically generates moves.
 *  @author Kevin Li
 */
class AI extends Player {

    /**
     * A position magnitude indicating a win (for white if positive, black
     * if negative).
     */
    private static final int WINNING_VALUE = Integer.MAX_VALUE - 1;
    /**
     * A magnitude greater than a normal value.
     */
    private static final int INFTY = Integer.MAX_VALUE;

    /**
     * A new AI with no piece or controller (intended to produce
     * a template).
     */
    AI() {
        this(null, null);
    }

    /**
     * A new AI playing PIECE under control of CONTROLLER.
     */
    AI(Piece piece, Controller controller) {
        super(piece, controller);
    }

    @Override
    Player create(Piece piece, Controller controller) {
        return new AI(piece, controller);
    }

    @Override
    String myMove() {
        Move move = findMove();
        _controller.reportMove(move);
        return move.toString();
    }

    /**
     * Return a move for me from the current position, assuming there
     * is a move.
     */
    private Move findMove() {
        Board b = new Board(board());
        if (_myPiece == WHITE) {
            findMove(b, maxDepth(b), true, 1, -INFTY, INFTY);
        } else {
            findMove(b, maxDepth(b), true, -1, -INFTY, INFTY);
        }
        return _lastFoundMove;
    }

    /**
     * The move found by the last call to one of the ...FindMove methods
     * below.
     */
    private Move _lastFoundMove;

    /**
     * Find a move from position BOARD and return its value, recording
     * the move found in _lastFoundMove iff SAVEMOVE. The move
     * should have maximal value or have value > BETA if SENSE==1,
     * and minimal value or value < ALPHA if SENSE==-1. Searches up to
     * DEPTH levels.  Searching at level 0 simply returns a static estimate
     * of the board value and does not set _lastMoveFound.
     */
    private int findMove(Board board, int depth, boolean saveMove, int sense,
                         int alpha, int beta) {
        if (depth == 0 || board.winner() != null) {
            return staticScore(board);
        }
        Iterator<Move> it = board.legalMoves(board.turn());
        while (it.hasNext()) {
            Move mv = it.next();
            board.makeMove(mv);
            int score = findMove(board, depth - 1, false, -sense, alpha, beta);
            if (sense == 1) {
                if (alpha < score) {
                    alpha = score;
                    if (saveMove) {
                        _lastFoundMove = mv;
                    }
                }
                if (alpha > beta) {
                    board.undoOnce();
                    return score;
                }
            } else if (sense == -1) {
                if (beta > score) {
                    beta = score;
                    if (saveMove) {
                        _lastFoundMove = mv;
                    }
                }
                if (alpha > beta) {
                    board.undoOnce();
                    return score;
                }
            }
            board.undoOnce();
        }
        if (sense == 1) {
            return alpha;
        }
        return beta;
    }

    /**
     * Return a heuristically determined maximum search depth
     * based on characteristics of BOARD.
     */
    int maxDepth(Board board) {
        int numMove = board.numMoves();
        int number = (2 * board.SIZE + 2) * 2;
        int number2 = number + (3 * board.SIZE);
        if (numMove <= number) {
            return 1;
        } else if (numMove >= number2) {
            return 5;
        } else {
            return 3;
        }
    }
    /**
     * Return a heuristic value for BOARD.
     */
    int staticScore(Board board) {
        Piece winner = board.winner();
        if (winner == BLACK) {
            return -WINNING_VALUE;
        } else if (winner == WHITE) {
            return WINNING_VALUE;
        }
        Set<Square> squareWhite = new HashSet<>();
        Set<Square> squareBlack = new HashSet<>();
        Iterator<Square> queenIter = Square.iterator();
        while (queenIter.hasNext()) {
            Square sq = queenIter.next();
            if (board.get(sq) == WHITE) {
                squareWhite.add(sq);
            } else if (board.get(sq) == BLACK) {
                squareBlack.add(sq);
            }
        }
        dist(squareWhite, squareBlack, board);
        int total = squareWhite.size() - squareBlack.size();
        return total;
    }
    /**
     territory(squareWhite, squareBlack, board);
     total += 4 * (squareWhite.size() - squareBlack.size());
     */
    /** Find reachable squares for each queen (both white and black).
     * @param setAI  Set of White
     * @param setOpp  Set of Black
     * @param board  Board    */
    private void dist(Set<Square> setAI, Set<Square> setOpp, Board board) {
        Iterator<Square> iterWhite = setAI.iterator();
        Iterator<Square> iterBlack = setOpp.iterator();
        Set<Square> additAI = new HashSet<>();
        Set<Square> additOpp = new HashSet<>();
        while (iterWhite.hasNext() || iterBlack.hasNext()) {
            if (iterWhite.hasNext()) {
                Square locAt = iterWhite.next();
                Iterator<Square> reachWhite = board.reachableFrom(locAt, null);
                while (reachWhite.hasNext()) {
                    Square temp = reachWhite.next();
                    if (temp != null && !setOpp.contains(temp)) {
                        additAI.add(temp);
                    }
                }
            }
            if (iterBlack.hasNext()) {
                Square locAt = iterBlack.next();
                Iterator<Square> reach = board.reachableFrom(locAt, null);
                while (reach.hasNext()) {
                    Square temp = reach.next();
                    if (temp != null && !setAI.contains(temp)) {
                        additOpp.add(temp);
                    }
                }
            }
        }
        setAI.removeAll(setAI);
        setOpp.removeAll(setOpp);
        setAI.addAll(additAI);
        setOpp.addAll(additOpp);
    }
    /** Find territory belonging to white and black.
     * @param setAI   Set of White
     * @param setOpp  Set of Black
     * @param board  Board
     */
    private void territory(Set<Square> setAI, Set<Square> setOpp, Board board) {
        Iterator<Square> iterWhite = setAI.iterator();
        Iterator<Square> iterBlack = setOpp.iterator();
        Set<Square> additAI = new HashSet<>();
        Set<Square> additOpp = new HashSet<>();
        while (iterWhite.hasNext() || iterBlack.hasNext()) {
            if (iterWhite.hasNext()) {
                Square locAt = iterWhite.next();
                Iterator<Square> reachWhite = board.reachableFrom(locAt, null);
                while (reachWhite.hasNext()) {
                    Square temp = reachWhite.next();
                    if (temp != null && !setOpp.contains(temp)) {
                        additAI.add(temp);
                    }
                }
            }
            if (iterBlack.hasNext()) {
                Square locAt = iterBlack.next();
                Iterator<Square> reach = board.reachableFrom(locAt, null);
                while (reach.hasNext()) {
                    Square temp = reach.next();
                    if (temp != null && !setAI.contains(temp)) {
                        additOpp.add(temp);
                    }
                }
            }
        }
        setAI.addAll(additAI);
        setOpp.addAll(additOpp);
    }
}
