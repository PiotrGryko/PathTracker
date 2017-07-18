package sample.piotr.com.pathbrowser.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import sample.piotr.com.pathbrowser.model.ModelPath;

@Dao
public interface PathDao {

    @Insert
    public long insertPath(ModelPath path);

    @Update
    public void updatePath(ModelPath path);

    @Query("SELECT * FROM path")
    public List<ModelPath> loadAllPaths();
}
