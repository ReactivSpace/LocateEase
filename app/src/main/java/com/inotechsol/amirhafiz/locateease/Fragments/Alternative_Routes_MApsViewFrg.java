package com.inotechsol.amirhafiz.locateease.Fragments;

import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.inotechsol.amirhafiz.locateease.AlternativeRoutesReturnType.AlternativeRoutes_APIHIT;
import com.inotechsol.amirhafiz.locateease.MainActivity;
import com.inotechsol.amirhafiz.locateease.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Amir on 5/13/2017.
 */

public class Alternative_Routes_MApsViewFrg extends Fragment implements OnMapReadyCallback {


    boolean selectedIndexDataExists_for_Purple = false;
    boolean selectedIndexDataExists_for_blue = false;
    boolean selectedIndexDataExists_for_Green = false;


    boolean purpleContainerSelected = false;
    boolean BlueContainerSelected = false;
     boolean GreenContainerSelected = false;



    RelativeLayout maps_view_purple_container, maps_view_blue_container, maps_view_green_container;
    TextView purple_container_time_tv, purple_container_distance_tv, blue_container_time_tv, blue_container_distance_tv, green_container_time_tv, green_container_distance_tv, via_route_tv;

    Button alternative_routes_go_btn;

    private GoogleMap mMap;
    private static View rootView;
    SupportMapFragment mapFragment;
    Polyline line;
    private List<LatLng> points;
    //    ProgressBar loading_progressBar;
    Polyline lineOne;
    Polyline lineTwo;
    Polyline lineThree;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        //View frgView = inflater.inflate(R.layout.alternative_routes_mapview_frg,container,false);

        if (rootView != null) {
            ViewGroup parent = (ViewGroup) rootView.getParent();
            if (parent != null)
                parent.removeView(rootView);
        }


        rootView = inflater.inflate(R.layout.alternative_routes_mapview_frg, container, false);

        mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.fragment_map2);
        mapFragment.getMapAsync(this);
        points = new ArrayList<LatLng>();


        //TextViews
        purple_container_time_tv = (TextView) rootView.findViewById(R.id.purple_container_time_tv);
        purple_container_distance_tv = (TextView) rootView.findViewById(R.id.purple_container_distance_tv);

        blue_container_time_tv = (TextView) rootView.findViewById(R.id.blue_container_time_tv);
        blue_container_distance_tv = (TextView) rootView.findViewById(R.id.blue_container_distance_tv);

        green_container_time_tv = (TextView) rootView.findViewById(R.id.green_container_time_tv);
        green_container_distance_tv = (TextView) rootView.findViewById(R.id.green_container_distance_tv);

        via_route_tv = (TextView) rootView.findViewById(R.id.via_route_tv);


        //Go Button
        alternative_routes_go_btn = (Button) rootView.findViewById(R.id.alternative_routes_go_btn);
        alternative_routes_go_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                MainActivity.AdCounter++;
                if (MainActivity.AdCounter % 5 == 0) {
                    if (MainActivity.mInterstitialAd.isLoaded()) {
                        MainActivity.mInterstitialAd.show();
                    }
                }


                if(purpleContainerSelected){
                    if (selectedIndexDataExists_for_Purple) {
                        ((Go_Fragment) getActivity().getSupportFragmentManager().findFragmentByTag("go_frgment"))
                                .updateFromAlternative_Routes_ListView_Frg(AlternativeRoutes_APIHIT.mRootObject, 0);


                        getActivity().getSupportFragmentManager().popBackStack();
                    } else {
                        Toast.makeText(getActivity(), "No data Exists", Toast.LENGTH_SHORT).show();
                    }


                }else if(BlueContainerSelected){

                    if (selectedIndexDataExists_for_blue) {
                        ((Go_Fragment) getActivity().getSupportFragmentManager().findFragmentByTag("go_frgment"))
                                .updateFromAlternative_Routes_ListView_Frg(AlternativeRoutes_APIHIT.mRootObject, 1);

                        getActivity().getSupportFragmentManager().popBackStack();
                    } else {
                        Toast.makeText(getActivity(), "No data Exists", Toast.LENGTH_SHORT).show();
                    }



                }else if(GreenContainerSelected){

                    if (selectedIndexDataExists_for_Green) {
                        ((Go_Fragment) getActivity().getSupportFragmentManager().findFragmentByTag("go_frgment"))
                                .updateFromAlternative_Routes_ListView_Frg(AlternativeRoutes_APIHIT.mRootObject, 2);

                        getActivity().getSupportFragmentManager().popBackStack();
                    } else {
                        Toast.makeText(getActivity(), "No data Exists", Toast.LENGTH_SHORT).show();
                    }


                }else{
                    Toast.makeText(getActivity(),"Please select one route",Toast.LENGTH_SHORT).show();
                }





            }
        });


        //Relative Layout Containers
        maps_view_purple_container = (RelativeLayout) rootView.findViewById(R.id.maps_view_purple_container);
        maps_view_blue_container = (RelativeLayout) rootView.findViewById(R.id.maps_view_blue_container);
        maps_view_green_container = (RelativeLayout) rootView.findViewById(R.id.maps_view_green_container);

        maps_view_purple_container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                MainActivity.AdCounter++;
                if (MainActivity.AdCounter % 5 == 0) {
                    if (MainActivity.mInterstitialAd.isLoaded()) {
                        MainActivity.mInterstitialAd.show();
                    }
                }


                purpleContainerSelected = true;
                BlueContainerSelected = false;
                GreenContainerSelected = false;



                //set All Relativelayout color to white
                maps_view_purple_container.setBackgroundColor(Color.parseColor("#FFFFFF"));
                maps_view_blue_container.setBackgroundColor(Color.parseColor("#FFFFFF"));
                maps_view_green_container.setBackgroundColor(Color.parseColor("#FFFFFF"));

                //Setting pointer bg to selected container
                maps_view_purple_container.setBackgroundResource(R.drawable.purple_pointer_unselected);
                //Setting textview colors of selected Container
                purple_container_time_tv.setTextColor(Color.parseColor("#FFFFFF"));
                purple_container_distance_tv.setTextColor(Color.parseColor("#FFFFFF"));


                blue_container_time_tv.setTextColor(Color.parseColor("#391eff"));
                blue_container_distance_tv.setTextColor(Color.parseColor("#391eff"));


                green_container_time_tv.setTextColor(Color.parseColor("#02c710"));
                green_container_distance_tv.setTextColor(Color.parseColor("#02c710"));


                if(AlternativeRoutes_APIHIT.mRootObject!=null && AlternativeRoutes_APIHIT.mRootObject.routes.size()>0) {

                    if(lineOne!=null || lineTwo!=null || lineThree!=null){

                        for (int i = 0; i < AlternativeRoutes_APIHIT.mRootObject.routes.size(); i++) {

                            if (i == 0) {
                                if (AlternativeRoutes_APIHIT.mRootObject.routes.get(0).legs.get(0) != null) {
                                    //PolyLine Colors Purple
                                    lineOne.setWidth(25.0f);
                                    lineOne.setZIndex(2.0f);
                                    via_route_tv.setText(AlternativeRoutes_APIHIT.mRootObject.routes.get(0).summary);
                                }
                            } else if (i == 1) {
                                if (AlternativeRoutes_APIHIT.mRootObject.routes.get(1).legs.get(0) != null) {
                                    //Blue
                                    lineTwo.setWidth(12.0f);
                                    lineTwo.setZIndex(1.0f);
                                }

                            } else if (i == 2) {
                                if (AlternativeRoutes_APIHIT.mRootObject.routes.get(2).legs.get(0) != null) {

                                    //Green
                                    lineThree.setWidth(12.0f);
                                    lineThree.setZIndex(1.0f);
                                }

                            }
                        }


                    }





                }

                if(!purple_container_distance_tv.getText().toString().equals("0.0 km") &&
                        !purple_container_time_tv.getText().toString().equals("0min")){

                    selectedIndexDataExists_for_Purple = true;

                }else{
                    selectedIndexDataExists_for_Purple = false;
                }



            }
        });


        maps_view_blue_container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                MainActivity.AdCounter++;
                if (MainActivity.AdCounter % 5 == 0) {
                    if (MainActivity.mInterstitialAd.isLoaded()) {
                        MainActivity.mInterstitialAd.show();
                    }
                }



                purpleContainerSelected = false;
                BlueContainerSelected = true;
                GreenContainerSelected = false;


                //set All Relativelayout color to white
                maps_view_purple_container.setBackgroundColor(Color.parseColor("#FFFFFF"));
                maps_view_blue_container.setBackgroundColor(Color.parseColor("#FFFFFF"));
                maps_view_green_container.setBackgroundColor(Color.parseColor("#FFFFFF"));

                //Setting pointer bg to selected container
                maps_view_blue_container.setBackgroundResource(R.drawable.blue_pointer_unselected);
                //Setting textview colors of selected Container
                blue_container_time_tv.setTextColor(Color.parseColor("#FFFFFF"));
                blue_container_distance_tv.setTextColor(Color.parseColor("#FFFFFF"));


                green_container_time_tv.setTextColor(Color.parseColor("#02c710"));
                green_container_distance_tv.setTextColor(Color.parseColor("#02c710"));


                purple_container_time_tv.setTextColor(Color.parseColor("#f105af"));
                purple_container_distance_tv.setTextColor(Color.parseColor("#f105af"));


                if(AlternativeRoutes_APIHIT.mRootObject!=null && AlternativeRoutes_APIHIT.mRootObject.routes.size()>0) {

                    if(lineOne!=null || lineTwo!=null || lineThree!=null){
                        for (int i = 0; i < AlternativeRoutes_APIHIT.mRootObject.routes.size(); i++) {

                            if (i == 0) {
                                if (AlternativeRoutes_APIHIT.mRootObject.routes.get(0).legs.get(0) != null) {
                                    //PolyLine Colors Purple
                                    lineOne.setWidth(12.0f);
                                    lineOne.setZIndex(1.0f);
                                }
                            } else if (i == 1) {
                                if (AlternativeRoutes_APIHIT.mRootObject.routes.get(1).legs.get(0) != null) {
                                    //Blue
                                    lineTwo.setWidth(25.0f);
                                    lineTwo.setZIndex(2.0f);


                                    via_route_tv.setText(AlternativeRoutes_APIHIT.mRootObject.routes.get(1).summary);

                                }

                            } else if (i == 2) {
                                if (AlternativeRoutes_APIHIT.mRootObject.routes.get(2).legs.get(0) != null) {

                                    //Green
                                    lineThree.setWidth(12.0f);
                                    lineThree.setZIndex(1.0f);
                                }

                            }
                        }

                    }



                }


                if(!blue_container_distance_tv.getText().toString().equals("0.0 km") &&
                        !blue_container_time_tv.getText().toString().equals("0min")){

                    selectedIndexDataExists_for_blue = true;

                }else{
                    selectedIndexDataExists_for_blue = false;
                }



            }
        });


        maps_view_green_container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                MainActivity.AdCounter++;
                if (MainActivity.AdCounter % 5 == 0) {
                    if (MainActivity.mInterstitialAd.isLoaded()) {
                        MainActivity.mInterstitialAd.show();
                    }
                }


                purpleContainerSelected = false;
                BlueContainerSelected = false;
                GreenContainerSelected = true;


                //set All Relativelayout color to white
                maps_view_purple_container.setBackgroundColor(Color.parseColor("#FFFFFF"));
                maps_view_blue_container.setBackgroundColor(Color.parseColor("#FFFFFF"));
                maps_view_green_container.setBackgroundColor(Color.parseColor("#FFFFFF"));

                //Setting pointer bg to selected container
                maps_view_green_container.setBackgroundResource(R.drawable.green_pointer_unselected);
                //Setting textview colors of selected Container
                green_container_time_tv.setTextColor(Color.parseColor("#FFFFFF"));
                green_container_distance_tv.setTextColor(Color.parseColor("#FFFFFF"));

                purple_container_time_tv.setTextColor(Color.parseColor("#f105af"));
                purple_container_distance_tv.setTextColor(Color.parseColor("#f105af"));

                blue_container_time_tv.setTextColor(Color.parseColor("#391eff"));
                blue_container_distance_tv.setTextColor(Color.parseColor("#391eff"));



                if(AlternativeRoutes_APIHIT.mRootObject!=null && AlternativeRoutes_APIHIT.mRootObject.routes.size()>0){


                    if(lineOne!=null || lineTwo!=null || lineThree!=null){

                        for (int i = 0; i < AlternativeRoutes_APIHIT.mRootObject.routes.size(); i++) {

                            if (i == 0) {
                                if (AlternativeRoutes_APIHIT.mRootObject.routes.get(0).legs.get(0) != null) {
                                    //PolyLine Colors Purple
                                    lineOne.setWidth(12.0f);
                                    lineOne.setZIndex(1.0f);
                                }
                            } else if (i == 1) {
                                if (AlternativeRoutes_APIHIT.mRootObject.routes.get(1).legs.get(0) != null) {
                                    //Blue
                                    lineTwo.setWidth(12.0f);
                                    lineTwo.setZIndex(1.0f);
                                }

                            } else if (i == 2) {
                                if (AlternativeRoutes_APIHIT.mRootObject.routes.get(2).legs.get(0) != null) {
                                    //Green
                                    lineThree.setWidth(25.0f);
                                    lineThree.setZIndex(2.0f);


                                    via_route_tv.setText(AlternativeRoutes_APIHIT.mRootObject.routes.get(2).summary);
                                }

                            }
                        }


                    }





                }





                if(!green_container_distance_tv.getText().toString().equals("0.0 km") &&
                        !green_container_time_tv.getText().toString().equals("0min")){

                    selectedIndexDataExists_for_Green = true;

                }else{
                    selectedIndexDataExists_for_Green = false;
                }





            }
        });


        return rootView;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;

        Marker marker = mMap.addMarker(
                new MarkerOptions().position(
                        new LatLng(
                                MainActivity.latitude,
                                MainActivity.longitude))
//                        .title("Me Location")
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.me_marker)));


        Marker markerDes = mMap.addMarker(
                new MarkerOptions().position(
                        new LatLng(
                                Double.parseDouble(Go_Fragment.Alternativelatitude),
                                Double.parseDouble(Go_Fragment.AlternativeLongitude)))
//                        .title("Me Location")
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.destination_marker)));


