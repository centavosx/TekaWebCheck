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

public class Decrypt extends AppCompatActivity {
    EditText encstring, string, key, id;
    Button encrypt, back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        encdecr encdecr = new encdecr();
        int checkProgress = 0;
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        try {
            this.getSupportActionBar().hide();
        } catch (NullPointerException e) {
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.decrypt);

        encstring = findViewById(R.id.decstring);
        string = findViewById(R.id.decryptstring);
        key = findViewById(R.id.decryptkey);
        id = findViewById(R.id.decryptid);
        encrypt = findViewById(R.id.decryptbtn);
        back = findViewById(R.id.goBack);


        encrypt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String stringval = string.getText().toString();
                    String stringkey = key.getText().toString();
                    int stringid = Integer.parseInt(id.getText().toString());
                    encstring.setText(encdecr.decrypt(stringval, stringkey, stringid));
                }catch (Exception e){

                }



            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Decrypt.this, MainMenu.class);
                startActivity(intent);
                finish();
            }
        });
    }

}
