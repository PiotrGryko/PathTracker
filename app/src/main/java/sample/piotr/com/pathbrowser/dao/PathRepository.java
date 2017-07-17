package sample.piotr.com.pathbrowser.dao;

import android.os.Handler;


import java.util.concurrent.Executor;

import sample.piotr.com.pathbrowser.api.api.ApiManager;

public class PathRepository {


    private MyRoomDatabase myRoomDatabase;
    private ApiManager apiManager;
    private Handler handler;
    private Executor executor;


    public PathRepository(Handler handler, MyRoomDatabase myRoomDatabase, ApiManager apiManager, Executor executor) {

        this.handler = handler;
        this.myRoomDatabase = myRoomDatabase;
        this.apiManager = apiManager;
        this.executor = executor;
    }


}