//        try {
//            // Customise the styling of the base map using a JSON object defined
//            // in a raw resource file.
//            boolean success = mMap.setMapStyle(
//                    MapStyleOptions.loadRawResourceStyle(
//                            getActivity(), R.raw.style_json));
//
//            if (!success) {
//                Log.e("MapsActivityRaw", "Style parsing failed.");
//            }
//        } catch (Resources.NotFoundException e) {
//            Log.e("MapsActivityRaw", "Can't find style.", e);
//        }


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


//        LatLng coordinate = new LatLng(Latitude, Latitude);


        LatLng latLng = new LatLng(MainActivity.latitude, MainActivity.longitude);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(13.0f));

        //Disable the Navigation of Google Maps
        mMap.getUiSettings().setMapToolbarEnabled(false);
        mMap.getUiSettings().setZoomControlsEnabled(false);

        CameraUpdate yourLocation = CameraUpdateFactory.newLatLngZoom(latLng, 14.0f);
        mMap.animateCamera(yourLocation);


    }

    public void getDataGmaps() {


        for (int i = 0; i < AlternativeRoutes_APIHIT.mRootObject.routes.size(); i++) {

            List<LatLng> list = decodePoly(AlternativeRoutes_APIHIT.mRootObject.routes.get(i).overview_polyline.points);

            points = list;

            if (i == 0) {

                lineOne = mMap.addPolyline(new PolylineOptions()
                        .addAll(list)
                        .width(12.0f)
                        .color(Color.parseColor("#f105af"))//Purple
                        .geodesic(true)
                );
            } else if (i == 1) {
                lineTwo = mMap.addPolyline(new PolylineOptions()
                        .addAll(list)
                        .width(12.0f)
                        .color(Color.parseColor("#391eff"))//Blue
                        .geodesic(true)
                );
            } else if (i == 2) {
                lineThree = mMap.addPolyline(new PolylineOptions()
                        .addAll(list)
                        .width(12.0f)
                        .color(Color.parseColor("#02c710"))//Green
                        .geodesic(true)
                );
            }


        }

        for (int i = 0; i < AlternativeRoutes_APIHIT.mRootObject.routes.size(); i++) {

            if (i == 0) {
                purple_container_time_tv.setText(AlternativeRoutes_APIHIT.mRootObject.routes.get(0).legs.get(0).duration.text);
                purple_container_distance_tv.setText(AlternativeRoutes_APIHIT.mRootObject.routes.get(0).legs.get(0).distance.text);
            } else if (i == 1) {
                blue_container_time_tv.setText(AlternativeRoutes_APIHIT.mRootObject.routes.get(1).legs.get(0).duration.text);
                blue_container_distance_tv.setText(AlternativeRoutes_APIHIT.mRootObject.routes.get(0).legs.get(0).distance.text);
            } else if (i == 2) {
                green_container_time_tv.setText(AlternativeRoutes_APIHIT.mRootObject.routes.get(2).legs.get(0).duration.text);
                green_container_distance_tv.setText(AlternativeRoutes_APIHIT.mRootObject.routes.get(2).legs.get(0).distance.text);
            }
        }


//        loading_progressBar.setVisibility(View.GONE);

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

}
