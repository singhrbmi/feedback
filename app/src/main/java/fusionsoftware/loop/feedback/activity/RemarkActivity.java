package fusionsoftware.loop.feedback.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;


import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import fusionsoftware.loop.feedback.R;
import fusionsoftware.loop.feedback.database.DbHelper;
import fusionsoftware.loop.feedback.model.ContentData;
import fusionsoftware.loop.feedback.model.Result;
import fusionsoftware.loop.feedback.myalert.SweetAlertDialog;
import fusionsoftware.loop.feedback.utility.Contants;
import fusionsoftware.loop.feedback.utility.Utility;

public class RemarkActivity extends AppCompatActivity {
    EditText edt_remark;
    Button btn_finalSubmit;
    ProgressDialog pd;
    String name, phone;
    int loginID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remark);
        Intent intent = getIntent();
        name = intent.getStringExtra("name");
        phone = intent.getStringExtra("phone");
        loginID = intent.getIntExtra("loginId", 0);
        edt_remark = findViewById(R.id.edt_remark);
        btn_finalSubmit = findViewById(R.id.btn_finalSubmit);
        btn_finalSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String data = edt_remark.getText().toString();
                UploadFeedback(data);

            }
        });
//        Toast.makeText(this, name+phone, Toast.LENGTH_SHORT).show();
    }

    private void UploadFeedback(final String data) {
        final DbHelper dbHelper = new DbHelper(this);
        List<Result> resultList = dbHelper.getAllFeedbackData();
        for (final Result result : resultList) {
            if (Utility.isOnline(this)) {
                pd = new ProgressDialog(this);
                pd.setCancelable(false);
                pd.show();
                pd.getWindow()
                        .setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                pd.setContentView(new ProgressBar(this));
                StringRequest stringRequest = new StringRequest(Request.Method.POST, Contants.SERVICE_BASE_URL + Contants.uploadFeedback,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    pd.dismiss();

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                Intent intent = new Intent(RemarkActivity.this, ThankuActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                                dbHelper.deleteFeedbackData();

                            }
                        },
                        new Response.ErrorListener()

                        {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                try {
                                    pd.dismiss();

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        })

                {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<String, String>();
                        if (name != null) {
                            params.put("name", name);
                        } else {
                            params.put("name", "");

                        }
                        params.put("phone", phone);
                        params.put("remark", data);
                        params.put("questionId", String.valueOf(result.getQuestionId()));
                        params.put("loginId", String.valueOf(loginID));
                        if (result.getFeedbackStatus().equalsIgnoreCase("bad")) {
                            params.put("feedbackStatus", result.getFeedbackStatus());
                            params.put("answerId", String.valueOf(1));
                        }
                        if (result.getFeedbackStatus().equalsIgnoreCase("happy")) {
                            params.put("feedbackStatus", result.getFeedbackStatus());
                            params.put("answerId", String.valueOf(2));

                        }
                        if (result.getFeedbackStatus().equalsIgnoreCase("good")) {
                            params.put("feedbackStatus", result.getFeedbackStatus());
                            params.put("answerId", String.valueOf(3));

                        }
                        if (result.getFeedbackStatus().equalsIgnoreCase("awosome")) {
                            params.put("feedbackStatus", result.getFeedbackStatus());
                            params.put("answerId", String.valueOf(4));

                        }
                        return params;
                    }
                };
                RequestQueue requestQueue = Volley.newRequestQueue(this);
                requestQueue.add(stringRequest);
            } else {

                new SweetAlertDialog(RemarkActivity.this, SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("Sorry...")
                        .setContentText("You are Offline. Please check your Internet Connection.Thank You ")
                        .show();
            }
        }
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        new SweetAlertDialog(RemarkActivity.this, SweetAlertDialog.ERROR_TYPE)
                .setTitleText("Sorry...")
                .setContentText("You Can't Back Please Submit Your Feedback.Thank You ")
                .show();
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
