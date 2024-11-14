package ui;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;

public class TerminalChessBoard {

    private static final String RESET = "\u001B[0m";
    private static final String BLACK = "\u001B[30m";
    private static final String WHITE = "\u001B[37m";
    private static final String LIGHT_GREY_BG = "\u001B[47m";
    private static final String BROWN_BG = "\u001B[48;5;95m";


    public static void printBoard(ChessBoard board, boolean whiteView) {
        printRowLetters(whiteView);

        for (int col = whiteView ? 8 : 1; whiteView ? col >= 1 : col <= 8; col = whiteView ? col - 1 : col + 1) {
            System.out.print(col + " ");

            for (int row = whiteView ? 1 : 8; whiteView ? row <= 8 : row >= 1; row = whiteView ? row + 1 : row - 1) {
                ChessPosition pos = new ChessPosition(col, row);
                ChessPiece piece = board.getPiece(pos);
                boolean isWhiteSquare = (col + row) % 2 == 1;
                printSquare(piece, isWhiteSquare);
            }

            System.out.println(" " + col);
        }

        printRowLetters(whiteView);
    }

    private static void printRowLetters(boolean whiteView) {
        System.out.print("  ");
        if (whiteView) {
            for (int i = 0; i < 8; i++) {
                System.out.print(" " + (char)('a' + i) + " ");
            }
        } else {
            for (int i = 7; i >= 0; i--) {
                System.out.print(" " + (char)('a' + i) + " ");
            }
        }
        System.out.println();
    }

    private static void printSquare(ChessPiece piece, boolean isWhiteSquare) {
        String background = isWhiteSquare ? LIGHT_GREY_BG : BROWN_BG;

        if (piece != null) {
            String pieceColor = piece.getTeamColor() == ChessGame.TeamColor.WHITE ? "" : BLACK;
            String pieceLetter = piece.stringLetter();
            System.out.print(background + pieceColor + " " + pieceLetter + " " + RESET);
        } else {
            System.out.print(background + "   " + RESET);
        }
    }

    public static void printBothViews(ChessBoard board) {
        board.resetBoard();
        System.out.println("\n");

        printBoard(board, false);

        System.out.println("\n");

        printBoard(board, true);
    }

    public static void main(String[] args) {
        printBothViews(new ChessBoard());
    }

}
