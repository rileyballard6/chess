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
        return pieceColor;
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
            // supply the getValidMoves function with board, position, a list of the directions the piece can move, and
            // whether it can move multiple tiles. Pawn gets its own function because it is annoying and stinks!
            case ROOK -> validMoves = getValidMoves(board, myPosition, new int[][] {{-1, 0}, {1, 0}, {0, 1}, {0, -1}}, true);
            case KING -> validMoves = getValidMoves(board, myPosition, new int[][] {{-1,0}, {1,0}, {0,1}, {0,-1}, {-1,-1}, {1,-1}, {-1,1}, {1,1},}, false );
            case BISHOP -> validMoves = getValidMoves(board, myPosition, new int[][] {{-1,-1}, {1,-1}, {-1,1}, {1,1},}, true);
            case QUEEN -> validMoves = getValidMoves(board, myPosition, new int[][] {{-1,0}, {1,0}, {0,1}, {0,-1}, {-1,-1}, {1,-1}, {-1,1}, {1,1},}, true);
            case KNIGHT -> validMoves = getValidMoves(board, myPosition, new int[][] {{1,2}, {1,-2}, {2,1}, {2, -1}, {-2,1}, {-2,-1}, {-1, 2}, {-1, -2}}, false);
            case PAWN -> validMoves = getValidMovesPawn(board, myPosition);
        }
        return validMoves;
    }

    private Collection<ChessMove> getValidMovesPawn(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> validMoves = new ArrayList<>();
        int column = myPosition.getColumn();
        int row = myPosition.getRow();


        return validMoves;
    }

    // returns the validMoves to the main function. Gets the initial row and column of the piece to pass into the loop function.
    private Collection<ChessMove> getValidMoves(ChessBoard board, ChessPosition myPosition, int[][] directions, boolean loop) {
        Collection<ChessMove> validMoves = new ArrayList<>();
        int column = myPosition.getColumn();
        int row = myPosition.getRow();

        if (loop) {
            pieceLoopMultipleMoves(board, myPosition, row, column, directions, validMoves);
        } else {
            pieceLoopOneMove(board, myPosition, row, column, directions, validMoves);
        }

        return validMoves;
    }

    //loop over each direction and while the condition is true, add that direction to the newRow and newCol to make a new position
    //Check if the position is occupied, if not, add ChessMove, if so, check if it is your piece or opponent to add or break
    private void pieceLoopMultipleMoves(ChessBoard board, ChessPosition myPosition, int row, int column, int[][] directions, Collection<ChessMove> validMoves) {
        for (int[] direction : directions) {
            int rowOffset = direction[0];
            int colOffset = direction[1];
            int newRow = row;
            int newCol = column;

            while (true) {
                newRow += rowOffset;
                newCol += colOffset;

                if (newRow <= 0 || newRow > 8 || newCol <= 0 || newCol > 8) break;

                ChessPosition new_position = new ChessPosition(newRow, newCol);

                if (board.getPiece(new_position) == null) {
                    validMoves.add(new ChessMove(myPosition, new_position, null));
                } else if (board.getPiece(new_position).getTeamColor() == pieceColor) {
                    System.out.println("Teammate here. Cannot move");
                    break;
                } else {
                    validMoves.add(new ChessMove(myPosition, new_position, null));
                    System.out.println("Captured Piece!");
                    break;
                }
            }
        }
    }

    // Same as the above loop but dont need "while true" since these pieces can only move once
    private void pieceLoopOneMove(ChessBoard board, ChessPosition myPosition, int row, int column, int[][] directions, Collection<ChessMove> validMoves) {
        for (int[] direction : directions) {
            int rowOffset = direction[0];
            int colOffset = direction[1];
            int newRow = row + rowOffset;
            int newCol = column + colOffset;

            if (newRow <= 0 || newRow > 8 || newCol <= 0 || newCol > 8) continue;
            ChessPosition new_position = new ChessPosition(newRow, newCol);

            if (board.getPiece(new_position) == null) {
                validMoves.add(new ChessMove(myPosition, new_position, null));
            } else if (board.getPiece(new_position).getTeamColor() == pieceColor) {
                System.out.println("Teammate here. Cannot move.");
            } else {
                System.out.println("Piece captured!");
                validMoves.add(new ChessMove(myPosition, new_position, null));
            }

        }
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
