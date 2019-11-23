package com.example.game.data;

/**
 * An enum defining what kinds of data take on integer values and will need to be stored during the
 * course of a multiplayer game
 */
public enum MultiplayerIntData {
    /**
     * Represents the statistic "player 1's longest winning streak in Blackjack".
     * <p>
     * This enum will be used to store and access this value to allow player 1 and player 2 to
     * compete in a game of blackjack
     */
    BLACKJACK_PLAYER1_LONGEST_STREAK(0, "player1LongestStreak"),

    /**
     * Represents the statistic "player 2's longest winning streak in Blackjack".
     */
    BLACKJACK_PLAYER2_LONGEST_STREAK(0, "player2LongestStreak"),

    /**
     * Represents whose turn it is in an ongoing blackjack game. Can either be 1 if it's player 1's
     * turn or 2 if it's player 2's.
     */
    BLACKJACK_PLAYER_TURN(1, "blackjackPlayerTurn");


    /**
     * The initial, default value of this particular statistic. What the statistic
     * should be initialized to at runtime when a multiplayer game starts
     */
    private int defaultValue;

    /**
     * The key to be used when storing the value. For example. If key="key1", then if the
     * multiplayer data is being stored in a file we would have:
     * ...
     * ...
     * ...
     * ...
     * key1=5
     * ...
     * ...
     * ...
     * or something similar in that file, meaning that the value of the statistic with key "key1"
     * is 5
     */
    private String key;

    /**
     * Initialize a new MultiplayerData object
     *
     * @param defaultValue - the default value of this statistic
     * @param key          - the key to be used when storing this statistic
     */
    MultiplayerIntData(int defaultValue, String key) {
        this.defaultValue = defaultValue;
        this.key = key;
    }

    /**
     * Access the default value of this MultiplayerData object
     *
     * @return - the default value of this MultiplayerData object
     */
    public int getDefaultValue() {
        return defaultValue;
    }

    /**
     * Access the key of this MultiplayerData object
     *
     * @return - the key of this MultiplayerData object
     */
    public String getKey() {
        return key;
    }
}
