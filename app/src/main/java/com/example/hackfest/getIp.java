package com.example.hackfest;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.AssetManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.StrictMode;
import android.text.format.Formatter;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;

public class getIp extends AppCompatActivity{
    Button but, back, quick, moderate, deep, but2;
    TextView result, resultport, iplocal, prog;
    EditText ip;
    static ArrayList<String> device = new ArrayList<String>();
    int search = 100;
    Thread thread;
    Thread thread2;
    ProgressBar pb;

    @Override

    protected void onCreate(Bundle savedInstanceState) {


        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        try {
            this.getSupportActionBar().hide();
        } catch (NullPointerException e) {
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ipget);
        but = findViewById(R.id.start);
        but2 = findViewById(R.id.start2);
        iplocal = findViewById(R.id.iplocal);
        result = findViewById(R.id.process);
        resultport = findViewById(R.id.process2);
        ip = findViewById(R.id.ipaddress);
        back = findViewById(R.id.goBack);
        quick = findViewById(R.id.quick);
        moderate = findViewById(R.id.moderate);
        deep = findViewById(R.id.deep);
        pb = findViewById(R.id.progressBar);
        prog = findViewById(R.id.textView45);
        try {
            WifiManager wifiMgr = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
            WifiInfo wifiInfo = wifiMgr.getConnectionInfo();
            int ip = wifiInfo.getIpAddress();
            String ipAddress = Formatter.formatIpAddress(ip);
            iplocal.append(ipAddress);
            device.add(ipAddress);
        }catch (Exception e){

        }
        quick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search = 100;
                Toast.makeText(getApplicationContext(), "Set to Quick Search!", Toast.LENGTH_SHORT).show();

            }
        });
        moderate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search = 300;
                Toast.makeText(getApplicationContext(), "Set to Moderate Search!", Toast.LENGTH_SHORT).show();
            }

        });
        deep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search = 500;
                Toast.makeText(getApplicationContext(), "Set to Deep Search!", Toast.LENGTH_SHORT).show();

            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getIp.this, MainMenu.class);
                startActivity(intent);
                finish();
            }
        });

        but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                result.setText("");
                but2.setEnabled(false);
                start(result, device, prog);
                but2.setEnabled(true);
            }
        });
        but2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                pb.setVisibility(View.VISIBLE);
                resultport.setText("Getting ports..");
                thread2 = new Thread(){
                    public void run() {
                        try {
                            String va = ip.getText().toString();


                            if (va != "") {
                                if(search == 100) {
                                    int listports[] = {135,80,30, 56, 80, 443, 445, 21, 22, 119, 995, 143, 993, 3306, 2082, 2083, 2086, 2087, 2095, 2096, 2077, 2078, 49664, 49665, 49666, 49667, 25, 26, 587, 808, 909, 101, 303, 307, 102, 194, 903, 381, 500, 512, 514, 513, 516, 518, 519, 520, 521, 4500, 5050, 1010, 2020, 3030, 4040, 5050, 6060, 7070, 9090, 1234,2345,3456,4567,5678,6789,7890,8901,9012,0123,8585, 9595, 1515, 2525, 3535, 6565, 7575, 8585, 1900, 1800, 1700, 9999, 4444, 5555, 1111, 2222, 3333, 6666, 7777, 8888, 4545, 4949, 8080, 8181, 8001, 49668, 49670, 49669, 65535, 65310, 63210, 30420, 53049, 42045};
                                    for (int i = 0; i < listports.length; i++) {
                                        if (checkport(va, listports[i])) {
                                            resultport.append("\nPort " + listports[i] + " is open!");
                                        }
                                    }
                                }else if(search==300){
                                    int listports[] = {30, 56, 80, 443, 445, 21, 22, 119, 995, 143, 993, 3306, 2082, 2083, 2086, 2087, 2095, 2096, 2077, 2078, 49664, 49665, 49666, 49667, 25, 26, 587, 808, 909, 101, 303, 307, 102, 194, 903, 381, 500, 512, 514, 513, 516, 518, 519, 520, 521, 4500, 5050, 1010, 2020, 3030, 4040, 5050, 6060, 7070, 9090, 1234,2345,3456,4567,5678,6789,7890,8901,9012,0123,8585, 9595, 1515, 2525, 3535, 6565, 7575, 8585, 1900, 1800, 1700, 9999, 4444, 5555, 1111, 2222, 3333, 6666, 7777, 8888, 4545, 4949, 8080, 8181, 8001, 49668, 49670, 49669, 65535, 65310, 63210, 30420, 53049, 42045};
                                    for (int i = 0; i < listports.length; i++) {
                                        if (checkport(va, listports[i])) {
                                            resultport.append("\nPort " + listports[i] + " is open!");
                                        }
                                    }
                                }else if(search==500){
                                    ArrayList<Integer> listports = new ArrayList<Integer>(Arrays.asList(30, 56, 80, 443, 445, 21, 22, 119, 995, 143, 993, 3306, 2082, 2083, 2086, 2087, 2095, 2096, 2077, 2078, 49664, 49665, 49666, 49667, 25, 26, 587, 808, 909, 101, 303, 307, 102, 194, 903, 381, 500, 512, 514, 513, 516, 518, 519, 520, 521, 4500, 5050, 1010, 2020, 3030, 4040, 5050, 6060, 7070, 9090, 1234,2345,3456,4567,5678,6789,7890,8901,9012,0123,8585, 9595, 1515, 2525, 3535, 6565, 7575, 8585, 1900, 1800, 1700, 9999, 4444, 5555, 1111, 2222, 3333, 6666, 7777, 8888, 4545, 4949, 8080, 8181, 8001, 49668, 49670, 49669, 65535, 65310, 63210, 30420, 53049, 42045));
                                    for (int i = 0; i < listports.size(); i++) {
                                        if (checkport(va, listports.get(i))) {
                                            resultport.append("\nPort " + listports.get(i) + " is open!");
                                        }
                                    }
                                    for(int i=1; i<9999; i++) {
                                        if (!listports.contains(i)) {
                                            if (checkport(va, i)){
                                                resultport.append("\nPort " + i + " is open!");
                                            }
                                        }
                                    }
                                }
                            pb.setVisibility(View.INVISIBLE);
                            }
                        } catch (Exception e) {
                            e.getLocalizedMessage();
                        }
                    }

                };
                thread2.start();

            }
        });
    }

    static boolean checkip(String val, int time){
        try{
            InetAddress address = InetAddress.getByName(val);
            boolean reachable = address.isReachable(time);
            return reachable;
        } catch (Exception e){
            return false;
        }
    }


    public static boolean checkport(String ip, int port) {
        try (Socket ignored = new Socket(ip, port)) {
            return true;
        } catch (IOException ignored) {
            return false;
        }
    }

    public void start(TextView result, ArrayList<String> device, TextView prog){
        thread = new Thread() {
            public void run() {
                try {
                    int runtime = search;
                    ArrayList<String> availableip = new ArrayList<String>();
                    String arr[] = String.valueOf(device.get(device.size() - 1)).split("\\.");
                    result.append("\nSearching for IP Addresses...");
                    final int[] a = {0};
                    int total = 255;
                    final double[] tot = {0};
                        new Thread(new Runnable() {
                            public void run() {
                                try {
                                while (a[0] <= total) {
                                        double percentage = (Double.valueOf(a[0]) * 100) / 255;
                                        String value = arr[0] + "." + arr[1] + "." + arr[2] + "." + a[0];
                                        tot[0] = percentage;
                                        if (checkip(value, runtime)) {
                                            try {
                                                availableip.add(value);
                                                String hostname = InetAddress.getByName(value).getCanonicalHostName();
                                                result.append("\n\nAvailable IP: " + value + "\nHostName: " + hostname);
                                            } catch (UnknownHostException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                        if (a[0] == 255) {
                                            throw new InterruptedException();
                                        }
                                        prog.post(new Runnable() {
                                        public void run() {
                                            prog.setText("Loading: "+(Math.floor(tot[0] * 100) / 100) + "%");
                                        }
                                        });
                                        a[0]++;
                                }
                                } catch (InterruptedException e) {
                                    result.append("\n\nDONE!");
                                    search = 100;
                                }
                            }

                        }).start();

                } catch (Exception e) {
                    e.getLocalizedMessage();
                }
            }

        };
        thread.start();
    }
}