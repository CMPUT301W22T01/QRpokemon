package com.qrpokemon.qrpokemon;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;

import com.qrpokemon.qrpokemon.activities.qrscanned.QrScannedActivity;
import com.qrpokemon.qrpokemon.models.DatabaseController;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LocationController {

    private static LocationController currentInstance;
    private DatabaseController databaseController = DatabaseController.getInstance();
    private LocationManager locationManager;


    public static LocationController getCurrentInstance() {
        if (currentInstance == null)
            currentInstance = new LocationController();
        return currentInstance;
    }

    /**
     * get the current position and transform to the city
     * @param location Longitude and latitude string
     * @return
     */
    public String getCity(String location, Context context){
        try{
            // location
            location.split(",");
            List<String> locationList = Arrays.asList(location.split(","));
            String latitudeStr = locationList.get(0);
            String longitudeStr = locationList.get(1);
            Double latitude = Double.parseDouble(latitudeStr);
            Double longitude = Double.parseDouble(longitudeStr);

            Geocoder geocoder = new Geocoder(context);
            List<Address> addresses = null;
            addresses = geocoder.getFromLocation(latitude, longitude,1);
            String country = addresses.get(0).getCountryCode();
            String city = addresses.get(0).getLocality();
            return city;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

    }

}
