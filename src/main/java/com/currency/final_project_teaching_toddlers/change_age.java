package com.currency.final_project_teaching_toddlers;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;

public class change_age extends AppCompatActivity {
    boolean foundUser = false;
    int items = 0;
    int userNotFound = 2;
    boolean lastEelement = false;
    int dontNotifyFalse = 2;
    int i = 0;
    Button Save_Changes;
    EditText email;
    EditText password;
    EditText age;
    String userKey = "";
    String username = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_age);
        Save_Changes = (Button) findViewById(R.id.SaveChanges);
        email = (EditText) findViewById(R.id.Cemail);
        password = (EditText) findViewById(R.id.Cpassword);
        age = (EditText) findViewById(R.id.Cage);
    }

    public class GetUserExists extends AsyncTask<String, Void, String> {
        protected String doInBackground(String... urls) {
            String result = "";
            URL url;
            HttpURLConnection urlConnection;
            try {
                String u_email = email.getText().toString().toUpperCase(Locale.ROOT);
                Log.i("EMAIL", u_email);
                String urlText = "https://teaching-toddlers-default-rtdb.firebaseio.com/users.json?orderBy=\"Email\"&equalTo=\""+u_email+"\"";
                url = new URL(urlText);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");

                InputStream in = urlConnection.getInputStream();
                InputStreamReader reader = new InputStreamReader(in);
                int data = reader.read();
                while (data != -1) {
                    char current = (char) data;
                    result += current;
                    data = reader.read();
                    Log.i("infooooooooooo", result);
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
            try {
                JSONObject json = new JSONObject(s);
                Iterator<?> key = json.keys();
                userKey = "";
                while(key.hasNext())
                {
                    userKey = (String) key.next();
                    Log.i("KEEYSSSSSSS", userKey);
                }
                Log.i("KEEYSSSSSSS", userKey);
                if(userKey.isEmpty())
                {
                    setNotFoundNotifyLog();
                }
                else
                {
                    ConfirmPass task = new ConfirmPass();
                    task.execute("https://teaching-toddlers-default-rtdb.firebaseio.com/users/"+ userKey +".json");
                }
            }
            catch(Exception e){
                e.printStackTrace();
            }
        }
    }
    //FETCH THE DATA FROM THE 1-JSON OBJECT OF THE WHOLE JSON OBJECT
    public class ConfirmPass extends AsyncTask<String, Void, String> {
        protected String doInBackground(String... urls) {
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
                    Log.i("infooooooooooo", result);
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
                Log.i("NAME ", "GET THE NAME");
                username = json.getString("Name");
                //IF THE ELEMENT MATCHES THE THE NAME

                if(json.getString("Email").equalsIgnoreCase(email.getText().toString())) {
                    if(BCrypt.checkpw(password.getText().toString(), json.getString("Password"))!=false) {
                        UpdateUser task = new UpdateUser();
                        task.execute("https://teaching-toddlers-default-rtdb.firebaseio.com/users/"+ userKey +".json");
                    }
                    else
                    {
                        setFalseNotifyLog();
                    }
                }
            }
            catch(Exception e){
                e.printStackTrace();
            }
        }
    }
    //FETCH THE DATA FROM THE 1-JSON OBJECT OF THE WHOLE JSON OBJECT
    public class UpdateUser extends AsyncTask<String, Void, String> {
        protected String doInBackground(String... urls) {
            String result = "";
            URL url;
            HttpURLConnection urlConnection;
            try {
                url = new URL(urls[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("PATCH");
                urlConnection.setDoOutput(true);
                urlConnection.setRequestProperty("Content-Type", "application/json; utf-8");

                Map<String,String> map = new HashMap<>();

                map.put("Age", age.getText().toString());

                String jsonString = new JSONObject(map).toString();

                System.out.println(jsonString);
                try(OutputStream os = urlConnection.getOutputStream()) {
                    byte[] input = jsonString.getBytes("utf-8");
                    os.write(input, 0, input.length);
                }
                try(BufferedReader br = new BufferedReader(
                        new InputStreamReader(urlConnection.getInputStream(), "utf-8"))) {
                    StringBuilder response = new StringBuilder();
                    String responseLine = null;
                    while ((responseLine = br.readLine()) != null) {
                        response.append(responseLine.trim());
                    }
                    System.out.println(response.toString());
                }
                return "true";
            } catch (Exception e) {
                e.printStackTrace();
                return "false";
            }
        }

        protected void onPostExecute(String s){
            super.onPostExecute(s);
            try{
                if (s.equalsIgnoreCase("true"))
                {
                    setTrueNotifyLog();
                }
                else
                {
                    setFalseNotifyLog();
                }
                JSONObject json = new JSONObject(s);
                Log.i("NAME ", "GET THE NAME");
                username = json.getString("Name");
                //IF THE ELEMENT MATCHES THE THE NAME
                if(json.getString("Email").equalsIgnoreCase(email.getText().toString())) {
                    if (BCrypt.checkpw(password.getText().toString(), json.getString("Password"))!=false)
                    {
                        setTrueNotifyLog();
                    }
                    else
                    {
                        setFalseNotifyLog();
                    }
                }
            }
            catch(Exception e){
                e.printStackTrace();
            }
        }
    }
    public void setNotFoundNotifyLog()
    {
        Toast.makeText(this, "No Account With Such Email!", Toast.LENGTH_LONG).show();
    }
    public void setTrueNotifyLog()
    {
        Intent intent = new Intent(this, levelchoose.class);
        intent.putExtra("USERNAME", username);
        startActivity(intent);
        Toast.makeText(this, "Changes Saved Successfully!", Toast.LENGTH_LONG).show();
    }
    public void setFalseNotifyLog()
    {
        Toast.makeText(this, "The Combination of Your Email and Password is Not Correct!", Toast.LENGTH_SHORT).show();
    }
    public void onUpdateRequest(View view) throws IOException, JSONException {
        String user_password = password.getText().toString();
        if(email.getText().toString().isEmpty() || password.getText().toString().isEmpty()) {
            Toast.makeText(this, "Please Fill With Valid Values!", Toast.LENGTH_SHORT).show();
        }
        else
        try {
            int user_age = Integer.parseInt(age.getText().toString());
            if (user_age < 0 || user_age > 5)
                Toast.makeText(this, "Please Enter a Valid Age Range!", Toast.LENGTH_SHORT).show();
            else {
                GetUserExists task = new GetUserExists();
                task.execute();
            }
        }
        catch(Exception e){
            Toast.makeText(this, "Please Enter a Valid Age Range!", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }
}
