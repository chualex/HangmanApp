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
import android.widget.LinearLayout;
import android.widget.TextView;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;


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

    /**
     * Gets called on creation of the class. Takes in the passed arguments
     *
     * @param savedInstanceState Bundle for the last saved state.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // gets the arguments from the intent passed
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

        // if loading from saved state gets arguments from the last saved state
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

    /**
     * Creates the layout and all button and button listeners
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
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
        // clicked after the user inputs a word for the second user to guess
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
        // if the letters are pressed calls letterPressed()
        char letter = 'a';

        // for loop to set all the letters
        for (int i = 0; i < mLetterButtons.length; i++) {
            final int I = i;
            final String buttonId = letter + "_button";

            // gets teh id of the button
            int id = getResources().getIdentifier(buttonId, "id", getActivity().getPackageName());
            // sets up the button in the button array
            mLetterButtons[I] = (Button) view.findViewById(id);
            if (mLetterClicked[i] == true) {
                mLetterButtons[I].setVisibility(View.INVISIBLE);
            }
            final char finalLetter = letter;

            // button listener for each letter button
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
        // pressed after win or loss to reset the game
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




        // set up quit button
        // sends the user back to the Welcome Fragment
        mQuitButton = (Button) view.findViewById(R.id.quit_game_button);
        mQuitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setReturnResult();
                getActivity().finish();
            }
        });

        //set up lost word display
        // displays the word when the user has lost
        mLostWordDisplay = (TextView) view.findViewById(R.id.display_word);


        //Initializes game based on computer or human player
        if (savedInstanceState == null) {
            Initialize();
        }

        // updates the layout
        updateUI();

        return view;
    }

    /**
     * sets the arguments of the last state when the activity is paused.
     * @param outState Bundle for the saved state
     */
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

    /**
     * Creates ne instance of this fragment with appropriate arguments
     * @param isHuman opponent setting
     * @param difficulty difficulty setting
     * @param wins number of wins
     * @param losses number of losses
     * @return the new fragment with the arguments
     */
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

    /**
     * sets the return results when being sent back to the Welcome Fragment
     */
    public void setReturnResult() {
        Intent resultIntent = new Intent();
        resultIntent.putExtra(ARGUMENT_WINS, mWins);
        resultIntent.putExtra(ARGUMENT_LOSSES, mLosses);
        getActivity().setResult(Activity.RESULT_OK, resultIntent);
    }

    /**
     * Initializes the game based on whether the opponent is human or computer player
     */
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

        mLostWordDisplay.setText(" ");
    }

    /**
     * updates the score text field with the wins and losses
     */
    public void updateScoreText() {
        mScoreText.setText("Wins: " + mWins + "     Losses: " + mLosses);
    }

    /**
     * Sets up the prompt human layout. Asks for the word
     */
    public void promptHuman() {
        hideGame(true);
        mHumanPromptLayout.setVisibility(View.VISIBLE);
    }

    /**
     * Hides or shows the game board the game board. Called before and after the game.
     * @param hideGame boolean for whether to show or not show the game board
     */
    public void hideGame(boolean hideGame) {
        if (hideGame) {
            mGameLayout.setVisibility(View.INVISIBLE);
        }
        else {
            mGameLayout.setVisibility(View.VISIBLE);
        }
    }

    /**
     * loads the words from the text file
     */
    public void loadWords() {
        String line;
        try {
            // gets all the words in the text file
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(getActivity().getAssets().open("words.txt")));

            // while the text file can read it reads the word into the respective array of words
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

    /**
     * Updates the number of guesses to the layout
     */
    public void updateGuesses() {
        mGuessesText.setText(Integer.toString(mGuesses));
    }

    /**
     * sets the display word initially to all pus signs
     */
    public void setWord() {
        mDisplayWord = "";
        for (int i = 0; i < mWord.length(); i++) {
            mDisplayWord = mDisplayWord + "+";
        }
        mWordTextView.setText(mDisplayWord);
    }

    /**
     * piscks a word for the user to guess from the array lists of words based on the difficulty
     */
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

    /**
     * updates the layout and decides whether the user won or lost on the move
     * @param letter the letter pressed
     */
    public void letterPressed(char letter) {
        // checks if the letter is in the word
        checkLetterInWord(letter);

        // checks if the user won
        boolean didWin = checkWin();

        // displays the winning layout
        if (didWin) {
            mWins = mWins + 1;
            mWinLossDisplay.setText("You Won!!");
            mPostGameLayout.setVisibility(View.VISIBLE);
            mGameLayout.setVisibility(View.INVISIBLE);
        }

        // displays the losing layout
        if (mGuesses <= 0) {
            mLosses = mLosses + 1;
            mWinLossDisplay.setText("You Lost!!");
            mPostGameLayout.setVisibility(View.VISIBLE);
            mGameLayout.setVisibility(View.INVISIBLE);
            mLostWordDisplay.setText("The word was: " + mWord);
        }

        // updates the layout
        updateUI();
    }

    /**
     * Checks if the letter is in the word
     * @param letter
     */
    public void checkLetterInWord(char letter) {
        boolean foundLetter = false;
        // iterates through the word to find the letter
        for (int i = 0; i < mWord.length(); i++) {
            if (mWord.charAt(i) == letter) {
                StringBuilder newDisplay = new StringBuilder(mDisplayWord);
                newDisplay.setCharAt(i,letter);
                mDisplayWord = newDisplay.toString();
                foundLetter = true;
            }
        }

        // if the letter is not in the word decrement the guesses
        if(!foundLetter) {
            mGuesses = mGuesses - 1;
        }
    }

    /**
     * Checks if the display word is equivelent to the word. If they are the same the user has won
     * @return if the user won
     */
    public boolean checkWin() {
        if (mDisplayWord.equalsIgnoreCase(mWord)) {
            return true;
        }
        return false;
    }

    /**
     * updates the layout with the current number of wins and losses, guesses, and display word
     */
    public void updateUI() {
        mScoreText.setText("Wins: " + mWins + "   Losses: " +mLosses);
        mGuessesText.setText(Integer.toString(mGuesses));
        mWordTextView.setText(mDisplayWord);
    }

    /**
     * resets the game based on the opponent asks for necessary inputs
     */
    public void resetGame() {
        mGuesses = 7;
        //prompts user for word
        if (mIsHuman) {
            promptHuman();
        }
        else {
            pickWord();
            mGameLayout.setVisibility(View.VISIBLE);
        }

        // sets all letter buttons visible
        for (int i = 0; i < mLetterButtons.length; i++) {
            mLetterButtons[i].setVisibility(View.VISIBLE);
        }
        // sets all buttons to not clicked
        Arrays.fill(mLetterClicked, false);

        mLostWordDisplay.setText("");

        // updates teh layout
        updateUI();
    }

    /**
     * returns teh number of wins the welcome fragment
     * @param result input intent
     * @return the number of wins
     */
    public static int returnWins(Intent result) {
        return  result.getIntExtra(ARGUMENT_WINS, 0);
    }
    /**
     * returns the number of losses the welcome fragment
     * @param result input intent
     * @return the number of losses
     */
    public static int returnLosses(Intent result) {
        return  result.getIntExtra(ARGUMENT_LOSSES, 0);
    }
}
