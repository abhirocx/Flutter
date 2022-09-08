package com.np.namasteyoga.ui.quiz;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.akexorcist.localizationactivity.core.LocalizationActivityDelegate;
import com.akexorcist.localizationactivity.core.OnLocaleChangedListener;
import com.google.gson.Gson;
import com.np.namasteyoga.R;
import com.np.namasteyoga.datasource.response.Datum;
import com.np.namasteyoga.ui.quiz.adapter.QuizQuestionPagerAdapter;

public class QuizActivity extends AppCompatActivity implements OnLocaleChangedListener {
    TextView toolbarTitle;
    Datum pollData;
    ViewPager viewPager;
    QuizQuestionPagerAdapter mCustomPagerAdapter;
    private Handler customHandler = new Handler();
    private long startTime = 0L;
    long timeInMilliseconds = 0L;
    long timeSwapBuff = 0L;
    long updatedTime = 0L;
    private int secs;
    private int mins;

    private LocalizationActivityDelegate localizationDelegate =  new LocalizationActivityDelegate(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        localizationDelegate.addOnLocaleChangedListener(this);
        localizationDelegate.onCreate();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        viewPager = findViewById(R.id.questionviewPager);

        ImageView onBackPressedClick = findViewById(R.id.onBackPressedClick);
        onBackPressedClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        toolbarTitle = findViewById(R.id.toolbarTitle);
        toolbarTitle.setText(R.string.quiz);
        String dataStr = getIntent().getStringExtra("PollData");
        Gson gson = new Gson();
        pollData = gson.fromJson(dataStr, Datum.class);

        mCustomPagerAdapter = new QuizQuestionPagerAdapter(QuizActivity.this, QuizActivity.this, pollData.getQuestionData(), pollData.getQuiz_id(), viewPager);
        viewPager.setAdapter(mCustomPagerAdapter);

        startTime = SystemClock.uptimeMillis();
        customHandler.postDelayed(updateTimerThread, 0);

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return false;
    }


    private Runnable updateTimerThread = new Runnable() {

        public void run() {

            timeInMilliseconds = SystemClock.uptimeMillis() - startTime;

            updatedTime = timeSwapBuff + timeInMilliseconds;

            secs = (int) (updatedTime / 1000);
            mins = secs / 60;
            secs = secs % 60;
            int milliseconds = (int) (updatedTime % 1000);
            customHandler.postDelayed(this, 0);
        }

    };

    public int getTime(){

        return secs;
    }


    @Override
    protected void onResume() {
        super.onResume();
        localizationDelegate.onResume(this);
    }
    @Override
    protected void attachBaseContext(Context newBase) {
        applyOverrideConfiguration(localizationDelegate.updateConfigurationLocale(newBase));
        super.attachBaseContext(newBase);
    }

    @Override
    public Context getApplicationContext() {
        return localizationDelegate.getApplicationContext(super.getApplicationContext());
    }

    @Override
    public Resources getResources() {
        return localizationDelegate.getResources(super.getResources());
    }

    @Override
    public void onAfterLocaleChanged() {

    }

    @Override
    public void onBeforeLocaleChanged() {

    }
}