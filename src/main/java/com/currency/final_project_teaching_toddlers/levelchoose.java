package com.currency.final_project_teaching_toddlers;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Serializable;
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

public class levelchoose extends AppCompatActivity {
    Button alpha;
    Button animal;
    TextView user;

    boolean clickable = true;
    ArrayList<Question_Solution> array = new ArrayList<>();
    ArrayList<Question_Solution> array_transfer = new ArrayList<>();
    ArrayList<String> array2 = new ArrayList<>();
    ArrayList<String> array_transfer2 = new ArrayList<>();
    int i = 0;
    public static class Question_Solution implements Serializable, Parcelable {

        private String image_name;
        private String image_Url;

        public Question_Solution(String name, String image_Url) {
            this.image_name = name;
            this.image_Url = image_Url;
        }

        protected Question_Solution(Parcel in) {
            image_name = in.readString();
            image_Url = in.readString();
        }

        public static final Creator<Question_Solution> CREATOR = new Creator<Question_Solution>() {
            @Override
            public Question_Solution createFromParcel(Parcel in) {
                return new Question_Solution(in);
            }

            @Override
            public Question_Solution[] newArray(int size) {
                return new Question_Solution[size];
            }
        };

        public String getImage_Name() {
            return image_name;
        }

        public void setName(String name) {
            this.image_name = name;
        }

        public String getImage_Url() {
            return image_Url;
        }

        public void setImage_Url(String image_Url) {
            this.image_Url = image_Url;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel parcel, int i) {
            parcel.writeString(image_name);
            parcel.writeString(image_Url);
        }
    }
    //GUESS THE ANIMAL LEVEL
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //METHOD TO FETCH ALL THE DATA OF 1 JASON OBJ
    public class DownloadTask extends AsyncTask<String, Void, String> {

        protected String doInBackground(String... urls){
            String result = "";
            URL url;
            HttpURLConnection urlConnection;
            try {
                url = new URL(urls[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                InputStream in = urlConnection.getInputStream();
                InputStreamReader reader = new InputStreamReader(in);
                int data = reader.read();
                while (data != -1) {
                    char current = (char) data;
                    result += current;
                    data = reader.read();
                }
                urlConnection.disconnect();
                return result;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
        protected void onPostExecute(String s){
            super.onPostExecute(s);
            try{
                JSONObject json = new JSONObject(s);
                array.add(new Question_Solution(json.getString("Name"), json.getString("url")));
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }
    public void GoToAnimalsActTimer()
    {
        new CountDownTimer(1000, 1000) {
            public void onTick(long millisUntilFinished) {
            }
            public void onFinish() {
                Collections.shuffle(array);
                if (array.size() > 28) {
                    Collections.shuffle(array);
                    for (int i = 0; i< 20; i++)
                    {
                        array_transfer.add(array.get(i));
                    }
                    GoToAnimalsActivity();
                }
                else
                {
                    GoToAnimalsActTimer();
                }
            }
        }.start();
    }
    public void GoToAnimalsActivity()
    {
        Intent intent = new Intent(this, Guess_The_Animal_Level2.class);
        intent.putExtra("QuestionListExtra", array_transfer);
        startActivity(intent);
    }

    //METHOD ON CLICK
    public void level2Animal(View view)
    {
        if (clickable == true) {
            clickable = false;
            for (i = 0; i < 30; i++) {
                DownloadTask task = new DownloadTask();
                task.execute("https://teaching-toddlers-default-rtdb.firebaseio.com/AnimalsQuestion/q" + i + ".json");
            }
            GoToAnimalsActTimer();

        }
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    //GUESS THE ALPHA LEVEL METHODS
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //METHOD TO FETCH ALL THE DATA OF 1 JASON OBJ
    public class DownloadTask2 extends AsyncTask<String, Void, String> {

        protected String doInBackground(String... urls){
            String result = "";
            URL url;
            HttpURLConnection urlConnection;
            try {
                url = new URL(urls[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                InputStream in = urlConnection.getInputStream();
                InputStreamReader reader = new InputStreamReader(in);
                int data = reader.read();
                while (data != -1) {
                    char current = (char) data;
                    result += current;
                    data = reader.read();
                }
                urlConnection.disconnect();
                return result;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
        protected void onPostExecute(String s){
            super.onPostExecute(s);
            try{
                JSONObject json = new JSONObject(s);
                array2.add(json.getString("Name"));
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }
    public void GoToAlphaActTimer()
    {
        new CountDownTimer(1000, 1000) {
            public void onTick(long millisUntilFinished) {
            }
            public void onFinish() {
                Collections.shuffle(array2);
                if (array2.size() > 28) {
                    Collections.shuffle(array2);
                    for (int i = 0; i< 20; i++)
                    {
                        array_transfer2.add(array2.get(i));
                    }
                    GoToAlphaActivity();
                }
                else
                {
                    GoToAlphaActTimer();
                }
            }
        }.start();
    }
    public void GoToAlphaActivity()
    {
        Intent intent = new Intent(this, Guess_The_Alpha_Level2.class);
        intent.putExtra("QuestionListExtra", array_transfer2);
        startActivity(intent);
    }

    //METHOD ON CLICK
    public void level2Alpha(View view)
    {
        if(clickable == true) {
            clickable = false;
            for (i = 0; i < 30; i++) {
                DownloadTask2 task = new DownloadTask2();
                task.execute("https://teaching-toddlers-default-rtdb.firebaseio.com/AlphasQuestion/q" + i + ".json");
            }
            GoToAlphaActTimer();

        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_levelchoose);
        alpha = (Button) findViewById(R.id.Alpha);
        animal = (Button) findViewById(R.id.Animal);
        user = (TextView) findViewById(R.id.UserName);
        Intent intent = getIntent();
        String name = intent.getStringExtra("USERNAME");
        user.setText(name);
    }
}