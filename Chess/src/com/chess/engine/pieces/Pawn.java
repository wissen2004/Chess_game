package com.chess.engine.pieces;

import com.chess.engine.BoardUtils;
import com.chess.engine.PieceColor;
import com.chess.engine.board.Board;
import com.chess.engine.board.Move;
import com.chess.engine.board.Move.*;
import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public final class Pawn extends Piece {
    private final static int[] MOVE_OFFSETS = {7, 8, 9, 16};

    public Pawn(final PieceColor pieceColor, final int piecePosition) {
        super(PieceType.PAWN,piecePosition, pieceColor,true);
    }

    @Override
    public Collection<Move> calculateLegalMoves(final Board board) {
        final List<Move> legalMoves = new ArrayList<>();
        for (final int currentOffset : MOVE_OFFSETS) {
            int destinationPosition = this.piecePosition + (this.pieceColor.getDirection() * currentOffset);
            if(!BoardUtils.validDestinationPosition(destinationPosition)){
                continue;
            }
            if(currentOffset == 8 && !board.getTile(destinationPosition).isTileOccupied()) {
                if(this.pieceColor.isPawnPromotionSquare(destinationPosition)){
                    legalMoves.add(new PawnPromotion(new PawnMove(board, this, destinationPosition)));
                } else {
                    legalMoves.add(new PawnMove(board, this, destinationPosition));
                }
            }
            else if(currentOffset == 16 && isFirstMove &&
                    ((BoardUtils.SEVENTH_RANK[piecePosition] && this.getPieceColor().isBlack()) ||
                    (BoardUtils.SECOND_RANK[piecePosition] && this.getPieceColor().isWhite()))){
                final int behindDestinationPosition = destinationPosition - (this.pieceColor.getDirection() * 8);
                if(!board.getTile(destinationPosition).isTileOccupied() &&
                        !board.getTile(behindDestinationPosition).isTileOccupied())
                    legalMoves.add(new PawnJump(board, this, destinationPosition));
            }
            else if (currentOffset == 7 &&
                    (!(BoardUtils.FIRST_COLUMN[piecePosition] && this.pieceColor.isBlack())) &&
                    (!(BoardUtils.EIGHTH_COLUMN[piecePosition] && this.pieceColor.isWhite()))){
                if(board.getTile(destinationPosition).isTileOccupied()){
                    final Piece destinationPiece = board.getTile(destinationPosition).getPiece();
                    if(this.pieceColor != destinationPiece.getPieceColor()){
                        if(this.pieceColor.isPawnPromotionSquare(destinationPosition)){
                            legalMoves.add(new PawnPromotion(new PawnAttackMove(board, this, destinationPosition, destinationPiece)));
                        } else {
                            legalMoves.add(new PawnAttackMove(board, this, destinationPosition, destinationPiece));
                        }
                    }
                } else if (board.getEnPassantPawn() != null && board.getEnPassantPawn().getPiecePosition() ==
                        (this.piecePosition + (this.pieceColor.getOppositeDirection()))) {
                    final Piece pieceOnCandidate = board.getEnPassantPawn();
                    if (this.pieceColor != pieceOnCandidate.getPieceColor())
                        legalMoves.add(new PawnEnPassantAttackMove(board, this, destinationPosition, pieceOnCandidate));
                }
            }
            else if (currentOffset == 9 &&
                    (!(BoardUtils.FIRST_COLUMN[piecePosition] && this.pieceColor.isWhite())) &&
                    (!(BoardUtils.EIGHTH_COLUMN[piecePosition] && this.pieceColor.isBlack()))){
                if(board.getTile(destinationPosition).isTileOccupied()){
                    final Piece destinationPiece = board.getTile(destinationPosition).getPiece();
                    if(this.pieceColor != destinationPiece.getPieceColor()){
                        if(this.pieceColor.isPawnPromotionSquare(destinationPosition)){
                            legalMoves.add(new PawnPromotion(new PawnAttackMove(board, this, destinationPosition, destinationPiece)));
                        } else {
                            legalMoves.add(new PawnAttackMove(board, this, destinationPosition, destinationPiece));
                        }
                    }
                } else if (board.getEnPassantPawn() != null && board.getEnPassantPawn().getPiecePosition() ==
                            (this.piecePosition - (this.pieceColor.getOppositeDirection()))) {
                        final Piece pieceOnCandidate = board.getEnPassantPawn();
                        if (this.pieceColor != pieceOnCandidate.getPieceColor())
                            legalMoves.add(new PawnEnPassantAttackMove(board, this, destinationPosition, pieceOnCandidate));
                }
            }
        }
        return ImmutableList.copyOf(legalMoves);
    }

    @Override
    public String toString(){
        return this.pieceType.toString();
    }

    @Override
    public Pawn movePiece(final Move move) {
        return new Pawn(move.getMovedPiece().getPieceColor(), move.getDestinationCoordinate());
    }

    @Override
    public int locationBonus() {
        return this.pieceColor.pawnBonus(this.piecePosition);
    }

    public Piece getPromotionPiece() {
        return new Queen(this.pieceColor, this.piecePosition, false);
    }
}

