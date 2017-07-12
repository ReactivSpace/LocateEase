package com.inotechsol.amirhafiz.locateease.FragmentHelpers;

import android.content.Context;
import android.graphics.Typeface;
import android.location.Location;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.inotechsol.amirhafiz.locateease.MainActivity;
import com.inotechsol.amirhafiz.locateease.R;
import com.inotechsol.amirhafiz.locateease.SearchedResult_Google_Fragment_Json_ReturnData.SearchedResult_Google_Fragment_API_HIT;

/**
 * Created by Amir on 5/5/2017.
 */

public class SearchedResult_Google_Fragment_API_HIT_Adapter extends BaseAdapter{

    Context mContext;
    public static LayoutInflater inflater = null;
    Typeface mRobotoLight,mRobotoRegular;



    public SearchedResult_Google_Fragment_API_HIT_Adapter(Context _Context){
        this.mContext = _Context;
        inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mRobotoLight = Typeface.createFromAsset(mContext.getAssets(),"fonts/Roboto-Light.ttf");
        mRobotoRegular = Typeface.createFromAsset(mContext.getAssets(),"fonts/Roboto-Regular.ttf");

    }

    @Override
    public int getCount() {
        return SearchedResult_Google_Fragment_API_HIT.mRootObject.results.size();
    }

    @Override
    public Object getItem(int position) {
        return SearchedResult_Google_Fragment_API_HIT.mRootObject.results.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    static class Holder{
        TextView detail_distance_name_txt;
        TextView distance_in_km;
        TextView km_simple_txt;

    }



    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder = null;

        if(convertView==null){


            convertView = inflater.inflate(R.layout.searched_result_goole_fragment_api_hit_list_item,null);

            holder = new Holder();


            holder.detail_distance_name_txt = (TextView)convertView.findViewById(R.id.detail_distance_name_txt);
            holder.distance_in_km = (TextView) convertView.findViewById(R.id.distance_in_km);
            holder.km_simple_txt = (TextView) convertView.findViewById(R.id.km_simple_txt);


            convertView.setTag(holder);

        }else{
            holder = (Holder)convertView.getTag();
        }



        holder.detail_distance_name_txt.setText(SearchedResult_Google_Fragment_API_HIT.mRootObject.results.get(position).formatted_address);
        holder.detail_distance_name_txt.setTypeface(mRobotoRegular);


        holder.distance_in_km.setTypeface(mRobotoRegular);

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

        String rounded = String.format("%.1f", distance);// 1.30


        holder.distance_in_km.setText(rounded);



        holder.km_simple_txt.setTypeface(mRobotoLight);











        return convertView;
    }
}
