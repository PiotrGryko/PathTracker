package sample.piotr.com.pathbrowser;

import android.Manifest;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.IBinder;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.transition.Fade;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import javax.inject.Inject;

import dagger.android.AndroidInjection;
import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasFragmentInjector;
import dagger.android.support.HasSupportFragmentInjector;
import sample.piotr.com.pathbrowser.list.FragmentList;
import sample.piotr.com.pathbrowser.map.FragmentMap;
import sample.piotr.com.pathbrowser.model.ModelPath;
import sample.piotr.com.pathbrowser.service.LocationService;
import sample.piotr.com.pathbrowser.util.DetailsTransition;

public class MainActivity extends AppCompatActivity implements HasSupportFragmentInjector, LocationService.OnPathListener {

    @Inject
    DispatchingAndroidInjector<Fragment> dispatchingAndroidInjector;
    private LocationService mService;
    private boolean mBound = false;

    private ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className, IBinder service) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
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

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }

        Intent intent = new Intent(this, LocationService.class);
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    public void onDestroy() {

        super.onDestroy();
        // Unbind from the service
        if (mBound) {
            mService.setOnPathListener(null);
            unbindService(mConnection);
            mBound = false;
        }
    }

    public void replaceFragmentWithTransition(Fragment fragment, View view, String transitionName) {

        Fragment current = getSupportFragmentManager().findFragmentById(R.id.fragments_container);

        if (current.getClass() == fragment.getClass()) {
            return;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            fragment.setSharedElementEnterTransition(new DetailsTransition());
            fragment.setEnterTransition(new Fade());
            current.setExitTransition(new Fade());
            fragment.setAllowEnterTransitionOverlap(false);
            fragment.setAllowReturnTransitionOverlap(true);
            fragment.setSharedElementReturnTransition(new DetailsTransition());
        }
        getSupportFragmentManager().beginTransaction().addSharedElement(view, transitionName).replace(R.id.fragments_container, fragment)
                .addToBackStack(null).commitAllowingStateLoss();
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
        // Handle item selection
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
    public void onPathUpdated(ModelPath path) {

        if (path == null) {
            return;
        }
        Fragment current = getSupportFragmentManager().findFragmentById(R.id.fragments_container);
        if (current instanceof LocationService.OnPathListener) {
            ((LocationService.OnPathListener) current).onPathUpdated(path);
        }
        Log.d("XXX", "main activity on path updated");
    }
}
