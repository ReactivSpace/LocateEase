package com.inotechsol.amirhafiz.locateease.DrawRoutes.JsonReturn;

import java.util.List;

/**
 * Created by Amir on 5/8/2017.
 */

public class Leg {
    public Distance distance;
    public Duration duration;
    public String end_address;
    public EndLocation end_location;
    public String start_address;
    public StartLocation start_location;
    public List<Step> steps;
    public List<Object> traffic_speed_entry;
    public List<Object> via_waypoint;
}
