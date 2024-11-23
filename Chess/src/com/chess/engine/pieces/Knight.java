package com.chess.engine.pieces;

import com.chess.engine.BoardUtils;
import com.chess.engine.PieceColor;
import com.chess.engine.board.Board;
import com.chess.engine.board.Move;
import com.chess.engine.board.Move.*;
import com.chess.engine.board.Tile;
import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Knight extends Piece {

    private final static int[] MOVE_OFFSETS= { -17, -15, -10, -6, 6, 10, 15, 17};
    public Knight(final PieceColor pieceColor, final int piecePosition) {
        super(PieceType.KNIGHT,piecePosition, pieceColor,true);
    }

    @Override
    public Collection<Move> calculateLegalMoves(final Board board) {
        final List<Move> legalMoves = new ArrayList<>();
        for(final int currentOffset : MOVE_OFFSETS){
            int destinationPosition = piecePosition + currentOffset;
            if(BoardUtils.validDestinationPosition(destinationPosition)) {
                if(isFirstColumnExclusion(this.piecePosition, currentOffset) ||
                        isSecondColumnExclusion(this.piecePosition, currentOffset) ||
                        isSeventhColumnExclusion(this.piecePosition, currentOffset) ||
                        isEighthColumnExclusion(this.piecePosition, currentOffset)) {
                    continue;
                }
                final Tile destinationTile = board.getTile(destinationPosition);
                if (!destinationTile.isTileOccupied()) {
                    legalMoves.add(new MajorMove(board, this, destinationPosition));
                } else {
                    final Piece destinationPiece = destinationTile.getPiece();
                    final PieceColor destinationPieceColor = destinationPiece.getPieceColor();

                    if (destinationPieceColor != this.pieceColor) {
                        legalMoves.add(new MajorAttackMove(board,this, destinationPosition, destinationPiece));
                    }
                }
            }
        }
        return ImmutableList.copyOf(legalMoves);
    }

    private static boolean isFirstColumnExclusion(final int currentPosition, final int currentOffset){
        return BoardUtils.FIRST_COLUMN[currentPosition] && (currentOffset == -17 || currentOffset == -10 || currentOffset == 6 || currentOffset == 15);
    }
    private static boolean isSecondColumnExclusion(final int currentPosition, final int currentOffset){
        return BoardUtils.SECOND_COLUMN[currentPosition] && (currentOffset == -10 || currentOffset == 6);
    }
    private static boolean isSeventhColumnExclusion(final int currentPosition, final int currentOffset){
        return BoardUtils.SEVENTH_COLUMN[currentPosition] && (currentOffset == 10 || currentOffset ==-6);
    }
    private static boolean isEighthColumnExclusion(final int currentPosition, final int currentOffset){
        return BoardUtils.EIGHTH_COLUMN[currentPosition] && (currentOffset == 17 || currentOffset == 10 || currentOffset == -6 || currentOffset == -15);
    }

    @Override
    public String toString(){
        return this.pieceType.toString();
    }

    @Override
    public int locationBonus() {
        return this.pieceColor.knightBonus(this.piecePosition);
    }

    @Override
    public Knight movePiece(final Move move) {
        return new Knight(move.getMovedPiece().getPieceColor(), move.getDestinationCoordinate());
    }
}
