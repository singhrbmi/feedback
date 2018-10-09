package fusionsoftware.loop.feedback.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.MenuItem;
import android.widget.ProgressBar;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import fusionsoftware.loop.feedback.R;
import fusionsoftware.loop.feedback.adapter.FeedbackAdapter;
import fusionsoftware.loop.feedback.database.DbHelper;
import fusionsoftware.loop.feedback.model.AnswerData;
import fusionsoftware.loop.feedback.model.ContentData;
import fusionsoftware.loop.feedback.model.Result;
import fusionsoftware.loop.feedback.myalert.SweetAlertDialog;
import fusionsoftware.loop.feedback.utility.Contants;
import fusionsoftware.loop.feedback.utility.Utility;

public class GetAllFeedbackActivity extends AppCompatActivity {
    ProgressDialog pd;
    RecyclerView recyclerView;
    List<Result> resultList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_get_all_feedback);
        init();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // app icon in action bar clicked; go home
                Intent intent = new Intent(this, AdminDashboardActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void init() {
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL));
        resultList = new ArrayList<>();
        getAllFeedbackData();
    }

    private void getAllFeedbackData() {
        final DbHelper dbHelper = new DbHelper(GetAllFeedbackActivity.this);
        if (Utility.isOnline(this)) {
            pd = new ProgressDialog(this);
            pd.setCancelable(false);
            pd.show();
            pd.getWindow()
                    .setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            pd.setContentView(new ProgressBar(this));
            StringRequest stringRequest = new StringRequest(Request.Method.POST, Contants.SERVICE_BASE_URL + Contants.getAllFeedbackGroup,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            pd.dismiss();
                            ContentData contentData = new Gson().fromJson(response, ContentData.class);
                            if (contentData != null) {
                                dbHelper.deleteFeedbackSummeryData();
                                for (Result result : contentData.getResult()) {
                                    if (result != null) {
//                                        resultList.addAll(Arrays.asList(result));
                                        dbHelper.insertFeedbackSummeryData(result);
                                    }
                                }

//                                startActivity(new Intent(GetAllFeedbackActivity.this, GetAllFeedbackActivity.class));
                                resultList = dbHelper.getAllFeedbackSummeryData();
                                shortSharingData(resultList);
//                                getValue(resultList);
                            }

                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            pd.dismiss();
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    return params;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(stringRequest);
        } else {
            new SweetAlertDialog(GetAllFeedbackActivity.this, SweetAlertDialog.ERROR_TYPE)
                    .setTitleText("Sorry...")
                    .setContentText("You are Offline. Please check your Internet Connection.Thank You ")
                    .show();
        }
    }

//    private void getValue(List<Result> results) {
    //make question list
//        ArrayList<Result> questionList = new ArrayList<Result>();
//        for (Result c : results) {
//            if (!dayDataExist(questionList, c.getQuestionId())) {
//                questionList.add(c);
//            }
//        }
    //create question and answer list for show data
//        ArrayList<Result> DataList = new ArrayList<Result>();
//        if (questionList != null && questionList.size() > 0) {
//            for (int i = 0; i < questionList.size(); i++) {
//                Result data = new Result();
//                data.setQuestionId(questionList.get(i).getQuestionId());
//                data.setQuestion(questionList.get(i).getQusetionName());
//                ArrayList<AnswerData> answerDataList = new ArrayList<AnswerData>();
//                for (int j = 0; j < results.size(); j++) {
//                    if (questionList.get(i).getQuestionId() == results.get(j).getQuestionId()) {
    // got the duplicate element
//                        AnswerData answerData = new AnswerData();
//                        answerData.setAnswerId(results.get(j).getAnswerId());
//                        answerData.setAnswer(results.get(j).getAnswerSize());
//                        answerData.setQuestionId(results.get(j).getQuestionId());
//                        answerDataList.add(answerData);
//                    }
//                }
//                data.setAnswerDataList(answerDataList);
//                DataList.add(data);
//            }
//            FeedbackAdapter feedbackAdapter = new FeedbackAdapter(GetAllFeedbackActivity.this, DataList);
//            recyclerView.setAdapter(feedbackAdapter);
//        }
//
//    }


    //sort  data.................
    private void shortSharingData(List<Result> resultList) {
        List<Result> newList = new ArrayList<Result>();
        for (Result c : resultList) {
            if (!dayDataExist(newList, c.getQuestionId())) {
                newList.add(c);
            }
        }

        FeedbackAdapter feedbackAdapter = new FeedbackAdapter(GetAllFeedbackActivity.this, newList);
        recyclerView.setAdapter(feedbackAdapter);
    }

    private boolean dayDataExist(List<Result> newList, int id) {
        for (Result c : newList) {
            if (c.getQuestionId() == id) {
                return true;
            }
        }
        return false;
    }
}
