package sample.piotr.com.pathbrowser.di;

import javax.inject.Singleton;

import dagger.Component;
import dagger.android.support.AndroidSupportInjectionModule;
import sample.piotr.com.pathbrowser.service.LocationService;
import sample.piotr.com.pathbrowser.MyApplication;

@Singleton
@Component(modules = {AppModule.class, AndroidSupportInjectionModule.class, InjectorsContrubiutionsModule.class})
public interface AppComponent {

    void inject(MyApplication myapplication);

    void inject(LocationService service);
}
