package com.csci448.alchu.alexchu_a2;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import javax.xml.datatype.Duration;

/**
 * Created by Alex on 3/1/18.
 */

public class HangmanFragment extends Fragment {
    private boolean mIsHuman;
    private Difficulty mDifficulty;
    private int mWins;
    private int mLosses;
    private TextView mScoreText;
    private List<String> mEasyWords;
    private List<String> mMediumWords;
    private List<String> mHardWords;
    private int mGuesses;
    private TextView mGuessesText;
    private Button mStartGameButton;
    private EditText mWordInput;
    private String mWord;
    private String mDisplayWord;
    private TextView mWordTextView;
    private Button[] mLetterButtons;
    private boolean[] mLetterClicked;
    private LinearLayout mGameLayout;
    private LinearLayout mHumanPromptLayout;
    private LinearLayout mPostGameLayout;
    private Button mPlayAgainButton;
    private TextView mWinLossDisplay;
    private Button mQuitButton;
    private TextView mLostWordDisplay;
    private static final String ARGUMENT_ISHUMAN = "com.hangmanactivity.ishuman";
    private static final String ARGUMENT_DIFFICULTY = "com.hangmanactivity.difficulty";
    private static final String ARGUMENT_WINS = "com.hangmanactivity.wins";
    private static final String ARGUMENT_LOSSES = "com.hangmanactivity.losses";
    private static final String ARGUMENT_GUESSES = "com.hangmanactivity.guesses";
    private static final String ARGUMENT_WORD = "com.hangmanactivity.word";
    private static final String ARGUMENT_DISPLAYWORD = "com.hangmanactivity.displayword";
    private static final String ARGUMENT_LETTERSCLICKED = "com.hangmanactivity.lettersclicked";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mIsHuman = getArguments().getBoolean(ARGUMENT_ISHUMAN);
        mDifficulty = (Difficulty) getArguments().getSerializable(ARGUMENT_DIFFICULTY);
        mWins = getArguments().getInt(ARGUMENT_WINS);
        mLosses = getArguments().getInt(ARGUMENT_LOSSES);
        mEasyWords = new ArrayList<String>();
        mHardWords = new ArrayList<String>();
        mMediumWords = new ArrayList<String>();
        mGuesses = 7;
        mLetterButtons = new Button[26];
        mLetterClicked = new boolean[26];
        Arrays.fill(mLetterClicked, false);
        loadWords();
        if (savedInstanceState != null) {
            mIsHuman = savedInstanceState.getBoolean(ARGUMENT_ISHUMAN);
            mDifficulty = (Difficulty) savedInstanceState.getSerializable(ARGUMENT_DIFFICULTY);
            mWins = savedInstanceState.getInt(ARGUMENT_WINS);
            mLosses = savedInstanceState.getInt(ARGUMENT_LOSSES);
            mGuesses = savedInstanceState.getInt(ARGUMENT_GUESSES);
            mWord = savedInstanceState.getString(ARGUMENT_WORD);
            mDisplayWord = savedInstanceState.getString(ARGUMENT_DISPLAYWORD);
            mLetterClicked = savedInstanceState.getBooleanArray(ARGUMENT_LETTERSCLICKED);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_hangman, container, false);


        // set up board layout
        mGameLayout = (LinearLayout) view.findViewById(R.id.game_board_layout);

        // set up human prompt layout
        mHumanPromptLayout = (LinearLayout) view.findViewById(R.id.human_prompt_layout);
        mHumanPromptLayout.setVisibility(View.INVISIBLE);

        // set up score text field
        mScoreText = (TextView) view.findViewById(R.id.score_text);
        updateScoreText();

        // set up guesses text view
        mGuessesText = (TextView) view.findViewById(R.id.guesses_num);
        updateGuesses();

        // set up Edit text for human input word
        mWordInput = (EditText) view.findViewById(R.id.word_edit_text);

        // sets up word text view
        mWordTextView = (TextView) view.findViewById(R.id.word_text);

