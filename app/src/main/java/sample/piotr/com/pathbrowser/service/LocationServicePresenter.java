package sample.piotr.com.pathbrowser.service;

import android.location.Location;

import java.util.List;

import sample.piotr.com.pathbrowser.dao.PathRepository;
import sample.piotr.com.pathbrowser.model.ModelPath;
import sample.piotr.com.pathbrowser.model.ModelPoint;

/**
 * Created by piotr on 17/07/17.
 */

public class LocationServicePresenter implements LocationPresenterContract.Presenter {

    private PathRepository pathRepository;
    private LocationPresenterContract.View view;

    public LocationServicePresenter(PathRepository pathRepository, LocationPresenterContract.View view) {

        this.pathRepository = pathRepository;
        this.view = view;
    }

    @Override
    public void onLocationFetched(ModelPath path, Location point) {

        path.getPoints().add(new ModelPoint(point.getLatitude(), point.getLongitude()));
        pathRepository.savePath(path, new PathRepository.OnPathCallback<ModelPath>() {

            @Override
            public void onSuccess(ModelPath result) {

                view.onPathReady(result);
            }

            @Override
            public void onFailure() {

            }
        });
    }

    @Override
    public void fetchPaths() {

        pathRepository.getAllPaths(new PathRepository.OnPathCallback<List<ModelPath>>() {

            @Override
            public void onSuccess(List<ModelPath> result) {

                view.onPathsFetched(result);
            }

            @Override
            public void onFailure() {

            }
        });
    }
}
