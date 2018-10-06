package fusionsoftware.loop.feedback.activity;

import android.app.ProgressDialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.Toast;

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
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fusionsoftware.loop.feedback.R;
import fusionsoftware.loop.feedback.adapter.QuestionUpdateAdapter;
import fusionsoftware.loop.feedback.database.DbHelper;
import fusionsoftware.loop.feedback.framework.IAsyncWorkCompletedCallback;
import fusionsoftware.loop.feedback.framework.ServiceCaller;
import fusionsoftware.loop.feedback.model.ContentData;
import fusionsoftware.loop.feedback.model.Result;
import fusionsoftware.loop.feedback.myalert.SweetAlertDialog;
import fusionsoftware.loop.feedback.swipeable_view.ItemTouchHelperAdapter;
import fusionsoftware.loop.feedback.swipeable_view.OnStartDragListener;
import fusionsoftware.loop.feedback.swipeable_view.SimpleItemTouchHelperCallback;
import fusionsoftware.loop.feedback.utility.Contants;
import fusionsoftware.loop.feedback.utility.Utility;

public class QuestionActivity extends AppCompatActivity implements OnStartDragListener {
    private RecyclerView mQuestionRecycle_View;
    private EditText mEdit_question;
    private Button mBtn_update_Add;
    private DbHelper dbHelper;
    private QuestionUpdateAdapter questionUpdateAdapter;
    private boolean mFlag = false;
    private String ques;
    private ItemTouchHelper mitemTouchHelper;
    int questionID;
    ProgressDialog pd;
    List<Result> resultList;
    RadioButton rbtn_Activate, rbtn_Deactivate;
    String questionStr, isActiveStr;
    RadioGroup radio_group;
    ScrollView scrollview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);
        mEdit_question = findViewById(R.id.edit_question);
        mBtn_update_Add = findViewById(R.id.btn_update_Add);
        mQuestionRecycle_View = findViewById(R.id.questionRecycle_View);
        rbtn_Activate = findViewById(R.id.btn_Activate);
        rbtn_Deactivate = findViewById(R.id.btn_Deactivate);
        radio_group = findViewById(R.id.radio_group);
        scrollview = findViewById(R.id.scrollview);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mQuestionRecycle_View.setLayoutManager(linearLayoutManager);
        resultList = new ArrayList<>();
        setAdapter();
        mBtn_update_Add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                mEdit_question.setVisibility(View.VISIBLE);
                if (mFlag) {
                    updateQuestion();
                    //update code
                } else {
//                    mEdit_question.setVisibility(View.VISIBLE);
                    addnewQuestion();
                    // code for add new ques.
                }
            }
        });
    }

    boolean validation() {
        questionStr = mEdit_question.getText().toString();
        if (questionStr.length() == 0) {
            mEdit_question.setError("Enter Question");
            return false;
        }
        int radioID = radio_group.getCheckedRadioButtonId();
        if (radioID == R.id.btn_Activate) {
            isActiveStr = "yes";
        }
        if (radioID == R.id.btn_Deactivate) {
            isActiveStr = "no";
        }
        return true;
    }

    private void addnewQuestion() {
        if (validation()) {
            if (Utility.isOnline(this)) {
                pd = new ProgressDialog(this);
                pd.setCancelable(false);
                pd.show();
                pd.getWindow()
                        .setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                pd.setContentView(new ProgressBar(this));
                StringRequest stringRequest = new StringRequest(Request.Method.POST, Contants.SERVICE_BASE_URL + Contants.addNewQuestion,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Toast.makeText(QuestionActivity.this, response, Toast.LENGTH_SHORT).show();
                                pd.dismiss();
                                setAdapter();
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
                        params.put("question", questionStr);
                        params.put("isActive", isActiveStr);
                        return params;
                    }
                };
                RequestQueue requestQueue = Volley.newRequestQueue(this);
                requestQueue.add(stringRequest);
            } else {

                new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("Sorry...")
                        .setContentText("You are Offline. Please check your Internet Connection.Thank You ")
                        .show();
            }

        }
    }

    private void updateQuestion() {
        if (validation()) {
            if (Utility.isOnline(this)) {
                pd = new ProgressDialog(this);
                pd.setCancelable(false);
                pd.show();
                pd.getWindow()
                        .setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                pd.setContentView(new ProgressBar(this));
                StringRequest stringRequest = new StringRequest(Request.Method.POST, Contants.SERVICE_BASE_URL + Contants.updateQuestion,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                pd.dismiss();
                                mBtn_update_Add.setText("Add");
                                mFlag = false;
                                setAdapter();
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
                        params.put("questionId", String.valueOf(questionID));
                        params.put("question", questionStr);
                        params.put("isActive", isActiveStr);
                        return params;
                    }
                };
                RequestQueue requestQueue = Volley.newRequestQueue(this);
                requestQueue.add(stringRequest);
            } else {

                new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("Sorry...")
                        .setContentText("You are Offline. Please check your Internet Connection.Thank You ")
                        .show();
            }

        }
    }

    public void setAdapter() {
        pd = new ProgressDialog(this);
        pd.setCancelable(false);
        pd.show();
        pd.getWindow()
                .setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        pd.setContentView(new ProgressBar(this));
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Contants.SERVICE_BASE_URL + Contants.getAllQuestion,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        pd.dismiss();
                        resultList.clear();
                        mEdit_question.setText("");
                        questionStr = null;
                        isActiveStr = null;
//                        Toast.makeText(QuestionActivity.this, response, Toast.LENGTH_SHORT).show();
                        ContentData data = new Gson().fromJson(response, ContentData.class);
                        for (Result result1 : data.getResult()) {
                            resultList.addAll(Arrays.asList(result1));
                        }
                        Collections.sort(resultList, new Comparator<Result>() {
                            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                            @Override
                            public int compare(Result result, Result t1) {
                                return Integer.compare(result.getQuestionStatus(), t1.getQuestionStatus());
                            }
                        });
                        questionUpdateAdapter = new QuestionUpdateAdapter(QuestionActivity.this, resultList, QuestionActivity.this, QuestionActivity.this);
                        mQuestionRecycle_View.setAdapter(questionUpdateAdapter);
                        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback((ItemTouchHelperAdapter) questionUpdateAdapter);
                        mitemTouchHelper = new ItemTouchHelper(callback);
                        mitemTouchHelper.attachToRecyclerView(mQuestionRecycle_View);
                        questionUpdateAdapter.notifyDataSetChanged();
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
    }


    public void updateData(int questionID, String ques, Boolean flag) {
        this.ques = ques;
        this.mFlag = flag;
        this.questionID = questionID;
        mEdit_question.setText(ques);
        mEdit_question.setSelection(ques.length());
        mBtn_update_Add.setText("Update");
        mEdit_question.setVisibility(View.VISIBLE);
        scrollview.fullScroll(View.FOCUS_DOWN);
    }

    @Override
    public void onStartDrag(RecyclerView.ViewHolder viewHolder) {
        mitemTouchHelper.startDrag(viewHolder);
    }
}



