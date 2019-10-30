package com.example.game.level2;
import java.util.ArrayList;

public class GameManager {
    /**
     * The current game this game manager holds.
     */
     private Game game;
    /**
     * Leader board keeps track of top three players.
     */
    int[] leaderBoard = new int[3];
    /**
     * Rules for the game.
     */
    String rules;

    /**
     * Create a gameManager
     */
    public GameManager(){
       this.game = new Game();
       this.rules = "";
    }

    // call this class whenever a new GTN game is about to be played
    public void startNewGame() {
        game = new Game();
    }

    public Game GetAGame(){
        return this.game;
    }

}
