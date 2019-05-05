package com.example.weatherapp;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    EditText cityName;
    TextView resultView;
    String apiKey="f10b2d8e33a895d225f30c1d34ae7841";
     public void findWeather(View view){

         Log.d("City Name", cityName.getText().toString());
         InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
         mgr.hideSoftInputFromWindow(cityName.getWindowToken(),0);
         DownloadTask task = new DownloadTask();
         task.execute("http://api.openweathermap.org/data/2.5/weather?q="+cityName.getText().toString()+"&appid="+apiKey);
     }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cityName = (EditText) findViewById(R.id.cityName);
        resultView = (TextView) findViewById(R.id.resultView);
    }
    public class DownloadTask extends AsyncTask<String,Void,String>{

        @Override
        protected String doInBackground(String... urls) {
            String result="";
            URL url;
            HttpURLConnection urlConnection = null;

            try {
                url= new URL(urls[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                InputStream in = urlConnection.getInputStream();
                InputStreamReader reader = new InputStreamReader(in);
                int data=reader.read();
                while(data != -1){
                    char current = (char) data;
                    result += current;
                   data = reader.read();
                }
                return result;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            try {
                String message = "";
                JSONObject jsonObject = new JSONObject(result);
                String weatherInfo = jsonObject.getString("weather");
                Log.d("weather content", weatherInfo);
                JSONArray arr = new JSONArray(weatherInfo);
                for(int i=0;i<arr.length();i++){
                    JSONObject jsonPart = arr.getJSONObject(i);
                    String main="";
                    String description="";
                    main=jsonPart.getString("main" );
                    description=jsonPart.getString("description");
                    if(main!=""&&description!=""){
                        message+=main +" : "+description+"\r\n";
                    }
                    if(message!=""){
                      resultView.setText(message);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
