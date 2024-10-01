package chess;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {
    private ChessBoard chessboard;
    private TeamColor teamTurn;

    public ChessGame() {
        this.chessboard = new ChessBoard();
    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return teamTurn;
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        this.teamTurn = team;
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        System.out.println(isInCheck(TeamColor.WHITE));
        ChessPiece piece = chessboard.getPiece(startPosition);
        if (piece == null) {
            return null;
        } else {
            return piece.pieceMoves(chessboard, startPosition);
        }
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        if (chessboard.getPiece(move.getStartPosition()) == null) {
            throw new InvalidMoveException("No piece here.");
        }
        ChessPiece piece = chessboard.getPiece(move.getStartPosition());
        Collection<ChessMove> validMoves = piece.pieceMoves(chessboard, move.getStartPosition());
        boolean isValidMove = false;
        for (ChessMove iterator : validMoves) {
            if (iterator.getEndPosition().equals(move.getEndPosition())) {
                isValidMove = true;
            }
        }
        if (isValidMove) {
            chessboard.RemovePiece(move.getStartPosition());
            chessboard.addPiece(move.getEndPosition(), piece);
        } else {
            throw new InvalidMoveException("Invalid Move!! Not in collection");
        }

    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        ChessPosition KingPosition = findKing(teamColor);
        HashMap<ChessPosition, ChessPiece> map = getEnemyPieces(teamColor);

        //Loop through the hashmap and get a collection of the moves they could use, if the king is in the endPosition, return true
        for (HashMap.Entry<ChessPosition, ChessPiece> entry : map.entrySet() ) {
            Collection<ChessMove> chessMoves = entry.getValue().pieceMoves(chessboard, entry.getKey());
            for (ChessMove iterator : chessMoves) {
                if (iterator.getEndPosition().equals(KingPosition)) {
                    return true;
                }
            }
        }
        return false;
    }

    //Loop through the board and find the teamColor king, return his position
    private ChessPosition findKing(TeamColor teamColor) {
        ChessPosition KingPosition = null;
        for (int i = 1; i < 8; i++) {
            for (int j = 1; j < 8; j++) {
                ChessPosition newPosition = new ChessPosition(i, j);
                ChessPiece piece = chessboard.getPiece(newPosition);
                if (piece != null) {
                    if (piece.getTeamColor() == teamColor && piece.getPieceType() == ChessPiece.PieceType.KING) {
                        KingPosition = newPosition;
                    }
                }

            }
        }
        return KingPosition;
    }

    //Loop through the board and return a map of enemy positions, and the pieces there.
    private HashMap<ChessPosition, ChessPiece> getEnemyPieces(TeamColor teamColor) {
        HashMap<ChessPosition, ChessPiece> enemyPieces = new HashMap<>();
        for (int i = 1; i < 8; i++) {
            for (int j = 1; j < 8; j++) {
                ChessPosition newPosition = new ChessPosition(i, j);
                ChessPiece piece = chessboard.getPiece(newPosition);
                if (piece != null) {
                     if (piece.getTeamColor() != teamColor) {
                        enemyPieces.put(newPosition, piece);
                    }
                }

            }
        }
        return enemyPieces;
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        if (isInCheck(teamColor)) {
            ChessPosition kingPosition = findKing(teamColor);
            ChessPiece King = chessboard.getPiece(kingPosition);
            Collection<ChessMove> validMoves = King.pieceMoves(chessboard, kingPosition);
            HashMap<ChessPosition, ChessPiece> map = getEnemyPieces(teamColor);
            if (validMoves.isEmpty()) {
                return true;
            }
        }
        return false;
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        this.chessboard = board;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return chessboard;
    }
}
