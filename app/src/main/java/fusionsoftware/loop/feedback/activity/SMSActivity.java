package fusionsoftware.loop.feedback.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

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
import fusionsoftware.loop.feedback.adapter.ReviewAdapter;
import fusionsoftware.loop.feedback.adapter.SmsAdapter;
import fusionsoftware.loop.feedback.database.DbHelper;
import fusionsoftware.loop.feedback.model.ContentData;
import fusionsoftware.loop.feedback.model.Result;
import fusionsoftware.loop.feedback.myalert.SweetAlertDialog;
import fusionsoftware.loop.feedback.utility.Contants;
import fusionsoftware.loop.feedback.utility.Utility;

@RequiresApi(api = Build.VERSION_CODES.M)
public class SMSActivity extends AppCompatActivity implements RecyclerView.OnScrollChangeListener {
    RecyclerView recyclerView;
    List<Result> resultList;
    private int requestCount = 1;
    CheckBox checkBox;
    SmsAdapter feedbackAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_sms);
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void init() {
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        resultList = new ArrayList<>();
        //Adding an scroll change listener to recyclerview
        recyclerView.setOnScrollChangeListener(this);
        checkBox = findViewById(R.id.checkbox);
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (feedbackAdapter != null) {
                    feedbackAdapter.selectAll(b);
                }
            }
        });
        getAllFeedbackData();
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
    private void getAllFeedbackData() {
        final DbHelper dbHelper = new DbHelper(SMSActivity.this);
        if (Utility.isOnline(this)) {
//            progressBar.setVisibility(View.VISIBLE);
//            setProgressBarIndeterminateVisibility(true);
            StringRequest stringRequest = new StringRequest(Request.Method.POST, Contants.SERVICE_BASE_URL + Contants.getAllFeedbackold,
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
//                            requestCount++;
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

            new SweetAlertDialog(SMSActivity.this, SweetAlertDialog.ERROR_TYPE)
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
//            getAllFeedbackData();
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
            if (!dayDataExist(newList, c.getLoginId())) {
                newList.add(c);
            }
        }

        feedbackAdapter = new SmsAdapter(SMSActivity.this, newList);
        recyclerView.setAdapter(feedbackAdapter);
        feedbackAdapter.notifyDataSetChanged();
    }

    private boolean dayDataExist(List<Result> newList, int id) {
        for (Result c : newList) {
            if (c.getLoginId() == (id)) {
                return true;
            }
        }
        return false;
    }

    //for hid keyboard when tab outside edittext box
    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (v instanceof EditText) {
                Rect outRect = new Rect();
                v.getGlobalVisibleRect(outRect);
                if (!outRect.contains((int) event.getRawX(), (int) event.getRawY())) {
                    v.clearFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
        }
        return super.dispatchTouchEvent(event);
    }
}
