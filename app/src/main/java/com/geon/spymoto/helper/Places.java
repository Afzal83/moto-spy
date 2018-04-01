package com.geon.spymoto.helper;

/**
 * Created by Babu on 1/7/2017.
 */

import android.util.Log;


import com.geon.spymoto.model.NearestLocation;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Places {

    public static final String STATUS = "status";
    public static final String OK = "OK";
    public static final String ZERO_RESULTS = "ZERO_RESULTS";
    public static final String REQUEST_DENIED = "REQUEST_DENIED";
    public static final String OVER_QUERY_LIMIT = "OVER_QUERY_LIMIT";
    public static final String UNKNOWN_ERROR = "UNKNOWN_ERROR";
    public static final String INVALID_REQUEST = "INVALID_REQUEST";

    //    Key for nearby places json from google
    public static final String GEOMETRY = "geometry";
    public static final String LOCATION = "location";
    public static final String LATITUDE = "lat";
    public static final String LONGITUDE = "lng";
    public static final String ICON = "icon";
    public static final String SUPERMARKET_ID = "id";
    public static final String NAME = "name";
    public static final String PLACE_ID = "place_id";
    public static final String REFERENCE = "reference";
    public static final String VICINITY = "vicinity";
    public static final String PLACE_NAME = "place_name";

    public List<NearestLocation> parseLocationResult(JSONObject result) {

        List<NearestLocation> nearestLocatoinList = new ArrayList<NearestLocation>();
        String id, place_id, placeName = null, reference, icon, vicinity = null;
        double latitude, longitude;
        JSONArray jsonArray = null;
        try {
            try {
                jsonArray = result.getJSONArray("results");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (result.getString(STATUS).equalsIgnoreCase(OK)) {

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject place = jsonArray.getJSONObject(i);
                    id = place.getString(SUPERMARKET_ID);
                    place_id = place.getString(PLACE_ID);
                    if (!place.isNull(NAME)) {
                        placeName = place.getString(NAME);
                    }
                    if (!place.isNull(VICINITY)) {
                        vicinity = place.getString(VICINITY);
                    }
                    latitude = place.getJSONObject(GEOMETRY).getJSONObject(LOCATION)
                            .getDouble(LATITUDE);
                    longitude = place.getJSONObject(GEOMETRY).getJSONObject(LOCATION)
                            .getDouble(LONGITUDE);
                    reference = place.getString(REFERENCE);
                    icon = place.getString(ICON);

                    NearestLocation mLocation = new NearestLocation(placeName,latitude,longitude,0.0);
                    nearestLocatoinList.add(mLocation);
                }

            } else if (result.getString(STATUS).equalsIgnoreCase(ZERO_RESULTS)) {

            }

        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("json error", "parseLocationResult: Error=" + e.getMessage());
        }

        return nearestLocatoinList;
    }
}