package com.chess.engine.player.ai;

import java.util.Collection;

import com.chess.engine.BoardUtils;
import com.chess.engine.board.Move;
import com.chess.engine.pieces.Piece;
import com.chess.engine.player.Player;

public final class KingSafetyAnalyzer {

    private static final KingSafetyAnalyzer INSTANCE = new KingSafetyAnalyzer();

    private KingSafetyAnalyzer() {
    }

    public static KingSafetyAnalyzer get() {
        return INSTANCE;
    }

    KingDistance calculateKingTropism(final Player player) {
        final int playerKingSquare = player.getPlayerKing().getPiecePosition();
        final Collection<Move> enemyMoves = player.getOpponent().getLegalMoves();
        Piece closestPiece = null;
        int closestDistance = Integer.MAX_VALUE;
        for(final Move move : enemyMoves) {
            final int currentDistance = calculateChebyshevDistance(playerKingSquare, move.getDestinationCoordinate());
            if(currentDistance < closestDistance) {
                closestDistance = currentDistance;
                closestPiece = move.getMovedPiece();
            }
        }
        return new KingDistance(closestPiece, closestDistance);
    }

    private int calculateChebyshevDistance(final int pieceOneSquare,
                                           final int pieceTwoSquare) {

        final int squareOneRank = getRank(pieceOneSquare);
        final int squareTwoRank = getRank(pieceTwoSquare);

        final int squareOneFile = getFile(pieceOneSquare);
        final int squareTwoFile = getFile(pieceTwoSquare);

        final int rankDistance = Math.abs(squareTwoRank - squareOneRank);
        final int fileDistance = Math.abs(squareTwoFile - squareOneFile);

        return Math.max(rankDistance, fileDistance);
    }

    private static int getFile(final int coordinate) {
        if(BoardUtils.FIRST_COLUMN[coordinate]) {
            return 1;
        } else if(BoardUtils.SECOND_COLUMN[coordinate]) {
            return 2;
        } else if(BoardUtils.THIRD_COLUMN[coordinate]) {
            return 3;
        } else if(BoardUtils.FOURTH_COLUMN[coordinate]) {
            return 4;
        } else if(BoardUtils.FIFTH_COLUMN[coordinate]) {
            return 5;
        } else if(BoardUtils.SIXTH_COLUMN[coordinate]) {
            return 6;
        } else if(BoardUtils.SEVENTH_COLUMN[coordinate]) {
            return 7;
        } else if(BoardUtils.EIGHTH_COLUMN[coordinate]) {
            return 8;
        }
        throw new RuntimeException("should not reach here!");
    }

    private static int getRank(final int coordinate) {
        if(BoardUtils.FIRST_RANK[coordinate]) {
            return 1;
        } else if(BoardUtils.SECOND_RANK[coordinate]) {
            return 2;
        } else if(BoardUtils.THIRD_RANK[coordinate]) {
            return 3;
        } else if(BoardUtils.FOURTH_RANK[coordinate]) {
            return 4;
        } else if(BoardUtils.FIFTH_RANK[coordinate]) {
            return 5;
        } else if(BoardUtils.SIXTH_RANK[coordinate]) {
            return 6;
        } else if(BoardUtils.SEVENTH_RANK[coordinate]) {
            return 7;
        } else if(BoardUtils.EIGHTH_RANK[coordinate]) {
            return 8;
        }
        throw new RuntimeException("should not reach here!");
    }

    static class KingDistance {

        final Piece closestEnemy;
        final int distance;

        KingDistance(final Piece closestEnemy,
                     final int distance) {
            this.closestEnemy = closestEnemy;
            this.distance = distance;
        }

        Piece getEnemyPiece() {
            return closestEnemy;
        }

        int getDistance() {
            return distance;
        }
    }

}