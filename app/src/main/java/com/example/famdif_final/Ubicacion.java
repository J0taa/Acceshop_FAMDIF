package com.example.famdif_final;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;

import androidx.annotation.NonNull;

public class Ubicacion implements LocationListener {
    Context cntx;
    LocationManager location;
    String proveedor;


    public Ubicacion(Context cntx) {
        this.cntx = cntx;
        location = (LocationManager) cntx.getSystemService(Context.LOCATION_SERVICE);
        proveedor = LocationManager.NETWORK_PROVIDER;

    }


    @Override
    public void onLocationChanged(@NonNull Location location) {

    }

    @Override
    public void onProviderEnabled(@NonNull String provider) {

    }
}
