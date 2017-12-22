package com.example.fiza1.googlemap;

import android.os.AsyncTask;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Fiza1 on 15-Dec-17.
 */

public class GetNearbyPlacesData extends AsyncTask<Object,String,String> {

    String GooglePlacesData;
    GoogleMap map;
    String url;


    @Override
    protected String doInBackground(Object... params) {
        map = (GoogleMap) params[0];
        url = (String) params[1];

        DownloadURL downloadURL = new DownloadURL();
        try {
            GooglePlacesData = downloadURL.readURL(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return GooglePlacesData;
    }

    protected void onPostExecute(String s) {
        List<HashMap<String, String>> nearbyplacesList = null;
        DataParser parser = new DataParser();
        nearbyplacesList = parser.parse(s);
        ShowNearbyPlaces(nearbyplacesList);
    }

    private void ShowNearbyPlaces(List<HashMap<String, String>> nearbyPlacesList) {

        for (int i = 0; i < nearbyPlacesList.size(); i++) {
            MarkerOptions markerOptions = new MarkerOptions();
            HashMap<String, String> googleplace = nearbyPlacesList.get(i);
            String placename = googleplace.get("place_name");
            String vicinity = googleplace.get("vicinity");
            double lattitude = Double.parseDouble(googleplace.get("lat"));
            double longitude = Double.parseDouble(googleplace.get("lng"));

            LatLng latLng = new LatLng(lattitude, longitude);
            markerOptions.position(latLng);
            markerOptions.title(placename + "  : " + vicinity);
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));

            map.addMarker(markerOptions);
            map.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            map.animateCamera(CameraUpdateFactory.zoomTo(20));


        }
    }
}