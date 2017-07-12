package com.inotechsol.amirhafiz.locateease.Fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateFormat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.vision.barcode.Barcode;
import com.inotechsol.amirhafiz.locateease.AlternativeRoutesReturnType.ReturnType.RootObject;
import com.inotechsol.amirhafiz.locateease.DrawRoutes.JsonReturn.Go_Fragment_APIHIT;
import com.inotechsol.amirhafiz.locateease.FragmentHelpers.Go_Fragment_Turns_Adapter;
import com.inotechsol.amirhafiz.locateease.Go_Fragment_TurnsHelpers.Go_Fragment_TurnsModel;
import com.inotechsol.amirhafiz.locateease.MainActivity;
import com.inotechsol.amirhafiz.locateease.R;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by Amir on 5/8/2017.
 */

public class Go_Fragment extends Fragment implements OnMapReadyCallback {


    //Alternative routes
    //http://maps.googleapis.com/maps/api/directions/json?origin=33.6433559,73.0509422&destination=33.63906160000001,73.0448806&sensor=false&units=metric&mode=alternatives=true
    //

    private LatLng previous_Location;
    static Geocoder geocoder;
    static List<Address> addresses = new ArrayList<>();


    boolean Info_listAnimated_visible = false;
    ListView go_frg_turns_items_lv;
    private GoogleMap mMap;
    private static View rootView;
    SupportMapFragment mapFragment;
    String passedLatitude, passedLongitude;
    public static String Alternativelatitude, AlternativeLongitude;
    ProgressDialog progressDialog;
    private List<LatLng> points; //added
    Polyline line; //added
    Marker marker;
    RelativeLayout top_container, wrapper_container, time_text_and_distance, routes_container, send_eta, stop_container, highlighted_route_tv_container;
    boolean isContainerVisible = false;
    Button later_btn, close_btn;
    TextView text_travel_time, text_distance, text_location_smple, text_via_destination, text_system_time, text_system_time_pm_am,
            eta_calculated_time_text;
    ImageView up_down_img;


