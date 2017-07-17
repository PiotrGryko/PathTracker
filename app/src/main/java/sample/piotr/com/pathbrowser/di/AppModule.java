package sample.piotr.com.pathbrowser.di;

import android.app.Application;
import android.arch.persistence.room.Room;
import android.os.Handler;


import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import sample.piotr.com.pathbrowser.api.api.ApiManager;
import sample.piotr.com.pathbrowser.dao.MyRoomDatabase;
import sample.piotr.com.pathbrowser.dao.PathRepository;

@Module
public class AppModule {

    Application mApplication;

    public AppModule(Application mApplication) {

        this.mApplication = mApplication;
    }

    @Provides
    @Singleton
    public ApiManager getApiManager() {

        return ApiManager.getInstance(mApplication);
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
    public PathRepository getPathRepository(Handler handler, MyRoomDatabase myRoomDatabase, ApiManager apiManager, Executor executor) {

        return new PathRepository(handler, myRoomDatabase, apiManager, executor);
    }
}
