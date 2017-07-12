package com.inotechsol.amirhafiz.locateease.ApplicationHelper;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.inotechsol.amirhafiz.locateease.R;

/**
 * Created by Amir on 5/3/2017.
 */

public class ListAdapter_leftNav extends BaseAdapter {


    LeftMenuNavigation[] mleftMenuNavigation;
    Context mContext;
    public static LayoutInflater inflater = null;
    Typeface mRobotoLight;


    public ListAdapter_leftNav(Context _Context,LeftMenuNavigation[] _leftMenuNavigation){

        this.mContext = _Context;
        this.mleftMenuNavigation = _leftMenuNavigation;
        inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mRobotoLight = Typeface.createFromAsset(mContext.getAssets(),"fonts/Roboto-Light.ttf");
    }


    @Override
    public int getCount() {
        return mleftMenuNavigation.length;
    }

    @Override
    public Object getItem(int position) {
        return mleftMenuNavigation[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }




    static class Holder{
        TextView textViewName;
        ImageView imageViewIcon;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder = null;

        if(convertView==null){


            convertView = inflater.inflate(R.layout.listview_left_nav,null);

            holder = new Holder();

            holder.imageViewIcon = (ImageView)
                    convertView.findViewById(R.id.imageViewIcon);

            holder.textViewName = (TextView)
                    convertView.findViewById(R.id.textViewName);



            convertView.setTag(holder);

        }else{
            holder = (Holder)convertView.getTag();
        }



        holder.textViewName.setText(mleftMenuNavigation[position].name);
        holder.textViewName.setTypeface(mRobotoLight);
        holder.imageViewIcon.setImageResource(mleftMenuNavigation[position].icon);

        return convertView;

    }
}
