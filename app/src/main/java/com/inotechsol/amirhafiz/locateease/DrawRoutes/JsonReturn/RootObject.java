package com.inotechsol.amirhafiz.locateease.DrawRoutes.JsonReturn;

import java.util.List;

/**
 * Created by Amir on 5/8/2017.
 */

public class RootObject {
    public List<GeocodedWaypoint> geocoded_waypoints;
    public List<Route> routes;
    public String status;
}
