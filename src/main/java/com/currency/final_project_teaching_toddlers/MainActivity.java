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

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Iterator;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    Button logIn;
    EditText password;
    EditText email;
    String userKey = "";
    String username = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        logIn = (Button) findViewById(R.id.login);
        password = (EditText) findViewById(R.id.password);
        email = (EditText) findViewById(R.id.email);
    }
    public void SignUp(View view)
    {
        Intent intent = new Intent(this, SignUp.class);
        startActivity(intent);
    }
    public void ChangeAge(View view)
    {
        Intent intent = new Intent(this, change_age.class);
        intent.putExtra("USERNAME", username);
        startActivity(intent);
    }

    //LOGIN////////////////////////////////////////////////////////////////////////////////////////////////////
    //METHOD TO FIND THE TABLE SIZE THEN EXECUTE EACH ROW IN THE TABLE
    public class GetTableSize extends AsyncTask<String, Void, String> {
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
                if(userKey.isEmpty())
                {
                    setNotFoundNotifyLog();
                }
                else
                {
                    GetAllUsers task2 = new GetAllUsers();
                    task2.execute("https://teaching-toddlers-default-rtdb.firebaseio.com/users/" + userKey + ".json");
                }
            }
            catch(Exception e){
                e.printStackTrace();
            }
        }
    }

    //FETCH THE DATA FROM THE 1-JSON OBJECT OF THE WHOLE JSON OBJECT
    public class GetAllUsers extends AsyncTask<String, Void, String> {
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
                Log.i("NAME ", username);
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
        Toast.makeText(this, "No account with such Email!", Toast.LENGTH_LONG).show();
    }
    public void setTrueNotifyLog()
    {
        Intent intent = new Intent(this, levelchoose.class);
        intent.putExtra("USERNAME", username);
        startActivity(intent);
        Toast.makeText(this, "LogIn Successfully!", Toast.LENGTH_LONG).show();
    }
    public void setFalseNotifyLog()
    {
        Toast.makeText(this, "The combination of your email and password is not Correct!", Toast.LENGTH_SHORT).show();
    }
    public void onLogInRequest(View view) throws IOException, JSONException {
        if(email.getText().toString().isEmpty() || password.getText().toString().isEmpty()) {
            Toast.makeText(this, "Please Fill With Valid Values!", Toast.LENGTH_SHORT).show();
        }
        else {
            GetTableSize task = new GetTableSize();
            task.execute();
        }
    }
}
//
//    public class GetUserInformation extends AsyncTask<String, Void, String> {
//        protected String doInBackground(String... urls) {
//            String result = "";
//            URL url;
//            HttpURLConnection urlConnection;
//            try {
//                url = new URL("https://teaching-toddlers-default-rtdb.firebaseio.com/users.json");
//                urlConnection = (HttpURLConnection) url.openConnection();
//                urlConnection.setRequestMethod("GET");
//                InputStream in = urlConnection.getInputStream();
//                InputStreamReader reader = new InputStreamReader(in);
//                int data = reader.read();
//                while (data != -1) {
//                    char current = (char) data;
//                    result += current;
//                    data = reader.read();
//                    Log.i("infooooooooooo", result);
//                }
//                urlConnection.disconnect();
//                return result;
//            } catch (Exception e) {
//                e.printStackTrace();
//                return null;
//            }
//        }
//        protected void onPostExecute(String s){
//            super.onPostExecute(s);
//            try{
//                JSONObject json = new JSONObject(s);
//                Log.i("NAME ", "GET THE NAME");
//
//                if(!json.has(username.getText().toString())) {
//                    setNotFoundNotifyLog();
//                }
//                else
//                {
//                    for (int i = 0; i < json.length() && foundUser != true; i++) {
//                        FetchUserInfo task = new FetchUserInfo();
//                        task.execute("https://teaching-toddlers-default-rtdb.firebaseio.com/users/user"+i+".json");
//                    }
//                }
//            }
//            catch(Exception e){
//                e.printStackTrace();
//            }
//        }
//    }
//    public class FetchUserInfo extends AsyncTask<String, Void, String> {
//        protected String doInBackground(String... urls){
//            String result = "";
//            URL url;
//            HttpURLConnection urlConnection;
//            try{
//                url = new URL(urls[0]);
//                urlConnection = (HttpURLConnection) url.openConnection();
//                InputStream in = urlConnection.getInputStream();
//                InputStreamReader reader = new InputStreamReader(in);
//                int data = reader.read();
//                while (data != -1) {
//                    char current = (char) data;
//                    result += current;
//                    data = reader.read();
//                }
//                return result;
//            }catch(Exception e) {
//                e.printStackTrace();
//                return null;
//            }
//        }
//        protected void onPostExecute(String s){
//            super.onPostExecute(s);
//            try{
//                JSONObject json = new JSONObject(s);
//                Log.i("Name::::::::: ", json.getString("Name"));
//                String JSusername = json.getString("Name");
//                if (JSusername.equalsIgnoreCase(username.getText().toString()))
//                    foundUser = true;
//                Log.i("Pass::::::::: ", json.getString("Password"));
//                String JSpass = json.getString("Password");
//                if (foundUser == true)
//                {
//                    if (JSusername.equalsIgnoreCase(username.getText().toString()) && JSpass.equalsIgnoreCase(password.getText().toString())) {
//                        setTrueNotifyLog();
//                    } else {
//                        setFalseNotifyLog();
//                    }
//                }
//            }catch(Exception e){
//                e.printStackTrace();
//            }
//        }
//    }
//
//
