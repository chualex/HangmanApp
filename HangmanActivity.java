package com.csci448.alchu.alexchu_a2;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.LayoutRes;
import android.support.v4.app.Fragment;

/**
 * HangmanActivity Class
 *
 * Launches the Hangman Fragment
 */

public class HangmanActivity extends SingleFragmentActivity {
    private static final String EXTRA_ISHUMAN = "com.hangmanactivity.ishuman";
    private static final String EXTRA_DIFFICULTY = "com.hangmanactivity.difficulty";
    private static final String EXTRA_WINS = "com.hangmanactivity.wins";
    private static final String EXTRA_LOSSES = "com.hangmanactivity.losses";

    /**
     * Gets the ID for the Hangman Layout
     * @return the hangman layout
     */
    @LayoutRes
    protected int getLayoutResId() {
        return R.layout.activity_hangman;
    }


    /**
     * creates the hangman fragment and places it in the layout.
     * @return new hangman fragment
     */
    @Override
    protected Fragment createFragment() {
        boolean isHuman = (boolean) getIntent().getBooleanExtra(EXTRA_ISHUMAN, false);
        Difficulty difficulty = (Difficulty) getIntent().getSerializableExtra(EXTRA_DIFFICULTY);
        int wins = (int) getIntent().getIntExtra(EXTRA_WINS, 0);
        int losses = (int) getIntent().getIntExtra(EXTRA_LOSSES,0);
        return HangmanFragment.newInstance(isHuman, difficulty, wins, losses);
    }

    /**
     * Starts new intent to launch the HangmanActivity. Called from the Welcome Fragment.
     * @param packageContext The context of the activity
     * @param isHuman opponent setting
     * @param difficulty difficulty setting
     * @param wins number of wins
     * @param losses number of losses
     * @return returns teh intent with the extras
     */
    public static Intent newIntent(Context packageContext, boolean isHuman, Difficulty difficulty, int wins, int losses) {
        Intent intent = new Intent(packageContext, HangmanActivity.class);
        intent.putExtra(EXTRA_ISHUMAN, isHuman);
        intent.putExtra(EXTRA_DIFFICULTY, difficulty);
        intent.putExtra(EXTRA_WINS, wins);
        intent.putExtra(EXTRA_LOSSES, losses);
        return intent;
    }




}
