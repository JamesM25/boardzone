package edu.greenriver.sdev.boardgames.tictactoe.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Implements MVC routes for all tic-tac-toe pages
 * @author James Motherwell
 * @version 1.0
 */
@Controller
@RequestMapping("tictactoe")
public class Pages {

    @GetMapping("")
    private String tictactoe() {
        return "tictactoe";
    }
}
