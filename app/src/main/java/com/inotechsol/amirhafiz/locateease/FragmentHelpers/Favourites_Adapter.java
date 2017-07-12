package com.inotechsol.amirhafiz.locateease.FragmentHelpers;

import android.content.Context;
import android.graphics.Typeface;
import android.location.Location;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.inotechsol.amirhafiz.locateease.Fragments.Favourites;
import com.inotechsol.amirhafiz.locateease.MainActivity;
import com.inotechsol.amirhafiz.locateease.R;

/**
 * Created by Amir on 5/7/2017.
 */

public class Favourites_Adapter extends BaseAdapter {

    Context mContext;
    public static LayoutInflater inflater = null;
    Favourites mSearchFragment;
    Typeface mRobotoLight,mRobotoRegular;



    public Favourites_Adapter(Context _Context,Favourites _SearchFragment){
        this.mContext = _Context;
        this.mSearchFragment = _SearchFragment;
        inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mRobotoLight = Typeface.createFromAsset(mContext.getAssets(),"fonts/Roboto-Light.ttf");
        mRobotoRegular = Typeface.createFromAsset(mContext.getAssets(),"fonts/Roboto-Regular.ttf");
    }


    @Override
    public int getCount() {
        return Favourites.favouriteDataModelList.size();
    }

    @Override
    public Object getItem(int position) {
        return Favourites.favouriteDataModelList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    static class Holder{
        TextView recent_search_distance_name_txt;
        TextView recent_search_distance_in_km;
        TextView recent_search_km_simple_txt;
        ImageView recent_search_distance_img;
        ImageView unfav_iv;

    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {


        Holder holder = null;

        if(convertView==null){


            convertView = inflater.inflate(R.layout.favourites_list_items,null);

            holder = new Holder();


            holder.recent_search_distance_name_txt = (TextView)convertView.findViewById(R.id.recent_search_distance_name_txt);
            holder.recent_search_distance_in_km = (TextView) convertView.findViewById(R.id.recent_search_distance_in_km);
            holder.recent_search_km_simple_txt = (TextView) convertView.findViewById(R.id.recent_search_km_simple_txt);
            holder.recent_search_distance_img = (ImageView) convertView.findViewById(R.id.recent_search_distance_img);
            holder.unfav_iv = (ImageView)convertView.findViewById(R.id.unfav_iv);

            convertView.setTag(holder);

        }else{
            holder = (Holder)convertView.getTag();
        }



        holder.recent_search_distance_name_txt.setText(Favourites.favouriteDataModelList.get(position).NameVicinity);
        holder.recent_search_distance_name_txt.setTypeface(mRobotoRegular);


        holder.recent_search_distance_in_km.setTypeface(mRobotoRegular);

        double latitude = Favourites.favouriteDataModelList.get(position).lat;
        double longitude = Favourites.favouriteDataModelList.get(position).lng;
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

        holder.recent_search_distance_img.setImageResource(Favourites.favouriteDataModelList.get(position).Marker);

        holder.recent_search_distance_in_km.setTypeface(mRobotoLight);

        holder.unfav_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSearchFragment.delateFavouriteItem(position);
            }
        });




        return convertView;
    }
}
