package sample.piotr.com.pathbrowser.dao;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;

import sample.piotr.com.pathbrowser.model.ModelPath;

@Database(entities = {ModelPath.class}, version = 1)
@TypeConverters({Converters.class})
public abstract class MyRoomDatabase extends RoomDatabase {

    public static final String DATABASE_NAME = "path-tracker-db";

    public abstract PathDao pathDao();
}
