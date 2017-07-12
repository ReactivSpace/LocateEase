package com.inotechsol.amirhafiz.locateease.FourSquarePlacesData.JsonReturnType;

import java.util.List;

/**
 * Created by Amir on 5/4/2017.
 */

public class Location {
    public String address;
    public double lat;
    public double lng;
    public List<LabeledLatLng> labeledLatLngs;
    public int distance;
    public String postalCode;
    public String cc;
    public String neighborhood;
    public String city;
    public String state;
    public String country;
    public List<String> formattedAddress;
    public String crossStreet;
}
