package sample.piotr.com.pathbrowser.di;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;
import sample.piotr.com.pathbrowser.MainActivity;
import sample.piotr.com.pathbrowser.list.FragmentList;
import sample.piotr.com.pathbrowser.map.FragmentMap;

@Module
public abstract class InjectorsContrubiutionsModule {

    @ContributesAndroidInjector
    abstract MainActivity contributeMainActivity();

    @ContributesAndroidInjector
    abstract FragmentList contributeFragmentList();

    @ContributesAndroidInjector
    abstract FragmentMap contributeFragmentMap();
}
