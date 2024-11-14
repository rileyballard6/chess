package ui;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;

public class TerminalChessBoard {

    private static final String RESET = "\u001B[0m";
    private static final String BLACK = "\u001B[30m";
    private static final String LIGHT_GREY_BG = "\u001B[47m";
    private static final String BROWN_BG = "\u001B[48;5;95m";


    public static void printBoard(ChessBoard board, boolean whitePerspective) {
        printLetters(whitePerspective);
        int col = whitePerspective ? 8 : 1;
        int row = whitePerspective ? 1 : 8;
        int colAddition = whitePerspective ? -1 : 1;
        int rowAddition = whitePerspective ? 1 : -1;

        for (int i = col; whitePerspective ? i >= 1 : i <= 8; i += colAddition) {
            System.out.print(i + " ");

            for (int j = row; whitePerspective ? j <= 8 : j >= 1; j += rowAddition) {
                ChessPosition pos = new ChessPosition(i, j);
                ChessPiece piece = board.getPiece(pos);
                boolean isWhiteSquare = (i + j) % 2 == 1;
                printTile(piece, isWhiteSquare);
            }

            System.out.println(" " + i);
        }

        printLetters(whitePerspective);
    }

    private static void printTile(ChessPiece piece, boolean isWhiteSquare) {
        String background = isWhiteSquare ? LIGHT_GREY_BG : BROWN_BG;

        if (piece != null) {
            String pieceColor = piece.getTeamColor() == ChessGame.TeamColor.WHITE ? "" : BLACK;
            String pieceLetter = piece.stringLetter();
            System.out.print(background + pieceColor + " " + pieceLetter + " " + RESET);
        } else {
            System.out.print(background + "   " + RESET);
        }
    }

    private static void printLetters(boolean whitePerspective) {
        System.out.print("  ");
        if (whitePerspective) {
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
