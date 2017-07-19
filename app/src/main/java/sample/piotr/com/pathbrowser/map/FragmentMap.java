package sample.piotr.com.pathbrowser.map;

import android.app.Activity;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnSuccessListener;

import javax.inject.Inject;

import dagger.android.support.AndroidSupportInjection;
import sample.piotr.com.pathbrowser.ActivityInterface;
import sample.piotr.com.pathbrowser.R;
import sample.piotr.com.pathbrowser.model.ModelPath;
import sample.piotr.com.pathbrowser.model.ModelPoint;
import sample.piotr.com.pathbrowser.service.LocationService;

public class FragmentMap extends Fragment implements LocationService.OnPathListener {

    private MapView mapView;
    private GoogleMap gMap;
    private ModelPath loadedPath;
    private ModelPath currentPath;

    @Inject
    FusedLocationProviderClient fusedLocationProviderClient;

    @Override
    public void onAttach(Activity activity) {

        AndroidSupportInjection.inject(this);
        super.onAttach(activity);
    }

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

        fusedLocationProviderClient.getLastLocation().addOnSuccessListener(this.getActivity(), new OnSuccessListener<Location>() {

            @Override
            public void onSuccess(Location location) {

                if (location != null) {
                    FragmentMap.this.gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()),
                            10));
                }
            }
        });

        if (this.getArguments() != null) {

            loadPathFromList(ModelPath.fromJson(getArguments().getString("path")));
        }

        if (getActivity() instanceof ActivityInterface) {
            drawPath(((ActivityInterface) getActivity()).getCurrentPath(), Color.BLUE);
        }
    }

    private void drawPath(ModelPath path, int color) {

        if (path == null) {
            return;
        }
        PolylineOptions options = new PolylineOptions();

        options.color(color);
        options.width(5);
        options.visible(true);

        for (ModelPoint point : path.getPoints()) {
            options.add(new LatLng(point.lat, point.lon));
        }

        this.gMap.addPolyline(options);
    }

    public void loadPathFromList(ModelPath path) {

        this.gMap.clear();
        loadedPath = path;
        if (loadedPath != null) {
            drawPath(path, Color.RED);
        }
        if (currentPath != null) {
            drawPath(currentPath, Color.BLUE);
        }
    }

    @Override
    public void onCurrentPathUpdated(ModelPath path) {

        this.gMap.clear();
        if (loadedPath != null) {
            drawPath(loadedPath, Color.RED);
        }
        currentPath = path;
        drawPath(currentPath, Color.BLUE);
    }
}
