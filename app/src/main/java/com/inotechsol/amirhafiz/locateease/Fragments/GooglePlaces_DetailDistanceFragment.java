package com.inotechsol.amirhafiz.locateease.Fragments;

import android.location.Location;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.inotechsol.amirhafiz.locateease.ApplicationHelper.AppConfig;
import com.inotechsol.amirhafiz.locateease.FragmentHelpers.DetailDistance_Adapter;
import com.inotechsol.amirhafiz.locateease.GooglePlacesPlacesData.PlacesDataHit;
import com.inotechsol.amirhafiz.locateease.MainActivity;
import com.inotechsol.amirhafiz.locateease.R;

/**
 * Created by Amir on 5/4/2017.
 */

public class GooglePlaces_DetailDistanceFragment extends Fragment {



    ProgressBar loading_progressBar;
    ListView detail_lv;
    FloatingActionButton fab;

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) {

        View frgView = inflater.inflate(R.layout.detail_distance,container,false);

        loading_progressBar = (ProgressBar)frgView.findViewById(R.id.loading_progressBar);

        //ListView
        detail_lv = (ListView) frgView.findViewById(R.id.detail_lv);
        detail_lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                MainActivity.AdCounter++;
                if (MainActivity.AdCounter % 5 == 0) {
                    if (MainActivity.mInterstitialAd.isLoaded()) {
                        MainActivity.mInterstitialAd.show();
                    }
                }


                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();


                Fragment fragment = new SingleMarkerMapView();
                Bundle bundle = new Bundle();
                bundle.putString("called_from", "GooglePlay_Detail");
                bundle.putString("latitude", PlacesDataHit.mRootObject.results.get(position).geometry.location.lat +"");
                bundle.putString("longitude", PlacesDataHit.mRootObject.results.get(position).geometry.location.lng +"");
                MainActivity.Name = PlacesDataHit.mRootObject.results.get(position).name;
                MainActivity.Address = PlacesDataHit.mRootObject.results.get(position).vicinity;
                MainActivity.Source = "Google";


                double latitude = PlacesDataHit.mRootObject.results.get(position).geometry.location.lat;
                double longitude = PlacesDataHit.mRootObject.results.get(position).geometry.location.lng;
                float distance=0;
                Location crntLocation=new Location("crntlocation");
                crntLocation.setLatitude(MainActivity.latitude);
                crntLocation.setLongitude(MainActivity.longitude);

                Location newLocation=new Location("newlocation");
                newLocation.setLatitude(latitude);
                newLocation.setLongitude(longitude);


                //float distance = crntLocation.distanceTo(newLocation);  in meters
                distance =crntLocation.distanceTo(newLocation) / 1000; // in km

                MainActivity.LocationDistance = String.format("%.1f", distance);// 1.30






                fragment.setArguments(bundle);

                fragmentManager
                        .beginTransaction()
                        .replace(R.id.contentPanel,fragment,"single_marker_view")
                        .addToBackStack("test")
                        .commit();


            }
        });

        fab = (FloatingActionButton) frgView.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Click action


                MainActivity.AdCounter++;
                if (MainActivity.AdCounter % 5 == 0) {
                    if (MainActivity.mInterstitialAd.isLoaded()) {
                        MainActivity.mInterstitialAd.show();
                    }
                }


                AppConfig.calledFromGooglePlaces = true;
                AppConfig.calledFromFourSquare = false;


                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager
                        .beginTransaction()
                        .replace(R.id.contentPanel, new MultiMarker_Fragment(), "multi_marker_frg")
                        .addToBackStack("test")
                        .commit();

            }
        });



        getData(MainActivity.PlaceType);


        return frgView;
    }


    public void getData(String type){

        loading_progressBar.setVisibility(View.VISIBLE);
        fab.setVisibility(View.GONE);
        StringBuilder sb = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
        sb.append("location=" + MainActivity.latitude + "," + MainActivity.longitude);
        sb.append("&radius=5000");
        sb.append("&types=" + type);//"restaurant");
        sb.append("&sensor=true");
        sb.append("&key=AIzaSyChpIrCdU0omB2nwQSvN9lq6w7JfEglncA");

        PlacesDataHit.getPlacesData(sb.toString(),getActivity(),GooglePlaces_DetailDistanceFragment.this);
    }

    public void addDataToList(){
        loading_progressBar.setVisibility(View.GONE);
        fab.setVisibility(View.VISIBLE);
        detail_lv.setAdapter(new DetailDistance_Adapter(getActivity()));
    }

}
