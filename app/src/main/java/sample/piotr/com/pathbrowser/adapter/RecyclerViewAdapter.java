package sample.piotr.com.pathbrowser.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

import sample.piotr.com.pathbrowser.R;
import sample.piotr.com.pathbrowser.model.ModelPath;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.AdapterViewHolder> {

    private Context context;
    private int resource;
    private ArrayList<ModelPath> data;
    private OnItemClickListener onItemClickListener;

    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());

    public RecyclerViewAdapter(Context context, ArrayList<ModelPath> data, int resource) {

        this.context = context;
        this.resource = resource;
        this.data = data;
    }

    public interface OnItemClickListener {

        public void onItemClick(int position, ModelPath item);
    }

    public class AdapterViewHolder extends RecyclerView.ViewHolder {

        private TextView tvStartTime;
        private TextView tvEndTime;
        private TextView tvPointsCount;

        public AdapterViewHolder(View itemView) {

            super(itemView);
            tvStartTime = (TextView) itemView.findViewById(R.id.tv_start);
            tvEndTime = (TextView) itemView.findViewById(R.id.tv_end);
            tvPointsCount = (TextView) itemView.findViewById(R.id.tv_paths_number);
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
    public void onBindViewHolder(final AdapterViewHolder viewHolder,int position) {

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                int adapterPosition = viewHolder.getAdapterPosition();
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(adapterPosition, data.get(adapterPosition));
                }
            }
        });

        viewHolder.tvStartTime.setText(sdf.format(data.get(position).getStartDate()));
        viewHolder.tvEndTime.setText(sdf.format(data.get(position).getEndDate()));
        viewHolder.tvPointsCount.setText(context.getString(R.string.points_prefix,data.size()));

    }

    @Override
    public int getItemCount() {

        return data.size();
    }

    public ModelPath getElement(int position) {

        return data.get(position);
    }
}
