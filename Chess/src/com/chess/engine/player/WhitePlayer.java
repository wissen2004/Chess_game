package com.chess.engine.player;

import com.chess.engine.PieceColor;
import com.chess.engine.board.Board;
import com.chess.engine.board.Move;
import com.chess.engine.board.Move.QueenSideCastleMove;
import com.chess.engine.board.Move.KingSideCastleMove;
import com.chess.engine.board.Tile;
import com.chess.engine.pieces.Piece;
import com.chess.engine.pieces.Rook;
import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class WhitePlayer extends Player{
    public WhitePlayer(Board board, Collection<Move> whitesLegals, Collection<Move> blacksLegals) {
        super(board, whitesLegals, blacksLegals);
    }

    @Override
    public PieceColor getPieceColor() {
        return PieceColor.WHITE;
    }

    @Override
    public Player getOpponent() {
        return this.board.blackPlayer();
    }

    @Override
    public Collection<Piece> getActivePieces() {
        return this.board.getWhitePieces();
    }

    @Override
    public Collection<Move> calculateKingCastles(Collection<Move> opponentLegals) {
        final List<Move> kingCastles = new ArrayList<>();
        if(this.playerKing.isFirstMove() && !this.isInCheck()){

            if(!this.board.getTile(61).isTileOccupied() && !this.board.getTile(62).isTileOccupied()){
                final Tile rookTile = this.board.getTile(63);
                if( rookTile.isTileOccupied() &&
                    rookTile.getPiece().getPieceType().isRook() &&
                    rookTile.getPiece().isFirstMove() &&
                    Player.calculateAttacksOnTile(61, opponentLegals).isEmpty() &&
                    Player.calculateAttacksOnTile(62, opponentLegals).isEmpty()){
                    kingCastles.add(new KingSideCastleMove(this.board,
                                       this.playerKing,
                                       62,
                                       (Rook)rookTile.getPiece(),
                                       rookTile.getTileCoordinate(),
                                       61));
                    }
                }
            }
            if(!this.board.getTile(59).isTileOccupied() &&
                    !this.board.getTile(58).isTileOccupied() &&
                    !this.board.getTile(57).isTileOccupied()){
                final Tile rookTile = this.board.getTile(56);
                if( rookTile.isTileOccupied() && rookTile.getPiece().isFirstMove() &&
                    Player.calculateAttacksOnTile(58, opponentLegals).isEmpty() &&
                    Player.calculateAttacksOnTile(59, opponentLegals).isEmpty() &&
                    rookTile.getPiece().getPieceType().isRook()){
                    kingCastles.add(new QueenSideCastleMove(this.board,
                                        this.playerKing,
                                        58,
                                        (Rook)rookTile.getPiece(),
                                        rookTile.getTileCoordinate(),
                                        59));
                    }
                }
        return ImmutableList.copyOf(kingCastles);
    }

    @Override
    public String toString(){
        return "White Player";
    }
}
