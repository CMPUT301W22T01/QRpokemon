package com.qrpokemon.qrpokemon.activities.map;

import android.content.Context;
import android.location.LocationManager;
import android.util.Log;

import androidx.annotation.Nullable;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.maps.SupportMapFragment;
import com.qrpokemon.qrpokemon.LocationController;
import com.qrpokemon.qrpokemon.models.DatabaseCallback;
import com.qrpokemon.qrpokemon.models.QrCodeController;

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

    public void getAllQr(DatabaseCallback databaseCallback, List<Map> result) {
        try {
            qrCodeController.getQR(databaseCallback, result,null);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("MapController: ", "Invalid collection name");
        }
    }
}
