package com.chess.engine.player.ai;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.chess.engine.BoardUtils;
import com.chess.engine.pieces.Piece;
import com.chess.engine.player.Player;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableList.Builder;
import com.google.common.collect.ListMultimap;

public final class PawnStructureAnalyzer {

    private static final PawnStructureAnalyzer INSTANCE = new PawnStructureAnalyzer();
    private static final ImmutableList<boolean[]> BOARD_COLUMNS = initColumns();

    private static final int ISOLATED_PAWN_PENALTY = -10;

    private PawnStructureAnalyzer() {
    }

    public static PawnStructureAnalyzer get() {
        return INSTANCE;
    }

    private static ImmutableList<boolean[]> initColumns() {
        final List<boolean[]> columns = new ArrayList<>();
        columns.add(BoardUtils.FIRST_COLUMN);
        columns.add(BoardUtils.SECOND_COLUMN);
        columns.add(BoardUtils.THIRD_COLUMN);
        columns.add(BoardUtils.FOURTH_COLUMN);
        columns.add(BoardUtils.FIFTH_COLUMN);
        columns.add(BoardUtils.SIXTH_COLUMN);
        columns.add(BoardUtils.SEVENTH_COLUMN);
        columns.add(BoardUtils.EIGHTH_COLUMN);
        return ImmutableList.copyOf(columns);
    }

    int pawnStructureScore(final Player player) {
        final Collection<Piece> playerPawns = calculatePlayerPawns(player);
        final ListMultimap<Integer, Piece> pawnsOnColumnTable = createPawnsOnColumnTable(playerPawns);
        return calculateIsolatedPawnPenalty(pawnsOnColumnTable);
    }

    private static Collection<Piece> calculatePlayerPawns(final Player player) {
        final Builder<Piece> playerPawnLocations = new Builder<>();
        for (final Piece piece : player.getActivePieces()) {
            if (piece.getPieceType().isPawn()) {
                playerPawnLocations.add(piece);
            }
        }
        return playerPawnLocations.build();
    }

    private static int calculateIsolatedPawnPenalty(final ListMultimap<Integer, Piece> pawnsOnColumnTable) {
        int numIsolatedPawns = 0;
        if (!pawnsOnColumnTable.get(0).isEmpty() &&
                pawnsOnColumnTable.get(1).isEmpty()) {
            numIsolatedPawns += pawnsOnColumnTable.get(0).size();
        }
        for (int i = 1; i < BOARD_COLUMNS.size() - 1; i++) {
            if (!pawnsOnColumnTable.get(i).isEmpty() &&
                    (pawnsOnColumnTable.get(i - 1).isEmpty() && pawnsOnColumnTable.get(i + 1).isEmpty())) {
                numIsolatedPawns += pawnsOnColumnTable.get(i).size();
            }
        }
        if (!pawnsOnColumnTable.get(BOARD_COLUMNS.size() - 1).isEmpty() &&
                pawnsOnColumnTable.get(BOARD_COLUMNS.size() - 2).isEmpty()) {
            numIsolatedPawns += pawnsOnColumnTable.get(BOARD_COLUMNS.size() - 1).size();
        }
        return numIsolatedPawns * ISOLATED_PAWN_PENALTY;
    }

    private static ListMultimap<Integer, Piece> createPawnsOnColumnTable(final Collection<Piece> playerPawns) {
        final ListMultimap<Integer, Piece> table = ArrayListMultimap.create(8, 6);
        for (int i = 0; i < BOARD_COLUMNS.size(); i++) {
            for (final Piece playerPawn : playerPawns) {
                if (BOARD_COLUMNS.get(i)[playerPawn.getPiecePosition()]) {
                    table.put(i, playerPawn);
                }
            }
        }
        return table;
    }
}
