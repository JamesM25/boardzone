package edu.greenriver.sdev.boardgames.controller;

import edu.greenriver.sdev.boardgames.domain.TicTacToeGame;
import edu.greenriver.sdev.boardgames.domain.TicTacToeSymbol;
import edu.greenriver.sdev.boardgames.service.TicTacToeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*")
public class TicTacToeAPI {

    private TicTacToeService service;

    public TicTacToeAPI(TicTacToeService service) {
        this.service = service;
    }

    @GetMapping("api/tictactoe")
    private ResponseEntity<TicTacToeGame> initGame() {
        return new ResponseEntity<>(new TicTacToeGame(new TicTacToeSymbol[] {
                TicTacToeSymbol.NONE, TicTacToeSymbol.NONE, TicTacToeSymbol.NONE,
                TicTacToeSymbol.NONE, TicTacToeSymbol.NONE, TicTacToeSymbol.NONE,
                TicTacToeSymbol.NONE, TicTacToeSymbol.NONE, TicTacToeSymbol.NONE
            }, TicTacToeSymbol.NONE),
            HttpStatus.OK);
    }

    @PostMapping("api/tictactoe")
    private ResponseEntity<TicTacToeGame> move(@RequestBody TicTacToeGame game) {
        if (!service.isTicTacToeGameValid(game)) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        var newState = service.getOpponentMove(game);

        if (newState == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(newState, HttpStatus.OK);
    }
}
