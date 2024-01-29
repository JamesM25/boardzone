package edu.greenriver.sdev.boardgames.service;

import edu.greenriver.sdev.boardgames.domain.TicTacToeGame;
import org.springframework.stereotype.Service;
import edu.greenriver.sdev.boardgames.domain.TicTacToeSymbol;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

@Service
public class TicTacToeService {
    private final int NUM_TILES = 9;

    private final TicTacToeSymbol PLAYER_SYMBOL = TicTacToeSymbol.X;
    private final TicTacToeSymbol CPU_SYMBOL = TicTacToeSymbol.O;
    private final int MAX_MINIMAX_DEPTH = 9;

    private final int[] choices = { 0, 1, 2, 3, 4, 5, 6, 7, 8 };

    private int encodeGameState(TicTacToeSymbol[] game) {
        assert game.length == NUM_TILES;

        // Store the state of each tile as a 2-bit code, according to the TicTacToeSymbol enum.
        int code = 0;
        for (int i = 0; i < game.length; i++) {
            code |= game[i].ordinal() << (i * 2);
        }

        return code;
    }
    private TicTacToeSymbol[] decodeGameState(int code) {
        var game = new TicTacToeSymbol[NUM_TILES];

        for (int i = 0; i < game.length; i++) {
            int ordinal = code >> (i * 2) & 0x03;
            game[i] = TicTacToeSymbol.values()[ordinal];
        }

        return game;
    }

    private TicTacToeSymbol checkGameEnd(TicTacToeSymbol[] board) {
        assert board.length == NUM_TILES;

        for (int i = 0; i < 3; i++) {
            // Horizontal
            if (board[i*3] != TicTacToeSymbol.NONE && board[i*3] == board[i*3 + 1] && board[i*3] == board[i*3 + 2]) {
                return board[i*3];
            }

            // Vertical
            if (board[i] != TicTacToeSymbol.NONE && board[i] == board[i + 3] && board[i] == board[i + 6]) {
                return board[i];
            }
        }

        // Diagonal
        if (board[4] != TicTacToeSymbol.NONE && (
                (board[4] == board[0] && board[4] == board[8])
            ||  (board[4] == board[2] && board[4] == board[6]))) {
            return board[4];
        }

        return TicTacToeSymbol.NONE;
    }

    private int minimaxRecurse(TicTacToeSymbol[] board, int depth) {
        var end = checkGameEnd(board);
        if (end == PLAYER_SYMBOL) {
            return depth - 100;
        } else if (end == CPU_SYMBOL) {
            return 1000 - depth;
        } else if (depth > MAX_MINIMAX_DEPTH) {
            return depth;
        }

        boolean odd = depth % 2 == 1;

        int defaultRating = odd ? Integer.MAX_VALUE : Integer.MIN_VALUE;
        int bestRating = defaultRating;

        for (int i = 0; i < board.length; i++) {
            int tile = choices[i];
            if (board[tile] != TicTacToeSymbol.NONE) {
                continue;
            }

            board[tile] = odd ? PLAYER_SYMBOL : CPU_SYMBOL;

            int rating = minimaxRecurse(board, depth + 1);
            bestRating = odd ? Integer.min(rating, bestRating) : Integer.max(rating, bestRating);

            // Revert the board state
            board[tile] = TicTacToeSymbol.NONE;
        }

        if (bestRating == defaultRating) {
            bestRating = depth;
        }

        return bestRating;
    }
    public TicTacToeGame getOpponentMove(TicTacToeGame game) {
        var board = game.getBoard();

        var winner = checkGameEnd(board);
        if (winner != TicTacToeSymbol.NONE) {
            game.setWinner(winner);
            return game;
        }

        var rand = new Random();
        for (int i = 0; i < choices.length; i++) {
            int j = rand.nextInt(choices.length);
            int temp = choices[i];
            choices[i] = choices[j];
            choices[j] = temp;
        }

        int bestMove = -1;
        int bestMoveRating = Integer.MIN_VALUE;

        for (int i = 0; i < board.length; i++) {
            int tile = choices[i];
            if (board[tile] != TicTacToeSymbol.NONE) {
                continue;
            }

            board[tile] = CPU_SYMBOL;

            var rating = minimaxRecurse(board, 1);
            if (rating > bestMoveRating) {
                bestMove = tile;
                bestMoveRating = rating;
            }

            board[tile] = TicTacToeSymbol.NONE;
        }

        if (bestMove < 0) {
            return null;
        }

        board[bestMove] = CPU_SYMBOL;
        winner = checkGameEnd(board);

        return new TicTacToeGame(board, winner);
    }

    public boolean isTicTacToeGameValid(TicTacToeGame game) {
        var board = game.getBoard();
        return board != null && board.length == NUM_TILES;
    }
}
