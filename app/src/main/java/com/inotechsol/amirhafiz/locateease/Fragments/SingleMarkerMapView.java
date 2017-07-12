package com.inotechsol.amirhafiz.locateease.Fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.inotechsol.amirhafiz.locateease.FavouritesHelpers.FavouriteDataModel;
import com.inotechsol.amirhafiz.locateease.MainActivity;
import com.inotechsol.amirhafiz.locateease.R;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Amir on 5/4/2017.
 */

public class SingleMarkerMapView extends Fragment implements OnMapReadyCallback {


    public static String Go_Frag_Location_Name;
    public ArrayList<FavouriteDataModel> favouriteDataModelList = new ArrayList<>();
    private GoogleMap mMap;
    String mType;
    String passedLatitude, passedLongitude, fav_distance, fav_Maps_source, fav_name, fav_Address, fav_calledFrom;
    boolean isFromFavourites;
    int fav_Maker;
    private static View rootView;
    TextView single_marker_location_address_simple, single_marker_location_name, single_marker_location_address, single_marker_location_sourse_simple,
            single_marker_location_distance;
    Typeface mRobotoLight, mRobotoRegular;
    RelativeLayout favourite_container, navigation_go_container,more_container;



    @Override
    public void onResume() {
        super.onResume();
        ((AppCompatActivity) getActivity()).getSupportActionBar().show();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //View frgView = inflater.inflate(R.layout.single_marker_view,container,false);


        if (rootView != null) {
            ViewGroup parent = (ViewGroup) rootView.getParent();
            if (parent != null){
                parent.removeView(rootView);
            }

        }
        try {

            Bundle bundle = this.getArguments();
            if (bundle != null) {
                mType = bundle.getString("called_from", "");
                passedLatitude = bundle.getString("latitude", "");
                passedLongitude = bundle.getString("longitude", "");
                fav_distance = bundle.getString("distance", "");
                fav_Maps_source = bundle.getString("map_source", "");
                fav_name = bundle.getString("name", "");
                fav_Address = bundle.getString("address", "");
                fav_calledFrom = bundle.getString("called_from", "");
                fav_Maker = bundle.getInt("marker", 0);
            }

            rootView = inflater.inflate(R.layout.single_marker_view, container, false);

            ((MainActivity)getActivity()).hideShowActionBar(true);

            single_marker_location_address_simple = (TextView) rootView.findViewById(R.id.single_marker_location_address_simple);
            single_marker_location_name = (TextView) rootView.findViewById(R.id.single_marker_location_name);
            single_marker_location_address = (TextView) rootView.findViewById(R.id.single_marker_location_address);
            single_marker_location_sourse_simple = (TextView) rootView.findViewById(R.id.single_marker_location_sourse_simple);
            single_marker_location_distance = (TextView) rootView.findViewById(R.id.single_marker_location_distance);




            more_container = (RelativeLayout)rootView.findViewById(R.id.more_container);
            more_container.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    MainActivity.AdCounter++;
                    if (MainActivity.AdCounter % 5 == 0) {
                        if (MainActivity.mInterstitialAd.isLoaded()) {
                            MainActivity.mInterstitialAd.show();
                        }
                    }


                    if (fav_calledFrom != null && fav_calledFrom.equals("favourites")) {

                        final String MessageBody  = "Hi this is the location of " + fav_name + "\n" +  "and coordinates are Longitude: " + MainActivity.longitude + "\n" + "Latitude: " + MainActivity.latitude + "\n" +
                                "https://maps.google.com/maps?sensor=false&t=m&z=17&q=" + passedLatitude + "," + passedLongitude;

                        Intent txtIntent = new Intent(Intent.ACTION_SEND);
                        txtIntent.setType("text/plain");
                        txtIntent.putExtra(Intent.EXTRA_SUBJECT, "Share");
                        txtIntent.putExtra(Intent.EXTRA_TEXT, MessageBody);
                        startActivity(Intent.createChooser(txtIntent, "Share"));

                    }else{

                        final String MessageBody  = "Hi this is the location of " + MainActivity.Name + "\n" +  "and coordinates are Longitude: " + MainActivity.longitude + "\n" + "Latitude: " + MainActivity.latitude + "\n" +
                                "https://maps.google.com/maps?sensor=false&t=m&z=17&q=" + passedLatitude + "," + passedLongitude;

                        Intent txtIntent = new Intent(Intent.ACTION_SEND);
                        txtIntent.setType("text/plain");
                        txtIntent.putExtra(Intent.EXTRA_SUBJECT, "Share");
                        txtIntent.putExtra(Intent.EXTRA_TEXT, MessageBody);
                        startActivity(Intent.createChooser(txtIntent, "Share"));
                    }





                }
            });


            navigation_go_container = (RelativeLayout) rootView.findViewById(R.id.navigation_go_container);
            navigation_go_container.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    MainActivity.AdCounter++;
                    if (MainActivity.AdCounter % 5 == 0) {
                        if (MainActivity.mInterstitialAd.isLoaded()) {
                            MainActivity.mInterstitialAd.show();
                        }
                    }

                    Fragment fragment = new Go_Fragment();
                    Bundle bundle = new Bundle();
                    bundle.putString("latitude", passedLatitude);
                    bundle.putString("longitude", passedLongitude);
                    bundle.putString("name", fav_name);

                    fragment.setArguments(bundle);
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();

                    fragmentManager
                            .beginTransaction()
                            .replace(R.id.contentPanel, fragment, "go_frgment")
