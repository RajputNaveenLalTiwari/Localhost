package com.example.workingonlocalhost;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity
{
    private final String API 	= "http://192.168.1.55/Android/index.php";//http://10.0.2.2:3306/Android/index.php";
    private final String TAG	= "LocalHost";
    private Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = MainActivity.this;
        LocalhostConnectionTask task = new LocalhostConnectionTask();
        task.execute(API);
    }

    class LocalhostConnectionTask extends AsyncTask<String, Void, String>
    {
        URL url = null;
        HttpURLConnection urlConnection = null;
        String requestMethod = "DELETE";
        InputStream inputStream = null;
        String response = null;
        @Override
        protected String doInBackground(String... input)
        {
            try
            {
                url = new URL(API);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setReadTimeout(15000);
                urlConnection.setConnectTimeout(15000);
                urlConnection.setRequestMethod(requestMethod);
				urlConnection.setDoInput(true);
				urlConnection.setDoOutput(true);
                urlConnection.connect();

                inputStream = urlConnection.getInputStream();

                int byteCharacter;
                while ((byteCharacter = inputStream.read()) != -1)
                {
                    response += (char) byteCharacter;
                }

				//response = urlConnection.getResponseMessage();//String.valueOf(urlConnection.getResponseCode());

                return response;
            }
            catch (MalformedURLException e)
            {
                Log.e(TAG, "Error while creating URL = "+e.getMessage());
            }
            catch (IOException e)
            {
                Log.e(TAG,"Error while opening connection "+e.getMessage());
            }
            finally
            {
                urlConnection.disconnect();
                try
                {
                    inputStream.close();
                }
                catch (IOException e)
                {
                    Log.e(TAG,"Error while closing inputstream "+e.getMessage());
                }
            }

            return response;
        }

        @Override
        protected void onPostExecute(String output)
        {
            if(output!=null)
            {
                Toast.makeText(context, TAG+" = "+output, Toast.LENGTH_LONG).show();
            }
        }

    }
}
