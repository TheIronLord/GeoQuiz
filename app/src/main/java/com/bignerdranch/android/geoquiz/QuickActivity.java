package com.bignerdranch.android.geoquiz;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class QuickActivity extends AppCompatActivity {
    private Button mTrueButton;
    private Button mFalseButton;
    private Button mCheatButton;

    private ImageButton mNextButton;
    private ImageButton mPrevButton;

    private TextView mTextView;
    private TextView mQuestionTextView;

    private static final String TAG = "QuickActivity";
    private static final String KEY_INDEX = "index";

    private float mRightAnswers = 0;
    private int[] mAnsweredQuestions = new int[6];
    private int mAnsweredQuestionsIndex = 0;
    private float mQuestionSize = 6;

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

        if(userPressedTrue == answerIsTrue){
            messageResId = R.string.correct_toast;
            mRightAnswers++;
            Log.d(TAG, "mRightAnswers: " + mRightAnswers);
        }else{
            messageResId = R.string.incorrect_toast;
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
    // to see if the buttons should be enabled of disableds
    private void nextPrevUnlockAndLockButtons(){
        if(!answeredQuestion()) {
            unlockButtons();
        }else {
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

        //Retrieves the savedInstanceState if there is information in it
        if(savedInstanceState != null) {
            mCurrentIndex = savedInstanceState.getInt(KEY_INDEX);
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
        //WHen mFalseButton is pressed it will call lockAnswer and checkAnswer
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
                startActivity(intent);
            }
        });
        updateQuestion();
    } //OnCreate

    /*Saves the mCurrentIndex into a savedInstanceState of type
    * Bundle to use again in onCreate(Bundle)*/
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState){
        super.onSaveInstanceState(savedInstanceState);
        Log.d(TAG, "onSaveInstanceState(Bundle savedInstanceState) Called");
        savedInstanceState.putInt(KEY_INDEX, mCurrentIndex);
    }//onSaveInstanceState(Bundle savedInstanceState)
}//QuickActivity
