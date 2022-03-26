package com.qrpokemon.qrpokemon;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationRequest;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

public class MapController implements OnMapReadyCallback, LocationListener {
    //TODO: add location in an arrayList<String> with latitude/longtitude
    // then pass qrcodes to Activity, ready for display
    private LocationRequest locationRequest;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private static final int REQUEST_CODE = 101;
    private Location currentLocation = null;
    private LocationManager locationManager;
    private SupportMapFragment supportMapFragment;
    private Context context;
    private static MapController currentInstance;

    public static MapController getInstance() {
        if (currentInstance == null)
            currentInstance = new MapController();

        return currentInstance;
    }

    public void run(Context contextActivity, @Nullable SupportMapFragment supportMapFragmentActivity, LocationManager locationManager, FusedLocationProviderClient fusedLocationProviderClientActivity) {

        context = contextActivity;
        supportMapFragment = supportMapFragmentActivity;
        fusedLocationProviderClient = fusedLocationProviderClientActivity;
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity) context,new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
        }
        if (currentLocation == null){
            Log.e("MapController ","currentLocation is null.");
            try{
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
            } catch(Exception e){ //if permission is not given:
                ActivityCompat.requestPermissions((Activity) context,new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
            }
        } else {
            getCurrentLocation();
        }
    }

    /**
     * Check permission for device, ask permission from user if not given.
     * Get current location of the device
     */
    public void getCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions((Activity) context,new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
        }
        Task<Location> task = fusedLocationProviderClient.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(@NonNull Location location) {
                if (location != null) {
                    currentLocation = location;
                    Log.e("getCurrentLocation: ", location.toString());
                    if (supportMapFragment != null){
                        supportMapFragment.getMapAsync(MapController.this);
                    }
                }
                if (location == null) {
                    getCurrentLocation();
//                    Log.e("MainActivity: ", "location is null!!!!!!");
                }
            }
        });
    }

    /**
     * A callback function, triggered once SupportMapFragment gets declared
     * It set the focus of view to current location
     * @param googleMap
     */
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        LatLng here = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
        googleMap.addMarker(new MarkerOptions().position(here).title("Current Location"));
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(here, 15));
        Log.e("onMapReady: ", "Map is now ready");
    }


    @Override
    public void onLocationChanged(@NonNull Location location) {
        if (currentLocation == null){
            currentLocation = location;
            Log.e("Latitude",location.toString());
            getCurrentLocation();
        }

    }

    public Location returnLocation() {
        return currentLocation;
    }
}