        // sets up start game button
        mStartGameButton = (Button) view.findViewById(R.id.start_game_button);
        mStartGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mWord = mWordInput.getText().toString();
                mHumanPromptLayout.setVisibility(View.INVISIBLE);
                hideGame(false);
                setWord();
            }
        });

        // set up letter buttons
        char letter = 'a';
        for (int i = 0; i < mLetterButtons.length; i++) {
            final int I = i;
            final String buttonId = letter + "_button";
            int id = getResources().getIdentifier(buttonId, "id", getActivity().getPackageName());
            mLetterButtons[I] = (Button) view.findViewById(id);
            if (mLetterClicked[i] == true) {
                mLetterButtons[I].setVisibility(View.INVISIBLE);
            }
            final char finalLetter = letter;
            mLetterButtons[I].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                letterPressed(finalLetter);
                mLetterClicked[I] = true;
                mLetterButtons[I].setVisibility(View.INVISIBLE);
                }
            });

            letter += 1;

        }

        // sets up post game display
        mPostGameLayout = (LinearLayout) view.findViewById(R.id.post_game_layout);
        mPostGameLayout.setVisibility(View.INVISIBLE);

        // set up play again button
        mPlayAgainButton = (Button) view.findViewById(R.id.play_again_button);
        mPlayAgainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetGame();
                mPostGameLayout.setVisibility(View.INVISIBLE);
            }
        });

        //set up win loss display
        mWinLossDisplay = (TextView) view.findViewById(R.id.win_loss_title);


        //Initializes game based on computer or human player
        if (savedInstanceState == null) {
            Initialize();
        }

        // set up quit button
        mQuitButton = (Button) view.findViewById(R.id.quit_game_button);
        mQuitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setReturnResult();
                getActivity().finish();
            }
        });

        //set up lost word display
        mLostWordDisplay = (TextView) view.findViewById(R.id.display_word);

        updateUI();

        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(ARGUMENT_ISHUMAN, mIsHuman);
        outState.putSerializable(ARGUMENT_DIFFICULTY, mDifficulty);
        outState.putInt(ARGUMENT_WINS, mWins);
        outState.putInt(ARGUMENT_LOSSES, mLosses);
        outState.putInt(ARGUMENT_GUESSES, mGuesses);
        outState.putString(ARGUMENT_WORD, mWord);
        outState.putString(ARGUMENT_DISPLAYWORD, mDisplayWord);
        outState.putBooleanArray(ARGUMENT_LETTERSCLICKED, mLetterClicked);
    }

    public static HangmanFragment newInstance(boolean isHuman, Difficulty difficulty, int wins, int losses) {
        Bundle args = new Bundle();
        args.putBoolean(ARGUMENT_ISHUMAN, isHuman);
        args.putSerializable(ARGUMENT_DIFFICULTY, difficulty);
        args.putInt(ARGUMENT_WINS, wins);
        args.putInt(ARGUMENT_LOSSES, losses);

        HangmanFragment frag = new HangmanFragment();
        frag.setArguments(args);
        return frag;
    }

    public void setReturnResult() {
        Intent resultIntent = new Intent();
        resultIntent.putExtra(ARGUMENT_WINS, mWins);
        resultIntent.putExtra(ARGUMENT_LOSSES, mLosses);
        getActivity().setResult(Activity.RESULT_OK, resultIntent);
    }

    public void Initialize() {
        // Prompts user for word
        if (mIsHuman) {
            promptHuman();
        }
        // Sets word
        else {
            mHumanPromptLayout.setVisibility(View.INVISIBLE);
            pickWord();
        }
    }
    public void updateScoreText() {
        mScoreText.setText("Wins: " + mWins + "     Losses: " + mLosses);
    }
    public void promptHuman() {
        hideGame(true);
        mHumanPromptLayout.setVisibility(View.VISIBLE);
    }

    public void hideGame(boolean hideGame) {
        if (hideGame) {
            mGameLayout.setVisibility(View.INVISIBLE);
        }
        else {
            mGameLayout.setVisibility(View.VISIBLE);
        }
    }

    public void loadWords() {
        String line;
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(getActivity().getAssets().open("words.txt")));

            while((line = bufferedReader.readLine()) != null) {
                if (line.length() >= 4 && line.length() <= 6) {
                    mEasyWords.add(line);
                }
                else if (line.length() < 8) {
                    mMediumWords.add(line);
                }
                else {
                    mHardWords.add(line);
                }
            }

            bufferedReader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void updateGuesses() {
        mGuessesText.setText(Integer.toString(mGuesses));
    }

    public void setWord() {
        mDisplayWord = "";
        for (int i = 0; i < mWord.length(); i++) {
            mDisplayWord = mDisplayWord + "+";
        }
        mWordTextView.setText(mDisplayWord);
    }

    public void pickWord() {
        Random rand = new Random();
        int num;
        switch (mDifficulty) {
            case EASY:
                num = rand.nextInt(mEasyWords.size());
                mWord = mEasyWords.get(num);
                break;
            case MEDIUM:
                num = rand.nextInt(mMediumWords.size());
                mWord = mMediumWords.get(num);
                break;
            case HARD:
                num = rand.nextInt(mHardWords.size());
                mWord = mHardWords.get(num);
                break;
        }
        setWord();
    }

    public void letterPressed(char letter) {
        checkLetterInWord(letter);

        boolean didWin = checkWin();


        if (didWin) {
            mWins = mWins + 1;
            mWinLossDisplay.setText("You Won!!");
            mPostGameLayout.setVisibility(View.VISIBLE);
            mGameLayout.setVisibility(View.INVISIBLE);
        }

        if (mGuesses <= 0) {
            mLosses = mLosses + 1;
            mWinLossDisplay.setText("You Lost!!");
            mPostGameLayout.setVisibility(View.VISIBLE);
            mGameLayout.setVisibility(View.INVISIBLE);
            mLostWordDisplay.setText("The word was: " + mWord);
        }

        updateUI();
    }

    public void checkLetterInWord(char letter) {
        boolean foundLetter = false;
        for (int i = 0; i < mWord.length(); i++) {
            if (mWord.charAt(i) == letter) {
                StringBuilder newDisplay = new StringBuilder(mDisplayWord);
                newDisplay.setCharAt(i,letter);
                mDisplayWord = newDisplay.toString();
                foundLetter = true;
            }
        }

        if(!foundLetter) {
            mGuesses = mGuesses - 1;
        }
    }

    public boolean checkWin() {
        if (mDisplayWord.equalsIgnoreCase(mWord)) {
            return true;
        }
        return false;
    }

    public void updateUI() {
        mScoreText.setText("Wins: " + mWins + "   Losses: " +mLosses);
        mGuessesText.setText(Integer.toString(mGuesses));
        mWordTextView.setText(mDisplayWord);
    }

    public void resetGame() {
        mGuesses = 7;
        if (mIsHuman) {
            promptHuman();
        }
        else {
            pickWord();
            mGameLayout.setVisibility(View.VISIBLE);
        }
        for (int i = 0; i < mLetterButtons.length; i++) {
            mLetterButtons[i].setVisibility(View.VISIBLE);
        }
        Arrays.fill(mLetterClicked, false);
        updateUI();
    }

    public static int returnWins(Intent result) {
        return  result.getIntExtra(ARGUMENT_WINS, 0);
    }

    public static int returnLosses(Intent result) {
        return  result.getIntExtra(ARGUMENT_LOSSES, 0);
    }
}
