package com.csci448.alchu.alexchu_a2;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.ToggleButton;

/**
 * Created by Alex on 3/1/18.
 */

public class OptionsFragment extends Fragment {
    private Button mGoBackButton;
    private ToggleButton mIsHumanButton;
    private Spinner mDifficultySpinner;
    private boolean mIsHuman;
    private Difficulty mDifficulty;
    private int mWins;
    private int mLosses;
    private Button mResetScoresButton;
    private static final String ARGUMENT_ISHUMAN = "com.optionsactivity.ishuman";
    private static final String ARGUMENT_DIFFICULTY = "com.optionsactivity.difficulty";
    private static final String ARGUMENT_WINS = "com.optionsactivity.wins";
    private static final String ARGUMENT_LOSSES = "com.optionsactivity.losses";


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mIsHuman = getArguments().getBoolean(ARGUMENT_ISHUMAN);
        mDifficulty = (Difficulty) getArguments().getSerializable(ARGUMENT_DIFFICULTY);
        mWins = getArguments().getInt(ARGUMENT_WINS);
        mLosses = getArguments().getInt(ARGUMENT_LOSSES);

        if (savedInstanceState != null) {
            mIsHuman = savedInstanceState.getBoolean(ARGUMENT_ISHUMAN);
            mDifficulty = (Difficulty) savedInstanceState.getSerializable(ARGUMENT_DIFFICULTY);
            mWins = savedInstanceState.getInt(ARGUMENT_WINS);
            mLosses = savedInstanceState.getInt(ARGUMENT_LOSSES);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_options, container, false);

        mIsHumanButton = (ToggleButton) view.findViewById(R.id.toggle_human);
        mIsHumanButton.setChecked(mIsHuman);
        mIsHumanButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                mIsHuman = b;
            }
        });

        mDifficultySpinner = (Spinner) view.findViewById(R.id.spinner_difficulty);
        mDifficultySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if ("Easy".equalsIgnoreCase(adapterView.getItemAtPosition(i).toString())) {
                    mDifficulty = Difficulty.EASY;
                }
                else if ("Medium".equalsIgnoreCase(adapterView.getItemAtPosition(i).toString())) {
                    mDifficulty = Difficulty.MEDIUM;
                }
                else if ("Hard".equalsIgnoreCase(adapterView.getItemAtPosition(i).toString())) {
                    mDifficulty = Difficulty.HARD;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        mResetScoresButton = (Button) view.findViewById(R.id.reset_scores);
        mResetScoresButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mLosses = 0;
                mWins = 0;
            }
        });

        mGoBackButton = (Button) view.findViewById(R.id.go_back_button);
        mGoBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setReturnResult();
                getActivity().finish();
            }
        });
        return view;
    }

    public static OptionsFragment newInstance(boolean isHuman, Difficulty difficulty, int wins, int losses) {
        Bundle args = new Bundle();
        args.putBoolean(ARGUMENT_ISHUMAN, isHuman);
        args.putSerializable(ARGUMENT_DIFFICULTY, difficulty);
        args.putInt(ARGUMENT_WINS, wins);
        args.putInt(ARGUMENT_LOSSES, losses);

        OptionsFragment frag = new OptionsFragment();
        frag.setArguments(args);
        return frag;
    }

    public void setReturnResult() {
        Intent resultIntent = new Intent();
        resultIntent.putExtra(ARGUMENT_ISHUMAN, mIsHuman);
        resultIntent.putExtra(ARGUMENT_DIFFICULTY, mDifficulty);
        resultIntent.putExtra(ARGUMENT_WINS, mWins);
        resultIntent.putExtra(ARGUMENT_LOSSES, mLosses);
        getActivity().setResult(Activity.RESULT_OK, resultIntent);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(ARGUMENT_ISHUMAN, mIsHuman);
        outState.putSerializable(ARGUMENT_DIFFICULTY, mDifficulty);
        outState.putInt(ARGUMENT_WINS, mWins);
        outState.putInt(ARGUMENT_LOSSES, mLosses);
    }

    public static boolean returnIsHuman(Intent result) {
        return result.getBooleanExtra(ARGUMENT_ISHUMAN, false);
    }

    public static Difficulty returnDifficulty(Intent result) {
        return (Difficulty) result.getSerializableExtra(ARGUMENT_DIFFICULTY);
    }

    public static int returnWins(Intent result) {
        return  result.getIntExtra(ARGUMENT_WINS, 0);
    }

    public static int returnLosses(Intent result) {
        return  result.getIntExtra(ARGUMENT_LOSSES, 0);
    }


}
