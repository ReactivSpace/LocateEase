package com.inotechsol.amirhafiz.locateease.FourSquarePlacesData;

import android.content.Context;
import android.widget.Toast;

import com.google.gson.Gson;
import com.inotechsol.amirhafiz.locateease.FourSquarePlacesData.JsonReturnType.RootObject;
import com.inotechsol.amirhafiz.locateease.Fragments.FourSquarePlacesDetailDistance_Fragment;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.Header;

/**
 * Created by Amir on 5/4/2017.
 */

public class FS_PlacesAPIHIT {


    public static RootObject mRootObject;
    public static void getPlacesData(String BASE_URL, final Context mContext, final FourSquarePlacesDetailDistance_Fragment mapsAllDetailFragment){


        AsyncHttpClient client = new AsyncHttpClient();
        client.get(BASE_URL, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                try {

                    mRootObject = null;

                    String responce = new String(responseBody, "UTF-8");
                    Gson gson = new Gson();

                    mRootObject = gson.fromJson(responce,RootObject.class);
                    mapsAllDetailFragment.addDataToList();

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


}