    String BASE_URL;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //Check if the Rootview is already there
        if (rootView != null) {
            ViewGroup parent = (ViewGroup) rootView.getParent();
            if (parent != null)
                parent.removeView(rootView);
        }
        try {


            Bundle bundle = this.getArguments();
            if (bundle != null) {

                passedLatitude = bundle.getString("latitude", "");
                passedLongitude = bundle.getString("longitude", "");

                Alternativelatitude = passedLatitude;
                AlternativeLongitude = passedLongitude;
                //Directions API
                //https://developers.google.com/maps/documentation/directions/
                BASE_URL = "https://maps.googleapis.com/maps/api/directions/json?origin=" +
                        MainActivity.latitude + "," + MainActivity.longitude + "&destination=" + passedLatitude + "," + passedLongitude + "&sensor=false&units=metric&mode=driving" +
                        "&key=AIzaSyAFV9mbabrsd6gy7oYABWPNNK7sQTfaGpg";


            }


            rootView = inflater.inflate(R.layout.go_fragment, container, false);



            /////////////////////////////////////////////////
            /////////////////////////////////////////////////
            /////////////////////////////////////////////////
            previous_Location = new LatLng(MainActivity.latitude,MainActivity.longitude);
            /////////////////////////////////////////////////
            /////////////////////////////////////////////////
            /////////////////////////////////////////////////


//            ((MainActivity)getActivity()).hideShowActionBar(false);
            ((AppCompatActivity) getActivity()).getSupportActionBar().hide();


            mapFragment = (SupportMapFragment) getChildFragmentManager()
                    .findFragmentById(R.id.fragment_map2);
            mapFragment.getMapAsync(this);

            points = new ArrayList<LatLng>();


            Calendar rightNow = Calendar.getInstance();
            int hour = rightNow.get(Calendar.HOUR);
            int min = rightNow.get(Calendar.MINUTE);


            //System Time in Digits
            text_system_time = (TextView) rootView.findViewById(R.id.text_system_time);
            text_system_time.setText(getReminingTime());


            //System Time in AM PM
            text_system_time_pm_am = (TextView) rootView.findViewById(R.id.text_system_time_pm_am);
            text_system_time_pm_am.setText(getTimeinAM_PM());


            //calculated ETA
            eta_calculated_time_text = (TextView) rootView.findViewById(R.id.eta_calculated_time_text);
            //Via Route
            text_via_destination = (TextView) rootView.findViewById(R.id.text_via_destination);
            //End Location
            text_location_smple = (TextView) rootView.findViewById(R.id.text_location_smple);
            //Distance to travel
            text_distance = (TextView) rootView.findViewById(R.id.text_distance);
            //Time to travel
            text_travel_time = (TextView) rootView.findViewById(R.id.text_travel_time);


            //Top ListView of Turns
            go_frg_turns_items_lv = (ListView) rootView.findViewById(R.id.go_frg_turns_items_lv);
//            go_frg_turns_items_lv.setAdapter(new Go_Fragment_Turns_Adapter(getActivity(),Go_Fragment.this));


            //ListView of Turns
            highlighted_route_tv_container = (RelativeLayout) rootView.findViewById(R.id.highlighted_route_tv_container);
            highlighted_route_tv_container.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    MainActivity.AdCounter++;
                    if (MainActivity.AdCounter % 10 == 0) {
                        if (MainActivity.mInterstitialAd.isLoaded()) {
                            MainActivity.mInterstitialAd.show();
                        }
                    }


                    if(Info_listAnimated_visible){//Is Visible
                        go_frg_turns_items_lv.setVisibility(View.GONE);
                                //.animate().translationY(convertDpToPixel(-600, getActivity()));
                        Info_listAnimated_visible = false;
                    }else{//Not Visible
                        go_frg_turns_items_lv.setVisibility(View.VISIBLE);
                                //animate().translationY(convertDpToPixel(600, getActivity()));

                        Info_listAnimated_visible = true;
                    }



                }
            });


            //updown Button
            up_down_img = (ImageView) rootView.findViewById(R.id.up_down_img);
            //Main Wrapper Animating Container
            wrapper_container = (RelativeLayout) rootView.findViewById(R.id.wrapper_container);
            time_text_and_distance = (RelativeLayout) rootView.findViewById(R.id.time_text_and_distance);
            top_container = (RelativeLayout) rootView.findViewById(R.id.top_container);

            //Routes Button
            routes_container = (RelativeLayout) rootView.findViewById(R.id.routes_container);
            routes_container.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    MainActivity.AdCounter++;
                    if (MainActivity.AdCounter % 10 == 0) {
                        if (MainActivity.mInterstitialAd.isLoaded()) {
                            MainActivity.mInterstitialAd.show();
                        }
                    }


                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    fragmentManager
                            .beginTransaction()
                            .replace(R.id.contentPanel, new Alternative_Routes_Frg(), "alternative_routes")
                            .addToBackStack("alternative_routes")
                            .commit();

                }
            });

            //Send ETA Button
            send_eta = (RelativeLayout) rootView.findViewById(R.id.send_eta);
            send_eta.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    Toast.makeText(getActivity(), "Send ETA", Toast.LENGTH_LONG).show();

                    MainActivity.AdCounter++;
                    if (MainActivity.AdCounter % 10 == 0) {
                        if (MainActivity.mInterstitialAd.isLoaded()) {
                            MainActivity.mInterstitialAd.show();
                        }
                    }


                    Intent txtIntent = new Intent(Intent.ACTION_SEND);
                    txtIntent.setType("text/plain");
                    txtIntent.putExtra(Intent.EXTRA_SUBJECT, "ETA");
                    txtIntent.putExtra(Intent.EXTRA_TEXT, "Hi\n \n" + "I will reach " +
                            SingleMarkerMapView.Go_Frag_Location_Name +
                            " Via: " + Go_Fragment_APIHIT.mRootObject.routes.get(0).summary + " at " +
                            calculateETA());
                    startActivity(Intent.createChooser(txtIntent, "Share"));


                }
            });

            //Stop Container
            stop_container = (RelativeLayout) rootView.findViewById(R.id.stop_container);
            stop_container.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MainActivity.AdCounter++;
                    if (MainActivity.AdCounter % 10 == 0) {
                        if (MainActivity.mInterstitialAd.isLoaded()) {
                            MainActivity.mInterstitialAd.show();
                        }
                    }

