package amazons;

/*
NOTICE:
This file is a SUGGESTED skeleton.  NOTHING here or in any other source
file is sacred.  If any of it confuses you, throw it out and do it your way.
*/

import java.util.Collections;
import java.util.Iterator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
import java.util.NoSuchElementException;

import static amazons.Piece.*;


/** The state of an Amazons Game.
 *  @author Kevin Li
 */
class Board {
    /** An empty iterator for initialization. */
    private static final Iterator<Square> NO_SQUARES =
            Collections.emptyIterator();

    /** Piece whose turn it is (BLACK or WHITE). */
    private Piece _turn;
    /** Cached value of winner on this board, or EMPTY if it has not been
     *  computed. */
    private Piece _winner;
    /** HashMap that represents the board. */
    private Map<Square, Piece> squares;
    /** Stack that stores the moves and removes
     * the most recently inserted move. */
    private Stack<Move> moves;
    /** Set of all the squares belonging to the queen.*/
    private HashSet<Square> queens;

    /** The number of squares on a side of the board. */
    static final int SIZE = 10;

    /** Initializes a game board with SIZE squares on a side in the
     *  initial position. */
    Board() {
        init();
    }

    /** Initializes a copy of MODEL. */
    Board(Board model) {
        copy(model);
    }

    /** Copies MODEL into me. */
    void copy(Board model) {
        if (model == this) {
            return;
        }
        squares = new HashMap<>();
        queens = new HashSet<>();
        moves = new Stack<>();
        this.squares.putAll(model.squares);
        this._turn = model._turn;
        this._winner = model._winner;
        this.queens.addAll(model.queens);
        this.moves.addAll(model.moves);
    }

    /** Clears the board to the initial position. */
    void init() {
        squares = new HashMap<>();
        moves = new Stack<>();
        queens = new HashSet<>();
        for (int row = SIZE - 1; row >= 0; row--) {
            for (int col = SIZE - 1; col >= 0; col--) {
                if (row == 3 || row == 0) {
                    if (row == 3 && (col == 0 || col == 9)) {
                        squares.put(Square.sq(col, row), Piece.WHITE);
                        queens.add(Square.sq(col, row));
                        continue;
                    }
                    if (row == 0 && (col == 3 || col == 6)) {
                        squares.put(Square.sq(col, row), Piece.WHITE);
                        queens.add(Square.sq(col, row));
                        continue;
                    }
                }
                if (row == 6 || row == 9) {
                    if (row == 6 && (col == 0 || col == 9)) {
                        squares.put(Square.sq(col, row), Piece.BLACK);
                        queens.add(Square.sq(col, row));
                        continue;
                    }
                    if (row == 9 && (col == 3 || col == 6)) {
                        squares.put(Square.sq(col, row), Piece.BLACK);
                        queens.add(Square.sq(col, row));
                        continue;
                    }
                }
                squares.put(Square.sq(col, row), Piece.EMPTY);
            }
        }
        _turn = WHITE;
        _winner = null;
    }

    /** Return the Piece whose move it is (WHITE or BLACK). */
    Piece turn() {
        return _turn;
    }

    /** Return the number of moves (that have not been undone) for this
     *  board. */
    int numMoves() {
        return moves.size();
    }

