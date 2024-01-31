package edu.greenriver.sdev.boardgames.tictactoe.controller;

import edu.greenriver.sdev.boardgames.tictactoe.domain.GameHistory;
import edu.greenriver.sdev.boardgames.tictactoe.domain.GameState;
import edu.greenriver.sdev.boardgames.tictactoe.service.GameService;
import edu.greenriver.sdev.boardgames.tictactoe.service.HistoryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("api/tictactoe")
public class API {

    private GameService gameService;
    private HistoryService historyService;

    public API(GameService service, HistoryService historyService) {
        this.gameService = service;
        this.historyService = historyService;
    }

    @PostMapping("game")
    private ResponseEntity<GameState> move(@RequestBody GameState game) {
        if (!gameService.isTicTacToeGameValid(game)) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        var newState = gameService.getOpponentMove(game);

        if (newState == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(newState, HttpStatus.OK);
    }

    @GetMapping("history")
    private ResponseEntity<List<GameHistory>> getAllHistories() {
        return new ResponseEntity<>(historyService.all(), HttpStatus.OK);
    }

    @GetMapping("history/{gameId}")
    private ResponseEntity<GameHistory> getHistory(@PathVariable int gameId) {
        var game = historyService.byId(gameId);
        if (game == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(game, HttpStatus.OK);
    }

    @PostMapping("history")
    private ResponseEntity<GameHistory> createHistory(@RequestBody GameHistory game) {
        if (!historyService.isHistoryValid(game)) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(historyService.addGame(game), HttpStatus.CREATED);
    }

    @PutMapping("history")
    private ResponseEntity<GameHistory> updateHistory(@RequestBody GameHistory game) {
        if (!historyService.isHistoryValid(game)) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        boolean exists = historyService.byId(game.getId()) != null;
        return new ResponseEntity<>(historyService.updateGame(game), exists ? HttpStatus.OK : HttpStatus.CREATED);
    }

    @DeleteMapping("history/{gameId}")
    private ResponseEntity<GameHistory> deleteHistory(@PathVariable int gameId) {
        var game = historyService.deleteGame(gameId);
        if (game == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(game, HttpStatus.OK);
    }

    @Override
    public String toString() {
        return "API{" +
                "gameService=" + gameService +
                ", historyService=" + historyService +
                '}';
    }
}
