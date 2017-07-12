package com.inotechsol.amirhafiz.locateease.Fragments;

import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Interpolator;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.inotechsol.amirhafiz.locateease.MainActivity;
import com.inotechsol.amirhafiz.locateease.R;
import com.sa90.materialarcmenu.ArcMenu;

/**
 * Created by Amir on 5/2/2017.
 */

public class MapsAllDetailFragment extends Fragment implements OnMapReadyCallback {


    private GoogleMap mMap;
    ArcMenu arcMenu;
    private static View rootView;
    Marker marker;
    private LatLng previous_Location;

    @Override
    public void onResume() {
        super.onResume();
        ((AppCompatActivity) getActivity()).getSupportActionBar().show();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //View frgView = inflater.inflate(R.layout.home_frg, container, false);





//
//        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
//                .findFragmentById(R.id.fragment_map2);
//        mapFragment.getMapAsync(this);



//        FloatingActionButton fab = (FloatingActionButton) frgView.findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                // Click action
//                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
//                fragmentManager
//                        .beginTransaction()
//                        .replace(R.id.contentPanel,new DetailDistance_Frg(),"home_frg")
//                .addToBackStack("test")
//                        .commit();
//
//            }
//        });


        //Check if the Rootview is already there
        if (rootView != null) {
            ViewGroup parent = (ViewGroup) rootView.getParent();
            if (parent != null)
                parent.removeView(rootView);
        }
        try {



            rootView = inflater.inflate(R.layout.home_frg, container, false);



            /////////////////////////////////////////////////
            /////////////////////////////////////////////////
            /////////////////////////////////////////////////
            previous_Location = new LatLng(MainActivity.latitude,MainActivity.longitude);
            /////////////////////////////////////////////////
            /////////////////////////////////////////////////
            /////////////////////////////////////////////////





            ((MainActivity)getActivity()).hideShowActionBar(true);

            SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                    .findFragmentById(R.id.fragment_map2);
            mapFragment.getMapAsync(this);

            arcMenu  = (ArcMenu)rootView.findViewById(R.id.arcMenu);



            FloatingActionButton fab_satellite = (FloatingActionButton)rootView.findViewById(R.id.fab_satellite);
            fab_satellite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    MainActivity.AdCounter++;
                    if (MainActivity.AdCounter % 5 == 0) {
                        if (MainActivity.mInterstitialAd.isLoaded()) {
                            MainActivity.mInterstitialAd.show();
                        }
                    }

                    mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                    mMap.setBuildingsEnabled(true);
                    arcMenu.toggleMenu();
                }
            });


            FloatingActionButton fab_default = (FloatingActionButton)rootView.findViewById(R.id.fab_default);
            fab_default.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    MainActivity.AdCounter++;
                    if (MainActivity.AdCounter % 5 == 0) {
                        if (MainActivity.mInterstitialAd.isLoaded()) {
                            MainActivity.mInterstitialAd.show();
                        }
                    }

                    mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                    mMap.setBuildingsEnabled(true);
                    arcMenu.toggleMenu();
                }
            });

            FloatingActionButton fab_hybrid = (FloatingActionButton)rootView.findViewById(R.id.fab_hybrid);
            fab_hybrid.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    MainActivity.AdCounter++;
                    if (MainActivity.AdCounter % 5 == 0) {
                        if (MainActivity.mInterstitialAd.isLoaded()) {
                            MainActivity.mInterstitialAd.show();
                        }
                    }

                    mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
                    mMap.setBuildingsEnabled(true);
                    arcMenu.toggleMenu();
                }
            });



        } catch (InflateException e) {
        /* map is already there, just return view as it is  */
        }


        MainActivity.isMapsAllDetailFragment_Inflated = true;

        return rootView;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        arcMenu.setVisibility(View.VISIBLE);

        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng TutorialsPoint = new LatLng(MainActivity.latitude,MainActivity.longitude);//(33.7294,73.0931);
