package com.chess.engine.player.ai;

import com.chess.engine.board.Board;
import com.chess.engine.board.Move;

public interface MoveStrategy {
    long getExecutionTime();

    Move execute(Board board);
}
