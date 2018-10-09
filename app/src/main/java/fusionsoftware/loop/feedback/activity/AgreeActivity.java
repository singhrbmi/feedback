package fusionsoftware.loop.feedback.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;

import fusionsoftware.loop.feedback.R;


public class AgreeActivity extends AppCompatActivity {
    Button bt_ok;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences sharedPreferences = getSharedPreferences("token", MODE_PRIVATE);
        String toen = sharedPreferences.getString("token", null);
        if (toen != null) {
            startActivity(new Intent(AgreeActivity.this, SplashActivity.class));
            finish();
        } else {
            setContentView(R.layout.activity_agree);
            init();
        }

    }

    private void init() {
        final String token = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        bt_ok = findViewById(R.id.bt_ok);
        bt_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sharedPreferences = getSharedPreferences("token", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("token", token);
                editor.apply();
                startActivity(new Intent(AgreeActivity.this, SplashActivity.class));
                finish();
            }
        });
    }
}
