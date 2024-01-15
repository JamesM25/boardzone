package edu.greenriver.sdev.boardgames.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class StaticPages {
    @GetMapping("/")
    public String home() {
        return "index";
    }
}
