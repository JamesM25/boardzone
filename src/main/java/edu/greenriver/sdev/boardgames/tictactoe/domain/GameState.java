package edu.greenriver.sdev.boardgames.tictactoe.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Stores the state of a tic-tac-toe game, including the 3x3 grid and whether a player has won the game.
 * @author James Motherwell
 * @version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GameState {
    private Symbol[] board;
    private Symbol winner;
    private GameDifficulty difficulty;
}
