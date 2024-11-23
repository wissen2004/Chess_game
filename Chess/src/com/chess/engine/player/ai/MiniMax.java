package com.chess.engine.player.ai;

import com.chess.engine.board.Board;
import com.chess.engine.board.Move;
import com.chess.engine.player.MoveTransition;

public final class MiniMax implements MoveStrategy {

    private final BoardEvaluator evaluator;
    private final int depth;
    private long executionTime;

    public MiniMax(final int searchDepth) {
        this.evaluator = new StandardBoardEvaluator();
        this.depth = searchDepth;
    }

    @Override
    public long getExecutionTime(){
        return this.executionTime;
    }

    @Override
    public String toString() {
        return "MiniMax";
    }

    @Override
    public Move execute(final Board board) {
        final long startTime = System.currentTimeMillis();
        Move bestMove = null;
        int highestSeenValue = Integer.MIN_VALUE;
        int lowestSeenValue = Integer.MAX_VALUE;
        int currentValue;
        System.out.println(board.currentPlayer() + " THINKING with depth = " +depth);

        for (final Move move : board.currentPlayer().getLegalMoves()) {
            final MoveTransition moveTransition = board.currentPlayer().makeMove(move);
            if (moveTransition.getMoveStatus().isDone()) {

                currentValue = board.currentPlayer().getPieceColor().isWhite() ?
                        min(moveTransition.getToBoard(), depth - 1) :
                        max(moveTransition.getToBoard(), depth - 1);

                if (board.currentPlayer().getPieceColor().isWhite() &&
                        currentValue > highestSeenValue) {
                    highestSeenValue = currentValue;
                    bestMove = move;
                } else if (board.currentPlayer().getPieceColor().isBlack() &&
                        currentValue < lowestSeenValue) {
                    lowestSeenValue = currentValue;
                    bestMove = move;
                }
            }
            executionTime = System.currentTimeMillis() - startTime;
        }
        return bestMove;
    }

    private int min(final Board board,
                    final int depth) {
        if(depth == 0 || isEndGameScenario(board)) {
            return this.evaluator.evaluate(board, depth);
        }

        int lowestSeenValue = Integer.MAX_VALUE;
        for (final Move move : board.currentPlayer().getLegalMoves()) {
            final MoveTransition moveTransition = board.currentPlayer().makeMove(move);
            if (moveTransition.getMoveStatus().isDone()) {
                final int currentValue = max(moveTransition.getToBoard(), depth - 1);
                if (currentValue <= lowestSeenValue) {
                    lowestSeenValue = currentValue;
                }
            }
        }
        return lowestSeenValue;
    }

    private int max(final Board board,
                    final int depth) {
        if(depth == 0 || isEndGameScenario(board)) {
            return this.evaluator.evaluate(board, depth);
        }

        int highestSeenValue = Integer.MIN_VALUE;
        for (final Move move : board.currentPlayer().getLegalMoves()) {
            final MoveTransition moveTransition = board.currentPlayer().makeMove(move);
            if (moveTransition.getMoveStatus().isDone()) {
                final int currentValue = min(moveTransition.getToBoard(), depth - 1);
                if (currentValue >= highestSeenValue) {
                    highestSeenValue = currentValue;
                }
            }
        }
        return highestSeenValue;
    }

    private static boolean isEndGameScenario(final Board board) {
        return  board.currentPlayer().isInCheckMate() ||
                board.currentPlayer().isInStaleMate();
    }
}