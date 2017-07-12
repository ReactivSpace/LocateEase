package com.inotechsol.amirhafiz.locateease.Fragments;

import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.inotechsol.amirhafiz.locateease.MainActivity;
import com.inotechsol.amirhafiz.locateease.R;

/**
 * Created by Amir on 5/5/2017.
 */

public class PickUpFriend extends Fragment {


    Button pickup_firend_send_message_btn,pickup_firend_send_email_btn;
    TextView pickup_firend_smple_text,simple_txt_des;
    Typeface mRobotoLight,mRobotoRegular;
    String MessageBody;

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) {

        View frgView = inflater.inflate(R.layout.pickup_friend,container,false);

        mRobotoLight = Typeface.createFromAsset(MainActivity.mContext.getAssets(),"fonts/Roboto-Light.ttf");
        mRobotoRegular = Typeface.createFromAsset(MainActivity.mContext.getAssets(),"fonts/Roboto-Regular.ttf");

        pickup_firend_smple_text = (TextView)frgView.findViewById(R.id.pickup_firend_smple_text);
        pickup_firend_smple_text.setTypeface(mRobotoRegular);


        simple_txt_des = (TextView)frgView.findViewById(R.id.simple_txt_des);
        simple_txt_des.setTypeface(mRobotoLight);

        MessageBody  = "Hi this is my current position. Kindly pick me up from there.\n" +  "Longitude: " + MainActivity.longitude + "\n" + "Latitude: " + MainActivity.latitude + "\n" +
                "https://maps.google.com/maps?sensor=false&t=m&z=17&q=" + MainActivity.latitude + "," + MainActivity.longitude;



        pickup_firend_send_message_btn = (Button)frgView.findViewById(R.id.pickup_firend_send_message_btn);
        pickup_firend_send_message_btn.setTypeface(mRobotoLight);
        pickup_firend_send_message_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                MainActivity.AdCounter++;
                if (MainActivity.AdCounter % 5 == 0) {
                    if (MainActivity.mInterstitialAd.isLoaded()) {
                        MainActivity.mInterstitialAd.show();
                    }
                }


                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("sms:" + ""));
                intent.putExtra("sms_body", MessageBody);
                startActivity(intent);
            }
        });


        pickup_firend_send_email_btn = (Button)frgView.findViewById(R.id.pickup_firend_send_email_btn);
        pickup_firend_send_email_btn.setTypeface(mRobotoLight);
        pickup_firend_send_email_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                MainActivity.AdCounter++;
                if (MainActivity.AdCounter % 5 == 0) {
                    if (MainActivity.mInterstitialAd.isLoaded()) {
                        MainActivity.mInterstitialAd.show();
                    }
                }


                Intent intent = new Intent(Intent.ACTION_SEND); intent.setType("text/html");
                intent.putExtra(Intent.EXTRA_EMAIL, "");
                intent.putExtra(Intent.EXTRA_SUBJECT, "My Current Coordinates");
                intent.putExtra(Intent.EXTRA_TEXT, MessageBody);
                startActivity(Intent.createChooser(intent, "Send Email"));
            }
        });


        return frgView;


    }
}
