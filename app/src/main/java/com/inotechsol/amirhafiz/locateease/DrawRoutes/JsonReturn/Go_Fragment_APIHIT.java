package com.inotechsol.amirhafiz.locateease.DrawRoutes.JsonReturn;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.widget.Toast;

import com.google.android.gms.vision.barcode.Barcode;
import com.google.gson.Gson;
import com.inotechsol.amirhafiz.locateease.Fragments.Go_Fragment;
import com.inotechsol.amirhafiz.locateease.Go_Fragment_TurnsHelpers.Go_Fragment_TurnsModel;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.Header;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by Amir on 5/8/2017.
 */

public class Go_Fragment_APIHIT {

    static Geocoder geocoder;
    static List<Address> addresses = new ArrayList<>();


    public static RootObject mRootObject;
    public static List<Go_Fragment_TurnsModel> go_fragment_turnsModels = new ArrayList<>();;

    public static void getPathData(String BASE_URL, final Context mContext, final Go_Fragment mapsAllDetailFragment) {


        AsyncHttpClient client = new AsyncHttpClient();
        client.setConnectTimeout(3000);
        client.get(BASE_URL, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                try {

                    mRootObject = null;

                    String responce = new String(responseBody, "UTF-8");
                    Gson gson = new Gson();

                    mRootObject = gson.fromJson(responce, RootObject.class);
                    FillData(mRootObject, mContext,mapsAllDetailFragment);



                } catch (Exception e) {
                    e.printStackTrace();
                }

            }


            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Toast.makeText(mContext, "Failure" + statusCode, Toast.LENGTH_LONG).show();

            }
        });

    }


    public static void FillData(final RootObject mRootObject, final Context mContext, final Go_Fragment mapsAllDetailFragment) {



        new AsyncTask<Barcode.GeoPoint, Void, Address>()
        {
            @Override
            protected Address doInBackground(Barcode.GeoPoint... geoPoints)
            {


                if(go_fragment_turnsModels.size()>0){
                    go_fragment_turnsModels.clear();
                }

                Go_Fragment_TurnsModel go_fragment_turnsModel;
                geocoder = new Geocoder(mContext, Locale.getDefault());

                for (int i = 0; i < mRootObject.routes.get(0).legs.get(0).steps.size(); i++) {

                    go_fragment_turnsModel = new Go_Fragment_TurnsModel();
                    go_fragment_turnsModel.distance = mRootObject.routes.get(0).legs.get(0).steps.get(i).distance.text;
                    go_fragment_turnsModel.lat= mRootObject.routes.get(0).legs.get(0).steps.get(i).start_location.lat;
                    go_fragment_turnsModel.lng= mRootObject.routes.get(0).legs.get(0).steps.get(i).start_location.lng;



                    if(addresses.size()>0){
                        addresses.clear();
                    }
                    String address = "";
                    try {
                        addresses = geocoder.getFromLocation(mRootObject.routes.get(0).legs.get(0).steps.get(i).start_location.lat,
                                mRootObject.routes.get(0).legs.get(0).steps.get(i).start_location.lng, 1);
                        address = addresses.get(0).getAddressLine(0);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    go_fragment_turnsModel.TurnLocationName = address;
                    go_fragment_turnsModels.add(go_fragment_turnsModel);

                }





//                    Geocoder geoCoder = new Geocoder(context);
//                    double latitude = geoPoints[0].getLatitudeE6() / 1E6;
//                    double longitude = geoPoints[0].getLongitudeE6() / 1E6;
//                    List<Address> addresses = geoCoder.getFromLocation(latitude, longitude, 1);
//                    if (addresses.size() > 0)
//                        return addresses.get(0);
                return null;
            }

            @Override
            protected void onPostExecute(Address address)
            {


                mapsAllDetailFragment.getDataGmaps();
                // do whatever you want/need to do with the address found
                // remember to check first that it's not null
            }
        }.execute();






//        if(go_fragment_turnsModels.size()>0){
//            go_fragment_turnsModels.clear();
//        }
//
//        Go_Fragment_TurnsModel go_fragment_turnsModel;
//        geocoder = new Geocoder(mContext, Locale.getDefault());
//
//        for (int i = 0; i < mRootObject.routes.get(0).legs.get(0).steps.size(); i++) {
//
//            go_fragment_turnsModel = new Go_Fragment_TurnsModel();
//            go_fragment_turnsModel.distance = mRootObject.routes.get(0).legs.get(0).steps.get(i).distance.text;
//
//
//            if(addresses.size()>0){
//                addresses.clear();
//            }
//            String address = "";
//            try {
//                addresses = geocoder.getFromLocation(mRootObject.routes.get(0).legs.get(0).steps.get(i).start_location.lat,
//                        mRootObject.routes.get(0).legs.get(0).steps.get(i).start_location.lng, 1);
//                address = addresses.get(0).getAddressLine(0);
//
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//
//            go_fragment_turnsModel.TurnLocationName = address;
//            go_fragment_turnsModels.add(go_fragment_turnsModel);
//
//        }

//        mapsAllDetailFragment.getDataGmaps();


    }






}
