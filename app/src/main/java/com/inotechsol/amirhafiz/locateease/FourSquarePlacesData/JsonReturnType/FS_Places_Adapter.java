package com.inotechsol.amirhafiz.locateease.FourSquarePlacesData.JsonReturnType;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.inotechsol.amirhafiz.locateease.FourSquarePlacesData.FS_PlacesAPIHIT;
import com.inotechsol.amirhafiz.locateease.MainActivity;
import com.inotechsol.amirhafiz.locateease.R;

/**
 * Created by Amir on 5/4/2017.
 */

public class FS_Places_Adapter extends BaseAdapter {


    Context mContext;
    public static LayoutInflater inflater = null;
    Typeface mRobotoLight,mRobotoRegular;


    public FS_Places_Adapter(Context _Context){
        this.mContext = _Context;
        inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mRobotoLight = Typeface.createFromAsset(mContext.getAssets(),"fonts/Roboto-Light.ttf");
        mRobotoRegular = Typeface.createFromAsset(mContext.getAssets(),"fonts/Roboto-Regular.ttf");
    }

    @Override
    public int getCount() {
        return FS_PlacesAPIHIT.mRootObject.response.venues.size();
    }

    @Override
    public Object getItem(int position) {
        return FS_PlacesAPIHIT.mRootObject.response.venues.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    static class Holder{
        TextView detail_distance_name_txt;
        TextView detail_distance_address_txt;
        TextView distance_in_km;
        TextView km_simple_txt;

        ImageView detail_distance_img;

    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        Holder holder = null;

        if(convertView==null){


            convertView = inflater.inflate(R.layout.detail_distance_list_item,null);

            holder = new Holder();


            holder.detail_distance_name_txt = (TextView)convertView.findViewById(R.id.detail_distance_name_txt);
            holder.detail_distance_address_txt = (TextView) convertView.findViewById(R.id.detail_distance_address_txt);
            holder.distance_in_km = (TextView) convertView.findViewById(R.id.distance_in_km);
            holder.km_simple_txt = (TextView) convertView.findViewById(R.id.km_simple_txt);
            holder.detail_distance_img = (ImageView)convertView.findViewById(R.id.detail_distance_img);


            convertView.setTag(holder);

        }else{
            holder = (Holder)convertView.getTag();
        }



        holder.detail_distance_name_txt.setText(FS_PlacesAPIHIT.mRootObject.response.venues.get(position).name);
        holder.detail_distance_name_txt.setTypeface(mRobotoRegular);

        holder.detail_distance_address_txt.setText(FS_PlacesAPIHIT.mRootObject.response.venues.get(position).location.address);
        holder.detail_distance_address_txt.setTypeface(mRobotoLight);


        holder.detail_distance_img.setImageResource(MainActivity.PlaceDrawable);


        holder.distance_in_km.setTypeface(mRobotoRegular);




        double latitude = FS_PlacesAPIHIT.mRootObject.response.venues.get(position).location.lat;
        double longitude = FS_PlacesAPIHIT.mRootObject.response.venues.get(position).location.lng;
        float distance=0;
        android.location.Location crntLocation=new android.location.Location("crntlocation");
        crntLocation.setLatitude(MainActivity.latitude);
        crntLocation.setLongitude(MainActivity.longitude);

        android.location.Location newLocation=new android.location.Location("newlocation");
        newLocation.setLatitude(latitude);
        newLocation.setLongitude(longitude);


        //float distance = crntLocation.distanceTo(newLocation);  in meters
        distance = crntLocation.distanceTo(newLocation) / 1000; // in km

        String rounded = String.format("%.1f", distance);// 1.30

        holder.distance_in_km.setText(rounded);



        holder.km_simple_txt.setTypeface(mRobotoLight);











        return convertView;

    }
}
