package com.example.game;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.game.data.GameConstants;
import com.example.game.data.GameData;

import java.io.File;
import java.io.IOException;

/**
 * The page displayed when the app initially starts up
 */
public class StartActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        if (GameData.MULTIPLAYER) {
            findViewById(R.id.multiplayerLoginTitle).setVisibility(View.VISIBLE);
        }

        initializeGame();
    }

    public void newAccount(View view) {
        Intent intent = new Intent(this, NewAccountActivity.class);
        startActivity(intent);
    }

    public void oldAccount(View view) {
        Intent intent = new Intent(this, OldAccountActivity.class);
        startActivity(intent);
    }

    public void Test(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    /**
     * A method for any code that needs to be run when the game starts
     */
    private void initializeGame() {
        try {
            GameData.setFilesDirPath(this.getFilesDir().getCanonicalPath());
            File blackjackHighscoreFile = new File(this.getFilesDir(), GameConstants.BLACKJACK_HIGHSCORE_FILE);
            File cowsAndBullsHighscoreFile = new File(this.getFilesDir(), GameConstants.COWS_AND_BULLS_HIGHSCORE_FILE);
            File guessTheNumberHighScoreFile = new File(this.getFilesDir(), GameConstants.GUESS_THE_NUMBER_HIGHSCORE_FILE);

            if(!blackjackHighscoreFile.exists()){
                blackjackHighscoreFile.createNewFile();
            }

            if(!cowsAndBullsHighscoreFile.exists()){
                cowsAndBullsHighscoreFile.createNewFile();
            }

            if(!guessTheNumberHighScoreFile.exists()){
                guessTheNumberHighScoreFile.createNewFile();
            }
        } catch (IOException e) {
            Log.e("StartActivity", "Failed to set root directory path");
        }
    }

    @Override
    public void onBackPressed(){

    }
}
