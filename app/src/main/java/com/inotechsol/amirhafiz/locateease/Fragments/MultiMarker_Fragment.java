package com.inotechsol.amirhafiz.locateease.Fragments;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.inotechsol.amirhafiz.locateease.ApplicationHelper.AppConfig;
import com.inotechsol.amirhafiz.locateease.FourSquarePlacesData.FS_PlacesAPIHIT;
import com.inotechsol.amirhafiz.locateease.GooglePlacesPlacesData.PlacesDataHit;
import com.inotechsol.amirhafiz.locateease.MainActivity;
import com.inotechsol.amirhafiz.locateease.R;

/**
 * Created by Amir on 5/4/2017.
 */

public class MultiMarker_Fragment extends Fragment implements OnMapReadyCallback {



    private GoogleMap mMap;

    private static View rootView;


    @Override
    public void onResume() {
        super.onResume();
        ((AppCompatActivity) getActivity()).getSupportActionBar().show();
    }


    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) {

//        View frgView = inflater.inflate(R.layout.multimarker_frg,container,false);
//
//
//        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
//                .findFragmentById(R.id.fragment_map2);
//        mapFragment.getMapAsync(this);

//        return frgView;


        if (rootView != null) {
            ViewGroup parent = (ViewGroup) rootView.getParent();
            if (parent != null)
                parent.removeView(rootView);
        }
        try {



            rootView = inflater.inflate(R.layout.multimarker_frg, container, false);


            ((MainActivity)getActivity()).hideShowActionBar(true);

            SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                    .findFragmentById(R.id.fragment_map2);
            mapFragment.getMapAsync(this);



        } catch (InflateException e) {
        /* map is already there, just return view as it is  */
        }

        return rootView;

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;


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






        // Add a marker in Sydney and move the camera
        LatLng TutorialsPoint = new LatLng(MainActivity.latitude,MainActivity.longitude);//(33.7294,73.0931);
//        mMap.addMarker(new
//                MarkerOptions().position(TutorialsPoint).title("Your Current Location"));


        mMap.addMarker(
                new MarkerOptions().position(
                        new LatLng(
                                MainActivity.latitude,
                                MainActivity.longitude))
                        .title("Your Current Location")
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.me_marker)));



        mMap.moveCamera(CameraUpdateFactory.newLatLng(TutorialsPoint));
        mMap.animateCamera( CameraUpdateFactory.zoomTo( 13.0f));

        //Disable the Navigation of Google Maps
        mMap.getUiSettings().setMapToolbarEnabled(false);

        if(AppConfig.calledFromGooglePlaces){
            AppConfig.calledFromGooglePlaces = false;
            addMoreMarkersGooglePlaces();
        }else if(AppConfig.calledFromFourSquare){
            AppConfig.calledFromFourSquare = false;
            addMoreMarkersFourSquarePlaces();

        }


    }


    public void addMoreMarkersFourSquarePlaces(){

        for (int i = 0; i < FS_PlacesAPIHIT.mRootObject.response.venues.size(); i++) {


//            int drawable
            Marker marker = mMap.addMarker(
                    new MarkerOptions().position(
                            new LatLng(
                                    FS_PlacesAPIHIT.mRootObject.response.venues.get(i).location.lat,
                                    FS_PlacesAPIHIT.mRootObject.response.venues.get(i).location.lng))
                            .title(FS_PlacesAPIHIT.mRootObject.response.venues.get(i).name)
                            .snippet(FS_PlacesAPIHIT.mRootObject.response.venues.get(i).location.address)
                            .icon(BitmapDescriptorFactory.fromResource(MainActivity.PlaceDrawable)));
            marker.showInfoWindow();
        }

    }

    public void addMoreMarkersGooglePlaces(){

        for (int i = 0; i < PlacesDataHit.mRootObject.results.size(); i++) {


//            int drawable
            Marker marker = mMap.addMarker(
                    new MarkerOptions().position(
                            new LatLng(
                                    PlacesDataHit.mRootObject.results.get(i).geometry.location.lat,
                                    PlacesDataHit.mRootObject.results.get(i).geometry.location.lng))
                            .title(PlacesDataHit.mRootObject.results.get(i).name)
                            .snippet(PlacesDataHit.mRootObject.results.get(i).vicinity)
                            .icon(BitmapDescriptorFactory.fromResource(MainActivity.PlaceDrawable)));
            marker.showInfoWindow();
        }

    }
}
