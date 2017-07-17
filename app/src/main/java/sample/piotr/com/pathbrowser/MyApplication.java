package sample.piotr.com.pathbrowser;

import android.app.Activity;
import android.app.Application;

import javax.inject.Inject;

import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasActivityInjector;
import sample.piotr.com.pathbrowser.di.AppComponent;
import sample.piotr.com.pathbrowser.di.AppModule;
import sample.piotr.com.pathbrowser.di.DaggerAppComponent;

public class MyApplication extends Application implements HasActivityInjector {

    @Inject DispatchingAndroidInjector<Activity> dispatchingAndroidInjector;
    AppComponent appComponent;

    public void onCreate() {

        super.onCreate();
        appComponent = DaggerAppComponent.builder().appModule(new AppModule(this)).build();
        appComponent.inject(this);
    }

    public AppComponent getAppComponent() {

        return appComponent;
    }

    @Override
    public AndroidInjector<Activity> activityInjector() {

        return dispatchingAndroidInjector;
    }
}
