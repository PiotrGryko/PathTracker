package sample.piotr.com.pathbrowser.list;

import java.util.List;

import sample.piotr.com.pathbrowser.model.ModelPath;

public interface ListPresenterContract {

    public interface Presenter {

        public void loadPaths();
    }

    public interface View {

        public void onPathsLoaded(List<ModelPath> paths);
    }
}
