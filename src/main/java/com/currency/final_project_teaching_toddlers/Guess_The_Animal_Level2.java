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

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Guess_The_Animal_Level2 extends AppCompatActivity {
    ArrayList<levelchoose.Question_Solution> questions = new ArrayList<>();
    ImageDownloader task;
    ImageView imgView;
    TextView Score, WinFail;
    TextView btn1, btn2;
    Button restart;
    int nbrOfQuestions = 0;
    int count =0;
    int score = 0;
    boolean clickable = true;
    int locationCorrect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guess_the_animal_level2);
        btn1 = (TextView) findViewById(R.id.btn1);
        btn2 = (TextView) findViewById(R.id.btn2);
        imgView = (ImageView) findViewById(R.id.imageView);
        Score = (TextView) findViewById(R.id.score);
        WinFail = (TextView) findViewById(R.id.WinFail);
        WinFail.setVisibility(View.INVISIBLE);
        restart = (Button) findViewById(R.id.restart);
        restart.setVisibility(View.INVISIBLE);
        questions = (ArrayList<levelchoose.Question_Solution>) getIntent().getSerializableExtra("QuestionListExtra");
        Log.i("SIZEEEE", String.valueOf(questions.size()));
        for (int i = 0; i< questions.size(); i++)
        {
            Log.i("QUESTIONNNNNN", questions.get(i).getImage_Name());
        }
        Collections.shuffle(questions);
        downloadImage(btn1);
    }
    public void downloadImage(View view){
        Random rand = new Random();
        locationCorrect = rand.nextInt(2);
        Log.i("CORRECTTT", String.valueOf(locationCorrect));
        try{
            for (int i = 0; i < 2 ; i++){
                if (locationCorrect == i) {
                    Log.i("CORRECTTT", String.valueOf(locationCorrect));
                    if(i == 0)

                        btn1.setText(String.valueOf(questions.get(count).getImage_Name()));

                    else
                    if (i == 1)
                        btn2.setText(String.valueOf(questions.get(count).getImage_Name()));
                    Log.i("COUNT", String.valueOf(count));
                }
                else
                {
                    int randomlocationAns = rand.nextInt(20);
                    while(randomlocationAns == count) randomlocationAns = rand.nextInt(20);

                    if (i == 0)
                        btn1.setText(String.valueOf(questions.get(randomlocationAns).getImage_Name()));
                    else
                    if (i == 1)
                        btn2.setText(String.valueOf(questions.get(randomlocationAns).getImage_Name()));
                }
            }

            task = new ImageDownloader();
            Bitmap downloadedImg;
            downloadedImg = task.execute(questions.get(count).getImage_Url()).get();
            imgView.setImageBitmap(downloadedImg);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    public class ImageDownloader extends AsyncTask<String, Void, Bitmap> {

        protected Bitmap doInBackground(String... urls) {

            try {
                URL url = new URL(urls[0]);

                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                InputStream in = connection.getInputStream();
                Bitmap downloadedImage = BitmapFactory.decodeStream(in);
                return downloadedImage;

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
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
                        downloadImage(imgView);
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
                imgView.setVisibility(View.INVISIBLE);
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
                imgView.setVisibility(View.INVISIBLE);
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
            imgView.setVisibility(View.VISIBLE);
            downloadImage(btn1);
            Collections.shuffle(questions);
        }
}