package edu.greenriver.sdev.boardgames.tictactoe.service;

import edu.greenriver.sdev.boardgames.tictactoe.data.HistoryRepository;
import edu.greenriver.sdev.boardgames.tictactoe.domain.GameHistory;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;

/**
 * Implements methods to create, read, update and delete game history records
 * @author James Motherwell
 * @version 1.0
 */
@Service
public class HistoryService {
    private HistoryRepository repo;

    /**
     * Constructs a new HistoryService object
     * @param repo A HistoryRepository object (provided automatically by Spring)
     */
    public HistoryService(HistoryRepository repo) {
        this.repo = repo;
    }

    /**
     * @param gameId A game ID
     * @return Game history corresponding to the given ID, or null if the ID is not present in the list
     */
    public GameHistory byId(int gameId) {
        return repo.findById(gameId).orElse(null);
    }

    /**
     * @return A list containing all game history resources
     */
    public List<GameHistory> all() {
        return repo.findAll();
    }

    /**
     * @param game A game to be added to the list
     * @return The game that was added, with its newly assigned ID
     */
    public GameHistory addGame(GameHistory game) {
        return repo.save(game);
    }

    /**
     * @param game A game to be updated
     * @return The game history object that was updated
     */
    public GameHistory updateGame(GameHistory game) {
        return repo.save(game);
    }

    /**
     * @param gameId ID of a game history resource
     * @return The game history object that was removed, or null if the ID was not present in the list
     */
    public GameHistory deleteGame(int gameId) {
        return repo.delete(gameId).orElse(null);
    }

    /**
     * Checks whether the given game history object is valid
     * @param game A game history object
     * @return True if the game history object is valid, otherwise false
     */
    public boolean isHistoryValid(GameHistory game) {
        var moves = game.getMoves();
        if (moves == null || moves.size() > 9) {
            return false;
        }

        var indices = new HashSet<Integer>();
        for (int index : moves) {
            if (index < 0 || index >= 9) {
                return false;
            }

            if (!indices.add(index)) {
                return false;
            }
        }

        return true;
    }

    @Override
    public String toString() {
        return "HistoryService{" +
                "repo=" + repo +
                '}';
    }
}
