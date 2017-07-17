package sample.piotr.com.pathbrowser.di;


import dagger.Module;
import dagger.android.ContributesAndroidInjector;
import sample.piotr.com.pathbrowser.MainActivity;

@Module
public abstract class ActivitiesInjectorsModule {

    @ContributesAndroidInjector
    abstract MainActivity contributeMainActivity();
}
