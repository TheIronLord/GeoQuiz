package com.bignerdranch.android.geoquiz;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class CheatActivity extends AppCompatActivity {
    private static final String EXTRA_ANSWER_IS_TRUE = "com.bignerdranch.geoquiz.answer_is_true";
    private static final String EXTRA_ANSWER_SHOWN = "com.bignerdranch.geoquiz.answer_shown";

    private boolean mAnswerIsTrue;

    private Button mShowAnswerButton;
    private TextView mAnswerTextView;


    /*Static methods*/
    /*newIntent is called in QuickActivity to create the Intent,
    * put the answer in it and return it*/
    public static Intent newIntent(Context packageContent, boolean answerIsTrue){
        Intent intent = new Intent(packageContent, CheatActivity.class);
        intent.putExtra(EXTRA_ANSWER_IS_TRUE, answerIsTrue);
        return intent;
    }
    /*Lets QuickActivity know if the answer was shown*/
    public static boolean wasAnswerShown(Intent result){
        return result.getBooleanExtra(EXTRA_ANSWER_SHOWN, false);
    }

    /*Private Method*/
    private void setAnswerShownResult(boolean isAnswerShown){
        //Set result
        Intent data = new Intent();
        data.putExtra(EXTRA_ANSWER_SHOWN, isAnswerShown);
        setResult(RESULT_OK, data);
    }

    /*Override Methods*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cheat);

        //Retrieve the Intent answer for the key EXTRA_ANSWER_IS_TRUE
        mAnswerIsTrue = getIntent().getBooleanExtra(EXTRA_ANSWER_IS_TRUE, false);

        //Retrieve Widgets
        mShowAnswerButton = (Button) findViewById(R.id.show_answer_button);
        mAnswerTextView = (TextView) findViewById(R.id.answer_text_view);

        //When mShowAnswerButton is pressed it will show the answer if it is
        //True or False
        mShowAnswerButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if(mAnswerIsTrue){
                    mAnswerTextView.setText(R.string.true_button);
                }else{
                    mAnswerTextView.setText(R.string.false_button);
                }
                setAnswerShownResult(true);
            }
        });
    }//onCreate()

}
