package sample.piotr.com.pathbrowser.service;

import android.location.Location;

import java.util.List;

import sample.piotr.com.pathbrowser.model.ModelPath;

/**
 * Created by piotr on 17/07/17.
 */

public interface LocationPresenterContract {

    interface Presenter{
        public void onLocationFetched(ModelPath path,Location point);
    }
    interface View{
        public void onPathReady(ModelPath path);
    }
}
