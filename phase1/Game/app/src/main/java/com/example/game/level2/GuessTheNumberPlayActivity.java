package com.example.game.level2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.game.MainActivity;
import com.example.game.R;

/**
 * This page displayed when user is actually playing a game.
 */
public class GuessTheNumberPlayActivity extends AppCompatActivity {
    GameManager gameManager = GameStartActivity.gameManager;
    static int guess;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gtnum__play_activity);
        this.updateScore();
    }

    /**
     * Assuming that a user inserted a number; this method gets the input.
     */
    private int getGuess() {
        return Integer.valueOf(((TextView) findViewById(R.id.guessInput)).getText().toString());
    }

    /**
     *Based on the user's guess, we display respective page/text and update some statistics.
     */
    public void submitGuess(View view) {
        try {
            guess = getGuess();
            Game currentGame = gameManager.getCurrentGame();
            ((TextView) findViewById(R.id.guessInput)).setText("");

            if (currentGame.checkTheRightGuess(guess)) {
                currentGame.finishTheGame(guess);
                gameManager.checkRounds();
                Intent intent = new Intent(this, GameFinishActivity.class);
                startActivity(intent);
            } else {
                if (currentGame.checkGuess(guess)) {
                    //((TextView)findViewById(R.id.textView)).setVisibility(View.INVISIBLE);
                    this.highGuess();
                } else {
                    this.lowGuess();
                }
                this.updateScore();
            }
        }
        catch(Exception e){
                this.BadNumber();
        }
    }

    /**
     * Allows user to pause the game, save it and exit to the MainActivity.
     */
    public void pauseExit(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    /**
     * Display the text if the user's guess is too high.
     */
    public void highGuess(){
        ((TextView) findViewById(R.id.textView)).setText("Your guess is too high, try again.");
    }

    /**
     * Display the text if the user's guess is too low.
     */
    public void lowGuess(){
        ((TextView) findViewById(R.id.textView)).setText("Your guess is too low, try again.");
    }

    /**
     * Displays the text if the user's input is null or a number bigger than 10^9.
     */
    public void BadNumber(){
        ((TextView) findViewById(R.id.textView)).setText("You either did not enter the number or your number is too big, please try again");
    }

    /**
     * Updates the display of the #points and #guesses.
     */
    public void updateScore(){
        Game currentGame = gameManager.getCurrentGame();
        ((TextView) findViewById(R.id.pointsFinishId)).setText(String.valueOf(currentGame.getPoints()));
        ((TextView) findViewById(R.id.guessesId)).setText(String.valueOf(currentGame.getNumOfGuess()));
    }


}
