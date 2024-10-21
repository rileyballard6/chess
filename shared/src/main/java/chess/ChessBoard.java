package chess;

import java.util.Arrays;
import java.util.Objects;

/**
 * A chessboard that can hold and rearrange chess pieces.
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessBoard {
    private ChessPiece[][] chessboard = new ChessPiece[9][9];

    public ChessBoard() {
        
    }

    /**
     * Adds a chess piece to the chessboard
     *
     * @param position where to add the piece to
     * @param piece    the piece to add
     */
    public void addPiece(ChessPosition position, ChessPiece piece) {
        chessboard[position.getRow()][position.getColumn()] = piece;
    }

    /**
     * Gets a chess piece on the chessboard
     *
     * @param position The position to get the piece from
     * @return Either the piece at the position, or null if no piece is at that
     * position
     */
    public ChessPiece getPiece(ChessPosition position) {
        if (position.getRow() < 0 || position.getRow() >= 9 || position.getColumn() < 0 || position.getColumn() >= 9) {
            return null;
        }
        return chessboard[position.getRow()][position.getColumn()];
    }

    public void removePiece(ChessPosition position) {
        chessboard[position.getRow()][position.getColumn()] = null;
    }

    /**
     * Sets the board to the default starting board
     * (How the game of chess normally starts)
     */
    public void resetBoard() {
        ChessPiece.PieceType[] pieceTypeOrder = {
                ChessPiece.PieceType.ROOK,
                ChessPiece.PieceType.KNIGHT,
                ChessPiece.PieceType.BISHOP,
                ChessPiece.PieceType.QUEEN,
                ChessPiece.PieceType.KING,
                ChessPiece.PieceType.BISHOP,
                ChessPiece.PieceType.KNIGHT,
                ChessPiece.PieceType.ROOK};
        chessboard = new ChessPiece[9][9];
        for (int i = 1; i <= 8; i++) {
            chessboard[2][i] = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
            chessboard[1][i] = new ChessPiece(ChessGame.TeamColor.WHITE, pieceTypeOrder[i-1]);
            chessboard[7][i] = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN);
            chessboard[8][i] = new ChessPiece(ChessGame.TeamColor.BLACK, pieceTypeOrder[i-1]);
        }

    }
    public ChessBoard copy() {
        ChessBoard newBoard = new ChessBoard();
        // Deep copy the board array
        newBoard.chessboard = new ChessPiece[9][9]; // assuming standard chessboard size
        for (int i = 1; i < 9; i++) {
            for (int j = 1; j < 9; j++) {
                if (this.chessboard[i][j] != null) {
                    newBoard.chessboard[i][j] = this.chessboard[i][j].copy(); // assuming Piece has a copy method
                }
            }
        }
        return newBoard;
    }

    @Override
    public String toString() {
        String chessboardString = "";
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (chessboard[i][j] != null) {
                    chessboardString += chessboard[i][j].stringLetter();
                } else {
                    chessboardString += "-";
                }
            }
        }
        return chessboardString;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {return true;}
        if (o == null || getClass() != o.getClass()) {return false;}
        ChessBoard that = (ChessBoard) o;
        return Objects.deepEquals(chessboard, that.chessboard);
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(chessboard);
    }
}