//                    Toast.makeText(getActivity(), "Stop", Toast.LENGTH_LONG).show();
                    getActivity().getSupportFragmentManager().popBackStack();
                }
            });

            //LAter Button
            later_btn = (Button) rootView.findViewById(R.id.later_btn);
            later_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    MainActivity.AdCounter++;
                    if (MainActivity.AdCounter % 10 == 0) {
                        if (MainActivity.mInterstitialAd.isLoaded()) {
                            MainActivity.mInterstitialAd.show();
                        }
                    }


                    if (isContainerVisible) {//is Visible

                        int height_views = top_container.getHeight() + time_text_and_distance.getHeight();
                        wrapper_container.animate()
                                .translationY(wrapper_container.getHeight() - height_views);

                        up_down_img.setImageResource(R.drawable.up_img);

                        isContainerVisible = false;


                    } else {// Not visible
                        wrapper_container.animate().translationY(convertDpToPixel(10, getActivity()));


                        up_down_img.setImageResource(R.drawable.down);

                        isContainerVisible = true;
                    }

                    getActivity().getSupportFragmentManager().popBackStack();
                }
            });

            //Close Button
            close_btn = (Button) rootView.findViewById(R.id.close_btn);
            close_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    MainActivity.AdCounter++;
                    if (MainActivity.AdCounter % 10 == 0) {
                        if (MainActivity.mInterstitialAd.isLoaded()) {
                            MainActivity.mInterstitialAd.show();
                        }
                    }


                    if (isContainerVisible) {//is Visible

                        int height_views = top_container.getHeight() + time_text_and_distance.getHeight();
                        wrapper_container.animate()
                                .translationY(wrapper_container.getHeight() - height_views);

                        up_down_img.setImageResource(R.drawable.up_img);

                        isContainerVisible = false;


                    } else {// Not visible
                        wrapper_container.animate().translationY(convertDpToPixel(10, getActivity()));

                        up_down_img.setImageResource(R.drawable.down);
                        isContainerVisible = true;
                    }
                }
            });

            progressDialog = new ProgressDialog(MainActivity.mContext);
            progressDialog.setMessage("Fetching route, Please wait...");
            progressDialog.setIndeterminate(true);
            progressDialog.setCancelable(false);

            progressDialog.show();

            Go_Fragment_APIHIT.getPathData(BASE_URL, getActivity(), Go_Fragment.this);


            wrapper_container.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    MainActivity.AdCounter++;
                    if (MainActivity.AdCounter % 10 == 0) {
                        if (MainActivity.mInterstitialAd.isLoaded()) {
                            MainActivity.mInterstitialAd.show();
                        }
                    }


                    if (isContainerVisible) {//is Visible

                        int height_views = top_container.getHeight() + time_text_and_distance.getHeight();
                        wrapper_container.animate()
                                .translationY(wrapper_container.getHeight() - height_views);

                        up_down_img.setImageResource(R.drawable.up_img);
                        isContainerVisible = false;


                    } else {// Not visible
                        wrapper_container.animate().translationY(convertDpToPixel(10, getActivity()));

                        up_down_img.setImageResource(R.drawable.down);
                        isContainerVisible = true;
                    }

                }
            });


        } catch (InflateException e) {
        /* map is already there, just return view as it is  */
        }


