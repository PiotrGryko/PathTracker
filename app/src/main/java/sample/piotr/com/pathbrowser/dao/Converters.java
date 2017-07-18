package sample.piotr.com.pathbrowser.dao;

import android.arch.persistence.room.TypeConverter;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import sample.piotr.com.pathbrowser.model.ModelPoint;

public class Converters {

    @TypeConverter
    public static String convertListToString(ArrayList<ModelPoint> points) {

        ModelPoint[] modelsArray = points.toArray(new ModelPoint[points.size()]);
        return new Gson().toJson(modelsArray, ModelPoint[].class);
    }

    @TypeConverter
    public static ArrayList<ModelPoint> convertStringToList(String pointsString) {

        ModelPoint[] pointsArray = new Gson().fromJson(pointsString, ModelPoint[].class);
        return new ArrayList<>(Arrays.asList(pointsArray));
    }

    @TypeConverter
    public static Date fromTimestamp(Long value) {

        return value == null ? null : new Date(value);
    }

    @TypeConverter
    public static Long dateToTimestamp(Date date) {

        return date == null ? null : date.getTime();
    }
}