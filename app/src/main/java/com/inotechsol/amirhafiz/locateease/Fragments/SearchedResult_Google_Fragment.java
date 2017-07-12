package com.inotechsol.amirhafiz.locateease.Fragments;

import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.inotechsol.amirhafiz.locateease.ApplicationHelper.AppConfig;
import com.inotechsol.amirhafiz.locateease.FragmentHelpers.SearchedResult_Google_Fragment_API_HIT_Adapter;
import com.inotechsol.amirhafiz.locateease.MainActivity;
import com.inotechsol.amirhafiz.locateease.R;
import com.inotechsol.amirhafiz.locateease.RecentSearched.RecentSearched_DataModel;
import com.inotechsol.amirhafiz.locateease.SearchedResult_FourSquarre_Fragment_Json_ReturnData.ReturnType.SearchedResult_FourSquare_Fragment_API_HIT;
import com.inotechsol.amirhafiz.locateease.SearchedResult_Google_Fragment_Json_ReturnData.SearchedResult_Google_Fragment_API_HIT;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Amir on 5/5/2017.
 */

public class SearchedResult_Google_Fragment extends Fragment {

    RelativeLayout not_found_container;
    ProgressBar loading_progressBar;
    ListView searched_result_google_location;
    public static ArrayList<RecentSearched_DataModel> SearchFragment_Recent_SearchDataModelList = new ArrayList<>();

    String BASE_URL = "http://maps.google.com/maps/api/geocode/json?address=" + MainActivity.ToSearch_FourSquare_And_Google +"&sensor=false";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View frgView = inflater.inflate(R.layout.searched_result_google,container,false);

        not_found_container = (RelativeLayout)frgView.findViewById(R.id.not_found_container);
        loading_progressBar = (ProgressBar)frgView.findViewById(R.id.loading_progressBar);
        searched_result_google_location = (ListView)frgView.findViewById(R.id.searched_result_google_location);
        searched_result_google_location.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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

                bundle.putString("latitude", SearchedResult_Google_Fragment_API_HIT.mRootObject.results.get(position).geometry.location.lat +"");
                AppConfig.SearchedLat = SearchedResult_Google_Fragment_API_HIT.mRootObject.results.get(position).geometry.location.lat;
                bundle.putString("longitude", SearchedResult_Google_Fragment_API_HIT.mRootObject.results.get(position).geometry.location.lng +"");
                AppConfig.SearchedLng = SearchedResult_Google_Fragment_API_HIT.mRootObject.results.get(position).geometry.location.lng;

                MainActivity.Name = SearchedResult_Google_Fragment_API_HIT.mRootObject.results.get(position).address_components.get(0).short_name;
                AppConfig.SearchedName = MainActivity.Name;

                MainActivity.Address = SearchedResult_Google_Fragment_API_HIT.mRootObject.results.get(position).formatted_address;
                MainActivity.Source = "Google";


                double latitude = SearchedResult_Google_Fragment_API_HIT.mRootObject.results.get(position).geometry.location.lat;
                double longitude = SearchedResult_Google_Fragment_API_HIT.mRootObject.results.get(position).geometry.location.lng;
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

                MainActivity.PlaceDrawable = R.drawable.images;


                //Add search to recent List
                addToRecent();


                fragment.setArguments(bundle);

                fragmentManager
                        .beginTransaction()
                        .replace(R.id.contentPanel,fragment,"single_marker_view")
                        .addToBackStack("test")
                        .commit();


            }
        });

        SearchedResult_Google_Fragment_API_HIT.getSearchedPlacesData(BASE_URL,getActivity(),SearchedResult_Google_Fragment.this);


        return frgView;

    }
    public void addDataToList(){
        loading_progressBar.setVisibility(View.GONE);
        if(SearchedResult_FourSquare_Fragment_API_HIT.mRootObject.response.venues.size()==0){

            not_found_container.setVisibility(View.VISIBLE);
            searched_result_google_location.setVisibility(View.GONE);
        }else{

            not_found_container.setVisibility(View.GONE);
            searched_result_google_location.setVisibility(View.VISIBLE);
        }

        searched_result_google_location.setAdapter(new SearchedResult_Google_Fragment_API_HIT_Adapter(getActivity()));
    }


    public void addToRecent() {

        SharedPreferences prefs = getActivity().getSharedPreferences("MY_PREFS_NAME", MODE_PRIVATE);
        String restoredText = prefs.getString("recent_search_list", "");
        if (restoredText != null && !restoredText.isEmpty()) {

            Gson gson = new Gson();
            SearchFragment_Recent_SearchDataModelList = gson.fromJson(restoredText, new TypeToken<ArrayList<RecentSearched_DataModel>>() {
            }.getType());


            RecentSearched_DataModel recentSearched_dataModel = new RecentSearched_DataModel();
            recentSearched_dataModel.lat = AppConfig.SearchedLat;
            recentSearched_dataModel.lng = AppConfig.SearchedLng;
            recentSearched_dataModel.NameVicinity = AppConfig.SearchedName;

            SearchFragment_Recent_SearchDataModelList.add(recentSearched_dataModel);
            Gson gsonTwo = new Gson();
            String jsonObj = gsonTwo.toJson(SearchFragment_Recent_SearchDataModelList, new TypeToken<ArrayList<RecentSearched_DataModel>>() {
            }.getType());

            Log.d("gons_Data_parsed", jsonObj);

           //Toast.makeText(getActivity(), "Added to favourites", Toast.LENGTH_LONG).show();

            SharedPreferences.Editor editor = getActivity().getSharedPreferences("MY_PREFS_NAME", MODE_PRIVATE).edit();
            editor.putString("recent_search_list", jsonObj);
            editor.commit();
        } else {


            RecentSearched_DataModel recentSearched_dataModel = new RecentSearched_DataModel();
            recentSearched_dataModel.lat = AppConfig.SearchedLat;
            recentSearched_dataModel.lng = AppConfig.SearchedLng;
            recentSearched_dataModel.NameVicinity = AppConfig.SearchedName;


            SearchFragment_Recent_SearchDataModelList.add(recentSearched_dataModel);
            Gson gsonTwo = new Gson();
            String jsonObj = gsonTwo.toJson(SearchFragment_Recent_SearchDataModelList, new TypeToken<ArrayList<RecentSearched_DataModel>>() {
            }.getType());

            Log.d("gons_Data_parsed", jsonObj);

           //Toast.makeText(getActivity(), "Added to favourites", Toast.LENGTH_LONG).show();

            SharedPreferences.Editor editor = getActivity().getSharedPreferences("MY_PREFS_NAME", MODE_PRIVATE).edit();
            editor.putString("recent_search_list", jsonObj);
            editor.commit();


        }


    }

}
