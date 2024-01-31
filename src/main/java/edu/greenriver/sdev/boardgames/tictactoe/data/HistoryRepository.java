package edu.greenriver.sdev.boardgames.tictactoe.data;

import edu.greenriver.sdev.boardgames.tictactoe.domain.GameHistory;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Implements CRUD operations for game history resources
 * @author James Motherwell
 * @version 1.0
 */
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

    /**
     * @return A list containing all game history resources
     */
    public List<GameHistory> findAll() {
        return Collections.unmodifiableList(games);
    }

    /**
     * @param gameId ID of a game history resource
     * @return The game history resource corresponding to the given ID, if one exists
     */
    public Optional<GameHistory> findById(int gameId) {
        int index = indexOfId(gameId);

        if (index < 0) {
            return Optional.empty();
        }

        return Optional.of(games.get(index));
    }

    /**
     * Updates an existing game history resource if a resource with the given ID exists,
     * otherwise assigns the given game history object a unique ID and adds it to the resource list.
     * @param game A game history object
     * @return The game history object with its unique ID
     */
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

    /**
     * Removes a game history resource
     * @param gameId ID of resource to be removed
     * @return The resource that was removed, or null if a resource with the given ID is not present
     */
    public Optional<GameHistory> delete(int gameId) {
        int index = indexOfId(gameId);

        if (index < 0) {
            return Optional.empty();
        } else {
            var game = games.get(index);
            games.remove(index);
            return Optional.of(game);
        }
    }

    @Override
    public String toString() {
        return "HistoryRepository{" +
                "games=" + games +
                '}';
    }
}
