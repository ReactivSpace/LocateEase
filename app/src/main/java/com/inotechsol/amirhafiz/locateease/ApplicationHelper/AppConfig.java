package com.inotechsol.amirhafiz.locateease.ApplicationHelper;

import android.Manifest;

/**
 * Created by Amir on 5/2/2017.
 */

public class AppConfig {

    public static boolean calledFromFourSquare = false;
    public static boolean calledFromGooglePlaces = false;
    public static boolean calledFromSEARCHED_GooglePlaces = false;
    public static boolean calledFromSEARCHED_FourSquare = false;
    public static double SearchedLat;
    public static double SearchedLng;
    public static String SearchedName;





    public static final int PERMISSION_REQUEST_CODE = 100;

    public static final String[] PERMISSION_ALL = {
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
    };

}
