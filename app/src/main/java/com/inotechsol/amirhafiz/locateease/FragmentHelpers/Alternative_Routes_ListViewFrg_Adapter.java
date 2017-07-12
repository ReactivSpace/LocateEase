package com.inotechsol.amirhafiz.locateease.FragmentHelpers;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.inotechsol.amirhafiz.locateease.AlternativeRoutesReturnType.AlternativeRoutes_APIHIT;
import com.inotechsol.amirhafiz.locateease.Fragments.Alternative_Routes_ListViewFrg;
import com.inotechsol.amirhafiz.locateease.MainActivity;
import com.inotechsol.amirhafiz.locateease.R;

/**
 * Created by Amir on 5/13/2017.
 */

public class Alternative_Routes_ListViewFrg_Adapter extends BaseAdapter{

    Context mContext;
    public static LayoutInflater inflater = null;
    Typeface mRobotoLight,mRobotoRegular;
    Alternative_Routes_ListViewFrg mAlternative_routes_listViewFrg;

    public Alternative_Routes_ListViewFrg_Adapter(Context _Context, Alternative_Routes_ListViewFrg _alternative_routes_listViewFrg){
        this.mContext = _Context;
        inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mRobotoLight = Typeface.createFromAsset(mContext.getAssets(),"fonts/Roboto-Light.ttf");
        mRobotoRegular = Typeface.createFromAsset(mContext.getAssets(),"fonts/Roboto-Regular.ttf");
        this.mAlternative_routes_listViewFrg = _alternative_routes_listViewFrg;

    }

    @Override
    public int getCount() {
        return AlternativeRoutes_APIHIT.mRootObject.routes.size();
    }

    @Override
    public Object getItem(int position) {
        return AlternativeRoutes_APIHIT.mRootObject.routes.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }



    static class Holder{
        TextView alternative_routes_listview_items_time;
        TextView alternative_routes_listview_items_distance;
        TextView alternative_routes_listview_items_via;
        Button alternative_routes_follow_btn;

    }




    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        Holder holder = null;

        if(convertView==null){


            convertView = inflater.inflate(R.layout.alternative_listview_frg_lv_items,null);

            holder = new Holder();


            holder.alternative_routes_listview_items_time = (TextView)convertView.findViewById(R.id.alternative_routes_listview_items_time);
            holder.alternative_routes_listview_items_distance = (TextView) convertView.findViewById(R.id.alternative_routes_listview_items_distance);
            holder.alternative_routes_listview_items_via = (TextView) convertView.findViewById(R.id.alternative_routes_listview_items_via);
            holder.alternative_routes_follow_btn = (Button) convertView.findViewById(R.id.alternative_routes_follow_btn);


            convertView.setTag(holder);

        }else{
            holder = (Holder)convertView.getTag();
        }


        holder.alternative_routes_listview_items_time.setText(AlternativeRoutes_APIHIT.mRootObject.routes.get(position).legs.get(0).duration.text);


        holder.alternative_routes_listview_items_distance.setText(AlternativeRoutes_APIHIT.mRootObject.routes.get(position).legs.get(0).distance.text);
        holder.alternative_routes_listview_items_distance.setTypeface(mRobotoLight);


        holder.alternative_routes_listview_items_via.setTypeface(mRobotoLight);
        holder.alternative_routes_listview_items_via.setText("Via: "+ AlternativeRoutes_APIHIT.mRootObject.routes.get(position).summary + " "+ MainActivity.Name);

        holder.alternative_routes_follow_btn.setTypeface(mRobotoLight);
        holder.alternative_routes_follow_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mAlternative_routes_listViewFrg.calledFromAdapter(position);


            }
        });


        return convertView;
    }
}
