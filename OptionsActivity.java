package com.csci448.alchu.alexchu_a2;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by Alex on 3/1/18.
 */

public class OptionsActivity extends SingleFragmentActivity {
    private static final String EXTRA_ISHUMAN = "com.optionsactivity.ishuman";
    private static final String EXTRA_DIFFICULTY = "com.optionsactivity.difficulty";
    private static final String EXTRA_WINS = "com.optionsactivity.wins";
    private static final String EXTRA_LOSSES = "com.optionsactivity.losses";

    @LayoutRes
    protected int getLayoutResId() {
        return R.layout.activity_options;
    }

    @Override
    protected Fragment createFragment() {
        boolean isHuman = (boolean) getIntent().getBooleanExtra(EXTRA_ISHUMAN, false);
        Difficulty difficulty = (Difficulty) getIntent().getSerializableExtra(EXTRA_DIFFICULTY);
        int wins = (int) getIntent().getIntExtra(EXTRA_WINS, 0);
        int losses = (int) getIntent().getIntExtra(EXTRA_LOSSES,0);
        return OptionsFragment.newInstance(isHuman, difficulty, wins, losses);
    }

    public static Intent newIntent(Context packageContext, boolean isHuman, Difficulty difficulty, int wins, int losses) {
        Intent intent = new Intent(packageContext, OptionsActivity.class);
        intent.putExtra(EXTRA_ISHUMAN, isHuman);
        intent.putExtra(EXTRA_DIFFICULTY, difficulty);
        intent.putExtra(EXTRA_WINS, wins);
        intent.putExtra(EXTRA_LOSSES, losses);
        return intent;
    }
}
