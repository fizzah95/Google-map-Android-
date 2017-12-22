package com.example.fiza1.googlemap;

import android.os.AsyncTask;

import com.google.android.gms.maps.GoogleMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Fiza1 on 15-Dec-17.
 */

public class DataParser {

    private HashMap<String, String> getPlace(JSONObject googlePlaceJson) { //to store one place we are using hash map
        HashMap<String, String> googlePlacesMap = new HashMap<>();
        String placeName = "-NA-";
        String vicnity = "-NA-";
        String lattitude = "";
        String longitude = "";
        String reference = "";


        try {
            if (!googlePlaceJson.isNull("name")) {
                placeName = googlePlaceJson.getString("name");
            }
            if (!googlePlaceJson.isNull("vicinity")) {
                placeName = googlePlaceJson.getString("vicinity");
            }
            lattitude = googlePlaceJson.getJSONObject("geometry").getJSONObject("location").getString("lattitude");
            longitude = googlePlaceJson.getJSONObject("geometry").getJSONObject("location").getString("longitude");
            reference=googlePlaceJson.getString("reference");

            googlePlacesMap.put("place_name",placeName);
            googlePlacesMap.put("vicinity",vicnity);
            googlePlacesMap.put("latitude",lattitude);
            googlePlacesMap.put("longitude",longitude);
            googlePlacesMap.put("reference",reference);

        } catch (JSONException e) {
            e.printStackTrace();
        }

return googlePlacesMap;
    }
    private List<HashMap<String,String>>getPlaces(JSONArray jsonArray){
        int count=jsonArray.length();
        List<HashMap<String,String>>PlacesList=new ArrayList<>();
        HashMap<String,String>placeMap=null;

        for(int i=0;i<count;i++){
            try {
                placeMap=getPlace((JSONObject)jsonArray.get(i));
                PlacesList.add(placeMap);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return PlacesList;
    }

    public List<HashMap<String,String>>parse(String jsonData){
        JSONArray jsonArray=null;
        JSONObject jsonObject;

        try {
            jsonObject=new JSONObject(jsonData);
            jsonArray=jsonObject.getJSONArray("results");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return getPlaces(jsonArray);
    }
}
