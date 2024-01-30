package edu.greenriver.sdev.boardgames.tictactoe.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GameState {
    private Symbol[] board;
    private Symbol winner;
}
