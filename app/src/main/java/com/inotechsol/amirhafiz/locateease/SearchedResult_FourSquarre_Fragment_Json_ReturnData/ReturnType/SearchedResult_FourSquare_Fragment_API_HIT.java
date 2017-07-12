package com.inotechsol.amirhafiz.locateease.SearchedResult_FourSquarre_Fragment_Json_ReturnData.ReturnType;

import android.content.Context;
import android.widget.Toast;

import com.google.gson.Gson;
import com.inotechsol.amirhafiz.locateease.Fragments.SearchedResult_FourSquare_Fragment;
import com.inotechsol.amirhafiz.locateease.SearchedResult_FourSquarre_Fragment_Json_ReturnData.ReturnType.*;
import com.inotechsol.amirhafiz.locateease.SearchedResult_FourSquarre_Fragment_Json_ReturnData.ReturnType.ReturnType.RootObject;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.Header;

/**
 * Created by Amir on 5/5/2017.
 */

public class SearchedResult_FourSquare_Fragment_API_HIT {


    public static RootObject mRootObject;

    public static void getSearchedPlacesData(String BASE_URL, final Context mContext, final SearchedResult_FourSquare_Fragment searchedResult_google_fragment){


        AsyncHttpClient client = new AsyncHttpClient();
        client.get(BASE_URL, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                try {


                    //FourSquare
                    mRootObject = null;

                    String responce = new String(responseBody);
                    Gson gson = new Gson();

                    mRootObject = gson.fromJson(responce,RootObject.class);
                    searchedResult_google_fragment.addDataToList();

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                Toast.makeText(mContext, "Check Your Internet Connection" + statusCode, Toast.LENGTH_LONG).show();

            }
        });

    }

}
