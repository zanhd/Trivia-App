package com.zanhd.trivia;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.zanhd.trivia.data.QuestionBank;
import com.zanhd.trivia.data.questionListAsyncResponce;
import com.zanhd.trivia.model.Question;
import com.zanhd.trivia.model.Score;
import com.zanhd.trivia.util.Prefs;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView questionTextview;
    private TextView questionCounterTextView;
    private Button trueButton;
    private Button falseButton;
    private ImageButton nextButton;
    private ImageButton prevButton;
    private int currQuesIndex = 0;
    private List<Question> questionList;

    private TextView scoreTextView;
    private TextView hightScoreTextview;
    private int scoreCounter = 0;
    private Score score;
    private Prefs prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        score = new Score();
        prefs  = new Prefs(MainActivity.this);

        nextButton = findViewById(R.id.next_button);
        prevButton = findViewById(R.id.prev_button);
        trueButton = findViewById(R.id.true_button);
        falseButton = findViewById(R.id.false_button);
        questionTextview = findViewById(R.id.question_textview);
        questionCounterTextView = findViewById(R.id.counter_text);
        scoreTextView = findViewById(R.id.score_Textview);
        hightScoreTextview = findViewById(R.id.hightest_score_TextView);

        nextButton.setOnClickListener(this);
        prevButton.setOnClickListener(this);
        trueButton.setOnClickListener(this);
        falseButton.setOnClickListener(this);

        hightScoreTextview.setText("Highest Score : " + prefs.getHighScore());
        //get previous State of question(index)
        currQuesIndex = prefs.getState();

        questionList = new QuestionBank().getQuestions(new questionListAsyncResponce() {
            @Override
            public void processFinished(ArrayList<Question> questionArrayList) {

                questionTextview.setText(questionArrayList.get(currQuesIndex).getQuestion());
                questionCounterTextView.setText((currQuesIndex+1) + " / " + questionList.size());
//                Log.d("Inside", "processFinished: "  + questionArrayList.get(0).getQuestion());
//                Log.d("Inside", "processFinished: " + questionArrayList.get(0).getAnswer());
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.prev_button  :
                currQuesIndex = (currQuesIndex>0) ? currQuesIndex-1 : questionList.size() - 1;
                UpdateQuestion();
                break;
            case R.id.next_button:
                goNext();
                break;
            case R.id.true_button:
                check_answer(true);
                UpdateQuestion();
                break;
            case R.id.false_button:
                check_answer(false);
                UpdateQuestion();
                break;
        }
    }

    @SuppressLint("SetTextI18n")
    private void UpdateQuestion(){
        questionTextview.setText(questionList.get(currQuesIndex).getQuestion());
        questionCounterTextView.setText((currQuesIndex+1) + " / " + questionList.size());
    }

    @SuppressLint("SetTextI18n")
    private void check_answer(boolean ans){
        boolean correct_ans = questionList.get(currQuesIndex).getAnswer();

        int toastMessageId;
        if(ans == correct_ans){
            toastMessageId = R.string.correct_answer;
            fadeView();
            addPoints();
        }else{
            toastMessageId = R.string.wrong_answer;
            shakeAnimation();
            deductPoints();
        }
        scoreTextView.setText("Score : " + score.getScore());
        Toast.makeText(this, toastMessageId, Toast.LENGTH_SHORT).show();
    }

    private void addPoints(){
        scoreCounter++;
        score.setScore(scoreCounter);
    }

    private void deductPoints(){
        scoreCounter = (scoreCounter>0)?scoreCounter-1:scoreCounter;
        score.setScore(scoreCounter);
    }

    void shakeAnimation(){
        Animation shake = AnimationUtils.loadAnimation(MainActivity.this,R.anim.shake_animation);
        final CardView cardView = findViewById(R.id.cardView);
        cardView.setAnimation(shake);

        shake.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                cardView.setCardBackgroundColor(Color.RED);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                cardView.setCardBackgroundColor(Color.WHITE);
                goNext();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    void fadeView(){
        final CardView cardView = findViewById(R.id.cardView);
        AlphaAnimation alphaAnimation = new AlphaAnimation(1.0f,0.0f);

        alphaAnimation.setDuration(150);
        alphaAnimation.setRepeatCount(1);
        alphaAnimation.setRepeatMode(Animation.REVERSE);

        cardView.setAnimation(alphaAnimation);

        alphaAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                cardView.setCardBackgroundColor(Color.GREEN);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                cardView.setCardBackgroundColor(Color.WHITE);
                goNext();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    private void goNext(){
        currQuesIndex = (currQuesIndex+1)%questionList.size();
        UpdateQuestion();
    }

    @Override
    protected void onPause() {
        prefs.saveHighScore(score.getScore());
        prefs.setState(currQuesIndex);
        super.onPause();
    }
}