//        mMap.addMarker(new
//                MarkerOptions().position(TutorialsPoint).title("Your Current Location"));


        marker = mMap.addMarker(
                new MarkerOptions().position(
                        new LatLng(
                                MainActivity.latitude,
                                MainActivity.longitude))
                        .title("Your Current Location")
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.me_marker)));





        try {
            // Customise the styling of the base map using a JSON object defined
            // in a raw resource file.

            if(MainActivity.MorningEvening.equals("Good Morning")){
                boolean success = mMap.setMapStyle(
                        MapStyleOptions.loadRawResourceStyle(
                                getActivity(), R.raw.morning_json));

                if (!success) {
                    Log.e("MapsActivityRaw", "Style parsing failed.");
                }

            }else if(MainActivity.MorningEvening.equals("Good Afternoon")){
                boolean success = mMap.setMapStyle(
                        MapStyleOptions.loadRawResourceStyle(
                                getActivity(), R.raw.afternoon_josn));

                if (!success) {
                    Log.e("MapsActivityRaw", "Style parsing failed.");
                }

            }else if(MainActivity.MorningEvening.equals("Good Evening")){
                boolean success = mMap.setMapStyle(
                        MapStyleOptions.loadRawResourceStyle(
                                getActivity(), R.raw.style_json));

                if (!success) {
                    Log.e("MapsActivityRaw", "Style parsing failed.");
                }

            }else if(MainActivity.MorningEvening.equals("Good Night")){
                boolean success = mMap.setMapStyle(
                        MapStyleOptions.loadRawResourceStyle(
                                getActivity(), R.raw.night_json));

                if (!success) {
                    Log.e("MapsActivityRaw", "Style parsing failed.");
                }
            }
        } catch (Resources.NotFoundException e) {
            Log.e("MapsActivityRaw", "Can't find style.", e);
        }

        mMap.moveCamera(CameraUpdateFactory.newLatLng(TutorialsPoint));
        mMap.animateCamera( CameraUpdateFactory.zoomTo( 16.0f));

        //Disable the Navigation of Google Maps
        mMap.getUiSettings().setMapToolbarEnabled(false);



//        getData(MainActivity.PlaceType);
    }


    //Update the marker as the location changes
    public void updateFromMainActivity(final double latitude, final double longitude){

        if(marker == null){
            if(mMap!=null){

                marker = mMap.addMarker(
                        new MarkerOptions().position(
                                new LatLng(
                                        MainActivity.latitude,
                                        MainActivity.longitude))
                                .title("Your Current Location")
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.me_marker)));
            }

        }
        else {
            if(mMap!=null){
//                marker.setPosition(new LatLng(latitude,longitude));
//                mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(latitude,longitude)));


                final Handler handler = new Handler();
                final long start = SystemClock.uptimeMillis();
                final Interpolator interpolator = new AccelerateDecelerateInterpolator();
                final float durationInMs = 5000;
                final boolean hideMarker = false;

                handler.post(new Runnable() {

                    long elapsed;
                    float t;
                    float v;
                    @Override
                    public void run() {

                        // Calculate progress using interpolator
                        elapsed = SystemClock.uptimeMillis() - start;
                        t = elapsed / durationInMs;
                        v = interpolator.getInterpolation(t);

                        LatLng currentPosition = new LatLng(
                                MainActivity.latitude*(1-t)+previous_Location.latitude*t,
                                MainActivity.longitude*(1-t)+previous_Location.longitude*t);


                        marker.setPosition(currentPosition);

                        // Repeat till progress is complete.
                        if (t < 1) {
                            // Post again 16ms later.
                            handler.postDelayed(this, 16);
                        } else {
                            if (hideMarker) {
                                marker.setVisible(false);
                            } else {
                                marker.setVisible(true);
                            }
                        }


                        previous_Location = new LatLng(latitude,longitude);

                    }
                });





            }

        }
    }


}
