package sample.piotr.com.pathbrowser.dao;

import android.os.Handler;

import java.util.List;
import java.util.concurrent.Executor;

import sample.piotr.com.pathbrowser.model.ModelPath;

public class PathRepository {

    public interface OnPathCallback<T> {

        public void onResult(T result);
    }

    private MyRoomDatabase myRoomDatabase;
    private Handler handler;
    private Executor executor;

    public PathRepository(Handler handler, MyRoomDatabase myRoomDatabase, Executor executor) {

        this.handler = handler;
        this.myRoomDatabase = myRoomDatabase;
        this.executor = executor;
    }

    private <T> void publishResult(final T result, final OnPathCallback<T> callback) {

        handler.post(new Runnable() {

            @Override
            public void run() {
                callback.onResult(result);
            }
        });
    }

    public void savePath(final ModelPath path, final OnPathCallback<ModelPath> callback) {

        executor.execute(new Runnable() {

            @Override
            public void run() {

                if (path.getId() == 0) {
                    long id = myRoomDatabase.pathDao().insertPath(path);
                    path.setId(id);
                }
                else {
                    myRoomDatabase.pathDao().updatePath(path);
                }
                publishResult(path, callback);
            }
        });
    }

    public void getAllPaths(final OnPathCallback<List<ModelPath>> callback) {

        executor.execute(new Runnable() {

            @Override
            public void run() {

                List<ModelPath> paths = myRoomDatabase.pathDao().loadAllPaths();
                publishResult(paths, callback);
            }
        });
    }
}
