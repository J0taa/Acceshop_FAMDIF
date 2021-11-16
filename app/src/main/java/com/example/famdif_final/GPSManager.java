package com.example.famdif_final;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

public class GPSManager {
    private static final int GPS_PERMISSIONS_CODE = 1;

    private AppCompatActivity activity;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private Location lastLocation;

    public GPSManager(AppCompatActivity activity) {
        this.activity = activity;
        this.fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this.activity);
        this.lastLocation = null;
    }

    public boolean checkPermissions() {
        int permissionState = ActivityCompat.checkSelfPermission(this.activity, Manifest.permission.ACCESS_FINE_LOCATION);
        return permissionState == PackageManager.PERMISSION_GRANTED;
    }

    public void requestPermissions() {
        ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, GPS_PERMISSIONS_CODE);
    }

    public FusedLocationProviderClient getFusedLocationProviderClient() {
        return fusedLocationProviderClient;
    }
}
