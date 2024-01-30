package edu.greenriver.sdev.boardgames.tictactoe.service;

import edu.greenriver.sdev.boardgames.tictactoe.domain.GameState;
import org.springframework.stereotype.Service;
import edu.greenriver.sdev.boardgames.tictactoe.domain.Symbol;

import java.util.Random;

@Service
public class GameService {
    private static final int NUM_TILES = 9;

    private static final Symbol PLAYER_SYMBOL = Symbol.X;
    private static final Symbol CPU_SYMBOL = Symbol.O;
    private static final int MAX_MINIMAX_DEPTH = 9;

    private final int[] choices = { 0, 1, 2, 3, 4, 5, 6, 7, 8 };

    private int encodeGameState(Symbol[] game) {
        assert game.length == NUM_TILES;

        // Store the state of each tile as a 2-bit code, according to the TicTacToeSymbol enum.
        int code = 0;
        for (int i = 0; i < game.length; i++) {
            code |= game[i].ordinal() << (i * 2);
        }

        return code;
    }
    private Symbol[] decodeGameState(int code) {
        var game = new Symbol[NUM_TILES];

        for (int i = 0; i < game.length; i++) {
            int ordinal = code >> (i * 2) & 0x03;
            game[i] = Symbol.values()[ordinal];
        }

        return game;
    }

    private static boolean checkRow(Symbol[] board, int row) {
        return board[row*3] == board[row*3 + 1] && board[row*3] == board[row*3 + 2];
    }
    private static boolean checkColumn(Symbol[] board, int col) {
        return board[col] == board[col + 3] && board[col] == board[col + 6];
    }
    private static boolean checkDiagonal(Symbol[] board) {
        return (board[4] == board[0] && board[4] == board[8])
            || (board[4] == board[2] && board[4] == board[6]);
    }
    private static Symbol checkGameEnd(Symbol[] board) {
        assert board.length == NUM_TILES;

        for (int i = 0; i < 3; i++) {
            // Horizontal
            if (board[i*3] != Symbol.NONE && checkRow(board, i)) {
                return board[i*3];
            }

            // Vertical
            if (board[i] != Symbol.NONE && checkColumn(board, i)) {
                return board[i];
            }
        }

        // Diagonal
        if (board[4] != Symbol.NONE && checkDiagonal(board)) {
            return board[4];
        }

        return Symbol.NONE;
    }

    private int minimaxRecurse(Symbol[] board, int depth) {
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
            if (board[tile] != Symbol.NONE) {
                continue;
            }

            board[tile] = odd ? PLAYER_SYMBOL : CPU_SYMBOL;

            int rating = minimaxRecurse(board, depth + 1);
            bestRating = odd ? Integer.min(rating, bestRating) : Integer.max(rating, bestRating);

            // Revert the board state
            board[tile] = Symbol.NONE;
        }

        if (bestRating == defaultRating) {
            bestRating = depth;
        }

        return bestRating;
    }
    public GameState getOpponentMove(GameState game) {
        var board = game.getBoard();

        var winner = checkGameEnd(board);
        if (winner != Symbol.NONE) {
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
            if (board[tile] != Symbol.NONE) {
                continue;
            }

            board[tile] = CPU_SYMBOL;

            var rating = minimaxRecurse(board, 1);
            if (rating > bestMoveRating) {
                bestMove = tile;
                bestMoveRating = rating;
            }

            board[tile] = Symbol.NONE;
        }

        if (bestMove < 0) {
            return null;
        }

        board[bestMove] = CPU_SYMBOL;
        winner = checkGameEnd(board);

        return new GameState(board, winner);
    }

    public boolean isTicTacToeGameValid(GameState game) {
        var board = game.getBoard();
        return board != null && board.length == NUM_TILES;
    }
}
