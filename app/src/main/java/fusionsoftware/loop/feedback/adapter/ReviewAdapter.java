package fusionsoftware.loop.feedback.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import fusionsoftware.loop.feedback.R;
import fusionsoftware.loop.feedback.activity.GetAllReviewActivity;
import fusionsoftware.loop.feedback.activity.ReviewDetailsActivity;
import fusionsoftware.loop.feedback.database.DbHelper;
import fusionsoftware.loop.feedback.model.Result;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ViewHolder> implements Filterable {
    private Context context;
    private List<Result> resultList, FilteruserList;

    public ReviewAdapter(Context context, List<Result> resultList) {
        this.context = context;
        this.resultList = resultList;
        this.FilteruserList = resultList;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemQuesView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_review, parent, false);
        return new ViewHolder(itemQuesView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        DbHelper dbHelper = new DbHelper(context);
        final List<Result> resultListData = dbHelper.getFeedbackSummeryDataByDate(FilteruserList.get(position).getDate());

        String date = FilteruserList.get(position).getDate();
        String[] splitData = date.split(" ");
        holder.tv_date.setText(splitData[0]);

        shortSharingData(resultListData, holder);
    }

    //sort  data.................
    private void shortSharingData(List<Result> resultList, ViewHolder holder) {
        List<Result> newList = new ArrayList<Result>();
        for (Result c : resultList) {
            if (!dayDataExist(newList, c.getLoginId())) {
                newList.add(c);
            }
        }

        ReviewInnerAdapter feedbackAdapter = new ReviewInnerAdapter(context, newList);
        holder.recyclerView.setAdapter(feedbackAdapter);
    }

    private boolean dayDataExist(List<Result> newList, int id) {
        for (Result c : newList) {
            if (c.getLoginId() == id) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int getItemCount() {
        return FilteruserList.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString().trim();
                // name match condition. this might differ depending on your requirement
                // here we are looking for name or phone number match
                if (charString.isEmpty()) {
                    FilteruserList = resultList;
                } else {
                    List<Result> filteredList = new ArrayList<>();
                    for (Result row : resultList) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getDate().toLowerCase().trim().contains(charString.toLowerCase())) {
                            filteredList.add(row);
                        }
                    }


                    FilteruserList = filteredList;
                }


                FilterResults filterResults = new FilterResults();
                filterResults.values = FilteruserList;
                return filterResults;
            }


            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                FilteruserList = (ArrayList<Result>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_date;
        CardView cardView;
        RecyclerView recyclerView;

        public ViewHolder(final View itemView) {
            super(itemView);
            tv_date = itemView.findViewById(R.id.tv_date);
            cardView = itemView.findViewById(R.id.cardView);
            recyclerView = itemView.findViewById(R.id.recyclerView);
            recyclerView.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL));
        }

    }
}