//                            .addToBackStack("test")
                            .commit();


                }
            });


            favourite_container = (RelativeLayout) rootView.findViewById(R.id.favourite_container);
            favourite_container.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    MainActivity.AdCounter++;
                    if (MainActivity.AdCounter % 5 == 0) {
                        if (MainActivity.mInterstitialAd.isLoaded()) {
                            MainActivity.mInterstitialAd.show();
                        }
                    }

                    if (isFromFavourites) {
                        Toast.makeText(getActivity(), "Item Alerady Favourite", Toast.LENGTH_LONG).show();
                    } else {
                        favouriteTheItem();

                    }


                }
            });


            mRobotoLight = Typeface.createFromAsset(MainActivity.mContext.getAssets(), "fonts/Roboto-Light.ttf");
            mRobotoRegular = Typeface.createFromAsset(MainActivity.mContext.getAssets(), "fonts/Roboto-Regular.ttf");


            single_marker_location_address_simple.setTypeface(mRobotoRegular);
            single_marker_location_sourse_simple.setTypeface(mRobotoRegular);

            single_marker_location_name.setTypeface(mRobotoLight);
            single_marker_location_address.setTypeface(mRobotoLight);
            single_marker_location_distance.setTypeface(mRobotoLight);

            if (fav_calledFrom != null && fav_calledFrom.equals("favourites")) {
                Go_Frag_Location_Name = fav_name;
                single_marker_location_name.setText(fav_name);
                single_marker_location_address.setText(fav_Address);
                single_marker_location_sourse_simple.setText("Source: " + fav_Maps_source);
                single_marker_location_distance.setText("Distance: " + fav_distance + " km");
                MainActivity.PlaceDrawable = fav_Maker;
                isFromFavourites = true;


            } else {
                Go_Frag_Location_Name = MainActivity.Name;
                single_marker_location_name.setText(MainActivity.Name);
                single_marker_location_address.setText(MainActivity.Address);
                single_marker_location_sourse_simple.setText("Source: " + MainActivity.Source);
                single_marker_location_distance.setText("Distance: " + MainActivity.LocationDistance + " km");
                isFromFavourites = false;
            }


            SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                    .findFragmentById(R.id.fragment_map_single_marker);
            mapFragment.getMapAsync(this);


        } catch (InflateException e) {
//            Toast.makeText(getActivity(),e+"",Toast.LENGTH_SHORT).show();
//            Log.e("ErrorAgain",""+e);
            getActivity().getSupportFragmentManager().popBackStack();
        /* map is already there, just return view as it is  */
        }

        return rootView;


    }

    public boolean isAlreadyExists() {

        boolean ExitsOrNot = false;

        SharedPreferences prefs = getActivity().getSharedPreferences("MY_PREFS_NAME", MODE_PRIVATE);
        String restoredText = prefs.getString("fav_list", "");
        if (restoredText != null && !restoredText.isEmpty()) {

            Gson gson = new Gson();
            favouriteDataModelList = gson.fromJson(restoredText, new TypeToken<ArrayList<FavouriteDataModel>>() {
            }.getType());

            FavouriteDataModel favourites_Item = new FavouriteDataModel();
            favourites_Item.NameVicinity = MainActivity.Name;
            favourites_Item.lat = Double.parseDouble(passedLatitude);
            favourites_Item.lng = Double.parseDouble(passedLongitude);

            for (int i = 0; i < favouriteDataModelList.size(); i++) {

                if (favouriteDataModelList.get(i).lat == favourites_Item.lat && favouriteDataModelList.get(i).lng == favourites_Item.lng) {
                    ExitsOrNot = true;
                } else {
                    ExitsOrNot = false;
                }

            }

            favouriteDataModelList.clear();

        }

        return ExitsOrNot;
    }


    public void favouriteTheItem() {

        if (!isAlreadyExists()) {
            SharedPreferences prefs = getActivity().getSharedPreferences("MY_PREFS_NAME", MODE_PRIVATE);
            String restoredText = prefs.getString("fav_list", "");
            if (restoredText != null && !restoredText.isEmpty()) {

                Gson gson = new Gson();
                favouriteDataModelList = gson.fromJson(restoredText, new TypeToken<ArrayList<FavouriteDataModel>>() {
                }.getType());

                FavouriteDataModel favourites_Item = new FavouriteDataModel();
                favourites_Item.NameVicinity = MainActivity.Name;
                favourites_Item.lat = Double.parseDouble(passedLatitude);
                favourites_Item.lng = Double.parseDouble(passedLongitude);
                favourites_Item.Address = MainActivity.Address;
                favourites_Item.Maps_Source = MainActivity.Source;
                favourites_Item.distance = MainActivity.LocationDistance;
                favourites_Item.Marker = MainActivity.PlaceDrawable;


                favouriteDataModelList.add(favourites_Item);
                Gson gsonTwo = new Gson();
                String jsonObj = gsonTwo.toJson(favouriteDataModelList, new TypeToken<ArrayList<FavouriteDataModel>>() {
                }.getType());

                Log.d("gons_Data_parsed", jsonObj);

                Toast.makeText(getActivity(), "Added to favourites", Toast.LENGTH_LONG).show();

                SharedPreferences.Editor editor = getActivity().getSharedPreferences("MY_PREFS_NAME", MODE_PRIVATE).edit();
                editor.putString("fav_list", jsonObj);
                editor.commit();
            } else {

                FavouriteDataModel favourites_Item = new FavouriteDataModel();
                favourites_Item.NameVicinity = MainActivity.Name;
                favourites_Item.lat = Double.parseDouble(passedLatitude);
                favourites_Item.lng = Double.parseDouble(passedLongitude);
                favourites_Item.Address = MainActivity.Address;
                favourites_Item.Maps_Source = MainActivity.Source;
                favourites_Item.distance = MainActivity.LocationDistance;
                favourites_Item.Marker = MainActivity.PlaceDrawable;


                favouriteDataModelList.add(favourites_Item);
                Gson gson = new Gson();
                String jsonObj = gson.toJson(favouriteDataModelList, new TypeToken<ArrayList<FavouriteDataModel>>() {
                }.getType());

                Log.d("gons_Data_parsed", jsonObj);

                Toast.makeText(getActivity(), "Added to favourites", Toast.LENGTH_LONG).show();

                SharedPreferences.Editor editor = getActivity().getSharedPreferences("MY_PREFS_NAME", MODE_PRIVATE).edit();
                editor.putString("fav_list", jsonObj);
                editor.commit();

            }
        } else {
            Toast.makeText(getActivity(), "Item Already Favourite", Toast.LENGTH_LONG).show();
        }
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;

        LatLng TutorialsPoint = new LatLng(Double.parseDouble(passedLatitude), Double.parseDouble(passedLongitude));//(33.7294,73.0931);

        Marker marker = mMap.addMarker(
                new MarkerOptions().position(
                        new LatLng(
                                Double.parseDouble(passedLatitude), Double.parseDouble(passedLongitude)))
                        .icon(BitmapDescriptorFactory.fromResource(MainActivity.PlaceDrawable)));
        marker.showInfoWindow();

        final LatLngBounds.Builder builder = new LatLngBounds.Builder();
        builder.include(TutorialsPoint);
        final LatLngBounds bounds = builder.build();

        final CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngBounds(bounds, 0);

        try {
            // Customise the styling of the base map using a JSON object defined
            // in a raw resource file.

            if (MainActivity.MorningEvening.equals("Good Morning")) {
                boolean success = mMap.setMapStyle(
                        MapStyleOptions.loadRawResourceStyle(
                                getActivity(), R.raw.morning_json));

                if (!success) {
                    Log.e("MapsActivityRaw", "Style parsing failed.");
                }

            } else if (MainActivity.MorningEvening.equals("Good Afternoon")) {
                boolean success = mMap.setMapStyle(
                        MapStyleOptions.loadRawResourceStyle(
                                getActivity(), R.raw.afternoon_josn));

                if (!success) {
                    Log.e("MapsActivityRaw", "Style parsing failed.");
                }

            } else if (MainActivity.MorningEvening.equals("Good Evening")) {
                boolean success = mMap.setMapStyle(
                        MapStyleOptions.loadRawResourceStyle(
                                getActivity(), R.raw.style_json));

                if (!success) {
                    Log.e("MapsActivityRaw", "Style parsing failed.");
                }

            } else if (MainActivity.MorningEvening.equals("Good Night")) {
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

        mMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
            @Override
            public void onMapLoaded() {
                mMap.moveCamera(cameraUpdate);
                mMap.animateCamera(CameraUpdateFactory.zoomTo(16.0f));

                //Disable the Navigation of Google Maps
                mMap.getUiSettings().setMapToolbarEnabled(false);
            }
        });



//        mMap.moveCamera(cameraUpdate);
//        mMap.moveCamera(CameraUpdateFactory.zoomTo(15.0f));
//
//        //Disable the Navigation of Google Maps
//        mMap.getUiSettings().setMapToolbarEnabled(false);

    }
}
