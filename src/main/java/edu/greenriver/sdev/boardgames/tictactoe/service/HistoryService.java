package edu.greenriver.sdev.boardgames.tictactoe.service;

import edu.greenriver.sdev.boardgames.tictactoe.data.HistoryRepository;
import edu.greenriver.sdev.boardgames.tictactoe.domain.GameHistory;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;

@Service
public class HistoryService {
    private HistoryRepository repo;

    public HistoryService(HistoryRepository repo) {
        this.repo = repo;
    }

    public GameHistory byId(int gameId) {
        return repo.findById(gameId).orElse(null);
    }

    public List<GameHistory> all() {
        return repo.findAll();
    }

    public GameHistory addGame(GameHistory game) {
        return repo.save(game);
    }

    public GameHistory updateGame(GameHistory game) {
        return repo.save(game);
    }

    public GameHistory deleteGame(int gameId) {
        return repo.delete(gameId).orElse(null);
    }

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
}
