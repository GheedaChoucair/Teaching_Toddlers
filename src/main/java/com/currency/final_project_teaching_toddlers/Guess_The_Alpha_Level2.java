package com.currency.final_project_teaching_toddlers;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class Guess_The_Alpha_Level2 extends AppCompatActivity {

    TextView btn1;
    TextView btn2;
    TextView Score, WinFail, QuestionText;
    Button restart;
    int nbrOfQuestions = 0;
    int count =0;
    int score = 0;
    boolean clickable = true;
    int locationCorrect;
    ArrayList<String> questions = new ArrayList<>();
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guess_the_alpha_level2);
        btn1 = (TextView) findViewById(R.id.btn1);
        btn2 = (TextView) findViewById(R.id.btn2);
        Score = (TextView) findViewById(R.id.score);
        WinFail = (TextView) findViewById(R.id.WinFail);
        WinFail.setVisibility(View.INVISIBLE);
        restart = (Button) findViewById(R.id.restart);
        restart.setVisibility(View.INVISIBLE);
        QuestionText = (TextView) findViewById(R.id.QuestionText);
        questions = (ArrayList<String>) getIntent().getSerializableExtra("QuestionListExtra");
        for (int i = 0; i< questions.size(); i++)
        {
            Log.i("QUESTIONNNNNN", questions.get(i));
        }
        downloadQuestion(btn1);
        Collections.shuffle(questions);

    }

    public void downloadQuestion(View view){

        Random rand = new Random();
        locationCorrect = rand.nextInt(2);
        Log.i("CORRECTTT", String.valueOf(locationCorrect));
        try{
            for (int i = 0; i < 2 ; i++){
                if (locationCorrect == i) {
                    Log.i("CORRECTTT", String.valueOf(locationCorrect));
                    if(i == 0)

                        btn1.setText(String.valueOf(questions.get(count)));
                    else
                    if (i == 1)
                        btn2.setText(String.valueOf(questions.get(count)));
                    Log.i("COUNT", String.valueOf(count));
                }
                else
                {
                    int randomlocationAns = rand.nextInt(20);
                    while(randomlocationAns == count) randomlocationAns = rand.nextInt(20);

                    if (i == 0)
                        btn1.setText(String.valueOf(questions.get(randomlocationAns)));
                    else
                    if (i == 1)
                        btn2.setText(String.valueOf(questions.get(randomlocationAns)));
                }
            }
            QuestionText.setText(String.valueOf(shuffle(questions.get(count).toUpperCase(Locale.ROOT))));
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    public static String shuffle(String string)
    {
        List<String> letters = Arrays.asList(string.split(""));
        Collections.shuffle(letters);
        String shuffled = "";
        for (String letter : letters) {
            shuffled += " " + letter;
        }
        return shuffled;
    }
    public void ClickBtn(View view){
        if(clickable == true) {
            String tag = view.getTag().toString();
            Log.i("TAGGGGG", tag);

            if (Integer.toString(locationCorrect).equalsIgnoreCase(tag)) {
                if (btn1.getTag().toString().equals(tag))
                    new CountDownTimer(500, 500) {
                        @Override
                        public void onTick(long l) {
                            btn1.setBackground(getResources().getDrawable(R.drawable.mybuttoncorrect));
                        }

                        @Override
                        public void onFinish() {
                            IncreaseScore();
                            btn1.setBackground(getResources().getDrawable(R.drawable.mybutton2));
                        }
                    }.start();

                else
                    new CountDownTimer(500, 500) {
                        @Override
                        public void onTick(long l) {
                            btn2.setBackground(getResources().getDrawable(R.drawable.mybuttoncorrect));
                        }

                        @Override
                        public void onFinish() {
                            IncreaseScore();
                            btn2.setBackground(getResources().getDrawable(R.drawable.mybutton2));

                        }
                    }.start();
                //Toast.makeText(this, "CORRECT!", Toast.LENGTH_SHORT).show();

                Log.i("SCOREEEE", String.valueOf(score));
            } else {
                if (btn1.getTag().toString().equalsIgnoreCase(tag))
                    new CountDownTimer(500, 500) {
                        @Override
                        public void onTick(long l) {
                            btn1.setBackground(getResources().getDrawable(R.drawable.mybuttonfalse));
                        }

                        @Override
                        public void onFinish() {
                            btn1.setBackground(getResources().getDrawable(R.drawable.mybutton2));
                        }
                    }.start();
                else
                    new CountDownTimer(500, 500) {
                        @Override
                        public void onTick(long l) {
                            btn2.setBackground(getResources().getDrawable(R.drawable.mybuttonfalse));
                        }

                        @Override
                        public void onFinish() {
                            btn2.setBackground(getResources().getDrawable(R.drawable.mybutton2));
                        }
                    }.start();
                //Toast.makeText(this, "INCORRECT!", Toast.LENGTH_SHORT).show();
            }
            try {
                new CountDownTimer(500, 500) {
                    @Override
                    public void onTick(long l) {
                    }

                    @Override
                    public void onFinish() {
                        count++;
                        downloadQuestion(btn1);
                        nbrOfQuestions++;
                        checkState();
                    }
                }.start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    public void IncreaseScore()
    {
        score++;
        Score.setText(String.valueOf(score));
        if (score>=10 && nbrOfQuestions < 20)
        {
            QuestionText.setVisibility(View.INVISIBLE);
            WinFail.setText("YOU WON!");
            WinFail.setVisibility(View.VISIBLE);
            restart.setVisibility(View.VISIBLE);
            clickable = false;
        }
    }
    public void checkState()
    {
        if (score< 10 && nbrOfQuestions >= 20)
        {
            QuestionText.setVisibility(View.INVISIBLE);
            WinFail.setText("YOU LOOSE!");
            WinFail.setVisibility(View.VISIBLE);
            restart.setVisibility(View.VISIBLE);
            clickable = false;
        }
    }
    public void Restart(View view)
    {
        score = 0;
        count = 0;
        Score.setText(String.valueOf(score));
        clickable = true;
        nbrOfQuestions = 0;
        WinFail.setVisibility(View.INVISIBLE);
        restart.setVisibility(View.INVISIBLE);
        QuestionText.setVisibility(View.VISIBLE);
        Collections.shuffle(questions);
        downloadQuestion(btn1);

    }
}
