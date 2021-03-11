package com.example.se2_einzelabgabe;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.se2_einzelabgabe.ui.main.MainFragment;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.Socket;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button btnSend;
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

        btnSend = findViewById(R.id.button);

        txt = findViewById(R.id.editNumber);

        btnSend.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        String matrikelnr = txt.getText().toString();
        NetworkThread networkThread = new NetworkThread(matrikelnr);
        new Thread(networkThread).start();
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
                    messageServer.setText(modifiedSentence);
                });
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}