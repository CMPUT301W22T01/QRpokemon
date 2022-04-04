package com.qrpokemon.qrpokemon.controllers;

import android.content.Context;
import android.location.LocationManager;
import android.util.Log;

import androidx.annotation.Nullable;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.maps.SupportMapFragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MapController {
    private LocationController locationController = LocationController.getInstance();
    private static MapController currentInstance;
    private QrCodeController qrCodeController = QrCodeController.getInstance();

    public static MapController getInstance() {
        if (currentInstance == null)
            currentInstance = new MapController();

        return currentInstance;
    }

    /**
     * Finds a qr code an pass it's location to location controller therefore display it on MapActivity
     * @param contextActivity MapActivity
     * @param supportMapFragmentActivity MapActivity that has a map fragment
     * @param locationManager Handles location passed from GPS
     * @param fusedLocationProviderClientActivity Client whom location needs to be recorded
     */
    public void run(Context contextActivity, @Nullable SupportMapFragment supportMapFragmentActivity, LocationManager locationManager, FusedLocationProviderClient fusedLocationProviderClientActivity) {
        DatabaseCallback databaseCallback = new DatabaseCallback(contextActivity) {
            @Override
            public void run(List<Map> dataList) {
                if (dataList.isEmpty()) { // no QR code globally:
                    locationController.run(contextActivity, supportMapFragmentActivity, locationManager, fusedLocationProviderClientActivity, null);
                } else {
                    locationController.run(contextActivity, supportMapFragmentActivity, locationManager, fusedLocationProviderClientActivity, dataList);
                }
            }
        };
        try {
            qrCodeController.getQR(databaseCallback,new ArrayList<>(),null);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
