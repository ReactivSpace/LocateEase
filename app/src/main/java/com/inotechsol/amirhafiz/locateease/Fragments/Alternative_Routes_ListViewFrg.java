package com.inotechsol.amirhafiz.locateease.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.inotechsol.amirhafiz.locateease.AlternativeRoutesReturnType.AlternativeRoutes_APIHIT;
import com.inotechsol.amirhafiz.locateease.FragmentHelpers.Alternative_Routes_ListViewFrg_Adapter;
import com.inotechsol.amirhafiz.locateease.MainActivity;
import com.inotechsol.amirhafiz.locateease.R;

/**
 * Created by Amir on 5/13/2017.
 */

public class Alternative_Routes_ListViewFrg extends Fragment {


    String BASE_URL;
    ProgressBar loading_progressBar;
    ListView alternative_routes_lv;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View frgView = inflater.inflate(R.layout.alternative_routes_listview_frg, container, false);


        //https://maps.googleapis.com/maps/api/directions/json?origin=33.644356,73.0497202&destination=33.629443,73.09047559999999
        // &sensor=false&units=metric&mode=driving&alternatives=true&key=AIzaSyAFV9mbabrsd6gy7oYABWPNNK7sQTfaGpg
        BASE_URL = "https://maps.googleapis.com/maps/api/directions/json?origin=" +
                MainActivity.latitude + "," + MainActivity.longitude + "&destination=" + Go_Fragment.Alternativelatitude + "," + Go_Fragment.AlternativeLongitude +
                "&sensor=false&units=metric&mode=driving&alternatives=true" +
                "&key=AIzaSyAFV9mbabrsd6gy7oYABWPNNK7sQTfaGpg";

        alternative_routes_lv = (ListView) frgView.findViewById(R.id.alternative_routes_lv);
        loading_progressBar = (ProgressBar) frgView.findViewById(R.id.loading_progressBar);


        loading_progressBar.setVisibility(View.VISIBLE);


        AlternativeRoutes_APIHIT.getPathData(BASE_URL, getActivity(), Alternative_Routes_ListViewFrg.this);


        return frgView;
    }

    public void calledFromAdapter(int position) {
        ((Go_Fragment) getActivity().getSupportFragmentManager().findFragmentByTag("go_frgment"))
                .updateFromAlternative_Routes_ListView_Frg(AlternativeRoutes_APIHIT.mRootObject, position);
        MainActivity.AdCounter++;
        if (MainActivity.AdCounter % 5 == 0) {
            if (MainActivity.mInterstitialAd.isLoaded()) {
                MainActivity.mInterstitialAd.show();
            }
        }


        getActivity().getSupportFragmentManager().popBackStack();

    }

    public void populate_list() {
        loading_progressBar.setVisibility(View.GONE);
        alternative_routes_lv.setAdapter(new Alternative_Routes_ListViewFrg_Adapter(getActivity(), Alternative_Routes_ListViewFrg.this));
//        alternative_routes_lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//
//            }
//        });

//        Additional_Info_Fragment frg = (Additional_Info_Fragment) ((MainActivity) getActivity()).mAdapter.getFragment(4);
//        frg.callFromEditAdditionalFragment();


        Alternative_Routes_MApsViewFrg frg = (Alternative_Routes_MApsViewFrg) ((Alternative_Routes_Frg.mAdapter.getFragment(1)));
        frg.getDataGmaps();
//        Alternative_Routes_MApsViewFrg frg = (Alternative_Routes_MApsViewFrg) ((Alternative_Routes_Frg)getActivity().getSupportFragmentManager().getFragments())
//                .mAdapter.getFragment(2);
//        frg.getDataGmaps();

//        ((Alternative_Routes_MApsViewFrg) getActivity()
//                .getSupportFragmentManager()
//                .findFragmentById(R.id.pager)
//        ).getDataGmaps();

//
//        ((Alternative_Routes_MApsViewFrg)
//                (getActivity().getSupportFragmentManager().findFragmentByTag(Alternative_Routes_Frg.makeFragmentName(R.id.pager,1)))).getDataGmaps();

//                (Alternative_Routes_Frg)getActivity().getSupportFragmentManager()
//                .findFragmentByTag(Alternative_Routes_Frg.makeFragmentName(R.id.pager,1);


    }
}