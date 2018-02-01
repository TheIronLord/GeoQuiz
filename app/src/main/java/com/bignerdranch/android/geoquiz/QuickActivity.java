package com.bignerdranch.android.geoquiz;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class QuickActivity extends AppCompatActivity {
    private Button              mTrueButton;
    private Button              mFalseButton;
    private Button              mCheatButton;

    private ImageButton         mNextButton;
    private ImageButton         mPrevButton;

    private TextView            mTextView;
    private TextView            mQuestionTextView;

    private static final String KEY_INDEX = "index";
    private static final String M_CHEATED_KEY = "mCheated";
    private static final String M_ANSWERED_QUESTIONS_KEY = "mAnsweredQuestions";
    private static final String M_ANSWERED_QUESTIONS_INDEX_KEY = "mAnsweredQuestionsIndex";
    private static final int    REQUEST_CODE_CHEAT = 0;

    private int[]               mAnsweredQuestions = new int[6];
    private boolean[]           mCheated = new boolean[6];

    private int                 mAnsweredQuestionsIndex = 0;
    private float               mQuestionSize = 6;
    private float               mRightAnswers = 0;

    /*Questions for True and False*/
    Question[] mQuestionBank = new Question[]{
            new Question(R.string.question_australia, true),
            new Question(R.string.question_ocean, true),
            new Question(R.string.question_mideast, false),
            new Question(R.string.question_africa, false),
            new Question(R.string.question_americas, true),
            new Question(R.string.question_asia, true)
    };

    //Keeps track of the current index of the mQuestionBank array
    private int mCurrentIndex = 0;

    //Get the next question from mQuestionBank and set it to the
    //mQuestionTextView
    private void updateQuestion() {
        int question = mQuestionBank[mCurrentIndex].getTextResId();
        mQuestionTextView.setText(question);
    }

    //Check if the answer is True or False and output it on the Toast
    private void checkAnswer(Boolean userPressedTrue){
        Boolean answerIsTrue = mQuestionBank[mCurrentIndex].isAnswerTrue();
        int messageResId;
        if(mCheated[mCurrentIndex]){
            messageResId = R.string.judgement_toast;
        }else{
            if(userPressedTrue == answerIsTrue){
                messageResId = R.string.correct_toast;
                mRightAnswers++;
            }else{
                messageResId = R.string.incorrect_toast;
            }
        }
        Toast.makeText(QuickActivity.this, messageResId, Toast.LENGTH_SHORT).show();
    }

    //Get the percentage and output it on the Toast
    public void quizScore(){
        float percentage = mRightAnswers / mQuestionSize;
        percentage *= 100;
        int convert = (int)percentage;
        String percentageOutput = Integer.toString(convert);
        percentageOutput += "%";
        Toast.makeText(QuickActivity.this, percentageOutput, Toast.LENGTH_SHORT).show();
    }

    //Check if the question was answered or not
    private boolean answeredQuestion(){
        int qBankIndex = mCurrentIndex;
        for(int i = 0; i <= mAnsweredQuestionsIndex; i++){
            if(mAnsweredQuestions[i] == qBankIndex){
                return true;
            }
        }
        return false;
    }

    //Locks the True/False Buttons and adds mCurrentIndex to the array of
    //mAnswerQuestions and also increments mAnswerQuestionIndex
    private void lockAnswer(){
        mAnsweredQuestions[mAnsweredQuestionsIndex++] = mCurrentIndex;

        mTrueButton.setEnabled(false);
        mFalseButton.setEnabled(false);
    }

    private void unlockButtons(){
        mTrueButton.setEnabled(true);
        mFalseButton.setEnabled(true);
    }

    //Check if the question was answered of not
    // to see if the buttons should be enabled of disables
    private void nextPrevUnlockAndLockButtons(){
        if(!answeredQuestion()) {
            unlockButtons();
        }else {
            mTrueButton.setEnabled(false);
            mFalseButton.setEnabled(false);
        }
    }

    private void isQuestionAnswered(){
        //Check if the question was answered
        boolean answered;
        answered = answeredQuestion();
        if(answered){
            mTrueButton.setEnabled(false);
            mFalseButton.setEnabled(false);
        }
    }

    ////////////////////////////////////////////////////////////////
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quick);

        //Retrieve all of the elements from the view
        mQuestionTextView = (TextView) findViewById(R.id.question_text_view);
        mTrueButton = (Button) findViewById(R.id.true_button);
        mFalseButton = (Button) findViewById(R.id.false_button);
        mNextButton = (ImageButton) findViewById(R.id.next_button);
        mPrevButton = (ImageButton) findViewById(R.id.prev_button);
        mCheatButton = (Button) findViewById(R.id.cheat_button);
        mTextView = (TextView) findViewById(R.id.question_text_view);
        mAnsweredQuestions[0] = -1;

        //Retrieves the savedInstanceState if there is information in it
        if(savedInstanceState != null) {
            mCurrentIndex = savedInstanceState.getInt(KEY_INDEX);

            mCheated = savedInstanceState.getBooleanArray(M_CHEATED_KEY);

            mAnsweredQuestions = savedInstanceState.getIntArray(M_ANSWERED_QUESTIONS_KEY);
            mAnsweredQuestionsIndex = savedInstanceState.getInt(M_ANSWERED_QUESTIONS_INDEX_KEY);
        }

        /*Displays the text on the screen and also has the ability to go the
        * the next question if it is pressed*/
        mTextView.setOnClickListener(new View.OnClickListener(){
           @Override
            public void onClick(View v){
               mCurrentIndex = (mCurrentIndex + 1) % mQuestionBank.length;
               updateQuestion();
           }
        });
        //WHen mTrueButton is pressed it will call lockAnswer and checkAnswer
        mTrueButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                checkAnswer(true);
                lockAnswer();
            }
        });
        //When mFalseButton is pressed it will call lockAnswer and checkAnswer
        mFalseButton.setOnClickListener(new View.OnClickListener() {
           @Override
            public void onClick(View v){
               checkAnswer(false);
               lockAnswer();
           }
        });
        /*When mNextButton is press it will go to the next question*/
        mNextButton.setOnClickListener(new View.OnClickListener(){
           @Override
           public void onClick(View v){
               mCurrentIndex = (mCurrentIndex + 1) % mQuestionBank.length;
               updateQuestion();
               nextPrevUnlockAndLockButtons();

               if(mAnsweredQuestionsIndex == mQuestionSize){
                   quizScore();
               }
           }
        });
        /*When mPrevButton is pressed it will go back to the previous question*/
        mPrevButton.setOnClickListener(new View.OnClickListener(){
           @Override
            public void onClick(View v){
               if(mCurrentIndex != 0){
                   mCurrentIndex--;
               }
               updateQuestion();
               nextPrevUnlockAndLockButtons();
           }
        });

        mCheatButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                //Start Cheat Activity
                boolean answerIsTrue = mQuestionBank[mCurrentIndex].isAnswerTrue();
                Intent intent = CheatActivity.newIntent(QuickActivity.this, answerIsTrue);
                startActivityForResult(intent, REQUEST_CODE_CHEAT);
            }
        });
        updateQuestion();
        isQuestionAnswered();
    } //OnCreate

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if(resultCode != Activity.RESULT_OK){
            return;
        }else if(requestCode == REQUEST_CODE_CHEAT){
            if(data == null){
                return;
            }else{
                mCheated[mCurrentIndex] = CheatActivity.wasAnswerShown(data);
            }
        }
    }

    /* Saves the values into a savedInstanceState
     * of typeBundle to use again in onCreate(Bundle)*/
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState){
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putInt(KEY_INDEX, mCurrentIndex);
        savedInstanceState.putBooleanArray(M_CHEATED_KEY, mCheated);
        savedInstanceState.putIntArray(M_ANSWERED_QUESTIONS_KEY, mAnsweredQuestions);
        savedInstanceState.putInt(M_ANSWERED_QUESTIONS_INDEX_KEY, mAnsweredQuestionsIndex);
    }//onSaveInstanceState(Bundle savedInstanceState)
}//QuickActivity
