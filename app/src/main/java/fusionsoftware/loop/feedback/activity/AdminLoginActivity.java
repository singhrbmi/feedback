package fusionsoftware.loop.feedback.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.text.Html;
import android.text.InputType;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fusionsoftware.loop.feedback.R;
import fusionsoftware.loop.feedback.database.DbHelper;
import fusionsoftware.loop.feedback.model.ContentData;
import fusionsoftware.loop.feedback.model.Result;
import fusionsoftware.loop.feedback.myalert.SweetAlertDialog;
import fusionsoftware.loop.feedback.utility.Contants;
import fusionsoftware.loop.feedback.utility.FontManager;
import fusionsoftware.loop.feedback.utility.Utility;


public class AdminLoginActivity extends AppCompatActivity {
    private SubscriptionManager mSubscriptionManager;

    public static boolean isMultiSimEnabled = false;
    public static String defaultSimName;

    public static List<SubscriptionInfo> subInfoList;
    public static ArrayList<String> Numbers;
    Button id_bt_login;
    TextView tv_admintic, tv_admin;
    EditText id_et_username, id_et_password;
    CheckBox showCheck;
    LinearLayout layout_admin;
    ProgressDialog pd;
    int role = 1;
    Typeface material;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_login);
        init();
    }

    private void init() {
        material = FontManager.getFontTypefaceMaterialDesignIcons(this, "fonts/materialdesignicons-webfont.otf");
        id_bt_login = findViewById(R.id.id_bt_login);
        tv_admintic = findViewById(R.id.tv_admintic);
        tv_admin = findViewById(R.id.tv_admin);
        id_et_username = findViewById(R.id.id_et_username);
        id_et_password = findViewById(R.id.id_et_password);
        showCheck = findViewById(R.id.show_password);
        layout_admin = findViewById(R.id.layout_admin);
        tv_admintic.setTypeface(material);
        tv_admintic.setText(Html.fromHtml("&#xf12c;"));
        id_bt_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AdminLoginActivity.this, AdminDashboardActivity.class));
                finish();
            }
        });
        layout_admin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layout_admin.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
//                layout_employee.setBackgroundColor(getResources().getColor(R.color.grey_color));
//                tv_emptic.setTextColor(getResources().getColor(R.color.grey_color));
                tv_admin.setTextColor(Color.WHITE);
                tv_admintic.setTextColor(Color.WHITE);
//                tv_employee.setTextColor(getResources().getColor(R.color.black));
                role = 1;
            }
        });
        showCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (showCheck.isChecked()) {
                    id_et_password.setInputType(InputType.TYPE_CLASS_TEXT);
                    id_et_password.setSelection(id_et_password.length());
                } else {
                    id_et_password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    id_et_password.setSelection(id_et_password.length());
                }
            }
        });
    }


    private void adminLoginFunction() {
        DbHelper dbHelper = new DbHelper(this);
        Result result = dbHelper.getAdminData();
        if (result != null) {
            final String adminPhone = id_et_username.getText().toString();
            final String userPass = id_et_password.getText().toString();
            if (adminPhone.length() == 0) {
                id_et_username.setError("Enter  Phone Number ");
            } else if (adminPhone.length() != 10) {
                id_et_username.setError("Enter  Valid Phone");
            } else if (!result.getAdminPhone().equalsIgnoreCase(adminPhone)) {
                id_et_username.setError("Enter  Valid Phone");
            } else if (userPass.length() == 0) {
                id_et_password.setError("Enter password");
            } else if (!userPass.equalsIgnoreCase(result.getAdminPassword())) {
                id_et_password.setError("Enter Valid password");
            } else {
                Intent intent = new Intent(AdminLoginActivity.this, AdminDashboardActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                Toast.makeText(this, "Successfully Login", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void checkRole() {
        if (role == 1) {
            //admin login
            if (Utility.isOnline(this)) {
                pd = new ProgressDialog(this);
                pd.setCancelable(false);
                pd.show();
                pd.getWindow()
                        .setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                pd.setContentView(new ProgressBar(this));
                StringRequest stringRequest = new StringRequest(Request.Method.POST, Contants.SERVICE_BASE_URL + Contants.Admin,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                pd.dismiss();
                                if (!response.equalsIgnoreCase("no")) {
                                    ContentData myPojo = new Gson().fromJson(response, ContentData.class);
                                    if (myPojo != null) {
                                        for (Result result : myPojo.getResult()) {
                                            if (result != null) {
                                                new DbHelper(AdminLoginActivity.this).upsertAdminData(result);
                                                adminLoginFunction();
                                            }
                                        }
                                    }

                                } else {
                                    new SweetAlertDialog(AdminLoginActivity.this, SweetAlertDialog.ERROR_TYPE)
                                            .setTitleText("Sorry...")
                                            .setContentText("Invalid Information")
                                            .show();
                                    id_et_username.setError("Invalid Information");
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

                new SweetAlertDialog(AdminLoginActivity.this, SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("Sorry...")
                        .setContentText("You are Offline. Please check your Internet Connection.Thank You ")
                        .show();
            }
        }
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
