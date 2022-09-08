package com.np.namasteyoga.ui.poll;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
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
import com.np.namasteyoga.ui.poll.adapter.QuestionPagerAdapter;

public class PollActivity extends AppCompatActivity implements OnLocaleChangedListener {


    TextView toolbarTitle;
    Datum pollData;
    ViewPager viewPager;
    QuestionPagerAdapter mCustomPagerAdapter;
    private LocalizationActivityDelegate localizationDelegate =  new LocalizationActivityDelegate(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        localizationDelegate.addOnLocaleChangedListener(this);
        localizationDelegate.onCreate();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_poll);
        viewPager = findViewById(R.id.questionviewPager);

        ImageView onBackPressedClick = findViewById(R.id.onBackPressedClick);
        onBackPressedClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        toolbarTitle = findViewById(R.id.toolbarTitle);
        toolbarTitle.setText(R.string.poll);
        String dataStr = getIntent().getStringExtra("PollData");
        Gson gson = new Gson();
        pollData = gson.fromJson(dataStr, Datum.class);

        mCustomPagerAdapter = new QuestionPagerAdapter(PollActivity.this, PollActivity.this, pollData.getQuestionData(), pollData.getPollId(), viewPager);
        viewPager.setAdapter(mCustomPagerAdapter);

    }

    @Override
    protected void onResume() {
        super.onResume();
        localizationDelegate.onResume(this);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return false;
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