package com.jonetech.asyncapp;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.lang.ref.WeakReference;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    public static final String MAINACTIVITY = "MAINACTIVITY";
    private WeakReference<TextView> fTextview;
    private TextView myText;
    private ProgressBar fProgressBar;
    public static final String TEXT_STATE = "currentText";
    Integer count = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if(savedInstanceState !=  null){

            String state = savedInstanceState.getString(TEXT_STATE);
            myText.setText(state);
        }

        myText = findViewById(R.id.textView);
        fProgressBar = findViewById(R.id.progressBar);

        fProgressBar.setMax(10);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putString(TEXT_STATE, myText.getText().toString());
    }

    /**
     *  Handles the on-click event of "Start Task" button and then runs or execute the
     *  SinpleAsyncTask off the UI thread
     *
     * @param view  The view consist everything about the "Button"
     */

    public void startTask(View view) {

        count = 1;

        fProgressBar.setVisibility(View.VISIBLE);

        fProgressBar.setProgress(0);
        // Set text to napping
        myText.setText(R.string.napping);

        // Create an instance of Async task
        new SimpleAsyncTask(myText).execute(10);

    }

    public class SimpleAsyncTask extends AsyncTask<Integer, Integer, String> {

        SimpleAsyncTask(TextView textView){
            fTextview = new WeakReference<>(textView);
        }

        @Override
        protected String doInBackground(Integer... params) {

            int number = new Random().nextInt(11) * 200;

            for(; count <= params[0]; count++){
                try{
                    Thread.sleep(number);
                    publishProgress(count);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }



            return "Awake at last after sleeping for "+ number +" Milliseconds";
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
//            super.onProgressUpdate(values);
            System.out.println(values[0]);
            fProgressBar.setProgress(values[0]);
        }


        @Override
        protected void onPostExecute(String s) {
            fProgressBar.setVisibility(View.GONE);
            fTextview.get().setText(s);
        }
    }
}
