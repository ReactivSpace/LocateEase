package com.inotechsol.amirhafiz.locateease;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.graphics.Typeface;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.inotechsol.amirhafiz.locateease.ApplicationHelper.AppConfig;
import com.inotechsol.amirhafiz.locateease.ApplicationHelper.LeftMenuNavigation;
import com.inotechsol.amirhafiz.locateease.ApplicationHelper.ListAdapter_leftNav;
import com.inotechsol.amirhafiz.locateease.Fragments.FourSquare_GooglePlaces_Fragment;
import com.inotechsol.amirhafiz.locateease.Fragments.Go_Fragment;
import com.inotechsol.amirhafiz.locateease.Fragments.MapsAllDetailFragment;
import com.inotechsol.amirhafiz.locateease.Fragments.NavigateContainer_Fragment;
import com.inotechsol.amirhafiz.locateease.SlidingMenuClasses.SlidingMenu;

import java.lang.reflect.InvocationTargetException;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity implements
        ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {


    public static InterstitialAd mInterstitialAd;
    private AdView mAdView;
    public static int AdCounter = 0;


    public static boolean isMapsAllDetailFragment_Inflated = false;
    public static boolean isGoFragmentInflated = false;


    public static String MorningEvening;
    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;
    LocationRequest mLocationRequest;
//    ProgressBar _progressBar;

    private static final int REQUEST_CODE = 10001;
    public static String PlaceType = "hospital";
    public static int PlaceDrawable;
    public static String FourSqureCatID = "";
    public static String Address;
    public static String Name;
    public static String Source;
    public static String LocationDistance;
    public static String ToSearch_FourSquare_And_Google;
    Typeface mRobotoLight, mRobotoRegular;


    public static double latitude = 0.0;
    public static double longitude = 0.0;

    TextView actionbar_tv;
    ImageView actionbar_menu_iv;
    LeftMenuNavigation[] mleftMenuNavigation;

    ListAdapter mExpendiablelistAdapter;
    ListView slidingmenu_left_drawer;

    SlidingMenu menuS;
    public static Activity mContext;
    GPSTracker tracker;


    public String getMorningAfternoonEvening() {

        String strReturn = "";

        Calendar c = Calendar.getInstance();
        int timeOfDay = c.get(Calendar.HOUR_OF_DAY);

//        if(timeOfDay >= 0 && timeOfDay < 12){
//            strReturn = "Good Morning";
//        }else if(timeOfDay >= 12 && timeOfDay < 16){
//            strReturn = "Good Afternoon";
//        }else if(timeOfDay >= 16 && timeOfDay < 21){
//            strReturn = "Good Evening";
//        }else if(timeOfDay >= 21 && timeOfDay < 24){
//            strReturn = "Good Night";
//        }

        if (timeOfDay >= 0 && timeOfDay < 6) {
            strReturn = "Good Evening";//"Good Night";
        } else if (timeOfDay >= 7 && timeOfDay < 12) {
            strReturn = "Good Morning";
        } else if (timeOfDay >= 12 && timeOfDay < 18) {
            strReturn = "Good Afternoon";
        } else if (timeOfDay >= 18 && timeOfDay < 21) {
            strReturn = "Good Evening";
        } else if (timeOfDay >= 21 && timeOfDay < 24) {
            strReturn = "Good Evening";//"Good Night";
        }


        return strReturn;
    }


    /*Ending the updates for the location service*/
    @Override
    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }


    @Override
    protected void onPostResume() {
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
        super.onPostResume();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

//        if(((Fragment)getSupportFragmentManager().findFragmentByTag("go_fragment")).isVisible()){
//            ((Go_Fragment)getSupportFragmentManager().findFragmentByTag("go_fragment")).hideActionBar();
//        }

    }

    public void hideShowActionBar(boolean isVisible) {

        if (isVisible) {
            getSupportActionBar().show();
        } else {
            getSupportActionBar().hide();
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.mContext = MainActivity.this;
        ///Bannar Ads
        MobileAds.initialize(getApplicationContext(), getResources().getString(R.string.add_mob_app_id));
        mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().addTestDevice("1691DAEE982CCB1A4867347A7BF3F349").build();
        mAdView.loadAd(adRequest);


        MainActivity.MorningEvening = getMorningAfternoonEvening();

        mRobotoLight = Typeface.createFromAsset(mContext.getAssets(), "fonts/Roboto-Light.ttf");
        mRobotoRegular = Typeface.createFromAsset(mContext.getAssets(), "fonts/Roboto-Regular.ttf");


//        mGoogleApiClient = new GoogleApiClient.Builder(this)
//                .addConnectionCallbacks(this)
//                .addOnConnectionFailedListener(this)
//                .addApi(LocationServices.API)
//                .build();


//        else
        //Toast.makeText(this, "Not Connected!", Toast.LENGTH_SHORT).show();


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ActivityCompat.requestPermissions(this, AppConfig.PERMISSION_ALL, AppConfig.PERMISSION_REQUEST_CODE);
            if ((ContextCompat.checkSelfPermission(getApplicationContext(),
                    Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
                    &&
                    (ContextCompat.checkSelfPermission(getApplicationContext(),
                            Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED)) {

                mGoogleApiClient = new GoogleApiClient.Builder(this)
                        .addConnectionCallbacks(this)
                        .addOnConnectionFailedListener(this)
                        .addApi(LocationServices.API)
                        .build();


                if (mGoogleApiClient != null) {
                    mGoogleApiClient.connect();
                }

//            Toast.makeText(getApplicationContext(), "Permission Granted", Toast.LENGTH_SHORT).show();
            }
        }else{
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();


            if (mGoogleApiClient != null) {
                mGoogleApiClient.connect();
            }
        }


//        if (mGoogleApiClient != null) {
//            mGoogleApiClient.connect();
//        }



        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setDisplayUseLogoEnabled(false);
        getSupportActionBar().setDisplayShowHomeEnabled(false);


        LayoutInflater inflator = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View customActionBarView = inflator.inflate(R.layout.action_bar, null);

        actionbar_tv = (TextView) customActionBarView.findViewById(R.id.actionbar_tv);
        actionbar_tv.setText(getApplicationContext().getResources().getString(R.string.app_name));

        ActionBar.LayoutParams params = new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT);
        getSupportActionBar().setCustomView(customActionBarView, params);

        Toolbar parent = (Toolbar) customActionBarView.getParent();
        parent.setContentInsetsAbsolute(0, 0);
        getSupportActionBar().show();

        //Navigation Bar menu Icon
        actionbar_menu_iv = (ImageView) customActionBarView.findViewById(R.id.actionbar_menu_iv);
        actionbar_menu_iv.setEnabled(true);
        actionbar_menu_iv.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                menuS.showMenu();
            }
        });


        menuS = new SlidingMenu(this);
        menuS.setBehindOffset(100);//(100);
        menuS.setMode(SlidingMenu.LEFT);
        menuS.setMenu(R.layout.slidingmenu_layout);
        menuS.setFadeDegree(0.35f);
        menuS.setFadeEnabled(true);
        /////////////////////////////////////////
        //Checking the Screen Resolution of Phone
        WindowManager wm = (WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        display.getMetrics(metrics);
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;
        float dp = 7f;
        float fpixels = metrics.density * dp;
        menuS.setShadowWidth((int) (fpixels + 0.5f));
        menuS.setShadowDrawable(R.drawable.background_shadow);
        menuS.attachToActivity(this, SlidingMenu.SLIDING_WINDOW);


        mleftMenuNavigation = new LeftMenuNavigation[37];

        //Place API Key
        //https://developers.google.com/places/web-service/get-api-key
        //https://developers.google.com/places/supported_types

        mleftMenuNavigation[0] = new LeftMenuNavigation(R.drawable.airport_marker, "Airport", "airport", "4bf58dd8d48988d1ed931735");
        mleftMenuNavigation[1] = new LeftMenuNavigation(R.drawable.hospital_marker, "Hospital", "hospital", "4bf58dd8d48988d196941735");
        mleftMenuNavigation[2] = new LeftMenuNavigation(R.drawable.atm_marker, "ATM", "atm", "52f2ab2ebcbc57f1066b8b56");
        mleftMenuNavigation[3] = new LeftMenuNavigation(R.drawable.bakery_marker, "Bakery", "bakery", "4bf58dd8d48988d16a941735");
        mleftMenuNavigation[4] = new LeftMenuNavigation(R.drawable.bank_marker, "Bank", "bank", "4bf58dd8d48988d10a951735");
        mleftMenuNavigation[5] = new LeftMenuNavigation(R.drawable.bar_marker, "Bar", "bar", "4bf58dd8d48988d112941735");
        mleftMenuNavigation[6] = new LeftMenuNavigation(R.drawable.cafe_marker, "Cafe", "cafe", "4bf58dd8d48988d16d941735");
        mleftMenuNavigation[7] = new LeftMenuNavigation(R.drawable.rent_a_car_marker, "Car Rental", "car_rental", "4bf58dd8d48988d1ef941735");
        mleftMenuNavigation[8] = new LeftMenuNavigation(R.drawable.car_repair_marker, "Car Repair", "car_repair", "4f04ae1f2fb6e1c99f3db0ba");//Car wash
        mleftMenuNavigation[9] = new LeftMenuNavigation(R.drawable.casino_marker, "Casino", "casino", "4bf58dd8d48988d17c941735");
        mleftMenuNavigation[10] = new LeftMenuNavigation(R.drawable.dentist_marker, "Dentist", "dentist", "4bf58dd8d48988d178941735");
        mleftMenuNavigation[11] = new LeftMenuNavigation(R.drawable.doctor_marker, "Doctor", "doctor", "4bf58dd8d48988d177941735");
        mleftMenuNavigation[12] = new LeftMenuNavigation(R.drawable.gas_station_marker, "Gas Station", "gas_station", "4bf58dd8d48988d113951735");
        mleftMenuNavigation[13] = new LeftMenuNavigation(R.drawable.gym_marker, "Gym", "gym", "4bf58dd8d48988d175941735");
        mleftMenuNavigation[14] = new LeftMenuNavigation(R.drawable.hair_care_marker, "Hair Care", "hair_care", "4bf58dd8d48988d110951735");
        mleftMenuNavigation[15] = new LeftMenuNavigation(R.drawable.hardware_marker, "Hardware Store", "hardware_store", "4bf58dd8d48988d112951735");
        mleftMenuNavigation[16] = new LeftMenuNavigation(R.drawable.temple_marker, "Hindu Temple", "hindu_temple", "52e81612bcbc57f1066b7a3f");
        mleftMenuNavigation[17] = new LeftMenuNavigation(R.drawable.zoo_marker, "Zoo", "zoo", "4bf58dd8d48988d17b941735");
        mleftMenuNavigation[18] = new LeftMenuNavigation(R.drawable.verternity_marker, "Veterinary Care", "veterinary_care", "4d954af4a243a5684765b473");
        mleftMenuNavigation[19] = new LeftMenuNavigation(R.drawable.university_marker, "University", "university", "4d4b7105d754a06372d81259");
        mleftMenuNavigation[20] = new LeftMenuNavigation(R.drawable.travel_agency_marker, "Travel Agency", "travel_agency", "4f04b08c2fb6e1c99f3db0bd");
        mleftMenuNavigation[21] = new LeftMenuNavigation(R.drawable.train_station_marker, "Train Station", "train_station", "4bf58dd8d48988d129951735");
        mleftMenuNavigation[22] = new LeftMenuNavigation(R.drawable.stadium_marker, "Stadium", "stadium", "4bf58dd8d48988d184941735");
        mleftMenuNavigation[23] = new LeftMenuNavigation(R.drawable.shopping_mall, "Shopping Mall", "shopping_mall", "4bf58dd8d48988d1fd941735");
        mleftMenuNavigation[24] = new LeftMenuNavigation(R.drawable.school_marker, "School", "school", "4bf58dd8d48988d13b941735");
        mleftMenuNavigation[25] = new LeftMenuNavigation(R.drawable.resturant_marker, "Restaurant", "restaurant", "4bf58dd8d48988d128941735");//cafetaria
        mleftMenuNavigation[26] = new LeftMenuNavigation(R.drawable.police_marker, "Police", "police", "4bf58dd8d48988d12e941735");//
        mleftMenuNavigation[27] = new LeftMenuNavigation(R.drawable.city_post_office_marker, "Post Office", "post_office", "4bf58dd8d48988d172941735");
        mleftMenuNavigation[28] = new LeftMenuNavigation(R.drawable.pharmacy_store, "Pharmacy", "pharmacy", "4bf58dd8d48988d10f951735");
        mleftMenuNavigation[29] = new LeftMenuNavigation(R.drawable.petstore_marker, "Pet Store", "pet_store", "4bf58dd8d48988d100951735");
        mleftMenuNavigation[30] = new LeftMenuNavigation(R.drawable.night_club, "Night Club", "night_club", "4bf58dd8d48988d11a941735");//Other Night Life
        mleftMenuNavigation[31] = new LeftMenuNavigation(R.drawable.museum_marker, "Museum", "museum", "4bf58dd8d48988d181941735");
        mleftMenuNavigation[32] = new LeftMenuNavigation(R.drawable.mosque_marker, "Mosque", "mosque", "4bf58dd8d48988d138941735");
        mleftMenuNavigation[33] = new LeftMenuNavigation(R.drawable.library_marker, "Library", "library", "4bf58dd8d48988d12f941735");
        mleftMenuNavigation[34] = new LeftMenuNavigation(R.drawable.jewlery_marker, "Jewelry Store", "jewelry_store", "4bf58dd8d48988d111951735");
        mleftMenuNavigation[35] = new LeftMenuNavigation(R.drawable.insurance_agency, "Insurance Agency", "insurance_agency", "58daa1558bbb0b01f18ec1f1");
        mleftMenuNavigation[36] = new LeftMenuNavigation(R.drawable.bus_station, "Bus Station", "bus_station", "4bf58dd8d48988d1fe931735");


        RelativeLayout navigate_rl = (RelativeLayout) findViewById(R.id.navigate_rl);
        navigate_rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                menuS.showContent(false);
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager
                        .beginTransaction()
                        .replace(R.id.contentPanel, new NavigateContainer_Fragment(), "navigate_frg")
                        .addToBackStack("navigate_frg")
                        .commit();
            }
        });
        slidingmenu_left_drawer = (ListView) findViewById(R.id.slidingmenu_left_drawer);


        //slidingmenu_left_drawer.addHeaderView(headerView);
        mExpendiablelistAdapter = new ListAdapter_leftNav(this, mleftMenuNavigation);
        slidingmenu_left_drawer.setAdapter(mExpendiablelistAdapter);
        slidingmenu_left_drawer.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                MainActivity.AdCounter++;
                if (MainActivity.AdCounter % 5 == 0) {
                    if (MainActivity.mInterstitialAd.isLoaded()) {
                        MainActivity.mInterstitialAd.show();
                    }
                }


                menuS.showContent(false);
                PlaceType = mleftMenuNavigation[position].value;
                PlaceDrawable = mleftMenuNavigation[position].icon;
                FourSqureCatID = mleftMenuNavigation[position].FSCatID;

                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager
                        .beginTransaction()
                        .replace(R.id.contentPanel, new FourSquare_GooglePlaces_Fragment(), "foursquare_googleplaces")
                        .addToBackStack("foursquare_googleplaces")
                        .commit();


            }
        });


        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId(getResources().getString(R.string.add_mob_intercial_id));

        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                requestNewInterstitial();
            }
        });

        requestNewInterstitial();


