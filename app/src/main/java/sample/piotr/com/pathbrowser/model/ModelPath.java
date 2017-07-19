package sample.piotr.com.pathbrowser.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by piotr on 17/07/17.
 */

@Entity(tableName = "path")
public class ModelPath {

    @PrimaryKey(autoGenerate = true)
    private long id;
    @ColumnInfo(name = "startDate")
    private Date startDate;
    @ColumnInfo(name = "endDate")
    private Date endDate;
    @ColumnInfo(name = "points")
    private ArrayList<ModelPoint> points;

    public ModelPath(Date startDate, Date endDate, ArrayList<ModelPoint> points) {

        this.startDate = startDate;
        this.endDate = endDate;

        this.points = points;
    }

    public void setId(long id) {

        this.id = id;
    }

    public long getId() {

        return id;
    }

    public void setStartDate(Date date) {

        this.startDate = date;
    }

    public Date getStartDate() {

        return startDate;
    }

    public void setEndDate(Date date) {

        this.endDate = date;
    }

    public Date getEndDate() {

        return endDate;
    }

    public void setPoints(ArrayList<ModelPoint> points) {

        this.points = points;
    }

    public ArrayList<ModelPoint> getPoints() {

        return points;
    }

    public boolean equals(Object object) {

        if (object == null) {
            return false;
        }
        else if (!(object instanceof ModelPath)) {
            return false;
        }
        else {
            return ((ModelPath) object).id == this.id;
        }
    }

    @Override
    public int hashCode() {

        int result = 17;
        result = 31 * result + (int) id;
        result = 31 * result + (int) startDate.getTime();
        result = 31 * result + (int) endDate.getTime();
        return result;
    }

    public static String toJson(ModelPath path) {

        return new Gson().toJson(path, ModelPath.class);
    }

    public static ModelPath fromJson(String json) {

        return new Gson().fromJson(json, ModelPath.class);
    }
}
