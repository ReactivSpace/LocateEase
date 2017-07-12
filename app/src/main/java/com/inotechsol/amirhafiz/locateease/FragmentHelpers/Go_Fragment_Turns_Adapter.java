package com.inotechsol.amirhafiz.locateease.FragmentHelpers;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.inotechsol.amirhafiz.locateease.DrawRoutes.JsonReturn.Go_Fragment_APIHIT;
import com.inotechsol.amirhafiz.locateease.Fragments.Go_Fragment;
import com.inotechsol.amirhafiz.locateease.R;
import com.squareup.picasso.Picasso;

/**
 * Created by Amir on 5/16/2017.
 */

public class Go_Fragment_Turns_Adapter extends BaseAdapter {


    Context mContext;
    public static LayoutInflater inflater = null;
    Go_Fragment mGo_fragment;
    Typeface mRobotoLight, mRobotoRegular;


    public Go_Fragment_Turns_Adapter(Context _Context, Go_Fragment _mGo_fragment) {

        this.mContext = _Context;
        this.mGo_fragment = _mGo_fragment;
        inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mRobotoLight = Typeface.createFromAsset(mContext.getAssets(), "fonts/Roboto-Light.ttf");
        mRobotoRegular = Typeface.createFromAsset(mContext.getAssets(), "fonts/Roboto-Regular.ttf");
    }


    @Override
    public int getCount() {
        return Go_Fragment_APIHIT.go_fragment_turnsModels.size();
    }

    @Override
    public Object getItem(int position) {
        return Go_Fragment_APIHIT.go_fragment_turnsModels.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    static class Holder {
        TextView go_frg_tunrs_distance_in_km;
        TextView go_frg_tunrs_road_name_tv;
        ImageView go_frg_turns_iv;

    }


    //go_frg_turn_adapter_lv_items
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder = null;

        if (convertView == null) {


            convertView = inflater.inflate(R.layout.go_frg_turn_adapter_lv_items, null);

            holder = new Holder();


            holder.go_frg_tunrs_distance_in_km = (TextView) convertView.findViewById(R.id.go_frg_tunrs_distance_in_km);
            holder.go_frg_tunrs_road_name_tv = (TextView) convertView.findViewById(R.id.go_frg_tunrs_road_name_tv);
            holder.go_frg_turns_iv = (ImageView) convertView.findViewById(R.id.go_frg_turns_iv);


            convertView.setTag(holder);

        } else {
            holder = (Holder) convertView.getTag();
        }


        holder.go_frg_tunrs_distance_in_km.setText(Go_Fragment_APIHIT.go_fragment_turnsModels.get(position).distance);
        holder.go_frg_tunrs_road_name_tv.setText(Go_Fragment_APIHIT.go_fragment_turnsModels.get(position).TurnLocationName);

        if(Go_Fragment_APIHIT.go_fragment_turnsModels.get(position).turnicon>0){
            Picasso.with(mContext)
                    .load(Go_Fragment_APIHIT.go_fragment_turnsModels.get(position).turnicon)
                    .fit()
                    .placeholder(R.drawable.loading)
                    .into(holder.go_frg_turns_iv);

        }


        //holder.go_frg_turns_iv.setImageResource();


        return convertView;
    }
}
