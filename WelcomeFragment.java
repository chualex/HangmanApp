package com.csci448.alchu.alexchu_a2;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by Alex on 3/1/18.
 */

public class WelcomeFragment extends Fragment {
    private Button mPlayButton;
    private Button mOptionsButton;
    private Button mQuitButton;
    private boolean mIsHuman;
    private int mWins;
    private int mLosses;
    private Difficulty mDifficulty;
    private TextView mScoreText;
    private static final int REQUEST_CODE_OPTIONS = 0;
    private static final int REQUEST_CODE_GAME = 1;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mIsHuman = true;
        mDifficulty = Difficulty.EASY;
        mLosses = 0;
        mWins = 0;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_welcome, container, false);

        mPlayButton = (Button) view.findViewById(R.id.play_button);
        mPlayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = HangmanActivity.newIntent(getActivity(), mIsHuman, mDifficulty, mWins, mLosses);
                startActivityForResult(i, REQUEST_CODE_GAME);
            }
        });


        mOptionsButton = (Button) view.findViewById(R.id.options_button);
        mOptionsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = OptionsActivity.newIntent(getActivity(), mIsHuman, mDifficulty, mWins, mLosses);
                startActivityForResult(i, REQUEST_CODE_OPTIONS);
            }
        });

        mQuitButton = (Button) view.findViewById(R.id.quit_button);
        mQuitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().finish();
            }
        });

        mScoreText = (TextView) view.findViewById(R.id.wins_and_loses);
        updateScoreText();
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        if (requestCode == REQUEST_CODE_OPTIONS) {
            if (data == null) {
                return;
            }
            mIsHuman = OptionsFragment.returnIsHuman(data);
            mDifficulty = OptionsFragment.returnDifficulty(data);
            mLosses = OptionsFragment.returnLosses(data);
            mWins = OptionsFragment.returnWins(data);
            updateScoreText();
        }

        if (requestCode == REQUEST_CODE_GAME) {
            mWins = HangmanFragment.returnWins(data);
            mLosses = HangmanFragment.returnLosses(data);
            updateScoreText();
        }
    }
    public void updateScoreText() {
        mScoreText.setText("Wins: " + mWins +"  Losses: " + mLosses);
    }
}
