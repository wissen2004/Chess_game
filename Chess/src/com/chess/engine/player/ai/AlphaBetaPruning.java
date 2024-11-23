package com.chess.engine.player.ai;

import com.chess.engine.board.Board;
import com.chess.engine.board.Move;
import com.chess.engine.player.MoveTransition;

public final class AlphaBetaPruning implements MoveStrategy {

    private final BoardEvaluator evaluator;
    private final int depth;
    private long executionTime;

    public AlphaBetaPruning(final int searchDepth) {
        this.evaluator = new StandardBoardEvaluator();
        this.depth = searchDepth;
    }

    @Override
    public long getExecutionTime() {
        return executionTime;
    }

    @Override
    public String toString() {
        return "AlphaBetaPruning";
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
                        min(moveTransition.getToBoard(), highestSeenValue, lowestSeenValue, depth - 1) :
                        max(moveTransition.getToBoard(), highestSeenValue, lowestSeenValue, depth - 1);
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
                    final int alpha,
                    final int beta,
                    final int depth) {
        if (depth == 0 || isEndGameScenario(board)) {
            return this.evaluator.evaluate(board, depth);
        }

        int currentLowest = beta;
        for (final Move move : board.currentPlayer().getLegalMoves()) {
            final MoveTransition moveTransition = board.currentPlayer().makeMove(move);
            if (moveTransition.getMoveStatus().isDone()) {
                currentLowest = Math.min(currentLowest, max(moveTransition.getToBoard(), alpha, currentLowest, depth-1));
                if(currentLowest <= alpha)
                    break;
            }
        }
        return currentLowest;
    }

    private int max(final Board board,
                    final int alpha,
                    final int beta,
                    final int depth) {
        if (depth == 0 || isEndGameScenario(board)) {
            return this.evaluator.evaluate(board, depth);
        }

        int currentHighest = alpha;
        for (final Move move : board.currentPlayer().getLegalMoves()) {
            final MoveTransition moveTransition = board.currentPlayer().makeMove(move);
            if (moveTransition.getMoveStatus().isDone()) {
                currentHighest = Math.max(currentHighest, min(moveTransition.getToBoard(), currentHighest, beta, depth-1));
                if(currentHighest >= beta)
                    break;
            }
        }
        return currentHighest;
    }

    private static boolean isEndGameScenario(final Board board) {
        return  board.currentPlayer().isInCheckMate() ||
                board.currentPlayer().isInStaleMate();
    }
}