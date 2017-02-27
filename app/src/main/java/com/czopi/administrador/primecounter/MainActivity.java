package com.czopi.administrador.primecounter;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

import java.util.ArrayList;
import java.util.Iterator;


public class MainActivity extends AppCompatActivity {

    SoundPool sp;
    TextView digit1, digit2, digit3, digit4, digit5;
    ImageView red_button;
    ArrayList<Integer> primes;
    String currentPrimeText;
    Boolean d1,d2,d3,d4;
    int newDigit1, newDigit2, newDigit3, newDigit4, newDigit5, primesDifference, currentPrime;
    private Handler mHandler, mHandler2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sp = new SoundPool(10, AudioManager.STREAM_MUSIC, 0);
        final int soundID = sp.load(this, R.raw.sound, 1);

        digit1 = (TextView)findViewById(R.id.digit1);
        digit2 = (TextView)findViewById(R.id.digit2);
        digit3 = (TextView)findViewById(R.id.digit3);
        digit4 = (TextView)findViewById(R.id.digit4);
        digit5 = (TextView)findViewById(R.id.digit5);
        red_button = (ImageView)findViewById(R.id.red_button);
        red_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentPrimeIndex;
                int newPrime;
                newDigit1 = 0;
                newDigit2 = 0;
                newDigit3 = 0;
                newDigit4 = 0;
                d1 = false;
                d2 = false;
                d3 = false;
                d4 = false;

                getNumberAsString(); //Obtains prime number shown on UI

                currentPrime = Integer.parseInt(currentPrimeText);
                currentPrimeIndex = primes.indexOf(currentPrime);//Finds prime number on 'primes' list
                try {
                    newPrime = primes.get(currentPrimeIndex + 1); //Finds next prime number on 'primes' list
                    primesDifference = newPrime - currentPrime; //Calculates difference between primes

                    //Increases prime on UI 1 by 1
                    mHandler = new Handler();
                    mHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            sp.play(soundID, 1, 1, 0, 0, 1);
                            newDigit5 = Integer.parseInt(digit5.getText().toString()) + 1; //Calculates next number to be shown
                            if (newDigit5 == 10) {
                                newDigit5 = 0;
                                newDigit4 = Integer.parseInt(digit4.getText().toString()) + 1;
                                d4 = true;
                                if (newDigit4 == 10) {
                                    newDigit4 = 0;
                                    newDigit3 = Integer.parseInt(digit3.getText().toString()) + 1;
                                    d3 = true;
                                    if (newDigit3 == 10) {
                                        newDigit3 = 0;
                                        newDigit2 = Integer.parseInt(digit2.getText().toString()) + 1;
                                        d2 = true;
                                        if (newDigit2 == 10) {
                                            newDigit2 = 0;
                                            newDigit1 = Integer.parseInt(digit1.getText().toString()) + 1;
                                            d1 = true;
                                        }
                                    }
                                }
                            }

                            digit5.setText(String.valueOf(newDigit5)); //Updates UI
                            if (d4) {
                                digit4.setText(String.valueOf(newDigit4));
                                d4 = false;
                                if (d3) {
                                    digit3.setText(String.valueOf(newDigit3));
                                    d3 = false;
                                    if (d2) {
                                        digit2.setText(String.valueOf(newDigit2));
                                        d2 = false;
                                        if (d1) {
                                            digit1.setText(String.valueOf(newDigit1));
                                            d1 = false;
                                        }
                                    }
                                }
                            }

                            primesDifference--;
                            if (primesDifference != 0) {
                                mHandler.postDelayed(this, 200);
                            }
                        }
                    }, 200); //Creates a 1000 milliseconds delay between iteration
                } catch (IndexOutOfBoundsException error) { //This line is ran if User reached the end of the 'prime' list
                    mHandler2 = new Handler();
                    mHandler2.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            sp.play(soundID, 1, 1, 0, 0, 1);
                            digit5.setText("2");
                            digit4.setText("0");
                            digit3.setText("0");
                            digit2.setText("0");
                            digit1.setText("0");
                        }
                    }, 200);
                }

                SharedPreferences prefs = getSharedPreferences(
                        "com.czopi.administrador.primecounter", Context.MODE_PRIVATE);
                getNumberAsString();
                prefs.edit().putString("currentPrime", currentPrimeText).apply();
            }
        });
        red_button.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        red_button.setImageResource(R.drawable.button_push);
                        break;
                    case MotionEvent.ACTION_UP:
                        red_button.setImageResource(R.drawable.button_normal);
                        break;
                    default:
                        break;
                }
                return false;
            }
        });

        //Obtains prime numbers list
        SharedPreferences prefs = getSharedPreferences(
                "com.czopi.administrador.primecounter", Context.MODE_PRIVATE);
        String jsonText = prefs.getString("primes", null);
        Gson gson = new Gson();
        if (jsonText == null) {
            createPrimes();
        } else {
            Type type = new TypeToken<ArrayList<Integer>>(){}.getType();
            primes = gson.fromJson(jsonText, type);
        }


    }

    @Override
    protected void onResume() {
        super.onResume();
        //Resumes last number
        SharedPreferences prefs = getSharedPreferences(
                "com.czopi.administrador.primecounter", Context.MODE_PRIVATE);
        currentPrimeText = prefs.getString("currentPrime", "00002");
        if (currentPrimeText.equals("00002")) {
            digit5.setText("2");
            digit4.setText("0");
            digit3.setText("0");
            digit2.setText("0");
            digit1.setText("0");
        } else {
            char[] currentPrimeTextArray = currentPrimeText.toCharArray();
            digit1.setText(String.valueOf(currentPrimeTextArray[1]));
            digit2.setText(String.valueOf(currentPrimeTextArray[2]));
            digit3.setText(String.valueOf(currentPrimeTextArray[3]));
            digit4.setText(String.valueOf(currentPrimeTextArray[4]));
            digit5.setText(String.valueOf(currentPrimeTextArray[5]));
        }
    }

    public void createPrimes() {
        primes = new ArrayList<>();

        int counterX = 2;
        //Creates numbers from 1 to N and saves them to primes
        while (counterX < 100000) {
            primes.add(counterX);
            counterX += 1;
        }
        //Iterates over primes and deletes compound numbers using Sieve of Eratosthenes
        for (int i = 2; i < 100000; i++) {
            for (Iterator<Integer> iterator = primes.iterator(); iterator.hasNext(); ) {
                Integer x = iterator.next();
                if (x % i == 0) {
                    if(x != i) {
                        iterator.remove();
                    }
                }
            }
        }

        //Saves prime numbers list
        SharedPreferences prefs = getSharedPreferences(
                "com.czopi.administrador.primecounter", Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String jsonText = gson.toJson(primes);
        prefs.edit().putString("primes", jsonText).apply();


    }

    public void getNumberAsString () { //Obtains prime number shown on UI
        currentPrimeText = "";
        currentPrimeText += digit1.getText().toString();
        currentPrimeText += digit2.getText().toString();
        currentPrimeText += digit3.getText().toString();
        currentPrimeText += digit4.getText().toString();
        currentPrimeText += digit5.getText().toString();
    }


}


