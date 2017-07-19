package sample.piotr.com.pathbrowser.model;

import android.arch.persistence.room.ColumnInfo;

public class ModelPoint {

    public ModelPoint(double lat, double lon)
    {
        this.lat=lat;
        this.lon=lon;
    }

    @ColumnInfo(name = "lat")
    public double lat;
    @ColumnInfo(name = "lon")
    public double lon;

}