//        FragmentManager fragmentManager = getSupportFragmentManager();
//        fragmentManager
//                .beginTransaction()
//                .replace(R.id.contentPanel,new MapsAllDetailFragment(),"home_frg")
////                .addToBackStack("test")
//                .commit();


    }

    private void requestNewInterstitial() {
        AdRequest adRequest = new AdRequest.Builder()
                //.addTestDevice("SEE_YOUR_LOGCAT_TO_GET_YOUR_DEVICE_ID")
                .build();

        mInterstitialAd.loadAd(adRequest);
    }

    public static Point getNavigationBarSize(Context context) {
        Point appUsableSize = getAppUsableScreenSize(context);
        Point realScreenSize = getRealScreenSize(context);

        // navigation bar on the right
        if (appUsableSize.x < realScreenSize.x) {
            return new Point(realScreenSize.x - appUsableSize.x, appUsableSize.y);
        }

        // navigation bar at the bottom
        if (appUsableSize.y < realScreenSize.y) {
            return new Point(appUsableSize.x, realScreenSize.y - appUsableSize.y);
        }

        // navigation bar is not present
        return new Point();
    }

    public static Point getAppUsableScreenSize(Context context) {
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        return size;
    }

    public static Point getRealScreenSize(Context context) {
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        Point size = new Point();

        if (Build.VERSION.SDK_INT >= 17) {
            display.getRealSize(size);
        } else if (Build.VERSION.SDK_INT >= 14) {
            try {
                size.x = (Integer) Display.class.getMethod("getRawWidth").invoke(display);
                size.y = (Integer) Display.class.getMethod("getRawHeight").invoke(display);
            } catch (IllegalAccessException e) {
            } catch (InvocationTargetException e) {
            } catch (NoSuchMethodException e) {
            }
        }

        return size;
    }

    public static void shutDownApp() {

        System.exit(1);
    }

    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {

        //respond to menu item selection
        switch (item.getItemId()) {
            case R.id.rate_app:
                MainActivity.AdCounter++;
                if (MainActivity.AdCounter % 5 == 0) {
                    if (MainActivity.mInterstitialAd.isLoaded()) {
                        MainActivity.mInterstitialAd.show();
                    }
                }

                Uri uri = Uri.parse("market://details?id=" + getPackageName());
                Intent myAppLinkToMarket = new Intent(Intent.ACTION_VIEW, uri);
                try {
                    startActivity(myAppLinkToMarket);
                } catch (ActivityNotFoundException e) {
                    Toast.makeText(this, " unable to find market app", Toast.LENGTH_LONG).show();
                }
                break;

                //return true;
            case R.id.more_apps:
                MainActivity.AdCounter++;
                if (MainActivity.AdCounter % 5 == 0) {
                    if (MainActivity.mInterstitialAd.isLoaded()) {
                        MainActivity.mInterstitialAd.show();
                    }
                }


                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://search?q=pub:" + "\"Inotechsol\""));
                startActivity(intent);
                break;

                //Toast.makeText(MainActivity.mContext, "More Apps", Toast.LENGTH_LONG).show();

            case R.id.share_app:


                try {
                    Intent i = new Intent(Intent.ACTION_SEND);
                    i.setType("text/plain");
                    i.putExtra(Intent.EXTRA_SUBJECT, "Locate Ease");
                    String sAux = "\nLet me recommend you this application\n\n";
                    sAux = sAux + "https://play.google.com/store/apps/details?id=" + getPackageName();
                    i.putExtra(Intent.EXTRA_TEXT, sAux);
                    startActivity(Intent.createChooser(i, "choose one"));
                } catch(Exception e) {
                    //e.toString();
                }


        }
        return true;
    }

    @Override
    protected void onPause() {
        mAdView.pause();
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mAdView.resume();
    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {
        settingRequest();
    }

    @Override
    public void onConnectionSuspended(int i) {
        Toast.makeText(this, "Connection Suspended!", Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(this, "Connection Failed!", Toast.LENGTH_SHORT).show();
        if (connectionResult.hasResolution()) {
            try {
                // Start an Activity that tries to resolve the error
                connectionResult.startResolutionForResult(this, 90000);
            } catch (IntentSender.SendIntentException e) {
                e.printStackTrace();
            }
        } else {
            Log.i("Current Location", "Location services connection failed with code " + connectionResult.getErrorCode());
        }
    }

    /*Method to get the enable location settings dialog*/
    public void settingRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(5000);//(10000);    // 10 seconds, in milliseconds
        mLocationRequest.setFastestInterval(1000);   // 1 second, in milliseconds
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(mLocationRequest);

        PendingResult<LocationSettingsResult> result =
                LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient,
                        builder.build());

        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {

            @Override
            public void onResult(LocationSettingsResult result) {
                final Status status = result.getStatus();
                final LocationSettingsStates state = result.getLocationSettingsStates();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        // All location settings are satisfied. The client can
                        // initialize location requests here.
                        getLocation();
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        // Location settings are not satisfied, but this can be fixed
                        // by showing the user a dialog.
                        try {
                            // Show the dialog by calling startResolutionForResult(),
                            // and check the result in onActivityResult().
                            status.startResolutionForResult(MainActivity.this, 1000);
                        } catch (IntentSender.SendIntentException e) {
                            // Ignore the error.
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        // Location settings are not satisfied. However, we have no way
                        // to fix the settings so we won't show the dialog.
                        break;
                }
            }

        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        final LocationSettingsStates states = LocationSettingsStates.fromIntent(data);
        switch (requestCode) {
            case 1000:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        // All required changes were successfully made
                        getLocation();
                        break;
                    case Activity.RESULT_CANCELED:
                        // The user was asked to change settings, but chose not to
                        Toast.makeText(this, "Location Service not Enabled", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        break;
                }
                break;
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case AppConfig.PERMISSION_REQUEST_CODE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
//                    moveToNextActivity();

                    if(mGoogleApiClient==null){

                        mGoogleApiClient = new GoogleApiClient.Builder(this)
                                .addConnectionCallbacks(this)
                                .addOnConnectionFailedListener(this)
                                .addApi(LocationServices.API)
                                .build();

                    }


                    if (mGoogleApiClient != null && !mGoogleApiClient.isConnected()) {
                        mGoogleApiClient.connect();
                    }

                } else {

                    Toast.makeText(getApplicationContext(),"Permission is required for App",Toast.LENGTH_LONG).show();
                    ActivityCompat.requestPermissions(this, AppConfig.PERMISSION_ALL, AppConfig.PERMISSION_REQUEST_CODE);
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }



    public void getLocation() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // Call some material design APIs here

            ActivityCompat.requestPermissions(this, AppConfig.PERMISSION_ALL, AppConfig.PERMISSION_REQUEST_CODE);
            if ((ContextCompat.checkSelfPermission(getApplicationContext(),
                    Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
                    &&
                    (ContextCompat.checkSelfPermission(getApplicationContext(),
                            Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED)) {

                final Dialog aboutusDialog = new Dialog(MainActivity.this);
                aboutusDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                aboutusDialog.setContentView(R.layout.capturing_lat_lng);
                aboutusDialog.setCancelable(false);
                aboutusDialog.show();

                TextView smple_text = (TextView) aboutusDialog.findViewById(R.id.smple_text);
                smple_text.setTypeface(mRobotoRegular);

                TextView second_txt = (TextView) aboutusDialog.findViewById(R.id.second_txt);
                second_txt.setTypeface(mRobotoLight);


            /*Getting the location after aquiring location service*/
                mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                        mGoogleApiClient);


                new CountDownTimer(4000, 1000) {

                    public void onTick(long millisUntilFinished) {
                        //here you can have your logic to set text to edittext
                    }

                    public void onFinish() {
                        aboutusDialog.cancel();
                        if (mLastLocation != null) {
//                _progressBar.setVisibility(View.INVISIBLE);
                            latitude = mLastLocation.getLatitude();
                            longitude = mLastLocation.getLongitude();

//                        Toast.makeText(getApplicationContext(), "Lat_: " + latitude + "  Lng_:" + longitude, Toast.LENGTH_LONG).show();


                            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, MainActivity.this);

                            FragmentManager fragmentManager = getSupportFragmentManager();
                            fragmentManager
                                    .beginTransaction()
                                    .replace(R.id.contentPanel, new MapsAllDetailFragment(), "home_frg")
//                .addToBackStack("test")
                                    .commit();
//                        isMapsAllDetailFragment_Inflated = true;

                        } else {
                /*if there is no last known location. Which means the device has no data for the loction currently.
                * So we will get the current location.
                * For this we'll implement Location Listener and override onLocationChanged*/
                            Log.i("Current Location", "No data for location found");

                            if (!mGoogleApiClient.isConnected())
                                mGoogleApiClient.connect();

//                        Toast.makeText(getApplicationContext(), "cannot find the current location ", Toast.LENGTH_LONG).show();

                            ActivityCompat.requestPermissions(MainActivity.mContext, AppConfig.PERMISSION_ALL, AppConfig.PERMISSION_REQUEST_CODE);

                            if ((ContextCompat.checkSelfPermission(getApplicationContext(),
                                    Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
                                    &&
                                    (ContextCompat.checkSelfPermission(getApplicationContext(),
                                            Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED)) {


                                LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, MainActivity.this);


                            }

                        }

                        FragmentManager fragmentManager = getSupportFragmentManager();
                        fragmentManager
                                .beginTransaction()
                                .replace(R.id.contentPanel, new MapsAllDetailFragment(), "home_frg")
//                .addToBackStack("test")
                                .commit();
//                    isMapsAllDetailFragment_Inflated = true;

                    }

                }.start();

            } else {
//                Toast.makeText(getApplicationContext(),"Permission not Granted",Toast.LENGTH_LONG).show();

                ActivityCompat.requestPermissions(this, AppConfig.PERMISSION_ALL, AppConfig.PERMISSION_REQUEST_CODE);

            }


        } else {
            // Implement this feature without material design


            final Dialog aboutusDialog = new Dialog(MainActivity.this);
            aboutusDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            aboutusDialog.setContentView(R.layout.capturing_lat_lng);
            aboutusDialog.setCancelable(false);
            aboutusDialog.show();

            TextView smple_text = (TextView) aboutusDialog.findViewById(R.id.smple_text);
            smple_text.setTypeface(mRobotoRegular);

            TextView second_txt = (TextView) aboutusDialog.findViewById(R.id.second_txt);
            second_txt.setTypeface(mRobotoLight);


            /*Getting the location after aquiring location service*/
            mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                    mGoogleApiClient);


            new CountDownTimer(4000, 1000) {

                public void onTick(long millisUntilFinished) {
                    //here you can have your logic to set text to edittext
                }

                public void onFinish() {
                    aboutusDialog.cancel();
                    if (mLastLocation != null) {
//                _progressBar.setVisibility(View.INVISIBLE);
                        latitude = mLastLocation.getLatitude();
                        longitude = mLastLocation.getLongitude();

//                        Toast.makeText(getApplicationContext(), "Lat_: " + latitude + "  Lng_:" + longitude, Toast.LENGTH_LONG).show();


                        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, MainActivity.this);

                        FragmentManager fragmentManager = getSupportFragmentManager();
                        fragmentManager
                                .beginTransaction()
                                .replace(R.id.contentPanel, new MapsAllDetailFragment(), "home_frg")
//                .addToBackStack("test")
                                .commit();
//                        isMapsAllDetailFragment_Inflated = true;

                    } else {
                /*if there is no last known location. Which means the device has no data for the loction currently.
                * So we will get the current location.
                * For this we'll implement Location Listener and override onLocationChanged*/
                        Log.i("Current Location", "No data for location found");

                        if (!mGoogleApiClient.isConnected())
                            mGoogleApiClient.connect();

//                        Toast.makeText(getApplicationContext(), "cannot find the current location ", Toast.LENGTH_LONG).show();

                        ActivityCompat.requestPermissions(MainActivity.mContext, AppConfig.PERMISSION_ALL, AppConfig.PERMISSION_REQUEST_CODE);

                        if ((ContextCompat.checkSelfPermission(getApplicationContext(),
                                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
                                &&
                                (ContextCompat.checkSelfPermission(getApplicationContext(),
                                        Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED)) {


                            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, MainActivity.this);


                        }

                    }

                    FragmentManager fragmentManager = getSupportFragmentManager();
                    fragmentManager
                            .beginTransaction()
                            .replace(R.id.contentPanel, new MapsAllDetailFragment(), "home_frg")
//                .addToBackStack("test")
                            .commit();
//                    isMapsAllDetailFragment_Inflated = true;

                }

            }.start();
        }


    }

    /*When Location changes, this method get called. */
    @Override
    public void onLocationChanged(Location location) {
        mLastLocation = location;
//        _progressBar.setVisibility(View.INVISIBLE);
        latitude = mLastLocation.getLatitude();
        longitude = mLastLocation.getLongitude();


        if (latitude != 0.0 && longitude != 0.0) {

            if (isMapsAllDetailFragment_Inflated == true) {
                //Update the marker as the location changes
                ((MapsAllDetailFragment) getSupportFragmentManager().findFragmentByTag("home_frg")).updateFromMainActivity(latitude, longitude);
            }


            if (isGoFragmentInflated == true) {
                ((Go_Fragment) getSupportFragmentManager().findFragmentByTag("go_frgment")).updatePolyLines(latitude, longitude);
            }

        }

    }


    public boolean isNetworkAvailable() {
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;

        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] netInfo = cm.getAllNetworkInfo();
        for (NetworkInfo ni : netInfo) {
            if (ni.getTypeName().equalsIgnoreCase("WIFI"))
                if (ni.isConnected())
                    haveConnectedWifi = true;
            if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
                if (ni.isConnected())
                    haveConnectedMobile = true;
        }
        return haveConnectedWifi || haveConnectedMobile;
    }


}
