package com.qrpokemon.qrpokemon.controllers;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
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

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class LocationController implements OnMapReadyCallback, LocationListener {

    private FusedLocationProviderClient fusedLocationProviderClient;
    private static final int REQUEST_CODE = 101;
    private Location currentLocation = null;
    private SupportMapFragment supportMapFragment;
    private Context context;
    private DatabaseProxy databaseProxy = DatabaseProxy.getInstance();
    private static LocationController currentInstance;
    private ArrayList<String> qrhashList = new ArrayList<>();
    private List<Map>  qrHashList;

    public static LocationController getInstance() {
        if (currentInstance == null)
            currentInstance = new LocationController();

        return currentInstance;
    }

    public void run(Context contextActivity, @Nullable SupportMapFragment supportMapFragmentActivity, LocationManager locationManager, FusedLocationProviderClient fusedLocationProviderClientActivity, @Nullable List<Map> qrList) {
        qrHashList = qrList;
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
    } public void run(Context contextActivity, @Nullable SupportMapFragment supportMapFragmentActivity, LocationManager locationManager, FusedLocationProviderClient fusedLocationProviderClientActivity) {
        run(contextActivity, supportMapFragmentActivity, locationManager, fusedLocationProviderClientActivity, null);
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
                        supportMapFragment.getMapAsync(LocationController.this);
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
     * @param googleMap Google map object
     */
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        LatLng here = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
        if (qrHashList != null) {
            for (Map i : qrHashList) { // loop through each qrcode
                if (i.get("Location") == null) {
                    continue;
                }
                for (String location : (ArrayList<String>) i.get("Location")) { // loop through each qrcode's location arrayList:
                    if (location != null) {
                        String[] parser = location.split(",");

                        if(i.get("Content") == "") {
                            googleMap.addMarker(new MarkerOptions().position(new LatLng(Double.valueOf(parser[0]),Double.valueOf(parser[1]))).title((String)i.get("Identifier")));
                        }
                        else
                        {
                            googleMap.addMarker(new MarkerOptions().position(new LatLng(Double.valueOf(parser[0]),Double.valueOf(parser[1]))).title((String)i.get("Content")));
                        }

                    }
                }
            }
        }
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

    /**
     * A getter method to return current location
     * @return a Location object with Latitude and Longitude data of current user.
     */
    public Location returnLocation() {
        return currentLocation;
    }

    /**
     * A getter method for city details of current Location
     * @return A string represents City name
     */
    public String getCity(Context context) {
        String cityName = null;
        Double lati = currentLocation.getLatitude();
        Double longti = currentLocation.getLongitude();
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        List<Address> address;

        try {
            address = geocoder.getFromLocation(lati,longti, 1);
            if (address.size() == 1) {
                if(address.get(0).getLocality() != null && address.get(0).getLocality().length() > 0){
                    cityName = address.get(0).getLocality();
                    Log.e("LocationController city name is: ", address.get(0).toString() );
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return cityName;
    }

    /**
     * Save qrcode by cityName/regionName
     * Create a new city if city has no qrcode before
     * City stores an HashMap <Location Coordinate, arrayList of qrCodes>
     * @param cityName city Name player is currently at
     * @param coordinate coordinate of location, in formation 'latitude, Longitude'
     * @param context Activity calls it, mostly QrScannedActivity
     * @param qrhash qrHashCode that is passed in
     */
    public void saveLocation (String cityName, String coordinate, Context context, String qrhash) {
        HashMap data = new HashMap();
        DatabaseCallback databaseCallback = new DatabaseCallback(context) {
            @Override
            public void run(List<Map> dataList) {
                if (dataList.isEmpty()){ //this city isn't found, create a new city category
                    Log.e("LocationController: ", "City is new");
                    data.put("Identifier", cityName);
                    qrhashList = new ArrayList<String >();
                    qrhashList.add(qrhash);
                    data.put(coordinate,qrhashList);
                    try {
                        databaseProxy.writeData("LocationIndex",cityName, data, true);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else { // There is qrcode in this city:
                    data.put("Identifier", cityName);
                    Log.e("LocationController: ", "There is qr code in this city");
                    Map cityInfo = dataList.get(0);
                    if (cityInfo.get(coordinate) == null) {} // there is no qr code at this location:
                    else { // There is qr code at this location, append them in the same list which identified by gps location.
                        Log.e("LocationController: ", "There is qr code at the same location!");
                        qrhashList = (ArrayList<String>) cityInfo.get(coordinate);
                    }

                    // update qrhash list at this location
                    qrhashList.add(qrhash);
                    data.put(coordinate,qrhashList);
                    // save this location info to city's information
                    cityInfo.put(coordinate,qrhashList);
                    Log.e("LocationController: ", "Location is now saved with :"+qrhashList.toString());
                    try { //Save City with modified information
                        databaseProxy.writeData("LocationIndex",cityName, data, true);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        try {
            databaseProxy.getData(databaseCallback,new ArrayList<>(), "LocationIndex", cityName, null, "Identifier");
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("Location Controller: ", "city not found!");
        }

    }

}
