package sample.piotr.com.pathbrowser.list;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.util.Pair;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.android.support.AndroidSupportInjection;
import sample.piotr.com.pathbrowser.ActivityInterface;
import sample.piotr.com.pathbrowser.R;
import sample.piotr.com.pathbrowser.adapter.RecyclerViewAdapter;
import sample.piotr.com.pathbrowser.dao.PathRepository;
import sample.piotr.com.pathbrowser.map.FragmentMap;
import sample.piotr.com.pathbrowser.model.ModelPath;
import sample.piotr.com.pathbrowser.service.LocationService;

/**
 * Created by piotr on 17/07/17.
 */

public class FragmentList extends Fragment implements LocationService.OnPathListener, ListPresenterContract.View {

    @Inject
    PathRepository pathRepository;

    private ListPresenter presenter;
    private RecyclerView recyclerView;
    private RecyclerViewAdapter adapter;
    private ArrayList<ModelPath> data;

    @Override
    public void onAttach(Activity activity) {

        AndroidSupportInjection.inject(this);
        super.onAttach(activity);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_list, parent, false);

        recyclerView = (RecyclerView) v.findViewById(R.id.rv);
        data = new ArrayList<>();
        adapter = new RecyclerViewAdapter(getActivity(), data, R.layout.row_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), layoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);
        presenter = new ListPresenter(this, pathRepository);
        presenter.loadPaths();

        adapter.setOnItemClickListener(new RecyclerViewAdapter.OnItemClickListener() {

            @Override
            public void onItemClick(int position, ModelPath item, Pair<View, String>... sharedViews) {

                ActivityInterface activity = (ActivityInterface) getActivity();
                activity.loadMap(item);
            }
        });

        return v;
    }

    @Override
    public void onCurrentPathUpdated(ModelPath path) {

        if (data.contains(path)) {
            int index = data.indexOf(path);
            data.set(index, path);
            adapter.notifyItemChanged(index);
        }
        else {
            data.add(path);
            adapter.notifyItemRangeChanged(0, data.size());
        }
    }

    @Override
    public void onPathsLoaded(List<ModelPath> paths) {

        if (isAdded()) {
            data.clear();
            data.addAll(paths);
            adapter.notifyDataSetChanged();
        }
    }
}
