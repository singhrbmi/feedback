package fusionsoftware.loop.feedback.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fusionsoftware.loop.feedback.R;
import fusionsoftware.loop.feedback.database.DbHelper;
import fusionsoftware.loop.feedback.model.Result;
import fusionsoftware.loop.feedback.myalert.SweetAlertDialog;
import fusionsoftware.loop.feedback.utility.Contants;
import fusionsoftware.loop.feedback.utility.Utility;


public class AdminDashboardActivity extends AppCompatActivity {
    Button btn_feedback, btn_question, btn_customer, btn_review;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);

        btn_feedback = findViewById(R.id.btn_feedback);
        btn_question = findViewById(R.id.btn_question);
        btn_customer = findViewById(R.id.btn_customer);
        btn_review = findViewById(R.id.btn_review);
        btn_customer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminDashboardActivity.this, FullScreenImageActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

        btn_question.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminDashboardActivity.this, QuestionActivity.class);

                startActivity(intent);
            }
        });

        btn_feedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminDashboardActivity.this, GetAllFeedbackActivity.class);

                startActivity(intent);
            }
        });
        btn_review.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminDashboardActivity.this, GetAllReviewActivity.class);
                startActivity(intent);
            }
        });

    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(AdminDashboardActivity.this, LoginActivity.class));
        finish();
    }

    @Override
    protected void onStart() {
//        updateQuestionStatus();
        super.onStart();
    }

    private void updateQuestionStatus() {
        DbHelper dbHelper = new DbHelper(this);
        List<Result> resultList = dbHelper.getAllQuestionData();
        for (final Result result : resultList) {
            if (Utility.isOnline(this)) {
                StringRequest stringRequest = new StringRequest(Request.Method.POST, Contants.SERVICE_BASE_URL + Contants.updateQuestionStatus,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                            }
                        }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("questionId", String.valueOf(result.getQuestionId()));
                        params.put("questionStatus", String.valueOf(result.getQuestionStatus()));
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
}