//        progressDialog.show();
//
//        Go_Fragment_APIHIT.getPathData(BASE_URL, getActivity(), Go_Fragment.this);


        return rootView;
    }

    public static float convertDpToPixel(float dp, Context context) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float px = dp * ((float) metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        return px;
    }


    //Update the marker and polyline as the location changes from MainActivity
    public void updatePolyLines(final double latitude, final double longitude) {

        if (marker == null) {
            marker = mMap.addMarker(
                    new MarkerOptions().position(
                            new LatLng(
                                    MainActivity.latitude,
                                    MainActivity.longitude))
                            .title("Your Current Location")
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.me_marker)));

        } else {

//            marker.setPosition(new LatLng(latitude, longitude));
//            LatLng latLng = new LatLng(latitude, longitude); //you already have this
//
//            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
//            //mMap.animateCamera( CameraUpdateFactory.zoomTo( 16.0f));
//            points.add(latLng); //added
//            redrawLine(); //added


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


    @Override
    public void onResume() {
        super.onResume();
        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();


    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;


//        mMap.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {
//            @Override
//            public void onMyLocationChange(Location location) {
//
//                double latitude = location.getLatitude();
//                double longitude = location.getLongitude();
//                LatLng latLng = new LatLng(latitude, longitude); //you already have this
//
//                marker = mMap.addMarker(
//                        new MarkerOptions().position(
//                                new LatLng(latitude, longitude))
////                        .title("Me Location")
//                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.me_marker)));
//
//
//                points.add(latLng); //added
//
//                redrawLine(); //added
//
//            }
//        });
        marker = mMap.addMarker(
                new MarkerOptions().position(
                        new LatLng(
                                MainActivity.latitude,
                                MainActivity.longitude))
//                        .title("Me Location")
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.me_marker)));

        Marker markerTwo = mMap.addMarker(
                new MarkerOptions().position(
                        new LatLng(
                                Double.parseDouble(passedLatitude),
                                Double.parseDouble(passedLongitude)))
//                        .title("")
                        .icon(BitmapDescriptorFactory.fromResource(MainActivity.PlaceDrawable)));


        LatLng latLng = new LatLng(MainActivity.latitude, MainActivity.longitude);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(16.0f));


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


        //Disable the Navigation of Google Maps
        mMap.getUiSettings().setMapToolbarEnabled(false);
        mMap.getUiSettings().setZoomControlsEnabled(false);

    }


    private void redrawLine() {

        mMap.clear();  //clears all Markers and Polylines

        PolylineOptions options = new PolylineOptions().width(5).color(Color.parseColor("#10b799")).geodesic(true);
        for (int i = 0; i < points.size(); i++) {
            LatLng point = points.get(i);
            options.add(point);
        }
        addMarker(); //add Marker in current position
        line = mMap.addPolyline(options); //add Polyline
    }

    private void addMarker() {

//        Marker marker = mMap.addMarker(
//                new MarkerOptions().position(
//                        new LatLng(
//                                MainActivity.latitude,
//                                MainActivity.longitude))
////                        .title("Me Location")
//                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.me_marker)));

        Marker markerTwo = mMap.addMarker(
                new MarkerOptions().position(
                        new LatLng(
                                Double.parseDouble(passedLatitude),
                                Double.parseDouble(passedLongitude)))
//                        .title("")
                        .icon(BitmapDescriptorFactory.fromResource(MainActivity.PlaceDrawable)));
    }

    public void getDataGmaps() {
        Runnable r = new Runnable() {
            @Override
            public void run() {
                int height_views = top_container.getHeight() + time_text_and_distance.getHeight();
                wrapper_container.animate()
                        .translationY(wrapper_container.getHeight() - height_views);


                wrapper_container.setVisibility(View.VISIBLE);

                up_down_img.setImageResource(R.drawable.up_img);

                text_via_destination.setText("Via: " + Go_Fragment_APIHIT.mRootObject.routes.get(0).summary);
                text_location_smple.setText(SingleMarkerMapView.Go_Frag_Location_Name);//(Go_Fragment_APIHIT.mRootObject.routes.get(0).legs.get(0).end_address);
                text_distance.setText(Go_Fragment_APIHIT.mRootObject.routes.get(0).legs.get(0).distance.text);
                text_travel_time.setText(Go_Fragment_APIHIT.mRootObject.routes.get(0).legs.get(0).duration.text);
                eta_calculated_time_text.setText(calculateETA());


            }
        };
        r.run();

        for (int i = 0; i <Go_Fragment_APIHIT.mRootObject.routes.get(0).legs.get(0).steps.size(); i++) {


            if(Go_Fragment_APIHIT.mRootObject.routes.get(0).legs.get(0).steps.get(i).maneuver!=null){
                switch(Go_Fragment_APIHIT.mRootObject.routes.get(0).legs.get(0).steps.get(i).maneuver){


                    case "turn-sharp-left":
                        Go_Fragment_APIHIT.go_fragment_turnsModels.get(i).turnicon = R.drawable.turn_sharp_left;
                        break;

                    case "uturn-right":
                        Go_Fragment_APIHIT.go_fragment_turnsModels.get(i).turnicon = R.drawable.uturn_right;
                        break;

                    case "turn-slight-right":
                        Go_Fragment_APIHIT.go_fragment_turnsModels.get(i).turnicon = R.drawable.turn_slight_right;
                        break;

                    case "merge":
                        Go_Fragment_APIHIT.go_fragment_turnsModels.get(i).turnicon = R.drawable.merge;
                        break;

                    case "roundabout-left":
                        Go_Fragment_APIHIT.go_fragment_turnsModels.get(i).turnicon = R.drawable.round_about_left;
                        break;

                    case "roundabout-right":
                        Go_Fragment_APIHIT.go_fragment_turnsModels.get(i).turnicon = R.drawable.round_about_right;
                        break;

                    case "uturn-left":
                        Go_Fragment_APIHIT.go_fragment_turnsModels.get(i).turnicon = R.drawable.uturn_left;
                        break;

                    case "turn-slight-left":
                        Go_Fragment_APIHIT.go_fragment_turnsModels.get(i).turnicon = R.drawable.turn_slight_left;
                        break;

                    case "turn-left":
                        Go_Fragment_APIHIT.go_fragment_turnsModels.get(i).turnicon = R.drawable.turn_left;
                        break;

                    case "ramp-right":
                        Go_Fragment_APIHIT.go_fragment_turnsModels.get(i).turnicon = R.drawable.ramp_right;
                        break;

                    case "turn-right":
                        Go_Fragment_APIHIT.go_fragment_turnsModels.get(i).turnicon = R.drawable.turn_right;
                        break;

                    case "fork-right":
                        Go_Fragment_APIHIT.go_fragment_turnsModels.get(i).turnicon = R.drawable.fork_right;
                        break;

                    case "straight":
                        Go_Fragment_APIHIT.go_fragment_turnsModels.get(i).turnicon = R.drawable.strainght;
                        break;

                    case "fork-left":
                        Go_Fragment_APIHIT.go_fragment_turnsModels.get(i).turnicon = R.drawable.fork_left;
                        break;

                    case "ferry-train":
                        Go_Fragment_APIHIT.go_fragment_turnsModels.get(i).turnicon = R.drawable.ferry_train;
                        break;

                    case "turn-sharp-right":
                        Go_Fragment_APIHIT.go_fragment_turnsModels.get(i).turnicon = R.drawable.turn_sharp_right;
                        break;

                    case "ramp-left":
                        Go_Fragment_APIHIT. go_fragment_turnsModels.get(i).turnicon = R.drawable.ramp_left;
                        break;

                    case "ferry":
                        Go_Fragment_APIHIT.go_fragment_turnsModels.get(i).turnicon = R.drawable.ferry;
                        break;
                }

            }else{
                Go_Fragment_APIHIT.go_fragment_turnsModels.get(i).turnicon = R.drawable.loading;
            }




        }



        go_frg_turns_items_lv.setAdapter(new Go_Fragment_Turns_Adapter(getActivity(), Go_Fragment.this));
        go_frg_turns_items_lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                MainActivity.AdCounter++;
                if (MainActivity.AdCounter % 10 == 0) {
                    if (MainActivity.mInterstitialAd.isLoaded()) {
                        MainActivity.mInterstitialAd.show();
                    }
                }


                if(Info_listAnimated_visible){//Is Visible
                    go_frg_turns_items_lv.setVisibility(View.GONE);
                    //.animate().translationY(convertDpToPixel(-600, getActivity()));
                    Info_listAnimated_visible = false;
                }else{//Not Visible
                    go_frg_turns_items_lv.setVisibility(View.VISIBLE);
                    //animate().translationY(convertDpToPixel(600, getActivity()));

                    Info_listAnimated_visible = true;
                }



                LatLng latLng = new LatLng(Go_Fragment_APIHIT.go_fragment_turnsModels.get(position).lat,
                        Go_Fragment_APIHIT.go_fragment_turnsModels.get(position).lng);
                mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                mMap.animateCamera(CameraUpdateFactory.zoomTo(17.0f));

            }
        });

        List<LatLng> list = decodePoly(Go_Fragment_APIHIT.mRootObject.routes.get(0).overview_polyline.points);

        points = list;

        line = mMap.addPolyline(new PolylineOptions()
                .addAll(list)
                .width(12)
                .color(Color.parseColor("#10b799"))//Google maps blue color
                .geodesic(true)
        );


        progressDialog.cancel();

    }




    private List<LatLng> decodePoly(String encoded) {

        List<LatLng> poly = new ArrayList<LatLng>();
        int index = 0, len = encoded.length();
        int lat = 0, lng = 0;

        while (index < len) {
            int b, shift = 0, result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;

            shift = 0;
            result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            LatLng p = new LatLng((((double) lat / 1E5)),
                    (((double) lng / 1E5)));
            poly.add(p);
        }

        return poly;
    }

    private String getReminingTime() {
        String delegate = "hh:mm";
        String str = (String) DateFormat.format(delegate, Calendar.getInstance().getTime());
        return str;
    }

    public String getTimeinAM_PM() {
        String delegate = "hh:mm aaa";
        String str = (String) DateFormat.format(delegate, Calendar.getInstance().getTime());

        String[] splited = str.split("\\s+");
        return splited[1].toString();
    }


    public String calculateETA() {

        String delegate = "hh:mm:ss";
        String str = (String) DateFormat.format(delegate, Calendar.getInstance().getTime());


        String colonSplited[] = str.split(":");
        int hours1 = Integer.parseInt(colonSplited[0]);
        int minutes1 = Integer.parseInt(colonSplited[1]);
        int seconds1 = Integer.parseInt(colonSplited[2]);


        int Webhours = Go_Fragment_APIHIT.mRootObject.routes.get(0).legs.get(0).duration.value / 3600;
        int Webminutes = (Go_Fragment_APIHIT.mRootObject.routes.get(0).legs.get(0).duration.value % 3600) / 60;
        int Webseconds = Go_Fragment_APIHIT.mRootObject.routes.get(0).legs.get(0).duration.value % 60;


        int totalHours = hours1 + Webhours;
        int totalMinutes = minutes1 + Webminutes;
        int totalSeconds = seconds1 + Webseconds;
        if (totalSeconds >= 60) {
            totalMinutes++;
            totalSeconds = totalSeconds % 60;
        }
        if (totalMinutes >= 60) {
            totalHours++;
            totalMinutes = totalMinutes % 60;
        }

        String comvertedHours = "";
        if (totalHours < 10 && totalHours > 0) {
            comvertedHours = "0" + totalHours;
        } else {
            comvertedHours = totalHours + "";
        }

        String convertedMinutes = "";
        if (totalMinutes < 10 && totalMinutes > 0) {
            convertedMinutes = "0" + totalMinutes;
        } else {
            convertedMinutes = totalMinutes + "";
        }


        String time_ = comvertedHours + ":" + convertedMinutes;


        SimpleDateFormat parseFormat = new SimpleDateFormat("hh:mm a");
//        try {
//            Date date = parseFormat.parse(time_);
//            long lastModifiedDate =  date.getTime();
//            Log.d("parsedDate12:",date+"");
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }

        String finalReturnString = "";
        String twenty_hourtime_ = comvertedHours + ":" + convertedMinutes;
        SimpleDateFormat displayFormat = new SimpleDateFormat("HH:mm");

        try {
            Date date = parseFormat.parse(twenty_hourtime_ + " " + getTimeinAM_PM());
//            Date date_twp = parseFormatTwo.parse(twenty_hourtime_ + getTimeinAM_PM());
            Log.d("parsedDate24Dynamic:", displayFormat.format(date) + "");
            String _24hours = displayFormat.format(date);

            final SimpleDateFormat sdf = new SimpleDateFormat("H:mm");
            Date dateObj = sdf.parse(_24hours);
            Log.d("parsed12Hours", new SimpleDateFormat("hh:mm aa").format(dateObj) + "");
            finalReturnString = new SimpleDateFormat("hh:mm aa").format(dateObj) + "";
//            Log.d("parsed12Hours",_12HourSDF.format(twenty_hourtime_)+"");


        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (finalReturnString.isEmpty()) {
            finalReturnString = time_;
        }


        return finalReturnString;//time_;// + ":" + totalMinutes;

    }


    public void updateFromAlternative_Routes_ListView_Frg(final RootObject mRootobject, final int index) {



        progressDialog.setMessage("Mapping New route, Please wait...");
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);

        progressDialog.show();


        Runnable r = new Runnable() {
            @Override
            public void run() {
                text_via_destination.setText("Via: " + mRootobject.routes.get(index).summary);
                text_location_smple.setText(SingleMarkerMapView.Go_Frag_Location_Name);
                text_distance.setText(mRootobject.routes.get(index).legs.get(0).distance.text);
                text_travel_time.setText(mRootobject.routes.get(index).legs.get(0).duration.text);
                eta_calculated_time_text.setText(calculateUpdatedETA(mRootobject.routes.get(index).legs.get(0).duration.value));


                if (isContainerVisible) {//is Visible

                    int height_views = top_container.getHeight() + time_text_and_distance.getHeight();
                    wrapper_container.animate()
                            .translationY(wrapper_container.getHeight() - height_views);

                    up_down_img.setImageResource(R.drawable.up_img);

                    isContainerVisible = false;


                } else {// Not visible
                    wrapper_container.animate().translationY(convertDpToPixel(10, getActivity()));

                    up_down_img.setImageResource(R.drawable.down);
                    isContainerVisible = true;
                }


            }
        };
        r.run();


        List<LatLng> list = decodePoly(mRootobject.routes.get(index).overview_polyline.points);

        if (points.size() > 0) {
            points.clear();
        }
        points = list;

        line.remove();
        line = mMap.addPolyline(new PolylineOptions()
                .addAll(list)
                .width(12)
                .color(Color.parseColor("#10b799"))//Google maps blue color
                .geodesic(true)
        );




        new AsyncTask<Barcode.GeoPoint, Void, Address>()
        {
            @Override
            protected Address doInBackground(Barcode.GeoPoint... geoPoints)
            {


                if(Go_Fragment_APIHIT.go_fragment_turnsModels.size()>0){
                    Go_Fragment_APIHIT.go_fragment_turnsModels.clear();
                }

                Go_Fragment_TurnsModel go_fragment_turnsModel;
                geocoder = new Geocoder(getActivity(), Locale.getDefault());

                for (int i = 0; i < mRootobject.routes.get(index).legs.get(0).steps.size(); i++) {

                    go_fragment_turnsModel = new Go_Fragment_TurnsModel();
                    go_fragment_turnsModel.distance = mRootobject.routes.get(index).legs.get(0).steps.get(i).distance.text;
                    go_fragment_turnsModel.lat= mRootobject.routes.get(index).legs.get(0).steps.get(i).start_location.lat;
                    go_fragment_turnsModel.lng= mRootobject.routes.get(index).legs.get(0).steps.get(i).start_location.lng;



                    if(addresses.size()>0){
                        addresses.clear();
                    }
                    String address = "";
                    try {
                        addresses = geocoder.getFromLocation(mRootobject.routes.get(index).legs.get(0).steps.get(i).start_location.lat,
                                mRootobject.routes.get(index).legs.get(0).steps.get(i).start_location.lng, 1);
                        address = addresses.get(0).getAddressLine(0);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    go_fragment_turnsModel.TurnLocationName = address;
                    Go_Fragment_APIHIT.go_fragment_turnsModels.add(go_fragment_turnsModel);

                }
                return null;
            }

            @Override
            protected void onPostExecute(Address address)
            {

                for (int i = 0; i <mRootobject.routes.get(index).legs.get(0).steps.size(); i++) {


                    if (mRootobject.routes.get(index).legs.get(0).steps.get(i).maneuver != null) {
                        switch (mRootobject.routes.get(index).legs.get(0).steps.get(i).maneuver) {


                            case "turn-sharp-left":
                                Go_Fragment_APIHIT.go_fragment_turnsModels.get(i).turnicon = R.drawable.turn_sharp_left;
                                break;

                            case "uturn-right":
                                Go_Fragment_APIHIT.go_fragment_turnsModels.get(i).turnicon = R.drawable.uturn_right;
                                break;

                            case "turn-slight-right":
                                Go_Fragment_APIHIT.go_fragment_turnsModels.get(i).turnicon = R.drawable.turn_slight_right;
                                break;

                            case "merge":
                                Go_Fragment_APIHIT.go_fragment_turnsModels.get(i).turnicon = R.drawable.merge;
                                break;

                            case "roundabout-left":
                                Go_Fragment_APIHIT.go_fragment_turnsModels.get(i).turnicon = R.drawable.round_about_left;
                                break;

                            case "roundabout-right":
                                Go_Fragment_APIHIT.go_fragment_turnsModels.get(i).turnicon = R.drawable.round_about_right;
                                break;

                            case "uturn-left":
                                Go_Fragment_APIHIT.go_fragment_turnsModels.get(i).turnicon = R.drawable.uturn_left;
                                break;

                            case "turn-slight-left":
                                Go_Fragment_APIHIT.go_fragment_turnsModels.get(i).turnicon = R.drawable.turn_slight_left;
                                break;

                            case "turn-left":
                                Go_Fragment_APIHIT.go_fragment_turnsModels.get(i).turnicon = R.drawable.turn_left;
                                break;

                            case "ramp-right":
                                Go_Fragment_APIHIT.go_fragment_turnsModels.get(i).turnicon = R.drawable.ramp_right;
                                break;

                            case "turn-right":
                                Go_Fragment_APIHIT.go_fragment_turnsModels.get(i).turnicon = R.drawable.turn_right;
                                break;

                            case "fork-right":
                                Go_Fragment_APIHIT.go_fragment_turnsModels.get(i).turnicon = R.drawable.fork_right;
                                break;

                            case "straight":
                                Go_Fragment_APIHIT.go_fragment_turnsModels.get(i).turnicon = R.drawable.strainght;
                                break;

                            case "fork-left":
                                Go_Fragment_APIHIT.go_fragment_turnsModels.get(i).turnicon = R.drawable.fork_left;
                                break;

                            case "ferry-train":
                                Go_Fragment_APIHIT.go_fragment_turnsModels.get(i).turnicon = R.drawable.ferry_train;
                                break;

                            case "turn-sharp-right":
                                Go_Fragment_APIHIT.go_fragment_turnsModels.get(i).turnicon = R.drawable.turn_sharp_right;
                                break;

                            case "ramp-left":
                                Go_Fragment_APIHIT.go_fragment_turnsModels.get(i).turnicon = R.drawable.ramp_left;
                                break;

                            case "ferry":
                                Go_Fragment_APIHIT.go_fragment_turnsModels.get(i).turnicon = R.drawable.ferry;
                                break;
                        }

                    } else {
                        Go_Fragment_APIHIT.go_fragment_turnsModels.get(i).turnicon = R.drawable.loading;
                    }

                }


                    progressDialog.cancel();
                // do whatever you want/need to do with the address found
                // remember to check first that it's not null
            }
        }.execute();
    }


    public String calculateUpdatedETA(int duration) {
        String delegate = "hh:mm:ss";
        String str = (String) DateFormat.format(delegate, Calendar.getInstance().getTime());


        String colonSplited[] = str.split(":");
        int hours1 = Integer.parseInt(colonSplited[0]);
        int minutes1 = Integer.parseInt(colonSplited[1]);
        int seconds1 = Integer.parseInt(colonSplited[2]);


        int Webhours = duration / 3600;
        int Webminutes = duration / 60;
        int Webseconds = duration % 60;


        int totalHours = hours1 + Webhours;
        int totalMinutes = minutes1 + Webminutes;
        int totalSeconds = seconds1 + Webseconds;
        if (totalSeconds >= 60) {
            totalMinutes++;
            totalSeconds = totalSeconds % 60;
        }
        if (totalMinutes >= 60) {
            totalHours++;
            totalMinutes = totalMinutes % 60;
        }

        String comvertedHours = "";
        if (totalHours < 10 && totalHours > 0) {
            comvertedHours = "0" + totalHours;
        } else {
            comvertedHours = totalHours + "";
        }

        String convertedMinutes = "";
        if (totalMinutes < 10 && totalMinutes > 0) {
            convertedMinutes = "0" + totalMinutes;
        } else {
            convertedMinutes = totalMinutes + "";
        }


        String time_ = comvertedHours + ":" + convertedMinutes;


        SimpleDateFormat parseFormat = new SimpleDateFormat("hh:mm a");
//        try {
//            Date date = parseFormat.parse(time_);
//            long lastModifiedDate =  date.getTime();
//            Log.d("parsedDate12:",date+"");
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }

        String finalReturnString = "";
        String twenty_hourtime_ = comvertedHours + ":" + convertedMinutes;
        SimpleDateFormat displayFormat = new SimpleDateFormat("HH:mm");

        try {
            Date date = parseFormat.parse(twenty_hourtime_ + " " + getTimeinAM_PM());
//            Date date_twp = parseFormatTwo.parse(twenty_hourtime_ + getTimeinAM_PM());
            Log.d("parsedDate24Dynamic:", displayFormat.format(date) + "");
            String _24hours = displayFormat.format(date);

            final SimpleDateFormat sdf = new SimpleDateFormat("H:mm");
            Date dateObj = sdf.parse(_24hours);
            Log.d("parsed12Hours", new SimpleDateFormat("hh:mm aa").format(dateObj) + "");
            finalReturnString = new SimpleDateFormat("hh:mm aa").format(dateObj) + "";
//            Log.d("parsed12Hours",_12HourSDF.format(twenty_hourtime_)+"");


        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (finalReturnString.isEmpty()) {
            finalReturnString = time_;
        }


        return finalReturnString;//time_;// + ":" + totalMinutes;
    }


}
