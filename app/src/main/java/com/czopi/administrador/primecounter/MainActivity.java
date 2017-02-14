package com.czopi.administrador.primecounter;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

import java.util.ArrayList;
import java.util.Iterator;


public class MainActivity extends AppCompatActivity {

    TextView digit0, digit1, digit2, digit3, digit4, digit5;
    ArrayList<Integer> primes;
    String currentPrimeText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        digit0 = (TextView)findViewById(R.id.digit0);
        digit1 = (TextView)findViewById(R.id.digit1);
        digit2 = (TextView)findViewById(R.id.digit2);
        digit3 = (TextView)findViewById(R.id.digit3);
        digit4 = (TextView)findViewById(R.id.digit4);
        digit5 = (TextView)findViewById(R.id.digit5);

        Gson gson = new Gson();
        SharedPreferences prefs = this.getSharedPreferences(
                "com.czopi.administrador.primecounter", Context.MODE_PRIVATE);
        currentPrimeText = prefs.getString("currentPrime", "000002");
        if (currentPrimeText.equals("000002")) {
            digit5.setText("2");
            digit4.setText("0");
            digit3.setText("0");
            digit2.setText("0");
            digit1.setText("0");
            digit0.setText("0");

        } else {
            char[] currentPrimeTextArray = currentPrimeText.toCharArray();
            digit0.setText(String.valueOf(currentPrimeTextArray[0]));
            digit1.setText(String.valueOf(currentPrimeTextArray[1]));
            digit2.setText(String.valueOf(currentPrimeTextArray[2]));
            digit3.setText(String.valueOf(currentPrimeTextArray[3]));
            digit4.setText(String.valueOf(currentPrimeTextArray[4]));
            digit5.setText(String.valueOf(currentPrimeTextArray[5]));
        }
        String jsonText = prefs.getString("primes", null);
        if (jsonText == null) {
            createPrimes();
        } else {
            Type type = new TypeToken<ArrayList<Integer>>(){}.getType();
            primes = gson.fromJson(jsonText, type);
        }


    }

    public void createPrimes() {
        primes = new ArrayList<>();

        int counterX = 2;

        while (counterX < 1000) {
            primes.add(counterX);
            counterX += 1;
        }

        for (int i = 2; i < 1000; i++) {
            for (Iterator<Integer> iterator = primes.iterator(); iterator.hasNext(); ) {
                Integer x = iterator.next();
                if (x % i == 0) {
                    if(x != i) {
                        iterator.remove();
                    }
                }
            }
        }


        SharedPreferences prefs = this.getSharedPreferences(
                "com.czopi.administrador.primecounter", Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String jsonText = gson.toJson(primes);
        prefs.edit().putString("primes", jsonText).apply();


    }

    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.red_button:
                int currentPrimeIndex;
                int currentPrime;
                int newPrime;
                int primesDifference;
                int newDigit0 = 0;
                int newDigit1 = 0;
                int newDigit2 = 0;
                int newDigit3 = 0;
                int newDigit4 = 0;
                int newDigit5;
                boolean d0 = false;
                boolean d1 = false;
                boolean d2 = false;
                boolean d3 = false;
                boolean d4 = false;

                getNumberAsString();

                currentPrime = Integer.parseInt(currentPrimeText);
                currentPrimeIndex = primes.indexOf(currentPrime);
                newPrime = primes.get(currentPrimeIndex + 1);
                primesDifference = newPrime - currentPrime;

                for(int i = primesDifference; i!= 0; i--){
                    newDigit5 = Integer.parseInt(digit5.getText().toString()) + 1;
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
                                    if (newDigit1 == 10) {
                                        newDigit1 = 0;
                                        newDigit0= Integer.parseInt(digit0.getText().toString()) + 1;
                                        d0 = true;
                                        if (newDigit0 == 10) {
                                            newDigit0 = 0;
                                            newDigit5 = 2;
                                        }
                                    }
                                }
                            }
                        }
                    }
                    digit5.setText(String.valueOf(newDigit5));
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
                                    if (d0) {
                                        digit0.setText(String.valueOf(newDigit0));
                                        d0 = false;
                                    }
                                }
                            }
                        }
                    }

                    getNumberAsString();

                    SharedPreferences prefs = this.getSharedPreferences(
                            "com.czopi.administrador.primeCounter", Context.MODE_PRIVATE);
                    prefs.edit().putString("currentPrime", currentPrimeText).apply();

                    try {
                        Thread.sleep(150);
                    } catch(InterruptedException ex) {
                        Thread.currentThread().interrupt();
                    }


                }



        }
    }

    public void getNumberAsString () {
        currentPrimeText = "";
        currentPrimeText += digit0.getText().toString();
        currentPrimeText += digit1.getText().toString();
        currentPrimeText += digit2.getText().toString();
        currentPrimeText += digit3.getText().toString();
        currentPrimeText += digit4.getText().toString();
        currentPrimeText += digit5.getText().toString();
    }

}


