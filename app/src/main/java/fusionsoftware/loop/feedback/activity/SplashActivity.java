package fusionsoftware.loop.feedback.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

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
import fusionsoftware.loop.feedback.framework.IAsyncWorkCompletedCallback;
import fusionsoftware.loop.feedback.framework.ServiceCaller;
import fusionsoftware.loop.feedback.model.ContentData;
import fusionsoftware.loop.feedback.model.Result;
import fusionsoftware.loop.feedback.myalert.SweetAlertDialog;
import fusionsoftware.loop.feedback.utility.Contants;
import fusionsoftware.loop.feedback.utility.Utility;


public class SplashActivity extends AppCompatActivity {
//    ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        requestWindowFeature(Window.FEATURE_ACTION_BAR);// Removes action bar
//        requestWindowFeature(Window.FEATURE_NO_TITLE);// Removes title bar
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);
        RotateAnimation rotate = new RotateAnimation(0, 180, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotate.setDuration(1000);
        rotate.setInterpolator(new LinearInterpolator());

        ImageView image = (ImageView) findViewById(R.id.splash);
        image.startAnimation(rotate);
        TextView tv_welcome = findViewById(R.id.tv_welcome);
        TextView tv_bottom = findViewById(R.id.tv_bottom);
//        Animation animation = AnimationUtils.loadAnimation(this, R.anim.slide_in_top);
        Animation bottomAmin = AnimationUtils.loadAnimation(this, R.anim.slide_in_bottom);
//        tv_welcome.startAnimation(animation);
        tv_bottom.startAnimation(bottomAmin);
       getAllQuestions();
        moveNext();
    }

    public void getAllQuestions() {
        if (Utility.isOnline(this)) {
            ServiceCaller serviceCaller = new ServiceCaller(this);
            serviceCaller.callgetQuestionData(new IAsyncWorkCompletedCallback() {
                @Override
                public void onDone(String workName, boolean isComplete) {

                }
            });
        }
    }


    private void moveNext() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        }, 2000);
    }
}