package sample.piotr.com.pathbrowser.list;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import sample.piotr.com.pathbrowser.R;

/**
 * Created by piotr on 17/07/17.
 */

public class FragmentList extends Fragment {

    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_list, parent, false);
        return v;
    }
}
