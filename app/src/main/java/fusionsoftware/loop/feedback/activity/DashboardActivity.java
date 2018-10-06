package fusionsoftware.loop.feedback.activity;

import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import fusionsoftware.loop.feedback.R;
import fusionsoftware.loop.feedback.adapter.DataAdapter;
import fusionsoftware.loop.feedback.database.DbHelper;
import fusionsoftware.loop.feedback.model.Result;
import fusionsoftware.loop.feedback.myalert.SweetAlertDialog;

public class DashboardActivity extends AppCompatActivity {
    RecyclerView recycle;
    Button btn_skip;
    DbHelper database;
    int count = 0;
    int skipCount = 0;
    List<Result> strings, filterList;
    DataAdapter dataAdapter;
    String name, phone;
    int loginId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        Intent intent = getIntent();
        name = intent.getStringExtra("name");
        phone = intent.getStringExtra("phone");
        loginId = intent.getIntExtra("loginId", 0);
        recycle = findViewById(R.id.recycle);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recycle.setLayoutManager(linearLayoutManager);
        database = new DbHelper(this);
        database.deleteFeedbackData();//delete feedback data if app in background
        strings = database.getAllQuestionData();
        filterList = new ArrayList<>();
        for (Result result : strings) {
            if (result.getIsActive().equalsIgnoreCase("yes")) {
                filterList.add(result);
            }
        }
        setAdapter();
        init();
    }

    public void setAdapter() {
        TextView tv_count = findViewById(R.id.tv_count);
        Collections.sort(filterList, new Comparator<Result>() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public int compare(Result result, Result t1) {
                return Integer.compare(result.getQuestionStatus(), t1.getQuestionStatus());
            }
        });
        if (count < filterList.size()) {
            dataAdapter = new DataAdapter(this, filterList.get(count).getQuestion(), filterList.get(count).getQuestionId(), DashboardActivity.this);
            recycle.setAdapter(dataAdapter);
            count++;
            tv_count.setText(count + " / " + filterList.size());
        } else

        {
            Intent intent = new Intent(DashboardActivity.this, RemarkActivity.class);
            intent.putExtra("name", name);
            intent.putExtra("phone", phone);
            intent.putExtra("loginId", loginId);
            startActivity(intent);
        }

    }


    private void init() {

        btn_skip = findViewById(R.id.btn_skip);
//        Listener for Skip button
        btn_skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                skipCount++;
                if (skipCount > 3) {
                    new SweetAlertDialog(DashboardActivity.this, SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("Sorry...")
                            .setContentText("You Can't Skip More Then 3 Questions Of Feedback.Thank You ")
                            .show();
                } else {
                    setAdapter();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        new SweetAlertDialog(DashboardActivity.this, SweetAlertDialog.ERROR_TYPE)
                .setTitleText("Sorry...")
                .setContentText("You Can't Back Please Complete Feedback Process.Thank You ")
                .show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        database.deleteFeedbackData();
    }
}