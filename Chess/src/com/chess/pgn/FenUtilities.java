package com.chess.pgn;

import com.chess.engine.BoardUtils;
import com.chess.engine.board.Board;
import com.chess.engine.pieces.Pawn;

import java.util.ArrayList;
import java.util.List;

public class FenUtilities {

    private static List<String> fenLog;

    private FenUtilities(){

    }

    public static List<String> getFENLog() {
        return fenLog;
    }

    public static String createFENfromBoard(final Board board){
        return  calculateBoardText(board) + " " +
                calculateCurrentPlayerText(board) + " " +
                calculateCastleText(board) + " " +
                calculateEnPassantSquare(board) +" " +
                "0 1";
    }

    private static String calculateBoardText(Board board) {
        final StringBuilder builder = new StringBuilder();
        for(int i=0; i<BoardUtils.NUM_TILES; i++)
            builder.append(board.getTile(i).toString());
        builder.insert(8, "/");
        builder.insert(17, "/");
        builder.insert(26, "/");
        builder.insert(35, "/");
        builder.insert(44, "/");
        builder.insert(53, "/");
        builder.insert(62, "/");
        return   builder.toString()
                .replaceAll("--------", "8")
                .replaceAll("-------", "7")
                .replaceAll("------", "6")
                .replaceAll("-----", "5")
                .replaceAll("----", "4")
                .replaceAll("---", "3")
                .replaceAll("--", "2")
                .replaceAll("-", "1");

    }

    private static String calculateCurrentPlayerText(Board board) {
        return board.currentPlayer().toString().substring(0,1).toLowerCase();
    }

    private static String calculateCastleText(final Board board) {
        final StringBuilder builder = new StringBuilder();
        if(board.whitePlayer().isKingSideCastleCapable())
            builder.append("K");
        if(board.whitePlayer().isQueenSideCastleCapable())
            builder.append("Q");
        if(board.blackPlayer().isKingSideCastleCapable())
            builder.append("k");
        if(board.blackPlayer().isQueenSideCastleCapable())
            builder.append("q");
        return builder.toString().isEmpty()? "-" : builder.toString();
    }

    private static String calculateEnPassantSquare(Board board) {
        final Pawn enPassantPawn = board.getEnPassantPawn();
        if(enPassantPawn != null){
            return BoardUtils.getPositionAtCoordinate(enPassantPawn.getPiecePosition() +
                    (8 * enPassantPawn.getPieceColor().getOppositeDirection()));
        }
        return "-";
    }

    public static void saveFEN(Board chessBoard) {
        if(fenLog == null)
            fenLog = new ArrayList<>();
        fenLog.add(createFENfromBoard(chessBoard));
    }
}
