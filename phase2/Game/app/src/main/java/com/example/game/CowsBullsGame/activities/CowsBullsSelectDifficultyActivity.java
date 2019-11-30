package com.example.game.CowsBullsGame.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.game.R;
import com.example.game.data.GameData;
import com.example.game.data.Setting;
import com.example.game.services.settings.SettingsManager;
import com.example.game.services.settings.SettingsManagerBuilder;

public class CowsBullsSelectDifficultyActivity extends AppCompatActivity {

    /**
     * SettingsManager for getting and setting Cows and Bulls difficulty
     */
    private SettingsManager settingsManager;

    /**
     * TextView to display current difficulty level
     */
    private TextView currDiffLevel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cows_bulls_select_difficulty);

        String username = GameData.USERNAME;
        settingsManager = new SettingsManagerBuilder().build(this, username);
        currDiffLevel = findViewById(R.id.currDiffLevel);

        int difficulty = settingsManager.getSetting(Setting.COWS_BULLS_DIFFICULTY);
        if (difficulty == 0) {
            currDiffLevel.setText("Easy");
        } else if (difficulty == 1) {
            currDiffLevel.setText("Medium");
        } else {
            currDiffLevel.setText("Insane");
        }
    }
    /**
     * Method to allow Easy button to set difficulty to easy
     *
     * @param view
     */
    public void easyDifficulty(View view){
        settingsManager.updateSetting(Setting.COWS_BULLS_DIFFICULTY, 0);
        currDiffLevel.setText("Easy");
    }

    /**
     * Method to allow Easy button to set difficulty to medium
     *
     * @param view
     */
    public void mediumDifficulty(View view){
        settingsManager.updateSetting(Setting.COWS_BULLS_DIFFICULTY, 1);
        currDiffLevel.setText("Medium");
    }

    /**
     * Method to allow Easy button to set difficulty to insane
     *
     * @param view
     */
    public void insaneDifficulty(View view){
        settingsManager.updateSetting(Setting.COWS_BULLS_DIFFICULTY, 2);
        currDiffLevel.setText("Insane");
    }
}
