package com.example.hackfest;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.method.ScrollingMovementMethod;
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
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.Timer;
import java.util.TimerTask;

import javax.net.ssl.SSLHandshakeException;

public class MainActivity extends AppCompatActivity {
    final Activity activity = this;
    EditText url;
    Button check, golink;
    TextView textView, textView2;
    ProgressBar pb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        final String[] valueurl = {""};
        int checkProgress = 0;
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        url = findViewById(R.id.link);
        check = findViewById(R.id.check);
        golink = findViewById(R.id.goLink);
        textView = findViewById(R.id.textView3);
        textView2= findViewById(R.id.textView4);
        pb = findViewById(R.id.progressBar);

       check.setOnClickListener(new View.OnClickListener() {
           @SuppressLint("ResourceAsColor")
           @Override
           public void onClick(View v) {
               pb.setVisibility(v.VISIBLE);
               final int[] counter = {0};
               final Timer t = new Timer();
               TimerTask tt = new TimerTask() {
                   @Override
                   public void run()
                   {

                       counter[0] +=2;
                       pb.setProgress(counter[0]);

                       if(counter[0] == 50){
                           try {
                               String val = url.getText().toString().toLowerCase();
                               boolean check = false;
                               String message = null;
                               String urltobeused = null;
                               try {
                                   String removeHttps = removeHttps(val);
                                   String site = "http://" + removeHttps;
                                   int response = response(site);
                                   String contents = "";
                                   int checker = 0;
                                   if(response != 403 || response != 401 || response!=503){
                                   String trueSite = finalURL(site);


                                   String removedHttpTruesite = removeHttps(trueSite);
                                   for(int i=0; i<2; i++) {

                                       if(i == 0) {
                                           String siteTest= "http://" + removedHttpTruesite;
                                           contents = request(siteTest);
                                           if(response != 301) {
                                               if (contents.length() != 0) {
                                                   checker++;
                                               }
                                           }
                                       }else{
                                           try {
                                               String siteTest = "https://" + removedHttpTruesite;
                                               int response2 = response("https://" + removeHttps);
                                               if (response2 == 503) {
                                                   contents = "";
                                               } else {
                                                   contents = request(siteTest);
                                               }
                                           }catch (Exception e){
                                               contents = "";
                                           }
                                       }
                                   }
                                       urltobeused = "http://" + removedHttpTruesite;
                                   }else{
                                       message = "Be Careful! This website can be harmful or a phishing site!";
                                       check = false;
                                   }

                                   if(checker!=1) {
                                       int checkCount = countHTMLs(contents);
                                       message = "";
                                       if (checkCount > 1) {
                                           message = "This website contains ads, it can be harmful or not";

                                       }
                                       check = true;
                                   }else{
                                       message = "Be Careful! This website can be harmful or a phishing site!";
                                   }

                                   if(!check){

                                       textView2.setText("Not Secured");
                                       textView2.setTextColor(getResources().getColor(R.color.red));
                                       textView.setText(message + "\nRedirected Url: " + urltobeused +"\nWebsite Hostname and IP: "+getIpAddress(urltobeused));
                                   }else{
                                       textView2.setText("Secured");
                                       textView2.setTextColor(getResources().getColor(R.color.green));
                                       textView.setText(message + "\nRedirected Url: " + urltobeused +"\nWebsite Hostname and IP: "+getIpAddress(urltobeused));

                                   }
                                   golink.setVisibility(v.VISIBLE);



                               } catch (MalformedURLException e) {
                                   message = "Website might Contain Viruses";
                                   check = false;
                               } catch (Exception e){
                                   e.printStackTrace();
                                   message = "Website not found";
                                   check = false;
                               }
                               valueurl[0] = url.getText().toString().toLowerCase();
                               pb.setVisibility(v.INVISIBLE);

                           }catch (Exception e){
                               e.printStackTrace();
                           }

                       }

                   }
               };

               t.schedule(tt,0,50);


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
        try {
            HttpURLConnection con = (HttpURLConnection) new URL(url).openConnection();
            con.setInstanceFollowRedirects(false);
            con.connect();
            con.getInputStream();

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

    public static String getIpAddress(String url) throws MalformedURLException, UnknownHostException {

            // Fetch IP address by getByName()
            InetAddress ip = InetAddress.getByName(new URL(url)
                    .getHost());

            return ip.toString();
    }
    public static int response(String uri) throws IOException{
        URL url = new URL(uri);
        HttpURLConnection http = (HttpURLConnection)url.openConnection();
        int statusCode = http.getResponseCode();
        return statusCode;
    }
}