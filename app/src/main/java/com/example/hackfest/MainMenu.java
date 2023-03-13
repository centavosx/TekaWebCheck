package com.example.hackfest;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

public class MainMenu extends AppCompatActivity {
    Button encrypt, decrypt, websitecheck, wifihack, ipget, clear, loc, db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        final String[] valueurl = {""};
        int checkProgress = 0;
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        try {
            this.getSupportActionBar().hide();
        } catch (NullPointerException e) {
        }
        super.onCreate(savedInstanceState);

        setContentView(R.layout.mainmenu);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity) this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 101);
        }

        encrypt = findViewById(R.id.encrypt);
        decrypt = findViewById(R.id.decrypt);
        websitecheck = findViewById(R.id.websitecheck);
        wifihack = findViewById(R.id.wifihack);
        ipget = findViewById(R.id.ipget3);
        loc = findViewById(R.id.getlocation);
        clear = findViewById(R.id.clearcache);
        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences myPref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
                myPref.edit().putString("pw", "").apply();
                myPref.edit().putInt("loop", 0).apply();
                myPref.edit().putInt("loop2", 0).apply();
                myPref.edit().putString("output2", "").apply();
                myPref.edit().putString("valuestring", "").apply();
                Toast.makeText(getApplicationContext(), "Cache cleared!", Toast.LENGTH_SHORT).show();
            }
        });
        loc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainMenu.this, Location.class);
                startActivity(intent);
                finish();
            }
        });
        ipget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainMenu.this, getIp.class);
                startActivity(intent);
                finish();
            }
        });
        encrypt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainMenu.this, Encrypt.class);
                startActivity(intent);
                finish();
            }
        });
        decrypt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainMenu.this, Decrypt.class);
                startActivity(intent);
                finish();
            }
        });
        websitecheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainMenu.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
        wifihack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainMenu.this, WiFiHack.class);
                startActivity(intent);finish();

            }
        });
    }

}