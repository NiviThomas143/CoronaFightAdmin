package com.example.coronafightauthorities;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.List;
import java.util.Locale;

public class Login extends AppCompatActivity {


    LocationManager locationManager;
    LocationListener locationListener;
    EditText state_edit;
    EditText dist_edit;
    EditText city_edit;
    Button submit;
    private String apiKey = "AIzaSyCDHI_w0Ii0MZmRgVMXCUZkL70Knhdy34Y";


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0 && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 3000, 0, locationListener);

            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        state_edit = findViewById(R.id.state);
        dist_edit = findViewById(R.id.district);
        city_edit = findViewById(R.id.city);
        submit = findViewById(R.id.submit);

        locationManager = (LocationManager) Login.this.getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {

            @Override
            public void onLocationChanged(Location location) {

                Geocoder geocoder = new Geocoder(Login.this, Locale.getDefault());
                try {
                    List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                    state_edit.setText(addresses.get(0).getAdminArea());
                    city_edit.setText(addresses.get(0).getLocality());
                    dist_edit.setText(addresses.get(0).getSubAdminArea());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            @Override
            public void onProviderEnabled(String provider) {
            }

            @Override
            public void onProviderDisabled(String provider) {
            }
        };

        if (ContextCompat.checkSelfPermission(Login.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(Login.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        } else {
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 3000, 0, locationListener);
        }

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!(state_edit.getText().toString().equals("") || dist_edit.getText().toString().equals("") || city_edit.getText().toString().equals(""))) {
                    splashscreen.sharedPreferences.edit().putBoolean("loggedIn", true).commit();
                    splashscreen.sharedPreferences.edit().putString("state", state_edit.getText().toString()).commit();
                    splashscreen.sharedPreferences.edit().putString("district", dist_edit.getText().toString()).commit();
                    splashscreen.sharedPreferences.edit().putString("city", city_edit.getText().toString()).commit();

                    Toast.makeText(Login.this, "Successful", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(Login.this, MainActivity.class));

                } else {
                    Toast.makeText(Login.this, "Fill all details", Toast.LENGTH_SHORT).show();
                }
            }

        });


    }
}
