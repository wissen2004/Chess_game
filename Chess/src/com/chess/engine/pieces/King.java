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

public class King extends Piece {
    private final static int[] MOVE_OFFSETS= { -1, -7, -8, -9, 9, 8, 7, 1};
    private final boolean isCastled;
    private final boolean kingSideCastleCapable;
    private final boolean queenSideCastleCapable;


    public King(final PieceColor pieceColor,
                final int piecePosition,
                final boolean kingSideCastleCapable,
                final boolean queenSideCastleCapable) {
        super(PieceType.KING,piecePosition, pieceColor,true);
        this.isCastled = false;
        this.kingSideCastleCapable = kingSideCastleCapable;
        this.queenSideCastleCapable = queenSideCastleCapable;
    }

    private King(final PieceColor pieceColor,
                 final int piecePosition,
                 final boolean isFirstMove,
                 final boolean isCastled,
                 final boolean kingSideCastleCapable,
                 final boolean queenSideCastleCapable) {
        super(PieceType.KING,piecePosition, pieceColor,isFirstMove);
        this.isCastled = isCastled;
        this.kingSideCastleCapable = kingSideCastleCapable;
        this.queenSideCastleCapable = queenSideCastleCapable;
    }

    public boolean isCastled() {
        return isCastled;
    }

    public boolean isKingSideCastleCapable() {
        return kingSideCastleCapable;
    }

    public boolean isQueenSideCastleCapable() {
        return queenSideCastleCapable;
    }

    @Override
    public Collection<Move> calculateLegalMoves(Board board) {
        final List<Move> legalMoves = new ArrayList<>();
        for (final int currentOffset : MOVE_OFFSETS) {
            if (isFirstColumnExclusion(this.piecePosition, currentOffset) ||
                    isEighthColumnExclusion(this.piecePosition, currentOffset)) {
                continue;
            }
            int destinationPosition = piecePosition + currentOffset;
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
                }
            }
        }
        return ImmutableList.copyOf(legalMoves);
    }

    private static boolean isFirstColumnExclusion(final int currentPosition, final int currentOffset){
        return BoardUtils.FIRST_COLUMN[currentPosition] && (currentOffset == -1 || currentOffset == -9 || currentOffset == 7);
    }

    private static boolean isEighthColumnExclusion(final int currentPosition, final int currentOffset){
        return BoardUtils.EIGHTH_COLUMN[currentPosition] && (currentOffset == 1 || currentOffset == 9 || currentOffset == -7);
    }
    @Override
    public String toString(){
        return this.pieceType.toString();
    }

    @Override
    public int locationBonus() {
        return this.pieceColor.kingBonus(this.piecePosition);
    }

    @Override
    public King movePiece(final Move move) {
        return new King(move.getMovedPiece().getPieceColor(),
                move.getDestinationCoordinate(),
                false,
                move.isCastlingMove(),
                false,
                false);
    }
}
