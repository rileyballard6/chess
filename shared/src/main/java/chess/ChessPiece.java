package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {

    private final PieceType type;
    private final ChessGame.TeamColor pieceColor;

    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.pieceColor = pieceColor;
        this.type = type;
    }

    /**
     * The various different chess piece options
     */
    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }

    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor() {
        throw new RuntimeException("Not implemented");
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return type;
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> validMoves = new ArrayList<>();
        switch (type) {
            case ROOK -> validMoves = getValidMovesRook(board, myPosition);
            case KING -> validMoves = getValidMovesKing(board, myPosition);
        }
        return validMoves;
    }

    public Collection<ChessMove> getValidMovesKing(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> validMoves = new ArrayList<>();
        int column = myPosition.getColumn();
        int row = myPosition.getRow();
        return validMoves;
    }

    public Collection<ChessMove> getValidMovesRook(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> validMoves = new ArrayList<>();
        int column = myPosition.getColumn();
        int row = myPosition.getRow();
        for (int i = row; i > 0; i--) {
            ChessPosition new_position = new ChessPosition(i, column);
            if (board.getPiece(new_position) == null) {
                validMoves.add(new ChessMove(myPosition, new_position, null));
            }
        }
        for (int i = row; i <= 8; i++) {
            ChessPosition new_position = new ChessPosition(i, column);
            if (board.getPiece(new_position) == null) {
                validMoves.add(new ChessMove(myPosition, new_position, null));
            }
        }
        for (int i = column; i <= 8; i++) {
            ChessPosition new_position = new ChessPosition(row, i);
            if (board.getPiece(new_position) == null) {
                validMoves.add(new ChessMove(myPosition, new_position, null));
            }
        }
        for (int i = column; i > 0; i--) {
            ChessPosition new_position = new ChessPosition(row, i);
            if (board.getPiece(new_position) == null) {
                validMoves.add(new ChessMove(myPosition, new_position, null));
            }
        }
        return validMoves;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessPiece that = (ChessPiece) o;
        return type == that.type && pieceColor == that.pieceColor;
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, pieceColor);
    }

    @Override
    public String toString() {
        return "ChessPiece{" + pieceColor +
                "," + type +
                '}';
    }
}
