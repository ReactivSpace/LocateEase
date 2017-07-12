package com.inotechsol.amirhafiz.locateease.FragmentHelpers;

import android.content.Context;
import android.graphics.Typeface;
import android.location.Location;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.inotechsol.amirhafiz.locateease.Fragments.SearchFragment;
import com.inotechsol.amirhafiz.locateease.MainActivity;
import com.inotechsol.amirhafiz.locateease.R;

/**
 * Created by Amir on 5/6/2017.
 */

public class SearchedRecentSavedAdapter extends BaseAdapter {

    Context mContext;
    public static LayoutInflater inflater = null;
    SearchFragment mSearchFragment;
    Typeface mRobotoLight,mRobotoRegular;



    public SearchedRecentSavedAdapter(Context _Context,SearchFragment _SearchFragment){
        this.mContext = _Context;
        this.mSearchFragment = _SearchFragment;
        inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mRobotoLight = Typeface.createFromAsset(mContext.getAssets(),"fonts/Roboto-Light.ttf");
        mRobotoRegular = Typeface.createFromAsset(mContext.getAssets(),"fonts/Roboto-Regular.ttf");
    }


    @Override
    public int getCount() {
        return SearchFragment.SearchFragment_Recent_SearchDataModelList.size();
    }

    @Override
    public Object getItem(int position) {
        return SearchFragment.SearchFragment_Recent_SearchDataModelList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    static class Holder{
        TextView recent_search_distance_name_txt;
        TextView recent_search_distance_in_km;
        TextView recent_search_km_simple_txt;

    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        Holder holder = null;

        if(convertView==null){


            convertView = inflater.inflate(R.layout.recent_searched_list_items,null);

            holder = new Holder();


            holder.recent_search_distance_name_txt = (TextView)convertView.findViewById(R.id.recent_search_distance_name_txt);
            holder.recent_search_distance_in_km = (TextView) convertView.findViewById(R.id.recent_search_distance_in_km);
            holder.recent_search_km_simple_txt = (TextView) convertView.findViewById(R.id.recent_search_km_simple_txt);


            convertView.setTag(holder);

        }else{
            holder = (Holder)convertView.getTag();
        }



        holder.recent_search_distance_name_txt.setText(SearchFragment.SearchFragment_Recent_SearchDataModelList.get(position).NameVicinity);
        holder.recent_search_distance_name_txt.setTypeface(mRobotoRegular);


        holder.recent_search_distance_in_km.setTypeface(mRobotoRegular);

        double latitude = SearchFragment.SearchFragment_Recent_SearchDataModelList.get(position).lat;
        double longitude = SearchFragment.SearchFragment_Recent_SearchDataModelList.get(position).lng;
        float distance=0;
        Location crntLocation=new Location("crntlocation");
        crntLocation.setLatitude(MainActivity.latitude);
        crntLocation.setLongitude(MainActivity.longitude);

        Location newLocation=new Location("newlocation");
        newLocation.setLatitude(latitude);
        newLocation.setLongitude(longitude);


        //float distance = crntLocation.distanceTo(newLocation);  in meters
        distance =crntLocation.distanceTo(newLocation) / 1000; // in km

        String rounded = String.format("%.1f", distance);// 1.30


        holder.recent_search_distance_in_km.setText(rounded);



        holder.recent_search_distance_in_km.setTypeface(mRobotoLight);




        return convertView;
    }
}
