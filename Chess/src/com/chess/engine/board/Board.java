package com.chess.engine.board;

import com.chess.engine.BoardUtils;
import com.chess.engine.PieceColor;
import com.chess.engine.pieces.*;
import com.chess.engine.player.BlackPlayer;
import com.chess.engine.player.Player;
import com.chess.engine.player.WhitePlayer;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;

import java.util.*;

public final class Board {

    private final List<Tile> gameBoard;
    private final Collection<Piece> whitePieces;
    private final Collection<Piece> blackPieces;
    private final WhitePlayer whitePlayer;
    private final BlackPlayer blackPlayer;
    private final Player currentPlayer;
    private final Pawn enPassantPawn;

    private Board(Builder builder){
        this.gameBoard = createGameBoard(builder);
        this.whitePieces = calculateActivePieces(builder, PieceColor.WHITE);
        this.blackPieces = calculateActivePieces(builder, PieceColor.BLACK);
        this.enPassantPawn = builder.enPassantPawn;
        final Collection<Move> whiteLegalMoves = calculateLegalMoves(this.whitePieces);
        final Collection<Move> blackLegalMoves = calculateLegalMoves(this.blackPieces);
        this.whitePlayer = new WhitePlayer(this, whiteLegalMoves, blackLegalMoves);
        this.blackPlayer = new BlackPlayer(this, whiteLegalMoves, blackLegalMoves);
        this.currentPlayer = builder.nextMoveMaker.choosePlayer(this.whitePlayer, this.blackPlayer);
    }

    public Collection<Piece> getWhitePieces(){
        return this.whitePieces;
    }

    public Collection<Piece> getBlackPieces(){
        return this.blackPieces;
    }

    public Iterable<Piece> getAllPieces() {
        return Iterables.unmodifiableIterable(Iterables.concat(this.whitePieces, this.blackPieces));
    }

    public Player whitePlayer(){
        return this.whitePlayer;
    }

    public Player blackPlayer(){
        return this.blackPlayer;
    }

    public Player currentPlayer(){
        return this.currentPlayer;
    }

    public Pawn getEnPassantPawn(){
        return this.enPassantPawn;
    }

    public Tile getTile(final int tilePosition)
    {
        return gameBoard.get(tilePosition);
    }

    private static List<Tile> createGameBoard(final Builder builder) {
        final Tile[] tiles = new Tile[BoardUtils.NUM_TILES];

        for(int i=0;i<BoardUtils.NUM_TILES;i++)
        {
            tiles[i] = Tile.createTile(i, builder.boardConfig.get(i));
        }
        return ImmutableList.copyOf(tiles);
    }

    private static Collection<Piece> calculateActivePieces(final Builder builder,
                                                           final PieceColor pieceColor) {
        final List<Piece> activePieces = new ArrayList<>();
        for(final Piece piece: builder.boardConfig.values()){
                if(piece.getPieceColor() == pieceColor)
                    activePieces.add(piece);
            }
        return ImmutableList.copyOf(activePieces);
    }

    private Collection<Move> calculateLegalMoves(final Collection<Piece> pieces) {
        final List<Move> legalMoves = new ArrayList<>();
        for(final Piece piece : pieces){
            legalMoves.addAll(piece.calculateLegalMoves(this));
        }
        return ImmutableList.copyOf(legalMoves);
    }

    Iterable<Move> getAllLegalMoves() {
        return Iterables.unmodifiableIterable(Iterables.concat(this.whitePlayer.getLegalMoves(),
                this.blackPlayer.getLegalMoves()));
    }

    public static Board createStandardBoard() {
        final Builder builder = new Builder();
        // Black Layout
        builder.setPiece(new Rook(PieceColor.BLACK, 0));
        builder.setPiece(new Knight(PieceColor.BLACK, 1));
        builder.setPiece(new Bishop(PieceColor.BLACK, 2));
        builder.setPiece(new Queen(PieceColor.BLACK, 3));
        builder.setPiece(new King(PieceColor.BLACK, 4, true, true));
        builder.setPiece(new Bishop(PieceColor.BLACK, 5));
        builder.setPiece(new Knight(PieceColor.BLACK, 6));
        builder.setPiece(new Rook(PieceColor.BLACK, 7));
        builder.setPiece(new Pawn(PieceColor.BLACK, 8));
        builder.setPiece(new Pawn(PieceColor.BLACK, 9));
        builder.setPiece(new Pawn(PieceColor.BLACK, 10));
        builder.setPiece(new Pawn(PieceColor.BLACK, 11));
        builder.setPiece(new Pawn(PieceColor.BLACK, 12));
        builder.setPiece(new Pawn(PieceColor.BLACK, 13));
        builder.setPiece(new Pawn(PieceColor.BLACK, 14));
        builder.setPiece(new Pawn(PieceColor.BLACK, 15));
        // White Layout
        builder.setPiece(new Pawn(PieceColor.WHITE, 48));
        builder.setPiece(new Pawn(PieceColor.WHITE, 49));
        builder.setPiece(new Pawn(PieceColor.WHITE, 50));
        builder.setPiece(new Pawn(PieceColor.WHITE, 51));
        builder.setPiece(new Pawn(PieceColor.WHITE, 52));
        builder.setPiece(new Pawn(PieceColor.WHITE, 53));
        builder.setPiece(new Pawn(PieceColor.WHITE, 54));
        builder.setPiece(new Pawn(PieceColor.WHITE, 55));
        builder.setPiece(new Rook(PieceColor.WHITE, 56));
        builder.setPiece(new Knight(PieceColor.WHITE, 57));
        builder.setPiece(new Bishop(PieceColor.WHITE, 58));
        builder.setPiece(new Queen(PieceColor.WHITE, 59));
        builder.setPiece(new King(PieceColor.WHITE, 60, true, true));
        builder.setPiece(new Bishop(PieceColor.WHITE, 61));
        builder.setPiece(new Knight(PieceColor.WHITE, 62));
        builder.setPiece(new Rook(PieceColor.WHITE, 63));
        //white to move first
        builder.setMoveMaker(PieceColor.WHITE);
        //build the board
        return builder.build();
    }

    @Override
    public String toString()
    {
        final StringBuilder stringBuilder= new StringBuilder();
        for(int i=0; i<BoardUtils.NUM_TILES; i++){
            stringBuilder.append(String.format("%3s",gameBoard.get(i).toString()));
            if((i+1) % BoardUtils.NUM_TILES_PER_ROW == 0)
                stringBuilder.append("\n");
        }
        return stringBuilder.toString();
    }

    public static class Builder{
        Map<Integer, Piece> boardConfig;
        PieceColor nextMoveMaker;
        Pawn enPassantPawn;

        Builder(){
            this.boardConfig = new HashMap<>();
        }

        public Builder setPiece(final Piece piece) {
            this.boardConfig.put(piece.getPiecePosition(), piece);
            return this;
        }

        Builder setMoveMaker(final PieceColor nextMoveMaker) {
            this.nextMoveMaker = nextMoveMaker;
            return this;
        }

        void setEnPassantPawn(Pawn enPassantPawn) {
            this.enPassantPawn = enPassantPawn;
        }

        Board build() {
            return new Board(this);
        }
    }
}
