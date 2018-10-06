package fusionsoftware.loop.feedback.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.inputmethodservice.InputMethodService;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
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
import java.util.Map;

import fusionsoftware.loop.feedback.R;
import fusionsoftware.loop.feedback.database.DbHelper;
import fusionsoftware.loop.feedback.model.ContentData;
import fusionsoftware.loop.feedback.model.Result;
import fusionsoftware.loop.feedback.myalert.SweetAlertDialog;
import fusionsoftware.loop.feedback.utility.Contants;
import fusionsoftware.loop.feedback.utility.Utility;


public class LoginActivity extends AppCompatActivity {
    Button btn_loginButton;
    EditText edt_name, edt_number;
    TextView tv_customerDetails;
    TextInputLayout layout_phone;
    ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        btn_loginButton = findViewById(R.id.btn_loginButton);
        edt_name = findViewById(R.id.edt_name);
        edt_number = findViewById(R.id.edt_number);
        layout_phone = findViewById(R.id.layout_phone);
//        tv_customerDetails = findViewById(R.id.tv_customerDetails);
        btn_loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validation()) {
                    if (Utility.isOnline(LoginActivity.this)) {
                        pd = new ProgressDialog(LoginActivity.this);
                        pd.setCancelable(false);
                        pd.show();
                        pd.getWindow()
                                .setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                        pd.setContentView(new ProgressBar(LoginActivity.this));
                        StringRequest stringRequest = new StringRequest(Request.Method.POST, Contants.SERVICE_BASE_URL + Contants.User,
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        pd.dismiss();
                                        if (!response.equalsIgnoreCase("no")) {
                                            ContentData myPojo = new Gson().fromJson(response, ContentData.class);
                                            if (myPojo != null) {
                                                for (Result result : myPojo.getResult()) {
                                                    if (result != null) {
                                                        Intent intent = new Intent(LoginActivity.this, DashboardActivity.class);
                                                        intent.putExtra("loginId", result.getLoginId());
                                                        intent.putExtra("name", result.getName());
                                                        intent.putExtra("phone", result.getPhone());
                                                        startActivity(intent);
                                                        edt_name.setText("");
                                                        edt_number.setText("");
                                                    }
                                                }
                                            }

                                        } else {
                                            new SweetAlertDialog(LoginActivity.this, SweetAlertDialog.ERROR_TYPE)
                                                    .setTitleText("Sorry...")
                                                    .setContentText("Invalid Information")
                                                    .show();
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
                                if (edt_name.getText().toString() != null) {
                                    params.put("name", edt_name.getText().toString());
                                } else {
                                    params.put("name", "");

                                }
                                params.put("phone", edt_number.getText().toString());
                                return params;
                            }
                        };
                        RequestQueue requestQueue = Volley.newRequestQueue(LoginActivity.this);
                        requestQueue.add(stringRequest);
                    } else {

                        new SweetAlertDialog(LoginActivity.this, SweetAlertDialog.ERROR_TYPE)
                                .setTitleText("Sorry...")
                                .setContentText("You are Offline. Please check your Internet Connection.Thank You ")
                                .show();
                    }
                }
            }
        });


    }

    boolean validation() {
        String phone = edt_number.getText().toString();
        if (phone.length() == 0) {
            edt_number.setError("Enter Number");
            requestFocus(edt_number);
            return false;
        } else if (phone.length() != 10) {
            edt_number.setError("Enter Valid Number");
            requestFocus(edt_number);
            return false;
        }
        return true;

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_admin) {
            startActivity(new Intent(LoginActivity.this, AdminLoginActivity.class));
            finish();

        }
        if (id == R.id.action_help) {
            startActivity(new Intent(LoginActivity.this, WebviewSiteActivity.class));

        }
        return super.onOptionsItemSelected(item);
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

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }
}
