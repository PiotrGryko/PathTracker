package sample.piotr.com.pathbrowser.list;

import java.util.List;

import sample.piotr.com.pathbrowser.dao.PathRepository;
import sample.piotr.com.pathbrowser.model.ModelPath;

/**
 * Created by piotr on 19/07/17.
 */

public class ListPresenter implements ListPresenterContract.Presenter {

    private ListPresenterContract.View view;
    private PathRepository pathRepository;

    public ListPresenter(ListPresenterContract.View view, PathRepository pathRepository) {

        this.view = view;
        this.pathRepository = pathRepository;
    }

    @Override
    public void loadPaths() {

        pathRepository.getAllPaths(new PathRepository.OnPathCallback<List<ModelPath>>() {

            @Override
            public void onResult(List<ModelPath> result) {

                view.onPathsLoaded(result);
            }
        });
    }
}
