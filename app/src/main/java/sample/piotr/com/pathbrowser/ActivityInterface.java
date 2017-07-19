package sample.piotr.com.pathbrowser;

import android.support.v4.app.Fragment;

import sample.piotr.com.pathbrowser.model.ModelPath;

/**
 * Created by piotr on 19/07/17.
 */

public interface ActivityInterface {

    public void replaceFragment(Fragment fragment, boolean backstack);

    public ModelPath getCurrentPath();
    public void  loadMap(ModelPath path);
}
