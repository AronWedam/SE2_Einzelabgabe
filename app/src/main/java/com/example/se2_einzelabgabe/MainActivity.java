package com.example.se2_einzelabgabe;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.Socket;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button btnSend;
    Button btnFind;
    EditText txt;
    TextView message1;
    TextView message2;
    TextView messageServer;

    private Handler mainHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
      /*  if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, MainFragment.newInstance())
                    .commitNow();
        } */

        message1 = findViewById(R.id.message1);
        message2 = findViewById(R.id.message2);
        messageServer = findViewById(R.id.messageServer);

        btnSend = findViewById(R.id.buttonSend);
        btnFind = findViewById(R.id.buttonFind);

        txt = findViewById(R.id.editNumber);

        btnSend.setOnClickListener(this);
        btnFind.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.buttonSend:
                String matrikelnr = txt.getText().toString();
                NetworkThread networkThread = new NetworkThread(matrikelnr);
                new Thread(networkThread).start();
            break;
            case R.id.buttonFind:
                matrikelnr = txt.getText().toString();
                GGTThread ggtThread = new GGTThread(matrikelnr);
                new Thread(ggtThread).start();
            break;
        }
    }

    class NetworkThread implements Runnable {

        private String matrikelnummer;
        private String sentence;
        private String modifiedSentence;

        NetworkThread(String matrikelnummer) {
            this.matrikelnummer = matrikelnummer;
        }


        @Override
        public void run() {
            try{
                BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));

                Socket clientSocked = new Socket("se2-isys.aau.at", 53212);


                DataOutputStream outToServer = new DataOutputStream(clientSocked.getOutputStream());


                BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocked.getInputStream()));

                sentence = matrikelnummer;

                outToServer.writeBytes(sentence + '\n');

                modifiedSentence = inFromServer.readLine();

                clientSocked.close();

                mainHandler.post(() -> {
                    message2.setText("Antwort vom Server");
                });

                mainHandler.post(() -> {
                    messageServer.setText(modifiedSentence);
                });
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
    }


    class GGTThread implements Runnable {

        String matrikelnr;

        GGTThread(String matrikelnr) {
            this.matrikelnr = matrikelnr;
        }

        @Override
        public void run() {
            StringBuilder builder = new StringBuilder();

            for (int i = 0; i < matrikelnr.length(); i++) {
                int a = Integer.parseInt(matrikelnr.substring(i, i+1));
                for (int k = i+1; k < matrikelnr.length(); k++) {
                    int b = Integer.parseInt(matrikelnr.substring(k, k + 1));

                    if(b > a){
                        for (int j = 2; j < b ; j++) {
                            if (a % j == 0 && b % j == 0 && a != 0 && b != 0) {
                                builder.append(i + " ");
                                builder.append(k + " ");
                            }
                        }
                    }

                    for (int j = 2; j < a; j++) {
                        if (a % j == 0 && b % j == 0 && a != 0 && b != 0) {
                            builder.append(i + " ");
                            builder.append(k + " ");
                        }
                    }

                }
            }

            String text = builder.toString();

            mainHandler.post(() -> {
                message2.setText("Index der Zahlen mit gleichem Teiler");
            });

            mainHandler.post(() -> {
                messageServer.setText(text);
            });
        }
    }
}