package com.example.hackfest;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.Timer;
import java.util.TimerTask;

import javax.net.ssl.SSLHandshakeException;

public class MainActivity extends AppCompatActivity {
    EditText url;
    Button check, golink,back;
    TextView textViews, textView2, textView3;
    final String[] valueurl = {""};
    final int[] g = {View.INVISIBLE};
    ProgressBar pb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        try
        {
            this.getSupportActionBar().hide();
        }
        catch (NullPointerException e){}
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        url = findViewById(R.id.link);
        check = findViewById(R.id.check);
        golink = findViewById(R.id.goLink);
        textViews = findViewById(R.id.textView3);
        textView2= findViewById(R.id.textView4);
        pb = findViewById(R.id.progressBar);
        textView3 = findViewById(R.id.ipAdd);
        back = findViewById(R.id.back);
        textView3.setText("Your public ip address: "+ getPublicIPaddress());
        check.setOnClickListener(new View.OnClickListener() {
           @SuppressLint("ResourceAsColor")
           @Override
           public void onClick(View v) {
               pb.setVisibility(v.VISIBLE);
               final String[] message = {null};
               final String[] title = {null};
               final int[] res = {0};
               final int[] counter = {0};
               final Timer t = new Timer();
               TimerTask tt = new TimerTask() {
                   @Override
                   public void run()
                   {
                       counter[0] +=2;
                       pb.setProgress(counter[0]);
                       boolean check = false;
                       try {
                       if(counter[0] == 20) {
                           try {
                               String val = url.getText().toString().toLowerCase();
                               String urltobeused = null;
                               String removeHttps = removeHttps(val);
                               String site = "http://" + removeHttps;
                               int response = response(site);
                               String contents = "";
                               int checker = 0;
                               if (response != 403 || response != 401 || response != 503) {
                                   String trueSite = finalURL(site);
                                   String removedHttpTruesite = removeHttps(trueSite);
                                   for (int i = 0; i < 2; i++) {
                                       if (i == 0) {
                                           String siteTest = "http://" + removedHttpTruesite;
                                           contents = request(siteTest);
                                           if (response != 301) {
                                               if (contents.length() != 0) {
                                                   checker++;
                                               }
                                           }
                                       } else {
                                           try {
                                               String siteTest = "https://" + removedHttpTruesite;
                                               int response2 = response("https://" + removeHttps);
                                               if (response2 == 503) {
                                                   contents = "";
                                               } else {
                                                   contents = request(siteTest);
                                               }
                                           } catch (Exception e) {
                                               contents = "";
                                           }
                                       }
                                   }
                                   urltobeused = trueSite;
                               } else {
                                   message[0] = "Be Careful! This website can be harmful or a phishing site!";
                                   check = false;
                               }

                               if (checker != 1) {
                                   int checkCount = countHTMLs(contents);
                                   message[0] = "";
                                   if (checkCount > 1) {
                                       message[0] = "This website contains ads, it can be harmful or not";
                                   }
                                   check = true;
                               } else {
                                   message[0] = "Be Careful! This website can be harmful or a phishing site!";
                               }
                               if (!check) {
                                   title[0] = "Not Secured";
                                   res[0] = getResources().getColor(R.color.red);
                                   message[0] = message[0] + "\nRedirected Url: " + urltobeused;
                                   g[0] = v.VISIBLE;

                               } else {
                                   title[0] = "Secured";
                                   res[0] = getResources().getColor(R.color.green);
                                   message[0] = message[0] + "\nRedirected Url: " + urltobeused;
                                   g[0] = v.VISIBLE;
                               }

                               valueurl[0] = url.getText().toString().toLowerCase();
                           } catch (UnknownHostException e) {
                               title[0] = "ERROR 404";
                               res[0] = getResources().getColor(R.color.red);
                               message[0] = "WEBSITE NOT FOUND";
                               pb.setVisibility(v.INVISIBLE);
                               g[0] = v.INVISIBLE;
                           } catch (Exception e) {
                                e.printStackTrace();
                           }

                           textView2.setText(title[0]);
                           textView2.setTextColor(res[0]);
                           textViews.setText(message[0]);

                       }else if(counter[0] == 24){
                           pb.setVisibility(v.INVISIBLE);
                           golink.setVisibility(g[0]);
                           t.cancel();
                       }



                       }catch (Exception e){

                           e.printStackTrace();
                       }
                   }

               };
               t.schedule(tt,0,24);
                   }
               });

            golink.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String removeHttps = removeHttps(valueurl[0]);
                    String site = "http://" + removeHttps;
                    Uri uri = Uri.parse(site); // missing 'http://' will cause crashed
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent);
                }
            });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MainMenu.class);
                startActivity(intent);
                finish();
            }
        });

    }
    public static String removeHttps(String val){
        String protocol[] = {"http://", "https://"};
        String result = val;
            if(val.contains(protocol[0])) {
                result = result.substring(7);
            }
            if(val.contains(protocol[1])){
                result = result.substring(8);
            }
        return result;
    }
    public static int countHTMLs(String val){
        int count = 0;
        String splitString[] = val.split(" ");
        for(int i=0; i<splitString.length; i++){
            if(splitString[i] == "<html"){
                count++;
            }
            if(splitString[i].contains("ads")){
                count++;
            }
        }
        return count;
    }

    public static String finalURL(String url) throws IOException {
            HttpURLConnection con = (HttpURLConnection) new URL(url).openConnection();
            con.setInstanceFollowRedirects(false);
            con.connect();
            con.getInputStream();
        try {
            if (con.getResponseCode() == HttpURLConnection.HTTP_MOVED_PERM || con.getResponseCode() == HttpURLConnection.HTTP_MOVED_TEMP) {

                String redirectUrl = con.getHeaderField("Location");
                return finalURL(redirectUrl);
            } else {
                return url;
            }
        }catch (Exception e){
            return url;
        }
    }

    public static String request(String uri) throws IOException {
        StringBuilder sb = new StringBuilder();
        URL url = new URL(uri);
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            BufferedReader bin = new BufferedReader(new InputStreamReader(in));
            String inputLine;
            while ((inputLine = bin.readLine()) != null) {
                sb.append(inputLine);
            }
        } catch(SSLHandshakeException e){
            return "";
        }finally {
            urlConnection.disconnect();
        }
        return sb.toString();
    }

    public static int response(String uri){
        URL url = null;
        int statusCode =0;
        try {
            url = new URL(uri);
            HttpURLConnection http = null;
            http = (HttpURLConnection)url.openConnection();
            statusCode = http.getResponseCode();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return statusCode;
    }
    public static String getPublicIPaddress(){
        String ip = null;
        try {
        URL whatismyip = new URL("http://checkip.amazonaws.com");
        BufferedReader in = new BufferedReader(new InputStreamReader(
                whatismyip.openStream()));
        ip = in.readLine(); //you get the IP as a String
        }catch (Exception e){
            e.printStackTrace();
        }
        return ip;
    }
}