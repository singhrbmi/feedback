package fusionsoftware.loop.feedback.adapter;

import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import fusionsoftware.loop.feedback.R;
import fusionsoftware.loop.feedback.database.DbHelper;
import fusionsoftware.loop.feedback.model.Result;

public class FeedbackDetailsAdapter extends RecyclerView.Adapter<FeedbackDetailsAdapter.ViewHolder> {
    private Context context;
    private List<Result> resultList;

    public FeedbackDetailsAdapter(Context context, List<Result> resultList) {
        this.context = context;
        this.resultList = resultList;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemQuesView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_feedback_details, parent, false);
        return new ViewHolder(itemQuesView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.tv_date.setText(resultList.get(position).getDate());
        holder.tv_phone.setText(resultList.get(position).getPhone());
        holder.tv_name.setText(resultList.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return resultList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_date,tv_phone,tv_name;

        public ViewHolder(final View itemView) {
            super(itemView);
            tv_date = itemView.findViewById(R.id.tv_date);
            tv_phone = itemView.findViewById(R.id.tv_phone);
            tv_name = itemView.findViewById(R.id.tv_name);
        }

    }
}
