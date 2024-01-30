package edu.greenriver.sdev.boardgames.tictactoe.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("tictactoe")
public class Pages {
    @GetMapping("")
    public String tictactoe() {
        return "tictactoe";
    }
}
