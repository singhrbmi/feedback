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

public class DataAdapter extends RecyclerView.Adapter<DataAdapter.MyClass> {
    Context context;
    List<String> stringList;
    private int questionId = 0;
    DashboardActivity dashboardActivity;
    String questionname;
    DbHelper dbHelper;

    public DataAdapter(Context context, List<String> stringList, DashboardActivity dashboardActivity) {
        this.context = context;
        this.stringList = stringList;
        this.dashboardActivity = dashboardActivity;
    }

    public DataAdapter(Context context, String questionname, int questionId, DashboardActivity dashboardActivity) {
        this.context = context;
        this.questionname = questionname;
        this.dashboardActivity = dashboardActivity;
        this.questionId = questionId;
        dbHelper = new DbHelper(context);
    }

    @Override
    public MyClass onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_data, parent, false);
        return new MyClass(itemView);
    }

    @Override
    public void onBindViewHolder(final MyClass holder, final int position) {
        holder.tv.setText(questionname);
        holder.lay_happy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fun("happy");
            }
        });
        holder.lay_good.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fun("good");
            }
        });
        holder.lay_average.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fun("awosome");
            }
        });
        holder.lay_bad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fun("bad");
            }
        });


    }

    public void fun(String ss) {
        dbHelper.insertFeedbackData(questionId, ss);
        dashboardActivity.setAdapter();
    }

    @Override
    public int getItemCount() {
        return 1;
    }


    public class MyClass extends RecyclerView.ViewHolder {
        TextView tv, good, bad, average, happy;
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
            Typeface typeface = FontManager.getFontTypefaceMaterialDesignIcons(context, "fonts/materialdesignicons-webfont.otf");
            happy.setTypeface(typeface);
            happy.setText(Html.fromHtml("&#xf1f6;"));
            happy.startAnimation(fadeAnimation);
            good.setTypeface(typeface);
            good.setText(Html.fromHtml("&#xf1f5;"));
            good.startAnimation(fadeAnimation);
            average.setTypeface(typeface);
            average.setText(Html.fromHtml("&#xf1f2;"));
            average.startAnimation(fadeAnimation);
            bad.setTypeface(typeface);
            bad.setText(Html.fromHtml("&#xf1f8;"));
            bad.startAnimation(fadeAnimation);

        }
    }
}
