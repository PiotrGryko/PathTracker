package sample.piotr.com.pathbrowser.map;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;

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

    private void initGoogleMap(GoogleMap googleMap) {

        this.gMap = googleMap;

    }
}
