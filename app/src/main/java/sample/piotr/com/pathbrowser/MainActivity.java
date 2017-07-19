package sample.piotr.com.pathbrowser;

import android.Manifest;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.IBinder;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.google.android.gms.location.FusedLocationProviderClient;

import javax.inject.Inject;

import dagger.android.AndroidInjection;
import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.support.HasSupportFragmentInjector;
import sample.piotr.com.pathbrowser.list.FragmentList;
import sample.piotr.com.pathbrowser.map.FragmentMap;
import sample.piotr.com.pathbrowser.model.ModelPath;
import sample.piotr.com.pathbrowser.service.LocationService;

public class MainActivity extends AppCompatActivity implements HasSupportFragmentInjector, LocationService.OnPathListener, ActivityInterface {

    @Inject
    DispatchingAndroidInjector<Fragment> dispatchingAndroidInjector;
    @Inject
    FusedLocationProviderClient fusedLocationProviderClient;
    private LocationService mService;
    private boolean mBound = false;
    private PermissionsChecker permissionsChecker;

    private ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className, IBinder service) {

            LocationService.LocalBinder binder = (LocationService.LocalBinder) service;
            mService = binder.getService();
            mService.setOnPathListener(MainActivity.this);
            mBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {

            mBound = false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            if (!isTablet()) {
                getSupportFragmentManager().beginTransaction().add(R.id.fragments_container, new FragmentList()).commitAllowingStateLoss();
            }
            else {
                getSupportFragmentManager().beginTransaction().add(R.id.list_container, new FragmentList()).commitAllowingStateLoss();
                getSupportFragmentManager().beginTransaction().add(R.id.fragments_container, new FragmentMap()).commitAllowingStateLoss();
            }
        }

        permissionsChecker = new PermissionsChecker(this);
        permissionsChecker.checkPermissions();
        Intent intent = new Intent(this, LocationService.class);
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    public void onDestroy() {

        super.onDestroy();
        if (mBound) {
            mService.setOnPathListener(null);
            unbindService(mConnection);
            mBound = false;
        }
    }

    public void loadMap(ModelPath path) {

        if (!isTablet()) {
            FragmentMap fragmentMap = new FragmentMap();
            Bundle bundle = new Bundle();
            bundle.putString("path", ModelPath.toJson(path));
            fragmentMap.setArguments(bundle);
            replaceFragment(fragmentMap, true);
        }
        else {
            FragmentMap fragmentMap = (FragmentMap) getSupportFragmentManager().findFragmentById(R.id.fragments_container);
            fragmentMap.loadPathFromList(path);
        }
    }

    public void replaceFragment(Fragment fragment, boolean backstack) {

        if (getSupportFragmentManager().findFragmentById(R.id.fragments_container).getClass() == fragment.getClass()) {
            return;
        }

        if (backstack) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragments_container, fragment).addToBackStack(null).commitAllowingStateLoss();
        }
        else {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragments_container, fragment).commitAllowingStateLoss();
        }
    }

    public boolean isTablet() {

        return getResources().getBoolean(R.bool.isTablet);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.map:
                replaceFragment(new FragmentMap(), true);
                return true;
            case R.id.toggle:
                if (mService != null) {
                    mService.toggleTracking(this);
                }
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public AndroidInjector<Fragment> supportFragmentInjector() {

        return dispatchingAndroidInjector;
    }

    public ModelPath getCurrentPath() {

        if (mService != null) {
            return mService.getCurrentPath();
        }
        else {
            return null;
        }
    }

    @Override
    public void onCurrentPathUpdated(ModelPath path) {

        if (path == null) {
            return;
        }
        Fragment current = getSupportFragmentManager().findFragmentById(R.id.fragments_container);
        if (current instanceof LocationService.OnPathListener) {
            ((LocationService.OnPathListener) current).onCurrentPathUpdated(path);
        }

        if (isTablet()) {
            current = getSupportFragmentManager().findFragmentById(R.id.list_container);
            if (current instanceof LocationService.OnPathListener) {
                ((LocationService.OnPathListener) current).onCurrentPathUpdated(path);
            }
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {

        super.onActivityResult(requestCode, resultCode, intent);

        if (resultCode == Activity.RESULT_CANCELED) {
            finish();
        }
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResult) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResult);

        boolean allPermissionsGranted = true;

        for (int result : grantResult) {
            if (result != PackageManager.PERMISSION_GRANTED) {
                allPermissionsGranted = false;
            }
        }

        if (allPermissionsGranted) {
            permissionsChecker.checkPermissions();
        }
    }
}
