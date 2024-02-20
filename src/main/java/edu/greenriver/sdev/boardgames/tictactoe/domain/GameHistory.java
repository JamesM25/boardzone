package edu.greenriver.sdev.boardgames.tictactoe.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
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
@Entity
public class GameHistory {
    @Id
    private int id;
    private List<Integer> moves = new ArrayList<>();
    private GameDifficulty difficulty;
    private LocalDateTime date = LocalDateTime.now();
}
