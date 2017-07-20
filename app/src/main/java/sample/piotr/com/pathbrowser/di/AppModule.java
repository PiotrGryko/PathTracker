package sample.piotr.com.pathbrowser.di;

import android.app.Application;
import android.arch.persistence.room.Room;
import android.os.Handler;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import sample.piotr.com.pathbrowser.dao.MyRoomDatabase;
import sample.piotr.com.pathbrowser.dao.PathRepository;

@Module
public class AppModule {

    private Application mApplication;

    public AppModule(Application mApplication) {

        this.mApplication = mApplication;
    }

    @Provides
    public Handler provideHandler() {

        return new Handler(mApplication.getMainLooper());
    }

    @Provides
    @Singleton
    public MyRoomDatabase provideMyRoomDatabase() {

        return Room.databaseBuilder(mApplication, MyRoomDatabase.class, MyRoomDatabase.DATABASE_NAME).build();
    }

    @Provides
    @Singleton
    public Executor provideExecutor() {

        int NUMBER_OF_CORES = Runtime.getRuntime().availableProcessors();
        final int KEEP_ALIVE_TIME = 1;
        TimeUnit KEEP_ALIVE_TIME_UNIT = TimeUnit.SECONDS;
        BlockingQueue<Runnable> mDecodeWorkQueue = new LinkedBlockingQueue<>();
        return new ThreadPoolExecutor(NUMBER_OF_CORES, NUMBER_OF_CORES, KEEP_ALIVE_TIME, KEEP_ALIVE_TIME_UNIT, mDecodeWorkQueue);
    }

    @Provides
    @Singleton
    public PathRepository getPathRepository(Handler handler, MyRoomDatabase myRoomDatabase, Executor executor) {

        return new PathRepository(handler, myRoomDatabase, executor);
    }

    @Provides
    @Singleton
    public FusedLocationProviderClient provideFusedLocationProviderClient() {

        return LocationServices.getFusedLocationProviderClient(mApplication);
    }

    @Singleton
    @Provides
    public LocationRequest provideLocationRequest() {

        LocationRequest request = new LocationRequest();
        request.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        request.setFastestInterval(30 * 1000);
        request.setInterval(60 * 5 * 1000);
        return request;
    }
}
