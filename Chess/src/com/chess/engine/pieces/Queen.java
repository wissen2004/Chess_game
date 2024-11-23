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

public class Queen extends Piece{
    private final static int[] MOVE_OFFSETS= {-7, -8, -9, -1, 1, 7, 8, 9};

    public Queen(final PieceColor pieceColor, final int piecePosition) {
        super(PieceType.QUEEN,piecePosition, pieceColor,true);
    }

    Queen(final PieceColor pieceColor, final int piecePosition, final boolean isFirstMove) {
        super(PieceType.QUEEN,piecePosition, pieceColor,isFirstMove);
    }

    @Override
    public Collection<Move> calculateLegalMoves(Board board) {
        final List<Move> legalMoves = new ArrayList<>();
        for(final int currentOffset : MOVE_OFFSETS) {
            int destinationPosition = this.piecePosition;
            while (BoardUtils.validDestinationPosition(destinationPosition)) {
                if(isFirstColumnExclusion(destinationPosition,currentOffset)||
                        isEighthColumnExclusion(destinationPosition,currentOffset))
                    break;
                destinationPosition += currentOffset;
                if (BoardUtils.validDestinationPosition(destinationPosition)) {
                    final Tile destinationTile = board.getTile(destinationPosition);
                    if (!destinationTile.isTileOccupied()) {
                        legalMoves.add(new MajorMove(board, this, destinationPosition));
                    } else {
                        final Piece destinationPiece = destinationTile.getPiece();
                        final PieceColor destinationPieceColor = destinationPiece.getPieceColor();

                        if (destinationPieceColor != this.pieceColor) {
                            legalMoves.add(new MajorAttackMove(board, this, destinationPosition, destinationPiece));
                        }
                        break;
                    }
                }
            }
        }

        return ImmutableList.copyOf(legalMoves);
    }
    private static boolean isFirstColumnExclusion(final int currentPosition, final int currentOffset){
        return BoardUtils.FIRST_COLUMN[currentPosition] && (currentOffset == -9 || currentOffset == -1 ||currentOffset == 7);
    }
    private static boolean isEighthColumnExclusion(final int currentPosition, final int currentOffset){
        return BoardUtils.EIGHTH_COLUMN[currentPosition] && (currentOffset == 9 || currentOffset == 1 ||currentOffset == -7);
    }
    @Override
    public String toString(){
        return this.pieceType.toString();
    }

    @Override
    public int locationBonus() {
        return this.pieceColor.queenBonus(this.piecePosition);
    }

    @Override
    public Queen movePiece(final Move move) {
        return new Queen(move.getMovedPiece().getPieceColor(), move.getDestinationCoordinate());
    }

}
