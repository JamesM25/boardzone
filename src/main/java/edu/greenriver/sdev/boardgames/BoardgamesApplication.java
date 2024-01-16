package edu.greenriver.sdev.boardgames;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * A Spring application for the Boardzone website
 * @author James Motherwell
 * @version 1.0
 */
@SpringBootApplication
public class BoardgamesApplication {

    /**
     * Runs the Spring application
     * @param args Command line arguments
     */
    public static void main(String[] args) {
        SpringApplication.run(BoardgamesApplication.class, args);
    }

}
