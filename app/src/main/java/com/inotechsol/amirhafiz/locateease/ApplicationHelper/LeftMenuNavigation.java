package com.inotechsol.amirhafiz.locateease.ApplicationHelper;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Amir on 5/3/2017.
 */

public class LeftMenuNavigation implements Parcelable {
    public int icon;
    public String name;
    public String value;
    public String FSCatID;

    public LeftMenuNavigation(int icon, String name,String value,String _FSCatID)
    {
        this.icon = icon;
        this.name = name;
        this.value = value;
        this.FSCatID = _FSCatID;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }
}
