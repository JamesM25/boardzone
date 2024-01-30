package edu.greenriver.sdev.boardgames.tictactoe.data;

import edu.greenriver.sdev.boardgames.tictactoe.domain.GameHistory;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Repository
public class HistoryRepository {
    private static int idCounter = 1;
    private List<GameHistory> games = new ArrayList<>();

    private int indexOfId(int gameId) {
        for (int i = 0; i < games.size(); i++) {
            if (games.get(i).getId() == gameId) {
                return i;
            }
        }

        return -1;
    }

    public GameHistory createHistory() {
        var newGame = new GameHistory(idCounter, new ArrayList<>());
        idCounter++;

        games.add(newGame);

        return newGame;
    }

    public List<GameHistory> findAll() {
        return Collections.unmodifiableList(games);
    }

    public Optional<GameHistory> findById(int gameId) {
        int index = indexOfId(gameId);

        if (index < 0) {
            return Optional.empty();
        }

        return Optional.of(games.get(index));
    }

    public GameHistory save(GameHistory game) {
        int index = indexOfId(game.getId());

        if (index < 0) {
            game.setId(idCounter);
            idCounter++;
            games.add(game);
        } else {
            games.set(index, game);
        }

        return game;
    }
}
