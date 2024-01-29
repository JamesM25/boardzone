package edu.greenriver.sdev.boardgames.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TicTacToeGame {
    private TicTacToeSymbol[] board;
    private TicTacToeSymbol winner;
}
