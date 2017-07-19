package sample.piotr.com.pathbrowser.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Binder;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;

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

    public interface OnPathListener {

        public void onCurrentPathUpdated(ModelPath path);
    }

    private final static int NOTIFICTATION_ID = 001;
    private LocationCallback mLocationCallback;
    private final IBinder mBinder = new LocalBinder();
    private boolean isTracking;
    private ModelPath currentPath;
    private LocationServicePresenter presenter;
    private OnPathListener onPathListener;

    @Inject
    PathRepository pathRepository;
    @Inject
    FusedLocationProviderClient fusedLocationProviderClient;
    @Inject
    LocationRequest request;

    private final static Calendar calendar;
    static {
        calendar = Calendar.getInstance();
    }
    public void setOnPathListener(OnPathListener onPathListener) {

        this.onPathListener = onPathListener;
    }

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

    }

    public boolean onUnbind(Intent intent) {

        return true;
    }

    public int onStartCommand(Intent intent, int flags, int startId) {

        return Service.START_STICKY;
    }

    @Override
    public void onCreate() {

        super.onCreate();
        ((MyApplication) getApplicationContext()).getAppComponent().inject(this);

        presenter = new LocationServicePresenter(pathRepository, this);
        mLocationCallback = new LocationCallback() {

            @Override
            public void onLocationResult(LocationResult locationResult) {

                for (Location location : locationResult.getLocations()) {
                    if (currentPath != null) {
                        presenter.onLocationFetched(currentPath, location);
                    }
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
    }

    @Override
    public void onPathReady(ModelPath path) {

        if (isTracking) {
            currentPath = path;
            if (onPathListener != null) {
                onPathListener.onCurrentPathUpdated(currentPath);
            }
        }
    }

    @Override
    public void onPathsFetched(List<ModelPath> paths) {

    }

    public void toggleTracking(Context context) {

        Intent intent = new Intent(context, LocationService.class);
        if (!isTracking) {
            startService(intent);
            startJourney();
        }
        else {
            stopJourney();
            stopService(intent);
        }
    }

    public ModelPath getCurrentPath()
    {
        return currentPath;
    }

    public void startJourney() {

        isTracking = true;
        currentPath = new ModelPath(calendar.getTime(), calendar.getTime(), new ArrayList<ModelPoint>());
        fusedLocationProviderClient.requestLocationUpdates(request, mLocationCallback, null);
        showNotification();
    }

    public void stopJourney() {

        fusedLocationProviderClient.removeLocationUpdates(mLocationCallback);
        stopForeground(true);
        isTracking = false;
        currentPath = null;
    }
}
