package com.example.game;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

<<<<<<< HEAD
import com.example.game.level1.BlackJackStartActivity;
import com.example.game.level2.GameStartActivity;
=======
import com.example.game.level1.activities.BlackjackStartActivity;
>>>>>>> 6a79296386dd2df5f93812461ca643329ea45bdc

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void buttonClick(View view){
        Intent intent = new Intent(this, BlackjackStartActivity.class);
        startActivity(intent);
    }
    public void chooseTheNum(View view){
        Intent intent = new Intent(this, GameStartActivity.class);
        startActivity(intent);
    }
}
