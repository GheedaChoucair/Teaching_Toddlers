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
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;

public class SignUp extends AppCompatActivity {

    EditText password;
    EditText username;
    int items = 0;
    Button SignUp;
    EditText email;
    EditText confirm_password;
    EditText age;
    String success = "";
    BCrypt BCrypt = new BCrypt();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        SignUp = (Button) findViewById(R.id.SignUp);
        email = (EditText) findViewById(R.id.email);
        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        confirm_password = (EditText) findViewById(R.id.confirmpassword);
        age = (EditText) findViewById(R.id.age);
    }
    //LOGIN////////////////////////////////////////////////////////////////////////////////////////////////////
    //FIND IF USER ALREADY EXISTS
    public class UserEmailExist extends AsyncTask<String, Void, String> {
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
                String userKey = "";
                while(key.hasNext())
                {
                    userKey = (String) key.next();
                }
                if(userKey.isEmpty())
                {
                    UserUserNameExist task2 = new UserUserNameExist();
                    task2.execute();
                }
                else
                {
                    setUserEmailAlreadyExist();
                }
            }
            catch(Exception e){
                e.printStackTrace();
            }
        }
    }
    public class UserUserNameExist extends AsyncTask<String, Void, String> {
        protected String doInBackground(String... urls) {
            String result = "";
            URL url;
            HttpURLConnection urlConnection;
            try {
                String u_email = username.getText().toString().toUpperCase(Locale.ROOT);
                Log.i("EMAIL", u_email);
                String urlText = "https://teaching-toddlers-default-rtdb.firebaseio.com/users.json?orderBy=\"Name\"&equalTo=\""+u_email+"\"";
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
                String userKey = "";
                while(key.hasNext())
                {
                    userKey = (String) key.next();
                }
                if(userKey.isEmpty())
                {
                    RegisterUser task2 = new RegisterUser();
                    task2.execute("https://teaching-toddlers-default-rtdb.firebaseio.com/users.json");
                }
                else
                {
                    setUserUsernameAlreadyExist();
                }
            }
            catch(Exception e){
                e.printStackTrace();
            }
        }
    }

    public void setUserEmailAlreadyExist()
    {
        Toast.makeText(this, "This Email Already Registered!", Toast.LENGTH_LONG).show();
    }
    public void setUserUsernameAlreadyExist()
    {
        Toast.makeText(this, "This UserName Already Exist! Please Choose Another One", Toast.LENGTH_LONG).show();
    }
    //METHOD TO FIND THE TABLE SIZE THEN EXECUTE EACH ROW IN THE TABLE
    public class RegisterUser extends AsyncTask<String, Void, String> {
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
                    if (current == '}')
                    {
                        //FIND THE SIZE OF THE TABLE
                        items++;
                    }
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
                PutUser task2 = new PutUser();
                task2.execute("https://teaching-toddlers-default-rtdb.firebaseio.com/users/user" + (items) + ".json");
            }
            catch(Exception e){
                e.printStackTrace();
            }
        }
    }

    public String hashPass(String plain_password)
    {
        String pw_hash = BCrypt.hashpw(plain_password, BCrypt.gensalt());
        return pw_hash;
    }

    //FETCH THE DATA FROM THE 1-JSON OBJECT OF THE WHOLE JSON OBJECT
    public class PutUser extends AsyncTask<String, Void, String> {
        protected String doInBackground(String... urls) {
            String result = "";
            URL url;
            HttpURLConnection urlConnection;
            try {
                url = new URL(urls[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("PUT");
                urlConnection.setRequestProperty("Content-Type", "application/json; utf-8");

                Map<String,String> map = new HashMap<>();

                map.put("Name", username.getText().toString().toUpperCase(Locale.ROOT));
                map.put("Email", email.getText().toString().toUpperCase(Locale.ROOT));
                map.put("Password", hashPass(password.getText().toString()));
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
                    success = "true";
                    System.out.println(response.toString());
                }
                return success;
            } catch (Exception e) {
                e.printStackTrace();
                return "false";
            }
        }
        protected void onPostExecute(String s){
            super.onPostExecute(s);
            try {
                if (s.equalsIgnoreCase("true"))
                    setTrueNotifySign();
                else
                    setNotFoundNotifySign();
            }
            catch(Exception e){
                e.printStackTrace();
            }
        }
    }

    public void setNotFoundNotifySign()
    {

        Toast.makeText(this, "An Error happened while trying to SignUp!", Toast.LENGTH_LONG).show();
    }
    public void setTrueNotifySign()
    {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        Toast.makeText(this, "SignUp Successfully!", Toast.LENGTH_LONG).show();
    }
    public void onSignUpRequest(View view) throws IOException, JSONException, NumberFormatException{
        items = 0;
        String user_password = password.getText().toString();
        String user_confirm_password = confirm_password.getText().toString();
        if(user_password.isEmpty() || user_confirm_password.isEmpty() || email.getText().toString().isEmpty() || username.getText().toString().isEmpty()) {
            Toast.makeText(this, "Please Fill With Valid Values!", Toast.LENGTH_SHORT).show();
        }
        else {
            try {

                int user_age = Integer.parseInt(age.getText().toString());

                if (!user_password.equalsIgnoreCase(user_confirm_password))
                    Toast.makeText(this, "Password and Confirm Password Do Not Match!", Toast.LENGTH_SHORT).show();
                else if (user_age < 0 || user_age > 5)
                    Toast.makeText(this, "Please Enter a Valid Age Range! Age Should be between 0 and 5!", Toast.LENGTH_SHORT).show();
                else {
                    UserEmailExist task = new UserEmailExist();
                    task.execute();
                }
            } catch (Exception e) {
                Toast.makeText(this, "Please Enter a Valid Age Range! Age Should be between 0 and 5!", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }
    }
}
