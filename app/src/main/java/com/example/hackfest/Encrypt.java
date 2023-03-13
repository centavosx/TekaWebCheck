package com.example.hackfest;


import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;

public class Encrypt extends AppCompatActivity {
    EditText encstring, string, key, id;
    Button encrypt, back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        try {
            this.getSupportActionBar().hide();
        } catch (NullPointerException e) {
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.encrypt);
        encstring = findViewById(R.id.encstring);
        string = findViewById(R.id.encryptstring);
        key = findViewById(R.id.encryptkey);
        id = findViewById(R.id.encryptid);
        encrypt = findViewById(R.id.encryptbtn);
        back = findViewById(R.id.goBack);
        encrypt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String stringval = string.getText().toString();
                String stringkey = key.getText().toString();
                int stringid = Integer.parseInt(id.getText().toString());
                encdecr encdecr = new encdecr();
                encstring.setText(encdecr.encrypt(stringval, stringkey, stringid));

            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Encrypt.this, MainMenu.class);
                startActivity(intent);
                finish();
            }
        });
    }

}
