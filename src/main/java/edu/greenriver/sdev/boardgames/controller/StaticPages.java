package edu.greenriver.sdev.boardgames.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Implements routes for all static pages on the website
 *
 * @author James Motherwell
 * @version 1.0
 */
@Controller
@CrossOrigin(origins = "*")
public class StaticPages {
    /**
     * @return Logical name of the home page (index)
     */
    @GetMapping("/")
    public String index() {
        return "index";
    }

    /**
     * Implement a home.html route to fulfill the requirements on the assignment rubric
     * @return Logical name of the home page (index)
     */
    @GetMapping("/home.html")
    public String home() {
        return "index";
    }

    @GetMapping("/tictactoe")
    public String tictactoe(Model model) {
        model.addAttribute("board", new String[] { "X", "X", "X", "O", "O", "O", "X", "X", "X" });
        return "tictactoe";
    }
}
