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
import com.inotechsol.amirhafiz.locateease.FourSquarePlacesData.FS_PlacesAPIHIT;
import com.inotechsol.amirhafiz.locateease.FourSquarePlacesData.JsonReturnType.FS_Places_Adapter;
import com.inotechsol.amirhafiz.locateease.MainActivity;
import com.inotechsol.amirhafiz.locateease.R;

/**
 * Created by Amir on 5/4/2017.
 */

public class FourSquarePlacesDetailDistance_Fragment extends Fragment {


    public static final String FS_ClientScret = "UA5PZZA3CSCCGE3XDFAF0LA5UVR1IQTSUO4A3DJZAZRV1UVV";
    public static final String FS_ClientID = "A5QRDACULBTE41W25OOT3WXGN1GBWNNFODTO4YL3NTTZLNTC";


    ProgressBar loading_progressBar;
    ListView detail_lv;
    FloatingActionButton fab;


    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) {

        View frgView = inflater.inflate(R.layout.foursquare_detail_distance,container,false);

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
                bundle.putString("called_from", "FourSquare_Detail");
                bundle.putString("latitude", FS_PlacesAPIHIT.mRootObject.response.venues.get(position).location.lat +"");
                bundle.putString("longitude", FS_PlacesAPIHIT.mRootObject.response.venues.get(position).location.lng +"");

                MainActivity.Name = FS_PlacesAPIHIT.mRootObject.response.venues.get(position).name;
                MainActivity.Address = FS_PlacesAPIHIT.mRootObject.response.venues.get(position).location.address;
                MainActivity.Source = "FourSquare";


                double latitude = FS_PlacesAPIHIT.mRootObject.response.venues.get(position).location.lat;
                double longitude = FS_PlacesAPIHIT.mRootObject.response.venues.get(position).location.lng;
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

                MainActivity.AdCounter++;
                if (MainActivity.AdCounter % 5 == 0) {
                    if (MainActivity.mInterstitialAd.isLoaded()) {
                        MainActivity.mInterstitialAd.show();
                    }
                }

                // Click action
                AppConfig.calledFromFourSquare = true;
                AppConfig.calledFromGooglePlaces = false;

                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager
                        .beginTransaction()
                        .replace(R.id.contentPanel, new MultiMarker_Fragment(), "multi_marker_frg")
                        .addToBackStack("test")
                        .commit();

            }
        });



        getFourSquareData(MainActivity.FourSqureCatID);


        return frgView;
    }


    public void getFourSquareData(String type){

        //4d4b7104d754a06370d81259
        loading_progressBar.setVisibility(View.VISIBLE);
        fab.setVisibility(View.GONE);
        String BASE_URL = "https://api.foursquare.com/v2/venues/search?" +
                "categoryId=" + type +
                "&ll="+ MainActivity.latitude +"," + MainActivity.longitude +
                "&client_id="+ FS_ClientID +
                "&client_secret="+ FS_ClientScret +
                "&v=20140828";
        FS_PlacesAPIHIT.getPlacesData(BASE_URL,getActivity(),FourSquarePlacesDetailDistance_Fragment.this);


    }

    public void addDataToList(){
        loading_progressBar.setVisibility(View.GONE);
        fab.setVisibility(View.VISIBLE);
        detail_lv.setAdapter(new FS_Places_Adapter(getActivity()));
    }





}
