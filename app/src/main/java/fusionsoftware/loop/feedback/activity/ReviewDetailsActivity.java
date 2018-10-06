package fusionsoftware.loop.feedback.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.widget.TextView;

import java.util.List;

import fusionsoftware.loop.feedback.R;
import fusionsoftware.loop.feedback.adapter.ReviewDetailsAdapter;
import fusionsoftware.loop.feedback.database.DbHelper;
import fusionsoftware.loop.feedback.model.Result;

public class ReviewDetailsActivity extends AppCompatActivity {
    int feedbackId, loginId;
    TextView tv_date, tv_phone, tv_name, tv_time, tv_remark;
    RecyclerView recyclerView;
    String date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_details);
        Intent intent = getIntent();
        feedbackId = intent.getIntExtra("feedbackId", 0);
        date = intent.getStringExtra("date");
        loginId = intent.getIntExtra("loginId", 0);
        init();
    }

    private void init() {
        final DbHelper dbHelper = new DbHelper(ReviewDetailsActivity.this);
        tv_date = findViewById(R.id.tv_date);
        tv_phone = findViewById(R.id.tv_phone);
        tv_name = findViewById(R.id.tv_name);
        tv_time = findViewById(R.id.tv_time);
        tv_remark = findViewById(R.id.tv_remark);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL));
        Result result = dbHelper.getFeedbackSummeryDataByFeedbackId(feedbackId);
        String nameData = result.getName();
        if (nameData != null && !nameData.equalsIgnoreCase("")) {
            tv_name.setText(nameData);
        } else {
            tv_name.setText("Name Not Available");
        }
        tv_phone.setText(result.getPhone());
        String remarkData = result.getRemark();
        if (remarkData != null && !remarkData.equalsIgnoreCase("")) {
            tv_remark.setText("Remark:\n" + result.getRemark());
        } else {
            tv_remark.setText("Remark Not Available");
        }
        String[] splitData = result.getDate().split(" ");
        tv_date.setText(splitData[0]);
        tv_time.setText(splitData[1]);
        dbHelper.deleteFeedbackData();
        final List<Result> resultFeedback = dbHelper.getAllFeedbackSummeryDataByAnswerIdLoginId(loginId);
        for (Result result1 : resultFeedback) {
            dbHelper.insertFeedbackData(result1.getQuestionId(), result1.getFeedbackStatus());
        }
        ReviewDetailsAdapter reviewDetailsAdapter = new ReviewDetailsAdapter(ReviewDetailsActivity.this, dbHelper.getAllFeedbackData());
        recyclerView.setAdapter(reviewDetailsAdapter);
    }
}
