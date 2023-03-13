package com.example.hackfest;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class Location extends AppCompatActivity {

    private GpsTracker gpsTracker;
    TextView lati,longi, addr,cityy,state2,country2,postal, known2;
    Button back, refresh;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        try {
            this.getSupportActionBar().hide();
        } catch (NullPointerException e) {
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.location);
        lati = (TextView)findViewById(R.id.latitude);
        longi = (TextView)findViewById(R.id.longitude);
        addr = (TextView)findViewById(R.id.address);
        cityy = (TextView)findViewById(R.id.city);
        state2 = (TextView)findViewById(R.id.state);
        country2 = (TextView)findViewById(R.id.country);
        postal = (TextView)findViewById(R.id.postal);
        known2 = (TextView)findViewById(R.id.knownname);
        back = findViewById(R.id.buttonbak);
        refresh = findViewById(R.id.refresh);
        try {
            getLocation();
        } catch (IOException e) {
            e.printStackTrace();
        }
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Location.this, MainMenu.class);
                startActivity(intent);
                finish();
            }
        });
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Location.this, Location.class);
                startActivity(intent);
                finish();
            }
        });
    }

    public void getLocation() throws IOException {
        gpsTracker = new GpsTracker(Location.this);
        if(gpsTracker.canGetLocation()){
            double latitude = gpsTracker.getLatitude();
            double longitude = gpsTracker.getLongitude();
            Geocoder geocoder;
            List<Address> addresses;
            geocoder = new Geocoder(this, Locale.getDefault());

            addresses = geocoder.getFromLocation(latitude, longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5

            String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
            String city = addresses.get(0).getLocality();
            String state = addresses.get(0).getAdminArea();
            String country = addresses.get(0).getCountryName();
            String postalCode = addresses.get(0).getPostalCode();
            String knownName = addresses.get(0).getFeatureName();
            lati.setText("Lat: "+latitude);
            longi.setText("Long: "+longitude);
            addr.setText("Address: "+address);
            cityy.setText("City: "+city);
            state2.setText("State: "+state);
            country2.setText("Country: "+country);
            postal.setText("Postal Code: "+postalCode);
            known2.setText("Known: "+knownName);

        }else{
            gpsTracker.showSettingsAlert();
        }
    }
}