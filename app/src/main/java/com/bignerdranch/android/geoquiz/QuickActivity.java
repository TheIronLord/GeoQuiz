package com.bignerdranch.android.geoquiz;

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
    private ImageButton mNextButton;
    private ImageButton mPrevButton;
    private TextView mTextView;
    private TextView mQuestionTextView;
    private static final String TAG = "QuickActivity";

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
    private void updateQuestion(){
        int question = mQuestionBank[mCurrentIndex].getTextResId();
        mQuestionTextView.setText(question);
    }

    private void checkAnswer(Boolean userPressedTrue){
        Boolean answerIsTrue = mQuestionBank[mCurrentIndex].isAnswerTrue();
        int messageResId;

        if(userPressedTrue == answerIsTrue){
            messageResId = R.string.correct_toast;
        }else{
            messageResId = R.string.incorrect_toast;
        }
        Toast.makeText(QuickActivity.this, messageResId, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "OnCreate(Bundle) Called");
        setContentView(R.layout.activity_quick);

        mQuestionTextView = (TextView) findViewById(R.id.question_text_view);
        mTrueButton = (Button) findViewById(R.id.true_button);
        mFalseButton = (Button) findViewById(R.id.false_button);
        mNextButton = (ImageButton) findViewById(R.id.next_button);
        mPrevButton = (ImageButton) findViewById(R.id.prev_button);
        mTextView = (TextView) findViewById(R.id.question_text_view);

        mTextView.setOnClickListener(new View.OnClickListener(){
           @Override
            public void onClick(View v){
               mCurrentIndex = (mCurrentIndex + 1) % mQuestionBank.length;
               updateQuestion();
           }
        });

        mTrueButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                checkAnswer(true);
            }
        });

        mFalseButton.setOnClickListener(new View.OnClickListener() {
           @Override
            public void onClick(View v){
               checkAnswer(false);
           }
        });

        mNextButton.setOnClickListener(new View.OnClickListener(){
           @Override
           public void onClick(View v){
               mCurrentIndex = (mCurrentIndex + 1) % mQuestionBank.length;
               updateQuestion();
           }
        });

        mPrevButton.setOnClickListener(new View.OnClickListener(){
           @Override
            public void onClick(View v){
               if(mCurrentIndex == 0){
                   mCurrentIndex = mQuestionBank.length - 1;
               }else{
                   mCurrentIndex--;
               }
               updateQuestion();
           }
        });
        updateQuestion();
    } //OnCreate

    @Override
    public void onStart(){
        super.onStart();
        Log.d(TAG, "onStart() Called");
    }//onStart()

    @Override
    public void onPause(){
        super.onPause();
        Log.d(TAG, "onPause() Called");
    }//onPause

    @Override
    public void onStop(){
        super.onStop();
        Log.d(TAG, "onStop() Called");
    }//onStop()

    @Override
    public void onDestroy(){
        super.onDestroy();
        Log.d(TAG, "onDestroy() Called");
    }//onDestroy()

    @Override
    public void onResume(){
        super.onResume();
        Log.d(TAG, "onResume() Called");
    }
}//QuickActivity
