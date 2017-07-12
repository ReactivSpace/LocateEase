package com.inotechsol.amirhafiz.locateease.Fragments;

import android.app.Dialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.inotechsol.amirhafiz.locateease.FavouritesHelpers.FavouriteDataModel;
import com.inotechsol.amirhafiz.locateease.FragmentHelpers.Favourites_Adapter;
import com.inotechsol.amirhafiz.locateease.MainActivity;
import com.inotechsol.amirhafiz.locateease.R;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Amir on 5/5/2017.
 */

public class Favourites extends Fragment {


//    public static ArrayList<FavouriteDataModel> FavouriteFragment_favouriteDataModelList = new ArrayList<>();

    public static ArrayList<FavouriteDataModel> favouriteDataModelList = new ArrayList<>();
    ListView favourte_items_lv;

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) {


        View frgView = inflater.inflate(R.layout.favourites,container,false);

        favourte_items_lv = (ListView) frgView.findViewById(R.id.favourte_items_lv);
        favourte_items_lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                MainActivity.AdCounter++;
                if (MainActivity.AdCounter % 5 == 0) {
                    if (MainActivity.mInterstitialAd.isLoaded()) {
                        MainActivity.mInterstitialAd.show();
                    }
                }


                SharedPreferences prefs = getActivity().getSharedPreferences("MY_PREFS_NAME", MODE_PRIVATE);
                String restoredText = prefs.getString("fav_list", "");

                if (restoredText != null && !restoredText.isEmpty()) {

                    Gson gson = new Gson();
                    ArrayList<FavouriteDataModel> Preferences_SearchFragment_Recent_SearchDataModelList = new ArrayList<>();
                    Preferences_SearchFragment_Recent_SearchDataModelList = gson.fromJson(restoredText,new TypeToken<ArrayList<FavouriteDataModel>>(){}.getType());
                    MainActivity.ToSearch_FourSquare_And_Google = Preferences_SearchFragment_Recent_SearchDataModelList.get(position).NameVicinity;

                    Fragment fragment = new SingleMarkerMapView();
                    Bundle bundle = new Bundle();
                    bundle.putString("called_from", "favourites");
                    bundle.putString("latitude", Preferences_SearchFragment_Recent_SearchDataModelList.get(position).lat + "");
                    bundle.putString("longitude", Preferences_SearchFragment_Recent_SearchDataModelList.get(position).lng + "");
                    bundle.putString("distance", Preferences_SearchFragment_Recent_SearchDataModelList.get(position).distance);
                    bundle.putString("map_source", Preferences_SearchFragment_Recent_SearchDataModelList.get(position).Maps_Source);
                    bundle.putString("address", Preferences_SearchFragment_Recent_SearchDataModelList.get(position).Address);
                    bundle.putString("name", Preferences_SearchFragment_Recent_SearchDataModelList.get(position).NameVicinity);
                    bundle.putInt("marker", Preferences_SearchFragment_Recent_SearchDataModelList.get(position).Marker);

                    fragment.setArguments(bundle);
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    fragmentManager
                            .beginTransaction()
                            .replace(R.id.contentPanel, fragment, "single_marker_view")
                            .addToBackStack("test")
                            .commit();

                }




            }
        });





        if(favouriteDataModelList.size()>0){
            favouriteDataModelList.clear();
        }

        SharedPreferences prefs = getActivity().getSharedPreferences("MY_PREFS_NAME", MODE_PRIVATE);
        String restoredText = prefs.getString("fav_list", "");

        if (restoredText != null && !restoredText.isEmpty()) {

            Gson gson = new Gson();
            favouriteDataModelList = gson.fromJson(restoredText,new TypeToken<ArrayList<FavouriteDataModel>>(){}.getType());
            favourte_items_lv.setAdapter(new Favourites_Adapter(getActivity(), Favourites.this));
        }








        return frgView;

    }

    public void delateFavouriteItem(final int position){

        final Dialog aboutusDialog = new Dialog(getActivity());
        aboutusDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        aboutusDialog.setContentView(R.layout.delete_fav_dialog);
        //aboutusDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


        TextView confirm_tv_ok = (TextView) aboutusDialog.findViewById(R.id.confirm_tv_ok);
        confirm_tv_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                SharedPreferences prefs = getActivity().getSharedPreferences("MY_PREFS_NAME", MODE_PRIVATE);
                String restoredText = prefs.getString("fav_list", "");
                if (restoredText != null && !restoredText.isEmpty()) {


                    Gson gson = new Gson();
                    favouriteDataModelList = gson.fromJson(restoredText,new TypeToken<ArrayList<FavouriteDataModel>>(){}.getType());
                    favouriteDataModelList.remove(position);

                    Gson gsonTwo = new Gson();
                    String jsonObj = gsonTwo.toJson(favouriteDataModelList, new TypeToken<ArrayList<FavouriteDataModel>>() {
                    }.getType());

                    Log.d("gons_Data_parsed", jsonObj);

                    Toast.makeText(getActivity(), "Item Removed", Toast.LENGTH_LONG).show();

                    SharedPreferences.Editor editor = getActivity().getSharedPreferences("MY_PREFS_NAME", MODE_PRIVATE).edit();
                    editor.putString("fav_list", jsonObj);
                    editor.commit();

                    favourte_items_lv.setAdapter(new Favourites_Adapter(getActivity(),Favourites.this));


                    aboutusDialog.cancel();

                }


            }
        });

        TextView confirm_tv_cancel = (TextView) aboutusDialog.findViewById(R.id.confirm_tv_cancel);
        confirm_tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                aboutusDialog.cancel();
            }
        });

        aboutusDialog.show();




















    }

}
