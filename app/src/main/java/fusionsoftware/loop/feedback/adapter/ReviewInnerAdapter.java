package fusionsoftware.loop.feedback.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import fusionsoftware.loop.feedback.R;
import fusionsoftware.loop.feedback.activity.ReviewDetailsActivity;
import fusionsoftware.loop.feedback.database.DbHelper;
import fusionsoftware.loop.feedback.model.Result;

public class ReviewInnerAdapter extends RecyclerView.Adapter<ReviewInnerAdapter.ViewHolder> {
    private Context context;
    private List<Result> resultList;

    public ReviewInnerAdapter(Context context, List<Result> resultList) {
        this.context = context;
        this.resultList = resultList;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemQuesView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_inner_review, parent, false);
        return new ViewHolder(itemQuesView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        DbHelper dbHelper = new DbHelper(context);
        final Result result = dbHelper.getFeedbackSummeryDataByLoginId(resultList.get(position).getLoginId());

        String date = result.getDate();
        holder.tv_phone.setText(result.getPhone());
        holder.tv_name.setText(result.getName());
        String[] splitData = date.split(" ");
        holder.tv_date.setText(splitData[0]);
        holder.tv_time.setText(splitData[1]);
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ReviewDetailsActivity.class);
                intent.putExtra("feedbackId",result.getFeedbackId());
                intent.putExtra("date", result.getDate());
                intent.putExtra("loginId", resultList.get(position).getLoginId());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return resultList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_date, tv_phone, tv_name, tv_time;
        LinearLayout cardView;

        public ViewHolder(final View itemView) {
            super(itemView);
            tv_date = itemView.findViewById(R.id.tv_date);
            tv_phone = itemView.findViewById(R.id.tv_phone);
            tv_name = itemView.findViewById(R.id.tv_name);
            tv_time = itemView.findViewById(R.id.tv_time);
            cardView = itemView.findViewById(R.id.cardView);
        }

    }
}
