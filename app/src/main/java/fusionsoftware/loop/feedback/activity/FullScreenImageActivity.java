package fusionsoftware.loop.feedback.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Timer;
import java.util.TimerTask;

import fusionsoftware.loop.feedback.R;
import fusionsoftware.loop.feedback.adapter.MyAdapter;
import fusionsoftware.loop.feedback.viewpagerindicator.CirclePageIndicator;
import me.relex.circleindicator.CircleIndicator;

public class FullScreenImageActivity extends AppCompatActivity {
    Button btn_proceed;
    private ViewPager mPager;
    private static int currentPage = 0;
    private static Integer[] XMEN = {R.drawable.images13, R.drawable.images14, R.drawable.images23, R.drawable.images15, R.drawable.images};
    private ArrayList<Integer> XMENArray = new ArrayList<Integer>();
    CirclePageIndicator circlePageIndicator;
    private static int NUM_PAGES = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_screen_image);
        init();
        viewPagerSetUp();
    }

    private void viewPagerSetUp() {
        mPager = findViewById(R.id.pager);
        circlePageIndicator = findViewById(R.id.indicator);
        for (Integer aXMEN : XMEN) {
            XMENArray.addAll(Collections.singletonList(aXMEN));
        }
        NUM_PAGES = XMENArray.size();
        if (XMENArray != null && XMENArray.size() > 0) {
            mPager.setAdapter(new MyAdapter(this, XMENArray));
            circlePageIndicator.setViewPager(mPager);
        }
        final float density = getResources().getDisplayMetrics().density;

//Set circle indicator radius
        circlePageIndicator.setRadius(5 * density);
        // Auto start of viewpager
        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {
                if (currentPage == NUM_PAGES) {
                    currentPage = 0;
                }
                mPager.setCurrentItem(currentPage++, true);
            }
        };
        Timer swipeTimer = new Timer();
        swipeTimer.schedule(new

                                    TimerTask() {
                                        @Override
                                        public void run() {
                                            handler.post(Update);
                                        }
                                    }, 1500, 1500);

        // Pager listener over indicator
        circlePageIndicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener()

        {

            @Override
            public void onPageSelected(int position) {
                currentPage = position;

            }

            @Override
            public void onPageScrolled(int pos, float arg1, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int pos) {

            }
        });
    }

    private void init() {
        btn_proceed = findViewById(R.id.btn_proceed);
        btn_proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FullScreenImageActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }
}