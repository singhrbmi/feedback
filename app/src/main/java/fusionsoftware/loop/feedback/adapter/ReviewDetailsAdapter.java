package fusionsoftware.loop.feedback.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import fusionsoftware.loop.feedback.R;
import fusionsoftware.loop.feedback.activity.DashboardActivity;
import fusionsoftware.loop.feedback.database.DbHelper;
import fusionsoftware.loop.feedback.model.Result;
import fusionsoftware.loop.feedback.utility.FontManager;

public class ReviewDetailsAdapter extends RecyclerView.Adapter<ReviewDetailsAdapter.MyClass> {
    Context context;
    List<Result> stringList;
    private int questionId = 0;
    DashboardActivity dashboardActivity;
    String questionname;
    DbHelper dbHelper;

    public ReviewDetailsAdapter(Context context, List<Result> stringList) {
        this.context = context;
        this.stringList = stringList;
    }

    public ReviewDetailsAdapter(Context context, String questionname, int questionId, DashboardActivity dashboardActivity) {
        this.context = context;
        this.questionname = questionname;
        this.dashboardActivity = dashboardActivity;
        this.questionId = questionId;
        dbHelper = new DbHelper(context);
    }

    @Override
    public MyClass onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_review_detals, parent, false);
        return new MyClass(itemView);
    }

    @Override
    public void onBindViewHolder(final MyClass holder, final int position) {
        DbHelper dbHelper = new DbHelper(context);
        Result result = dbHelper.getQuestionDataByLoginId(stringList.get(position).getQuestionId());
        holder.tv.setText(result.getQuestion());
        String status = stringList.get(position).getFeedbackStatus();
        if (status.equalsIgnoreCase("bad")) {
            holder.lay_bad.setVisibility(View.VISIBLE);
            holder.bad.setText(Html.fromHtml("&#xf1f8;"));
            holder.txt_bad.setText("Bad");
        }
        if (status.equalsIgnoreCase("Happy")) {
            holder.lay_happy.setVisibility(View.VISIBLE);
            holder.happy.setText(Html.fromHtml("&#xf1f6;"));
            holder.txt_happy.setText("Happy");
        }
        if (status.equalsIgnoreCase("Good")) {
            holder.lay_good.setVisibility(View.VISIBLE);
            holder.good.setText(Html.fromHtml("&#xf1f5;"));
            holder.txt_good.setText("Good");
        }
        if (status.equalsIgnoreCase("Awosome")) {
            holder.lay_average.setVisibility(View.VISIBLE);
            holder.average.setText(Html.fromHtml("&#xf1f2;"));
            holder.txt_average.setText("Awosome");
        }

    }


    @Override
    public int getItemCount() {
        return stringList.size();
    }


    public class MyClass extends RecyclerView.ViewHolder {
        TextView tv, good, bad, average, happy, txt_bad, txt_happy, txt_good, txt_average;
        LinearLayout lay_happy, lay_good, lay_average, lay_bad;
        Animation fadeAnimation;


        public MyClass(View itemView) {
            super(itemView);
            fadeAnimation = AnimationUtils.loadAnimation(context, R.anim.fadeanimation);
            lay_happy = itemView.findViewById(R.id.lay_happy);
            lay_good = itemView.findViewById(R.id.lay_good);
            lay_average = itemView.findViewById(R.id.lay_average);
            lay_bad = itemView.findViewById(R.id.lay_bad);
            tv = itemView.findViewById(R.id.tv);
            happy = itemView.findViewById(R.id.happy);
            good = itemView.findViewById(R.id.good);
            average = itemView.findViewById(R.id.average);
            bad = itemView.findViewById(R.id.bad);
            txt_bad = itemView.findViewById(R.id.txt_bad);
            txt_happy = itemView.findViewById(R.id.txt_happy);
            txt_good = itemView.findViewById(R.id.txt_good);
            txt_average = itemView.findViewById(R.id.txt_average);
            Typeface typeface = FontManager.getFontTypefaceMaterialDesignIcons(context, "fonts/materialdesignicons-webfont.otf");
            happy.setTypeface(typeface);
//            happy.setText(Html.fromHtml("&#xf1f6;"));
            happy.startAnimation(fadeAnimation);
            good.setTypeface(typeface);
//            good.setText(Html.fromHtml("&#xf1f5;"));
            good.startAnimation(fadeAnimation);
            average.setTypeface(typeface);
//            average.setText(Html.fromHtml("&#xf1f2;"));
            average.startAnimation(fadeAnimation);
            bad.setTypeface(typeface);
            bad.startAnimation(fadeAnimation);

        }
    }
}
