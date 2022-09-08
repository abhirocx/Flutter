package com.np.namasteyoga.ui.quiz;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.akexorcist.localizationactivity.core.LocalizationActivityDelegate;
import com.akexorcist.localizationactivity.core.OnLocaleChangedListener;
import com.np.namasteyoga.R;

public class QuizThankYouActivity extends AppCompatActivity implements OnLocaleChangedListener {
    TextView score;
    private ImageView img;
    private LocalizationActivityDelegate localizationDelegate =  new LocalizationActivityDelegate(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        localizationDelegate.addOnLocaleChangedListener(this);
        localizationDelegate.onCreate();
        super.onCreate(savedInstanceState);
        int dataStr = getIntent().getIntExtra("Quiz", 0);
        String scroe = getIntent().getStringExtra("Score");

        setContentView(R.layout.activity_quiz_thank_you);
        score = findViewById(R.id.tvScore);
        img = findViewById(R.id.ivThanku);
        score.setText(scroe);
        if (dataStr == 1)
            img.setImageResource(R.drawable.win);
        else
            img.setImageResource(R.drawable.lose);


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                finish();
            }
        }, 3000);
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