    /** Return the winner in the current position, or null if the game is
     *  not yet finished. */
    Piece winner() {
        HashSet<Square> black = new HashSet<>();
        HashSet<Square> white = new HashSet<>();
        for (Square sq : queens) {
            if (get(sq) == WHITE) {
                white.add(sq);
            } else if (get(sq) == BLACK) {
                black.add(sq);
            }
        }
        if (black.isEmpty() || white.isEmpty()) {
            Iterator<Square> sq = Square.iterator();
            while (sq.hasNext()) {
                Square s = sq.next();
                if (get(s) == WHITE) {
                    white.add(s);
                } else if (get(s) == BLACK) {
                    black.add(s);
                }
            }
        }
        if (isSurrounded(white) && turn() == WHITE) {
            _winner = BLACK;
        } else if (isSurrounded(black) && turn() == BLACK) {
            _winner = WHITE;
        } else {
            _winner = null;
        }
        return _winner;
    }
    /** Tests whether the queens are surrounded by spears or other queens.
     * @param  setSq  Set of Squares
     * @return boolean  */
    boolean isSurrounded(Set<Square> setSq) {
        for (Square sq : setSq) {
            for (int i = -1; i < 2; i++) {
                for (int j = -1; j < 2; j++) {
                    if (Square.exists(sq.col() + i, sq.row() + j)
                            && get(Square.sq(sq.col() + i,
                            sq.row() + j)) == EMPTY) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    /** Return the contents the square at S. */
    final Piece get(Square s) {
        if (s == null) {
            return null;
        }
        return squares.get(s);
    }

    /** Return the contents of the square at (COL, ROW), where
     *  0 <= COL, ROW <= 9. */
    final Piece get(int col, int row) {
        return get(Square.sq(col, row));
    }

    /** Return the contents of the square at COL ROW. */
    final Piece get(char col, char row) {
        return get(col - 'a', row - '1');
    }

    /** Set square S to P. */
    final void put(Piece p, Square s) {
        squares.put(s, p);
    }

    /** Set square (COL, ROW) to P. */
    final void put(Piece p, int col, int row) {
        put(p, Square.sq(col, row));
    }

    /** Set square COL ROW to P. */
    final void put(Piece p, char col, char row) {
        put(p, col - 'a', row - '1');
    }

    /** Return true iff FROM - TO is an unblocked queen move on the current
     *  board, ignoring the contents of ASEMPTY, if it is encountered.
     *  For this to be true, FROM-TO must be a queen move and the
     *  squares along it, other than FROM and ASEMPTY, must be
     *  empty. ASEMPTY may be null, in which case it has no effect. */
    boolean isUnblockedMove(Square from, Square to, Square asEmpty) {
        if (to == null || from == null || !from.isQueenMove(to)) {
            return false;
        }
        int colDif = Math.abs(to.col() - from.col());
        int rowDif = Math.abs(to.row() - from.row());
        for (int i = 1; i <= Math.max(colDif, rowDif); i++) {
            Square test = from.queenMove(from.direction(to), i);
            if (test != null && test != asEmpty
                    && !get(test).equals(Piece.EMPTY)) {
                return false;
            }
        }
        return true;
    }

    /** Return true iff FROM is a valid starting square for a move. */
    boolean isLegal(Square from) {
        return get(from).equals(turn());
    }

    /** Return true iff FROM-TO is a valid first part of move, ignoring
     *  spear throwing. */
    boolean isLegal(Square from, Square to) {
        return isLegal(from) && isUnblockedMove(from, to, null);
    }

    /** Return true iff FROM-TO(SPEAR) is a legal move in the current
     *  position. */
    boolean isLegal(Square from, Square to, Square spear) {
        return isLegal(from)
                && isUnblockedMove(from, to, null)
                && isUnblockedMove(to, spear, from);
    }

    /** Return true iff MOVE is a legal move in the current
     *  position. */
    boolean isLegal(Move move) {
        return isLegal(move.from(), move.to(), move.spear());
    }

    /** Move FROM-TO(SPEAR), assuming this is a legal move. */
    void makeMove(Square from, Square to, Square spear) {
        if (isLegal(from, to, spear)) {
            Move legalMove = Move.mv(from, to, spear);
            moves.add(legalMove);
            put(Piece.EMPTY, from);
            put(turn(), to);
            put(Piece.SPEAR, spear);
            queens.add(legalMove.to());
            queens.remove(legalMove.from());
            _turn = _turn.opponent();
        }
    }

    /** Move according to MOVE, assuming it is a legal move. */
    void makeMove(Move move) {
        makeMove(move.from(), move.to(), move.spear());
    }
    /** Undo two moves.  Has no effect on the initial board. */
    void undo() {
        for (int i = 0; i < 2 && !moves.empty(); i++) {
            undoOnce();
        }
    }

    /** Undo one move.  Has no effect on the initial board. */
    void undoOnce() {
        if (moves.empty()) {
            return;
        }
        Move moveToRemove = moves.pop();
        put(Piece.EMPTY, moveToRemove.spear());
        put(get(moveToRemove.to()), moveToRemove.from());
        put(Piece.EMPTY, moveToRemove.to());
        queens.add(moveToRemove.from());
        queens.remove(moveToRemove.to());
        _turn = turn().opponent();
    }

    /** Return an Iterator over the Squares that are reachable by an
     *  unblocked queen move from FROM. Does not pay attention to what
     *  piece (if any) is on FROM, nor to whether the game is finished.
     *  Treats square ASEMPTY (if non-null) as if it were EMPTY.  (This
     *  feature is useful when looking for Moves, because after moving a
     *  piece, one wants to treat the Square it came from as empty for
     *  purposes of spear throwing.) */
    Iterator<Square> reachableFrom(Square from, Square asEmpty) {
        return new ReachableFromIterator(from, asEmpty);
    }

    /** Return an Iterator over all legal moves on the current board. */
    Iterator<Move> legalMoves() {
        return new LegalMoveIterator(_turn);
    }

    /** Return an Iterator over all legal moves on the current board for
     *  SIDE (regardless of whose turn it is). */
    Iterator<Move> legalMoves(Piece side) {
        return new LegalMoveIterator(side);
    }

    /** An iterator used by reachableFrom. */
    private class ReachableFromIterator implements Iterator<Square> {
        /** Starting square. */
        private Square _from;
        /** Current direction. */
        private int _dir;
        /** Current distance. */
        private int _steps;
        /** Square treated as empty. */
        private Square _asEmpty;

        /** Iterator of all squares reachable by queen move from FROM,
         *  treating ASEMPTY as empty. */
        ReachableFromIterator(Square from, Square asEmpty) {
            _from = from;
            _dir = 0;
            _steps = 0;
            _asEmpty = asEmpty;
            toNext();
        }

        @Override
        public boolean hasNext() {
            return _dir < 8;
        }

        @Override
        public Square next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            Square save = _from.queenMove(_dir, _steps);
            toNext();
            return save;
        }

        /** Advance _dir and _steps, so that the next valid Square is
         *  _steps steps in direction _dir from _from. */
        private void toNext() {
            _steps += 1;
            Square save = _from.queenMove(_dir, _steps);
            while (save == null
                    || (save != _asEmpty && get(save) != Piece.EMPTY)) {
                if (_dir >= 8) {
                    break;
                }
                _dir += 1;
                _steps = 1;
                save = _from.queenMove(_dir, _steps);
            }
        }
    }

    /** An iterator used by legalMoves. */
    private class LegalMoveIterator implements Iterator<Move> {
        /** Color of side whose moves we are iterating. */
        private Piece _fromPiece;
        /** Current starting square. */
        private Square _start;
        /** Remaining starting squares to consider. */
        private Iterator<Square> _startingSquares;
        /** Current piece's new position. */
        private Square pNext;
        /** Remaining moves from _start to consider. */
        private Iterator<Square> _pieceMoves;
        /** Remaining spear throws from _piece to consider. */
        private Iterator<Square> _spearThrows;


        /** All legal moves for SIDE (WHITE or BLACK). */
        LegalMoveIterator(Piece side) {
            _startingSquares = Square.iterator();
            _spearThrows = NO_SQUARES;
            _pieceMoves = NO_SQUARES;
            _fromPiece = side;
            toNext();
        }

        @Override
        public boolean hasNext() {
            return _pieceMoves.hasNext() || _spearThrows.hasNext()
                    || _startingSquares.hasNext();
        }

        @Override
        public Move next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            Square sNext = _spearThrows.next();
            Move mv = Move.mv(_start, pNext, sNext);
            toNext();
            return mv;
        }

        /** Advance so that the next valid Move is
         *  _start-_nextSquare(sp), where sp is the next value of
         *  _spearThrows. */
        private void toNext() {
            if (!_spearThrows.hasNext()) {
                if (!_pieceMoves.hasNext()) {
                    while (_startingSquares.hasNext()) {
                        Square test = _startingSquares.next();
                        if (get(test) == _fromPiece) {
                            _start = test;
                            _pieceMoves = reachableFrom(test, null);
                            break;
                        } else if (!_startingSquares.hasNext()) {
                            return;
                        }
                    }
                    if (!_pieceMoves.hasNext()) {
                        if (_startingSquares.hasNext()) {
                            toNext();
                        }
                        return;
                    }
                }
                pNext = _pieceMoves.next();
                _spearThrows = reachableFrom(pNext, _start);
            }
        }
    }

    @Override
    public String toString() {
        Iterator<Square> sq = Square.iterator();
        String string = "";
        for (int i = 0; sq.hasNext(); i += SIZE) {
            String temp = "   ";
            for (int j = i; j < i + SIZE; j++) {
                temp += get(sq.next()).toString() + " ";
            }
            string = temp.substring(0, temp.length() - 1) + "\n" + string;
        }
        return string;
    }

}
