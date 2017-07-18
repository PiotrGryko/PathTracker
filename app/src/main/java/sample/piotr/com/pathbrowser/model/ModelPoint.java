package sample.piotr.com.pathbrowser.model;

import android.arch.persistence.room.ColumnInfo;

public class ModelPoint {

    @ColumnInfo(name = "lat")
    public double lat;
    @ColumnInfo(name = "lon")
    public double lon;

}
