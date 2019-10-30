package com.example.game.data;

public enum Setting {
    NUM_HANDS("NumHands", 5),
    DARK_MODE("DarkMode", 0),
    NUM_ROUNDS("NumRounds", 5);

    /**
     * The key that will be used to store this setting in the settings file
     * For example:
     * NUM_HANDS.key = "NumHands" means that the settings file will contain
     *
     * NumHands=
     *
     * followed by the value for that setting
     */
    private String key;

    /**
     * The default value for this setting
     */
    private int defaultValue;

    /**
     * Create a new Setting with the given key and defaultValue
     * @param key - the key for storing the setting in a file
     * @param defaultValue - the default value of the setting
     */
    Setting(String key, int defaultValue) {
        this.key = key;
        this.defaultValue = defaultValue;
    }

    /**
     * Get this Setting's key
     * @return the key of this Setting
     */
    public String getKey() {
        return key;
    }

    /**
     * Get this Setting's default value
     * @return the default value of this Setting
     */
    public int getDefaultValue() {
        return defaultValue;
    }
}
