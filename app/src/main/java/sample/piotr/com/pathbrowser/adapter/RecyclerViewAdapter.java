package sample.piotr.com.pathbrowser.adapter;

import android.content.Context;
import android.support.v4.util.Pair;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import sample.piotr.com.pathbrowser.R;
import sample.piotr.com.pathbrowser.model.ModelPath;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.AdapterViewHolder> {

    private Context context;
    private int resource;
    private ArrayList<ModelPath> data;
    private OnItemClickListener onItemClickListener;

    public RecyclerViewAdapter(Context context, ArrayList<ModelPath> data, int resource) {

        this.context = context;
        this.resource = resource;
        this.data = data;
    }

    public interface OnItemClickListener {

        public void onItemClick(int position, ModelPath item, Pair<View, String>... sharedViews);
    }

    public class AdapterViewHolder extends RecyclerView.ViewHolder {

        private TextView textview_1_1;
        private ImageView imageview_1_0;

        public AdapterViewHolder(View itemView) {

            super(itemView);
            textview_1_1 = (TextView) itemView.findViewById(R.id.textview_1_1);
        }
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {

        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public AdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View row = inflater.inflate(resource, parent, false);
        return new AdapterViewHolder(row);
    }

    @Override
    public void onBindViewHolder(final AdapterViewHolder viewHolder, final int position) {

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(position, data.get(position), new Pair<View, String>(viewHolder.imageview_1_0, "test"), new Pair<View, String>
                            (viewHolder.textview_1_1, "test2"));
                }
            }
        });

        viewHolder.textview_1_1.setText(Long.toString(data.get(position).getId()));
        ViewCompat.setTransitionName(viewHolder.textview_1_1, String.valueOf(position) + "_test2");
    }

    @Override
    public int getItemCount() {

        return data.size();
    }

    public ModelPath getElement(int position) {

        return data.get(position);
    }
}
