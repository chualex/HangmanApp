package com.csci448.alchu.alexchu_a2;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.LayoutRes;
import android.support.v4.app.Fragment;

/**
 * Created by Alex on 3/1/18.
 */

public class HangmanActivity extends SingleFragmentActivity {
    private static final String EXTRA_ISHUMAN = "com.hangmanactivity.ishuman";
    private static final String EXTRA_DIFFICULTY = "com.hangmanactivity.difficulty";
    private static final String EXTRA_WINS = "com.hangmanactivity.wins";
    private static final String EXTRA_LOSSES = "com.hangmanactivity.losses";

    @LayoutRes
    protected int getLayoutResId() {
        return R.layout.activity_hangman;
    }


    @Override
    protected Fragment createFragment() {
        boolean isHuman = (boolean) getIntent().getBooleanExtra(EXTRA_ISHUMAN, false);
        Difficulty difficulty = (Difficulty) getIntent().getSerializableExtra(EXTRA_DIFFICULTY);
        int wins = (int) getIntent().getIntExtra(EXTRA_WINS, 0);
        int losses = (int) getIntent().getIntExtra(EXTRA_LOSSES,0);
        return HangmanFragment.newInstance(isHuman, difficulty, wins, losses);
    }

    public static Intent newIntent(Context packageContext, boolean isHuman, Difficulty difficulty, int wins, int losses) {
        Intent intent = new Intent(packageContext, HangmanActivity.class);
        intent.putExtra(EXTRA_ISHUMAN, isHuman);
        intent.putExtra(EXTRA_DIFFICULTY, difficulty);
        intent.putExtra(EXTRA_WINS, wins);
        intent.putExtra(EXTRA_LOSSES, losses);
        return intent;
    }




}
