package com.example.game.GuessTheNumber.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.game.GuessTheNumber.domain.Game;
import com.example.game.GuessTheNumber.game_logic.GameManager;
import com.example.game.MainActivity;
import com.example.game.R;
import com.example.game.data.GameData;
import com.example.game.data.MultiplayerGameData;
import com.example.game.data.Statistic;
import com.example.game.services.scoreboard.ScoreboardRepository;
import com.example.game.services.scoreboard.ScoreboardRepositoryFactory;
import com.example.game.services.stats.StatsManager;
import com.example.game.services.stats.StatsManagerBuilder;

import java.util.List;

/**
 * This activity appears when the user successfully finishes each round of GuessTheNumber.
 */
public class GameFinishActivity extends AppCompatActivity {

    /**
     * An object for updating and accessing highscores, when necessary
     */
    private ScoreboardRepository GuessNumHighscoreManager;

    GameManager gameManager = GameStartActivity.gameManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_finish_activity);
        GuessNumHighscoreManager = new ScoreboardRepositoryFactory().build(ScoreboardRepository.Game.GUESS_THE_NUMBER);
        Game currentGame = gameManager.getCurrentGame();
        currentGame.setIsFinished();
        this.updateStatistics();

        if (this.CheckForHighScore(currentGame.getPoints())) {
            this.askForHighScore();
        }

        ((TextView) findViewById(R.id.points16)).setText(String.valueOf(currentGame.getPoints()));
        ((TextView) findViewById(R.id.finalGuesses)).setText(String.valueOf(currentGame.getNumOfGuess()));
        ((TextView) findViewById(R.id.currentRoundText)).setText(String.valueOf(gameManager.getCurrentRound()));
        ((TextView) findViewById(R.id.totalRoundsText)).setText(String.valueOf(gameManager.getRoundsToPlay()));

        if (gameManager.getKeepPlaying()) {
            this.inverseVisibility();
            hideNextPlayerButton();

        } else {
            boolean isFirstPlayersTurn = gameManager.getIsFirstPlayersTurn();

            if (GameData.MULTIPLAYER && isFirstPlayersTurn) {
                showNextPlayerButton();
                hideAllButtons();
                gameManager.changeIsFirstPlayersTurn();
            } else if (GameData.MULTIPLAYER && !isFirstPlayersTurn) {
                hideNextPlayerButton();
                endMultiplayerGame();
                gameManager.resetGameManager();
            } else {
                this.reverseVisibility();
                hideNextPlayerButton();
            }

            gameManager.resetCurrentRounds();
        }
    }

    /**
     * When the main menu button is clicked, the user is taken to the MainActivity activity.
     */
    public void mainMenuClick(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    /**
     * When the playAgain button is clicked, the user is taken to the GuessTheNumberPlayActivity activity
     * which will allow them to play GuessTheNumber again.
     */
    public void playAgainClick(View view) {
        Intent intent = new Intent(this, GuessTheNumberPlayActivity.class);
        gameManager.startNewGame();
        startActivity(intent);
    }

    /**
     * This button should only be visible in Multiplayer mode.
     */
    public void nextPlayerClick(View view) {
        Intent intent = new Intent(this, GameStartActivity.class);
        gameManager.startNewGame();
        startActivity(intent);
    }

    /**
     * When nextRound button is clicked, the user proceeds to go onto the next round of
     * GuessTheNumber.
     */
    public void nextRound(View view) {
        Intent intent = new Intent(this, GuessTheNumberPlayActivity.class);
        gameManager.startNewGame();
        startActivity(intent);
    }

    /**
     * Shows the user the buttons that she/he is allowed to click when there is at least one round
     * left to play.
     */
    public void inverseVisibility() {
        findViewById(R.id.mainMenuButton).setVisibility(View.INVISIBLE);
        findViewById(R.id.playAgainButton).setVisibility(View.INVISIBLE);
        findViewById(R.id.nextRoundButton).setVisibility(View.VISIBLE);
    }

    /**
     * Shows user the buttons that he/she is allowed to click when the all the rounds are finished.
     */
    public void reverseVisibility() {
        findViewById(R.id.mainMenuButton).setVisibility(View.VISIBLE);
        findViewById(R.id.playAgainButton).setVisibility(View.VISIBLE);
        findViewById(R.id.nextRoundButton).setVisibility(View.INVISIBLE);
    }

    /**
     * Shows the nextPlayer button when the first player has finished their rounds of GuessTheNumber.
     * This button should only be visible in Multiplayer mode.
     */
    public void showNextPlayerButton() {
        findViewById(R.id.nextPlayerButton).setVisibility(View.VISIBLE);
    }

    /**
     * Hides the nextPlayerButton button.
     */
    public void hideNextPlayerButton() {
        findViewById(R.id.nextPlayerButton).setVisibility(View.INVISIBLE);
    }

    /**
     * Hides buttons mainMenuButton, playAgainButton, nextRoundButton
     */
    public void hideAllButtons() {
        findViewById(R.id.mainMenuButton).setVisibility(View.INVISIBLE);
        findViewById(R.id.playAgainButton).setVisibility(View.INVISIBLE);
        findViewById(R.id.nextRoundButton).setVisibility(View.INVISIBLE);
    }

    /**
     * Updates statistics in the game. If the user made the new record(guessed the number with fewest
     * guess, we update their statistics.
     */
    public void updateStatistics() {
        String username;
        if (GameData.MULTIPLAYER) {
            boolean isFirstPlayersTurn = gameManager.getIsFirstPlayersTurn();
            if (isFirstPlayersTurn) {
                username = MultiplayerGameData.getPlayer1Username();
            } else {
                username = MultiplayerGameData.getPlayer2Username();
            }
        } else {
            username = GameData.USERNAME;
        }

        StatsManager statsManager = new StatsManagerBuilder().build(this, username);
        int guesses = gameManager.getCurrentGame().getNumOfGuess();

        int userBest = statsManager.getStat(Statistic.FEWEST_GUESSES);
        if (guesses < userBest) {
            statsManager.setStat(Statistic.FEWEST_GUESSES, guesses);
        }
    }

    /**
     * Return True iff the score can be saved in the ScoreBoardRepository.(If number of points is
     * small enough to be in top 10.
     *
     * @param score - score, that user just got, after finishing the round.
     */
    private boolean CheckForHighScore(int score) {
        List<Pair<String, Integer>> allScores = GuessNumHighscoreManager.getHighScores(10);
        if (allScores.size() < 10) {
            return true;
        } else {
            //Keep track of how many elements are greater, than our score are already in the ScoreBoard.
            int local = 0;
            for (Pair<String, Integer> temp : allScores) {
                if (temp.second <= score) {
                    local++;
                }
            }
            return local < 10;
        }
    }

    /**
     * Ask user to type in the score to store it our repository.
     **/
    public void askForHighScore() {
        findViewById(R.id.highScoreCongrats).setVisibility(View.VISIBLE);
        findViewById(R.id.typeYourNameWindow).setVisibility(View.VISIBLE);
        findViewById(R.id.saveScore).setVisibility(View.VISIBLE);
        findViewById(R.id.typeNamePLease).setVisibility(View.VISIBLE);
    }
    //

    /**
     * Record the given highscore under the given name
     *
     * @param name  - the name to record along with the score
     * @param score - the highscore to save under the given name
     *              <p>
     *              Precondition:
     */
    private void recordHighScore(String name, int score) {
        GuessNumHighscoreManager.addScore(name, score);
    }

    /**
     * Button to submit the name of the user if they hit a highscore.
     */
    //IDK how to save the score here, so as soon as we press save, the texted "Your name has been
    //added to a scoreBoard should appear and save buttons with all the text in the bottom should disappear.
    //something to do with submit save idk.
    public void saveScore(View view) {
        String name = ((EditText) (findViewById(R.id.typeNamePLease))).getText().toString();
        recordHighScore(name, gameManager.getCurrentGame().getPoints());
        ((TextView)findViewById(R.id.highScoreCongrats)).setText("You can see your name on the ScoreBoard!");
        findViewById(R.id.typeNamePLease).setVisibility(View.INVISIBLE);
        findViewById(R.id.saveScore).setVisibility(View.INVISIBLE);
        findViewById(R.id.typeYourNameHere).setVisibility(View.INVISIBLE);

    }

    /**
     * Hide nextRoundButton, playAgainButton and show mainMenuButton when multiplayer game ends.
     */
    public void endMultiplayerGame() {
        findViewById(R.id.nextRoundButton).setVisibility(View.INVISIBLE);
        findViewById(R.id.playAgainButton).setVisibility(View.INVISIBLE);
        findViewById(R.id.mainMenuButton).setVisibility(View.VISIBLE);
    }

    /**
     * Prevents the user from being able to return to the game screen by pressing the back button
     */
    @Override
    public void onBackPressed() {
    }
}
