package com.example.fiza1.googlemap;

import android.*;
import android.Manifest;
import android.app.Dialog;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.ZoomControls;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.LocationSource;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.io.IOException;
import java.net.URL;
import java.util.List;

import static com.example.fiza1.googlemap.R.styleable.View;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private GoogleMap mMap;
    private final static int MyPermission = 99;
    private GoogleApiClient googleApiClient;
    private LocationRequest locationRequest;
    private Location LastLocation;
    private Marker CurrentLocationMarker;
    int PROXIMITY_RADIUS=10000;
    double latitude,longitude;
    protected static final String TAG = "MapsActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
            checkLocationPermission();
        }

        final SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }



    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ) {
                buildGoogleApiClient();
                mMap.setMyLocationEnabled(true);



            }
        }


        protected synchronized void buildGoogleApiClient()
        {
            googleApiClient = new GoogleApiClient.Builder(this)
                    .addApi(LocationServices.API)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .build();
            googleApiClient.connect();
        }


    public void GoButton(View view) {
        Object dataTranfer[]=new Object[2];
        GetNearbyPlacesData getNearbyPlacesData=new GetNearbyPlacesData();

        switch (view.getId()) {
            case R.id.btnGo: {
                EditText start = (EditText) findViewById(R.id.start);
                String location = start.getText().toString();
                List<Address> addressList = null;
                MarkerOptions markerOptions = new MarkerOptions();

                if (!location.equals("")) {
                    Geocoder geocoder = new Geocoder(this);
                    try {
                        addressList = geocoder.getFromLocationName(location, 1);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    for (int i = 0; i < addressList.size(); i++) {

                        Address myAddress = addressList.get(i);
                        LatLng latLng = new LatLng(myAddress.getLatitude(), myAddress.getLongitude());
                        markerOptions.position(latLng);
                        markerOptions.title("Point");

                        mMap.addMarker(markerOptions);

                        mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
                    }
                }
            }
            break;
            case R.id.hospitals:
                mMap.clear();
                String hospital="hospital";
                String url=geturl(latitude,longitude,hospital);
                dataTranfer[0]=mMap;
                dataTranfer[1]=url;

                getNearbyPlacesData.execute(dataTranfer);
                Toast.makeText(MapsActivity.this,"Showing Nearby Hospitals",Toast.LENGTH_LONG).show();
                break;

            case R.id.schools:
                mMap.clear();
                String schools="schools";
                 url=geturl(latitude,longitude,schools);
                dataTranfer[0]=mMap;
                dataTranfer[1]=url;
                getNearbyPlacesData.execute(dataTranfer);
                Toast.makeText(MapsActivity.this,"Showing Nearby Schools",Toast.LENGTH_LONG).show();
                break;
            case R.id.resturants:
                mMap.clear();
                String restaurant="resturant";
                 url=geturl(latitude,longitude,restaurant);

                dataTranfer[0]=mMap;
                dataTranfer[1]=url;
                getNearbyPlacesData.execute(dataTranfer);
                Toast.makeText(MapsActivity.this,"Showing Nearby Restaurants",Toast.LENGTH_LONG).show();
                break;

            case R.id.Malls:
                mMap.clear();
                String malls="malls";
                url=geturl(latitude,longitude,malls);

                dataTranfer[0]=mMap;
                dataTranfer[1]=url;
                getNearbyPlacesData.execute(dataTranfer);
                Toast.makeText(MapsActivity.this,"Showing Nearby Malls",Toast.LENGTH_LONG).show();
                break;

        }
    }

    private String geturl(double longitude,double latitude,String nearbyplaces){
        StringBuilder googleplaceUrl= new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
        googleplaceUrl.append("Location="+latitude+","+longitude);
        googleplaceUrl.append("&radius="+PROXIMITY_RADIUS);
        googleplaceUrl.append("&type"+nearbyplaces);
        googleplaceUrl.append("&sensor=true");
        googleplaceUrl.append("&key="+"AIzaSyCGB3EepdYtP06yCckm-UQFp-Sq1euY9IM");
        return  googleplaceUrl.toString();

    }


  @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
      switch (requestCode) {
            case MyPermission:
                if (grantResults.length >0 && grantResults[0]==PackageManager.PERMISSION_GRANTED) {
                    //permission granted
                    if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
                    {
                        if(googleApiClient==null){
                           buildGoogleApiClient();
                      }
                      mMap.setMyLocationEnabled(true);
                    }
                }
                else { // permission denied
                    Toast.makeText(getApplicationContext(), "permission denied", Toast.LENGTH_LONG).show();
                    return;
                }
        }
    }

   @Override
    public void onConnected(@Nullable Bundle bundle) {
       locationRequest = new LocationRequest();
       locationRequest = new LocationRequest();
       locationRequest.setInterval(1000); //in milliseconds
       locationRequest.setFastestInterval(1000);
       locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

       if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
           LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);
       }
   }
    public boolean checkLocationPermission(){
        if(ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            if(ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.ACCESS_FINE_LOCATION)){
                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},MyPermission);
            }
            else{
                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},MyPermission);
            }
            return false; //don't ask again permission checked by the user
        }
        else return true;
    }


    private void requestLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ) {
            LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);
                    //to get current location-fused location
        }

    }

    @Override
    public void onConnectionSuspended(int i) {

        Log.i(TAG,"Connection Suspended");
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
      Log.i(TAG,"Connection Failed:ConnectionResult.getErrorCode = " + connectionResult.getErrorCode());
    }


    @Override
    public void onLocationChanged(Location location) {
        LastLocation = location;
        if (CurrentLocationMarker != null) {
            CurrentLocationMarker.remove();

        }
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title("Current Location");
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));

        CurrentLocationMarker = mMap.addMarker(markerOptions);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomBy(20));

        if (googleApiClient != null) {
LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient,this);
        }
    }


}
