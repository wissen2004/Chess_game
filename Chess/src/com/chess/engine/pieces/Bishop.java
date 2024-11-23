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

public class Bishop extends Piece{
    private final static int[] MOVE_OFFSETS= {-7, -9, 7, 9};

    public Bishop(final PieceColor pieceColor, final int piecePosition) {
        super(PieceType.BISHOP,piecePosition, pieceColor,true);
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
                        //Any obstacle=>bishop has to stop
                        break;
                    }
                }
            }
        }

        return ImmutableList.copyOf(legalMoves);
    }

    private static boolean isFirstColumnExclusion(final int currentPosition, final int currentOffset){
        return BoardUtils.FIRST_COLUMN[currentPosition] && (currentOffset == -9 || currentOffset == 7);
    }
    private static boolean isEighthColumnExclusion(final int currentPosition, final int currentOffset){
        return BoardUtils.EIGHTH_COLUMN[currentPosition] && (currentOffset == 9 || currentOffset == -7);
    }
    @Override
    public String toString(){
        return this.pieceType.toString();
    }

    @Override
    public int locationBonus() {
        return this.pieceColor.bishopBonus(this.piecePosition);
    }

    @Override
    public Bishop movePiece(final Move move) {
        return new Bishop(move.getMovedPiece().getPieceColor(), move.getDestinationCoordinate());
    }
}
