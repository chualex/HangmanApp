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
 * Options Fragment Class
 *
 * Handles all inputs from the options page. Sends options set by user back to the Welcome Fragment
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

    /**
     * Called when the class is created. sets up the arguments passed from the Welcome Activity.
     *
     * @param savedInstanceState Bundle for the last saved state of the fragment. Used to not change
     *                           the layout when the device is flipped.
     */
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

    /**
     * creates the View. Sets up the buttons and layout. Sets up button listeners and stores changed
     * data.
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_options, container, false);

        // sets uo button for whether the opponent is a computer or human player
        mIsHumanButton = (ToggleButton) view.findViewById(R.id.toggle_human);
        mIsHumanButton.setChecked(mIsHuman);
        mIsHumanButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                mIsHuman = b;
            }
        });

        // Sets up drop down menu for the difficulty
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

        // Sets up the reset scores button
        // if pressed it resets the wins and loses to 0
        mResetScoresButton = (Button) view.findViewById(R.id.reset_scores);
        mResetScoresButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mLosses = 0;
                mWins = 0;
            }
        });

        // sets up go back button
        // if pressed brings up the welcome fragment
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

    /**
     * Called in the Welcome Fragment to start the options fragment. Passes in values needed for the
     * options menu.
     * @param isHuman boolean for human or computer opponent
     * @param difficulty Enum for the difficulty setting of the computer opponent
     * @param wins number of wins
     * @param losses number of losses
     * @return new instance of the options fragment with the parameters passed as a bundle.
     *
     */
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

    /**
     * Saves the current state of the options. prevents loss of progress when the device is flipped
     * @param outState saved state bundle
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(ARGUMENT_ISHUMAN, mIsHuman);
        outState.putSerializable(ARGUMENT_DIFFICULTY, mDifficulty);
        outState.putInt(ARGUMENT_WINS, mWins);
        outState.putInt(ARGUMENT_LOSSES, mLosses);
    }

    /**
     * Returns whether the user set a human or computer payer to the welcome fragment.
     *
     * @param result Intent being passed from
     * @return Whether the user set a computer or human opponent
     */
    public static boolean returnIsHuman(Intent result) {
        return result.getBooleanExtra(ARGUMENT_ISHUMAN, false);
    }

    /**
     * Returns what difficulty the user set to the Welcome Fragment
     * @param result Intent being passed from
     * @return The difficulty the user set
     */
    public static Difficulty returnDifficulty(Intent result) {
        return (Difficulty) result.getSerializableExtra(ARGUMENT_DIFFICULTY);
    }

    /**
     * Returns the number of wins back to the Welcome Fragment
     * @param result Intent being passed from
     * @return The number of wins
     */
    public static int returnWins(Intent result) {
        return  result.getIntExtra(ARGUMENT_WINS, 0);
    }

    /**
     * Returns the number of losses back to the Welcome Fragment
     * @param result Intent being passed from
     * @return The number of losses
     */
    public static int returnLosses(Intent result) {
        return  result.getIntExtra(ARGUMENT_LOSSES, 0);
    }


}
