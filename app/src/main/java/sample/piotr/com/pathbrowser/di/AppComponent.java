package sample.piotr.com.pathbrowser.di;


import javax.inject.Singleton;

import dagger.Component;
import dagger.android.support.AndroidSupportInjectionModule;
import sample.piotr.com.pathbrowser.service.LocationService;
import sample.piotr.com.pathbrowser.MyApplication;
import sample.piotr.com.pathbrowser.api.ApiManager;

@Singleton
@Component(
    modules = {
        AppModule.class,
        AndroidSupportInjectionModule.class,
        ActivitiesInjectorsModule.class
    }
)
public interface AppComponent {

    ApiManager apiManager();

    void inject(MyApplication myapplication);
    void inject(LocationService service);

}
