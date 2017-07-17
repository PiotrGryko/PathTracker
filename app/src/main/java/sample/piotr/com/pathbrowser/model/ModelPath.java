package sample.piotr.com.pathbrowser.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

/**
 * Created by piotr on 17/07/17.
 */

@Entity(tableName = "path")
public class ModelPath {

    @PrimaryKey(autoGenerate = true)
    private int id;

    public ModelPath(int id) {

        this.id = id;
    }

    public int getId() {

        return id;
    }
}
