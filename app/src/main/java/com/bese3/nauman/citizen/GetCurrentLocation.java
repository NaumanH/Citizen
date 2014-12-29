package com.bese3.nauman.citizen;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class GetCurrentLocation extends FragmentActivity {

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_map);
        setUpMapIfNeeded();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    /**
     * Sets up the map if it is possible to do so (i.e., the Google Play services APK is correctly
     * installed) and the map has not already been instantiated.. This will ensure that we only ever
     * call {@link #setUpMap()} once when {@link #mMap} is not null.
     * <p/>
     * If it isn't installed {@link SupportMapFragment} (and
     * {@link com.google.android.gms.maps.MapView MapView}) will show a prompt for the user to
     * install/update the Google Play services APK on their device.
     * <p/>
     * A user can return to this FragmentActivity after following the prompt and correctly
     * installing/updating/enabling the Google Play services. Since the FragmentActivity may not
     * have been completely destroyed during this process (it is likely that it would only be
     * stopped or paused), {@link #onCreate(Bundle)} may not be called again so we should call this
     * method in {@link #onResume()} to guarantee that it will be called.
     */
    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }

    /**
     * This is where we can add markers or lines, add listeners or move the camera. In this case, we
     * just add a marker near Africa.
     * <p/>
     * This should only be called once and when we are sure that {@link #mMap} is not null.
     */
    private void setUpMap() {
        
    	   LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
    	   
    	    Criteria criteria = new Criteria();
    	    criteria.setAccuracy(Criteria.ACCURACY_FINE);
    	    
    	    String provider = locationManager.getBestProvider(criteria, true);
    	    
    	    LocationListener locationListener = new LocationListener() {
    	    
    	        @Override
    	        public void onLocationChanged(Location location) {
    	            showCurrentLocation(location);
    	        }
    	        @Override
    	        public void onStatusChanged(String s, int i, Bundle bundle) {
    	        }
    	        @Override
    	        public void onProviderEnabled(String s) {
    	        }
    	        @Override
    	        public void onProviderDisabled(String s) {
    	        }
    	    };
    	        
    	    locationManager.requestLocationUpdates(provider, 2000, 0, locationListener);
    	    
    	    // Getting initial Location
    	    Location location = locationManager.getLastKnownLocation(provider);
    	    // Show the initial location
    	    if(location != null)
    	    {
    	        showCurrentLocation(location);
    	    }
    	}
    
    private void showCurrentLocation(Location location){
        
        mMap.clear();
        
        LatLng currentPosition = new LatLng(location.getLatitude(),location.getLongitude());
        
        mMap.addMarker(new MarkerOptions()
                .position(currentPosition)
                .snippet("Lat: " + location.getLatitude() + ", Lng: " + location.getLongitude())
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_peterleow))
                .flat(true)
                .title("I'm here!"));
        
        // Zoom in, animating the camera.
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentPosition, 18));
    }
}
