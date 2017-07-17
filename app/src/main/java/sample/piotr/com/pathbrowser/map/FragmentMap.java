package sample.piotr.com.pathbrowser.map;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import sample.piotr.com.pathbrowser.R;

public class FragmentMap extends Fragment {

    private MapView mapView;
    private GoogleMap gMap;

    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_map, parent, false);
        mapView = (MapView) v.findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);

        if (mapView != null) {

            mapView.getMapAsync(new OnMapReadyCallback() {

                @Override
                public void onMapReady(GoogleMap googleMap) {

                    initGoogleMap(googleMap);
                }
            });
        }
        return v;
    }

    @Override
    public void onResume() {

        mapView.onResume();
        super.onResume();
    }

    @Override
    public void onPause() {

        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onDestroy() {

        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {

        super.onLowMemory();
        mapView.onLowMemory();
    }

    private void initGoogleMap(GoogleMap googleMap) {

        this.gMap = googleMap;
        LatLng sydney = new LatLng(-34, 151);
        this.gMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        this.gMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }
}
