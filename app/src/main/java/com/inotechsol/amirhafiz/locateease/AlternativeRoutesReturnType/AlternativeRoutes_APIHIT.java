package com.inotechsol.amirhafiz.locateease.AlternativeRoutesReturnType;

import android.content.Context;
import android.widget.Toast;

import com.google.gson.Gson;
import com.inotechsol.amirhafiz.locateease.AlternativeRoutesReturnType.ReturnType.RootObject;
import com.inotechsol.amirhafiz.locateease.Fragments.Alternative_Routes_ListViewFrg;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.Header;

/**
 * Created by Amir on 5/13/2017.
 */

public class AlternativeRoutes_APIHIT {



    public static int RoutesSize;
    public static RootObject mRootObject;
    public static void getPathData(String BASE_URL, final Context mContext, final Alternative_Routes_ListViewFrg alternative_routes_frg){


        AsyncHttpClient client = new AsyncHttpClient();
        client.setConnectTimeout(3000);
        client.get(BASE_URL, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                try {

                    mRootObject = null;

                    String responce = new String(responseBody, "UTF-8");
                    Gson gson = new Gson();

                    mRootObject = gson.fromJson(responce,RootObject.class);

                    RoutesSize = mRootObject.routes.size();
                    alternative_routes_frg.populate_list();

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
            //{
//            https://console.developers.google.com/apis/credentials?project=_


            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Toast.makeText(mContext, "Failure" + statusCode, Toast.LENGTH_LONG).show();

            }
        });

    }
}
