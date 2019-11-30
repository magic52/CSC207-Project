package com.example.game.CowsBullsGame.activities;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.InputType;
import android.view.Gravity;
import android.view.View;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.game.CowsBullsGame.domain.Guess;
import com.example.game.CowsBullsGame.game_logic.GameManager;
import com.example.game.CowsBullsGame.game_logic.TurnData;
import com.example.game.CowsBullsGame.services.CowsBullsStatsManager;
import com.example.game.R;
import com.example.game.data.GameData;
import com.example.game.data.MultiplayerGameData;
import com.example.game.data.Setting;
import com.example.game.services.multiplayer_data.MultiplayerDataManager;
import com.example.game.services.multiplayer_data.MultiplayerDataManagerFactory;
import com.example.game.services.settings.SettingsManager;
import com.example.game.services.settings.SettingsManagerBuilder;
import com.example.game.services.stats.StatsManager;
import com.example.game.services.stats.StatsManagerBuilder;

import java.util.ArrayList;

import static com.example.game.data.MultiplayerIntData.BLACKJACK_PLAYER_TURN;
import static com.example.game.data.MultiplayerIntData.COWS_BULLS_PLAYER_TURN;
import static java.security.AccessController.getContext;


/*
 * Image for cows_and_bull received from http://benjdd.com/courses/cs110/fall-2018/pas/bulls_and_cows/
 */

/**
 * The activity that appears right before the user is about to start a game of Cows and Bulls.
 */
public class CowsBullsActivity extends AppCompatActivity {



    //The object this class will use to manage multiplayer data if necessary
    private MultiplayerDataManager multiplayerDataManager;


    //If multiplayer = true, tells this activity whether the game being played is for player 1
    //or player 2
    private boolean player1Turn;

    // The text view for user input.
    private EditText guess;

    // The last guess that was made.
    static String currentGuess;

    // The timer for this game.
    private Chronometer chronometer;

    // The layout for the past guesses in the game.
    private LinearLayout linLayout;

    // The GameManager for this game.
    private GameManager gameManager;

    //The StatsManager for this game.
    private StatsManager statsManager;

    //The SettingsManager for this game.
    private SettingsManager settingsManager;

    //The CowsBullsStatsManager for this game.
    private CowsBullsStatsManager cowsBullsStatsManager;

    // The time in milliseconds when the player started the game.
    private long startTime;

    //Indicates whether multiplayer mode is on
    private boolean multiplayer;

    // The player's username.
    String username = GameData.USERNAME;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cows_bulls);

        multiplayerDataManager = new MultiplayerDataManagerFactory().build();
        player1Turn = multiplayerDataManager.getMultiplayerData(COWS_BULLS_PLAYER_TURN) == 1;

        startTime = System.currentTimeMillis();
        chronometer = findViewById(R.id.timer);
        chronometer.start();
        guess = findViewById(R.id.guessNumber);
        linLayout = findViewById(R.id.linLayout);
        settingsManager = new SettingsManagerBuilder().build(this, username);



        gameManager = new GameManager(5, settingsManager.getSetting(Setting.ALPHABET));
        cowsBullsStatsManager = new CowsBullsStatsManager(statsManager);
        multiplayer = GameData.MULTIPLAYER;

        if (multiplayer){
            if (player1Turn) {
                statsManager = new StatsManagerBuilder().build(this, MultiplayerGameData.getPlayer1Username());
            } else {
                statsManager = new StatsManagerBuilder().build(this, MultiplayerGameData.getPlayer2Username());
            }
        } else {
            statsManager = new StatsManagerBuilder().build(this, GameData.USERNAME);
        }

        if (settingsManager.getSetting(Setting.ALPHABET) == 1) {
            guess.setInputType(InputType.TYPE_CLASS_TEXT);
        } else {
            guess.setInputType(InputType.TYPE_CLASS_NUMBER);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState){
        super.onSaveInstanceState(savedInstanceState);
    }

    /**
     * @return - the guess input of user as a String if guess length matches GUESS_SIZE, otherwise
     * return null.
     */
    private String guessInput() {
        try {
            if (guess.getText().toString().length() > 0)
                return guess.getText().toString().replaceAll("\\s+", "");
            return "null";
        } catch (Exception e) {
            return "null";
        }
    }

    /**
     * This method performs the tasks after user had made a guess through the interface.
     *
     * @param view - view of the Activity
     */
    public void checkGuess(View view) {
        currentGuess = guessInput();

        if (gameManager.checkGuess(currentGuess)) {
            Guess guessArray = new Guess(currentGuess);


            this.gameManager.setGuess(guessArray);
            int bulls = this.gameManager.getResults()[1];
            int cows = this.gameManager.getResults()[0];
            System.out.println(gameManager.gameEnd());
            if (gameManager.gameEnd()) {
                long stopTime = System.currentTimeMillis();
                chronometer.stop();
                long elapsedTime = stopTime - startTime;
                int seconds = turnToSeconds(elapsedTime);
                int numberOfGuesses = getStatistics().size();
                cowsBullsStatsManager.update(seconds, numberOfGuesses);

                if (multiplayer) {
                    Intent intent = new Intent(this, CowsBullsMidMultiplayerActivity.class);
                }
            }

            TextView currGuess = new TextView(CowsBullsActivity.this);
            String textToDisplay = currentGuess + "     Bulls: " + bulls + " Cows: " + cows;
            currGuess.setText(textToDisplay);
            currGuess.setGravity(Gravity.CENTER);
            linLayout.addView(currGuess);

        }
        guess.setText("");
    }

    /**
     * Returns elapsedTime in seconds in and int
     *
     * @param elapsedTime - the time elapsed in milliSeconds
     */
    private int turnToSeconds(long elapsedTime) {
        return (int) (elapsedTime / 1000);
    }


    /**
     * A method that returns all of the data / statistics collected so far in level 3.
     *
     * @return An array of TurnData objects which store the data for each turn.
     */
    public ArrayList<TurnData> getStatistics() {
        return this.gameManager.getStatistics();
    }
}
