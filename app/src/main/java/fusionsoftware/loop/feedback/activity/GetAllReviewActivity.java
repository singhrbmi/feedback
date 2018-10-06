package fusionsoftware.loop.feedback.activity;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fusionsoftware.loop.feedback.R;
import fusionsoftware.loop.feedback.adapter.FeedbackAdapter;
import fusionsoftware.loop.feedback.adapter.ReviewAdapter;
import fusionsoftware.loop.feedback.database.DbHelper;
import fusionsoftware.loop.feedback.model.ContentData;
import fusionsoftware.loop.feedback.model.Result;
import fusionsoftware.loop.feedback.myalert.SweetAlertDialog;
import fusionsoftware.loop.feedback.utility.Contants;
import fusionsoftware.loop.feedback.utility.Utility;

@RequiresApi(api = Build.VERSION_CODES.M)
public class GetAllReviewActivity extends AppCompatActivity implements RecyclerView.OnScrollChangeListener {
    RecyclerView recyclerView;
    List<Result> resultList;
    private int requestCount = 1;
//    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_all_review);
        init();
    }

    @TargetApi(Build.VERSION_CODES.M)
    private void init() {
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        resultList = new ArrayList<>();
        //Adding an scroll change listener to recyclerview
        recyclerView.setOnScrollChangeListener(this);
//        progressBar =findViewById(R.id.progressBar);
        getAllFeedbackData();
    }

    private void getAllFeedbackData() {
        final DbHelper dbHelper = new DbHelper(GetAllReviewActivity.this);
        if (Utility.isOnline(this)) {
//            progressBar.setVisibility(View.VISIBLE);
//            setProgressBarIndeterminateVisibility(true);
            StringRequest stringRequest = new StringRequest(Request.Method.POST, Contants.SERVICE_BASE_URL + Contants.getAllFeedback + String.valueOf(requestCount),
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            //Hiding the progressbar
//                            progressBar.setVisibility(View.GONE);
                            ContentData contentData = new Gson().fromJson(response, ContentData.class);
                            if (contentData != null) {
                                dbHelper.deleteFeedbackSummeryData();
                                for (Result result : contentData.getResult()) {
                                    if (result != null) {
                                        dbHelper.upsertFeedbackSummeryDataData(result);
                                    }
                                }
//                                startActivity(new Intent(GetAllFeedbackActivity.this, GetAllFeedbackActivity.class));
                                resultList = dbHelper.getAllFeedbackSummeryData();
//                                ReviewAdapter feedbackAdapter = new ReviewAdapter(GetAllReviewActivity.this, resultList);
//                                recyclerView.setAdapter(feedbackAdapter);
                                shortSharingData(resultList);
                            }
                            //Incrementing the request counter
                            requestCount++;
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            //Hiding the progressbar
//                            progressBar.setVisibility(View.GONE);
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

            new SweetAlertDialog(GetAllReviewActivity.this, SweetAlertDialog.ERROR_TYPE)
                    .setTitleText("Sorry...")
                    .setContentText("You are Offline. Please check your Internet Connection.Thank You ")
                    .show();
        }

    }

    //Overriden method to detect scrolling
    @Override
    public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
        //Ifscrolled at last then
        if (isLastItemDisplaying(recyclerView)) {
            //Calling the method getdata again
            getAllFeedbackData();
        }
    }

    //This method would check that the recyclerview scroll has reached the bottom or not
    private boolean isLastItemDisplaying(RecyclerView recyclerView) {
        if (recyclerView.getAdapter().getItemCount() != 0) {
            int lastVisibleItemPosition = ((LinearLayoutManager) recyclerView.getLayoutManager()).findLastCompletelyVisibleItemPosition();
            if (lastVisibleItemPosition != RecyclerView.NO_POSITION && lastVisibleItemPosition == recyclerView.getAdapter().getItemCount() - 1)
                return true;
        }
        return false;
    }

    //sort  data.................
    private void shortSharingData(List<Result> resultList) {
        List<Result> newList = new ArrayList<Result>();
        for (Result c : resultList) {
            if (!dayDataExist(newList, c.getDate())) {
                newList.add(c);
            }
        }

        ReviewAdapter feedbackAdapter = new ReviewAdapter(GetAllReviewActivity.this, newList);
        recyclerView.setAdapter(feedbackAdapter);
        feedbackAdapter.notifyDataSetChanged();
    }

    private boolean dayDataExist(List<Result> newList, String id) {
        for (Result c : newList) {
            if (c.getDate().equalsIgnoreCase(id)) {
                return true;
            }
        }
        return false;
    }
}
