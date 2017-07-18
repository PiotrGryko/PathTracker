package sample.piotr.com.pathbrowser.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Binder;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.inject.Inject;

import sample.piotr.com.pathbrowser.MyApplication;
import sample.piotr.com.pathbrowser.R;
import sample.piotr.com.pathbrowser.dao.PathRepository;
import sample.piotr.com.pathbrowser.model.ModelPath;
import sample.piotr.com.pathbrowser.model.ModelPoint;

public class LocationService extends Service implements LocationPresenterContract.View {

    private final static int NOTIFICTATION_ID = 001;
    private LocationCallback mLocationCallback;
    private final IBinder mBinder = new LocalBinder();
    private boolean isTracking;
    private ModelPath currentPath;
    private LocationServicePresenter presenter;

    @Inject
    PathRepository pathRepository;
    @Inject
    FusedLocationProviderClient fusedLocationProviderClient;
    @Inject
    LocationRequest request;

    public class LocalBinder extends Binder {

        public LocationService getService() {
            // Return this instance of LocalService so clients can call public methods
            return LocationService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return mBinder;
    }

    public void onRebind(Intent intent) {

        Log.d("XXX", "on rebind");
    }

    public boolean onUnbind(Intent intent) {

        Log.d("XXX", "onunbind");
        return true;
    }

    public int onStartCommand(Intent intent, int flags, int startId) {

        Log.d("XXX", "onstart command " + intent);
        return Service.START_STICKY;
    }

    @Override
    public void onCreate() {

        super.onCreate();
        Log.d("XXX", "on create " + isTracking + " " + mLocationCallback);
        ((MyApplication) getApplicationContext()).getAppComponent().inject(this);

        presenter = new LocationServicePresenter(pathRepository, this);
        mLocationCallback = new LocationCallback() {

            @Override
            public void onLocationResult(LocationResult locationResult) {

                Log.d("XXX", "location fetched");
                for (Location location : locationResult.getLocations()) {
                    if (currentPath != null) { presenter.onLocationFetched(currentPath, location); }
                }
            }
        };
    }

    public void showNotification() {

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this).setSmallIcon(R.mipmap.ic_launcher).setContentTitle("Tracking " +
                "location...").setContentText("Hello World!");

        startForeground(NOTIFICTATION_ID, mBuilder.build());
    }

    public void onDestroy() {

        super.onDestroy();
        Log.d("XXX", "on destroy");
    }

    @Override
    public void onPathReady(ModelPath path) {

        Log.d("XXX", "on path ready");
        if (isTracking) { currentPath = path; }
    }

    @Override
    public void onPathsFetched(List<ModelPath> paths) {

    }

    public void toggleTracking(Context context) {

        Intent intent = new Intent(context, LocationService.class);
        if (!isTracking) {
            startService(intent);
            startJourney();
        } else {
            stopJourney();
            stopService(intent);
        }
    }

    public void startJourney() {

        isTracking = true;
        currentPath = new ModelPath(Calendar.getInstance().getTime(),Calendar.getInstance().getTime(),new ArrayList<ModelPoint>());
        fusedLocationProviderClient.requestLocationUpdates(request, mLocationCallback, null);
        showNotification();
        Log.d("XXX", "start journey");
    }

    public void stopJourney() {

        fusedLocationProviderClient.removeLocationUpdates(mLocationCallback);
        //notificationManager.cancel(NOTIFICTATION_ID);
        stopForeground(true);
        isTracking = false;
        currentPath = null;
        Log.d("XXX", "stop journey");
    }
}
