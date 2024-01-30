package edu.greenriver.sdev.boardgames.tictactoe.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * Stores all moves that took place during a game of tic-tac-toe.
 * @author James Motherwell
 * @version 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GameHistory {
    private int id;
    private List<Integer> moves = new ArrayList<>();
}
