package com.inotechsol.amirhafiz.locateease.Fragments;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.inotechsol.amirhafiz.locateease.FragmentHelpers.SearchedRecentSavedAdapter;
import com.inotechsol.amirhafiz.locateease.MainActivity;
import com.inotechsol.amirhafiz.locateease.R;
import com.inotechsol.amirhafiz.locateease.RecentSearched.RecentSearched_DataModel;

import java.util.ArrayList;
import java.util.Locale;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Amir on 5/5/2017.
 */

public class SearchFragment extends Fragment {


    private final int REQ_CODE_SPEECH_INPUT = 100;
    ImageView search_frg_search_iv, search_frg_speech_iv;
    EditText search_frg_search_edt;
    public static String edtText;
    ListView last_search_items_lv;
    public static ArrayList<RecentSearched_DataModel> SearchFragment_Recent_SearchDataModelList = new ArrayList<>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View frgView = inflater.inflate(R.layout.search_only, container, false);


        search_frg_search_edt = (EditText) frgView.findViewById(R.id.search_frg_search_edt);


        //List View
        last_search_items_lv = (ListView) frgView.findViewById(R.id.last_search_items_lv);
        last_search_items_lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                MainActivity.AdCounter++;
                if (MainActivity.AdCounter % 5 == 0) {
                    if (MainActivity.mInterstitialAd.isLoaded()) {
                        MainActivity.mInterstitialAd.show();
                    }
                }



                SharedPreferences prefs = getActivity().getSharedPreferences("MY_PREFS_NAME", MODE_PRIVATE);
                String restoredText = prefs.getString("recent_search_list", "");

                if (restoredText != null && !restoredText.isEmpty()) {

                    Gson gson = new Gson();
                    ArrayList<RecentSearched_DataModel> Preferences_SearchFragment_Recent_SearchDataModelList = new ArrayList<>();
                    Preferences_SearchFragment_Recent_SearchDataModelList = gson.fromJson(restoredText, new TypeToken<ArrayList<RecentSearched_DataModel>>() {
                    }.getType());
                    MainActivity.ToSearch_FourSquare_And_Google = Preferences_SearchFragment_Recent_SearchDataModelList.get(position).NameVicinity;


                }


//                if (!edtText.isEmpty() && edtText!=null){
//                    MainActivity.ToSearch_FourSquare_And_Google = edtText;
//                }

                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager
                        .beginTransaction()
                        .replace(R.id.contentPanel, new SearchedComplete_Fragment(), "search_complete")
                        .addToBackStack("search_complete")
                        .commit();


            }
        });

        if (SearchFragment_Recent_SearchDataModelList.size() > 0) {
            SearchFragment_Recent_SearchDataModelList.clear();
        }

        SharedPreferences prefs = getActivity().getSharedPreferences("MY_PREFS_NAME", MODE_PRIVATE);
        String restoredText = prefs.getString("recent_search_list", "");

        if (restoredText != null && !restoredText.isEmpty()) {

            Gson gson = new Gson();
            SearchFragment_Recent_SearchDataModelList = gson.fromJson(restoredText, new TypeToken<ArrayList<RecentSearched_DataModel>>() {
            }.getType());
            last_search_items_lv.setAdapter(new SearchedRecentSavedAdapter(getActivity(), SearchFragment.this));
        }


        search_frg_speech_iv = (ImageView) frgView.findViewById(R.id.search_frg_speech_iv);
        search_frg_speech_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                promptSpeechInput();
            }
        });


        search_frg_search_iv = (ImageView) frgView.findViewById(R.id.search_frg_search_iv);
        search_frg_search_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                MainActivity.AdCounter++;
                if (MainActivity.AdCounter % 5 == 0) {
                    if (MainActivity.mInterstitialAd.isLoaded()) {
                        MainActivity.mInterstitialAd.show();
                    }
                }


                edtText = search_frg_search_edt.getText().toString();
                if (!edtText.isEmpty()) {


                    MainActivity.ToSearch_FourSquare_And_Google = edtText;
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    fragmentManager
                            .beginTransaction()
                            .replace(R.id.contentPanel, new SearchedComplete_Fragment(), "search_complete")
                            .addToBackStack("search_complete")
                            .commit();
                    search_frg_search_edt.setText("");

                } else {
                    Toast.makeText(getActivity(), "Cannot search empty", Toast.LENGTH_LONG).show();
                }

            }
        });


        return frgView;
    }

    private void promptSpeechInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                getString(R.string.app_name));
        try {
            getParentFragment().startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
//            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(getActivity(),
                    "Speech not Supported",
                    Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Receiving speech input
     */

    public void yourMethod(Intent data) {
        // Do whatever you want with your data
        if (data != null) {
            ArrayList<String> result = data
                    .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            search_frg_search_edt.setText(result.get(0));
        }
    }

//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        //super.onActivityResult(requestCode, resultCode, data);
//
//        switch (requestCode) {
//            case REQ_CODE_SPEECH_INPUT: {
//                if (resultCode == getActivity().RESULT_OK && null != data) {
//
//                    ArrayList<String> result = data
//                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
//                    search_frg_search_edt.setText(result.get(0));
//                }
//                break;
//            }
//
//        }
//    }


}
