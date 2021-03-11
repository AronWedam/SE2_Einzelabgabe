package com.example.se2_einzelabgabe;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.se2_einzelabgabe.ui.main.MainFragment;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button btnSend;
    EditText txt;
    TextView message1;
    TextView message2;
    TextView messageServer;

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

    }
}