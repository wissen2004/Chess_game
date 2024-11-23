package com.chess.engine.pieces;

import com.chess.engine.PieceColor;
import com.chess.engine.board.Board;
import com.chess.engine.board.Move;

import java.util.*;

public abstract class Piece {

    final PieceType pieceType;
    final int piecePosition;
    final PieceColor pieceColor;
    final boolean isFirstMove;
    private final int cachedHashCode;

    Piece(final PieceType pieceType,
          final int piecePosition,
          final PieceColor pieceColor,
          final boolean isFirstMove){
        this.pieceType = pieceType;
        this.piecePosition = piecePosition;
        this.pieceColor = pieceColor;
        this.isFirstMove = isFirstMove;
        this.cachedHashCode = calculateHashCode();
    }

    public PieceType getPieceType()
    {
        return this.pieceType;
    }

    public int getPiecePosition()
    {
        return this.piecePosition;
    }

    public PieceColor getPieceColor(){
        return this.pieceColor;
    }

    public int getPieceValue() {
        return this.pieceType.getPieceValue();
    }

    public boolean isFirstMove() {
        return this.isFirstMove;
    }

    @Override
    public boolean equals(final Object other){
        if(this == other)
            return true;
        if(! (other instanceof Piece))
            return false;
        final Piece otherPiece = (Piece) other;
        return piecePosition == otherPiece.getPiecePosition() &&
                pieceColor == otherPiece.getPieceColor() &&
                pieceType == otherPiece.getPieceType() &&
                isFirstMove == otherPiece.isFirstMove();
    }

    @Override
    public int hashCode(){
        return this.cachedHashCode;
    }

    private int calculateHashCode() {
        int result = pieceType.hashCode();
        result = 31 * result + pieceColor.hashCode();
        result = 31 * result + piecePosition;
        result = 31 * result + (isFirstMove? 1 : 0);
        return result;
    }

    public abstract Collection<Move> calculateLegalMoves(final Board board);
    public abstract Piece movePiece(Move move);
    public abstract int locationBonus();

    public enum PieceType {
        
        PAWN(100, "P") {
            @Override
            public boolean isPawn() {
                return true;
            }

            @Override
            public boolean isRook() {
                return false;
            }

            @Override
            public boolean isKing() {
                return false;
            }
        },
        KNIGHT(300, "N") {
            @Override
            public boolean isPawn() {
                return false;
            }

            @Override
            public boolean isRook() {
                return false;
            }

            @Override
            public boolean isKing() {
                return false;
            }
        },
        BISHOP(300, "B") {
            @Override
            public boolean isPawn() {
                return false;
            }

            @Override
            public boolean isRook() {
                return false;
            }

            @Override
            public boolean isKing() {
                return false;
            }
        },
        ROOK(500, "R") {
            @Override
            public boolean isPawn() {
                return false;
            }

            @Override
            public boolean isRook() {
                return true;
            }

            @Override
            public boolean isKing() {
                return false;
            }
        },
        QUEEN(900, "Q") {
            @Override
            public boolean isPawn() {
                return false;
            }

            @Override
            public boolean isRook() {
                return false;
            }

            @Override
            public boolean isKing() {
                return false;
            }
        },
        KING(10000, "K") {
            @Override
            public boolean isPawn() {
                return false;
            }

            @Override
            public boolean isRook() {
                return false;
            }

            @Override
            public boolean isKing() {
                return true;
            }
        };

        @Override
        public String toString() {
            return this.pieceName;
        }

        private final int pieceValue;
        private final String pieceName;

        PieceType(final int pieceValue, final String pieceName) {
            this.pieceName = pieceName;
            this.pieceValue = pieceValue;
        }

        public int getPieceValue(){
            return this.pieceValue;
        }

        public abstract boolean isPawn();
        public abstract boolean isRook();
        public abstract boolean isKing();

    }
}
