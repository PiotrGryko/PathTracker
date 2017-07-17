package sample.piotr.com.pathbrowser.dao;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import sample.piotr.com.pathbrowser.model.ModelPath;

@Database(entities = {ModelPath.class}, version = 1)
public abstract class MyRoomDatabase extends RoomDatabase {

    public static final String DATABASE_NAME = "basic-sample-db";

    public abstract PathDao userDao();
}
