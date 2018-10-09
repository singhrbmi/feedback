package fusionsoftware.loop.feedback.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import fusionsoftware.loop.feedback.R;
import fusionsoftware.loop.feedback.database.DbHelper;
import fusionsoftware.loop.feedback.model.Result;

public class SmsAdapter extends RecyclerView.Adapter<SmsAdapter.ViewHolder> {
    private Context context;
    private List<Result> resultList;
   private boolean isSelectedAll;

    public SmsAdapter(Context context, List<Result> resultList) {
        this.context = context;
        this.resultList = resultList;
    }
    public SmsAdapter(Context context) {
        this.context = context;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemQuesView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_sms, parent, false);
        return new ViewHolder(itemQuesView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        DbHelper dbHelper = new DbHelper(context);
        final Result result = dbHelper.getFeedbackSummeryDataByLoginId(resultList.get(position).getLoginId());

        holder.tv_phone.setText(result.getPhone());
        holder.tv_name.setText(result.getName());
        if (isSelectedAll) {
            holder.checkbox.setChecked(true);
        } else {
            holder.checkbox.setChecked(false);

        }
    }

    public void selectAll(boolean isSelectedAll){
        this.isSelectedAll=isSelectedAll;
        notifyDataSetChanged();
    }


    @Override
    public int getItemCount() {
        return resultList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_date, tv_phone, tv_name, tv_time;
        CheckBox checkbox;

        public ViewHolder(final View itemView) {
            super(itemView);
            tv_phone = itemView.findViewById(R.id.tv_phone);
            tv_name = itemView.findViewById(R.id.tv_name);
            checkbox = itemView.findViewById(R.id.checkbox);
        }

    }
}
