package com.np.namasteyoga.ui.asana.video;

import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.text.HtmlCompat;
import androidx.lifecycle.Lifecycle;

import com.google.gson.Gson;
import com.np.namasteyoga.R;
import com.np.namasteyoga.datasource.response.AsanaListData;
import com.np.namasteyoga.utils.ConstUtility;
import com.np.namasteyoga.utils.UIUtils;
import com.pierfrancescosoffritti.youtubeplayer.player.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.youtubeplayer.player.YouTubePlayer;
import com.pierfrancescosoffritti.youtubeplayer.player.YouTubePlayerFullScreenListener;
import com.pierfrancescosoffritti.youtubeplayer.player.YouTubePlayerView;

public class AsanaVideoActivity extends AppCompatActivity {

    private AsanaListData asanaData;
    private YouTubePlayer initializedYouTubePlayer;
    private YouTubePlayerView youtubePlayerView;
    private TextView toolbarTitle;
    private  View detailView;
    private  View toolbarHeader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_asana_video);
        UIUtils.INSTANCE.setStatusBarGradient(this,R.color.white);
        TextView tvOverview = findViewById(R.id.tvOverview);
        youtubePlayerView = (YouTubePlayerView) findViewById(R.id.youtube_view);
        toolbarTitle = findViewById(R.id.toolbarTitle);
        ImageView onBackPressedClick = findViewById(R.id.onBackPressedClick);

        detailView = findViewById(R.id.detailView);
        toolbarHeader = findViewById(R.id.toolbarHeader);

//        int orientation = AsanaVideoActivity.this.getResources().getConfiguration().orientation;
//        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
//            detailView.setVisibility(View.GONE);
//        } else {
//            detailView.setVisibility(View.VISIBLE);
//        }

        String dataStr = getIntent().getStringExtra("asanaData");
        Gson gson = new Gson();
        asanaData = gson.fromJson(dataStr, AsanaListData.class);

        getLifecycle().addObserver(youtubePlayerView);

        onBackPressedClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });


        tvOverview.setText(asanaData.getAasana_description());

        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            string.setSpan(new BulletSpan(40, Color.GRAY, 20), 10, 22, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }*/

        TextView tvPractices = findViewById(R.id.tvPractices);
        String practices = " ■ " + asanaData.getAssana_instruction().replaceAll("@", "<br/> ■ ");
        tvPractices.setText(HtmlCompat.fromHtml(practices, HtmlCompat.FROM_HTML_MODE_LEGACY));

        TextView tvBenefits = findViewById(R.id.tvBenefits);
        String benefits = " ■ " + asanaData.getAssana_benifits().replaceAll("@", "<br/> ■ ");
        tvBenefits.setText(HtmlCompat.fromHtml(benefits, HtmlCompat.FROM_HTML_MODE_LEGACY));

        toolbarTitle.setText(asanaData.getAasana_name());
        toolbarTitle.setText(asanaData.getAasana_name());

        UIUtils.INSTANCE.setStatusBarGradient(this, R.color.colorPrimaryDark);

        //  getLifecycle().addObserver(youtubePlayerView);
        init();
        setVideo();

    }

    private void init() {


    }

    private void setVideo() {
        youtubePlayerView.getPlayerUIController().showFullscreenButton(true);
        youtubePlayerView.getPlayerUIController().showYouTubeButton(false);
        youtubePlayerView.getPlayerUIController().showPlayPauseButton(true);
        youtubePlayerView.getPlayerUIController().showMenuButton(false);
        youtubePlayerView.getPlayerUIController().showUI(true);
        youtubePlayerView.getPlayerUIController().showCustomAction1(false);
        youtubePlayerView.getPlayerUIController().showVideoTitle(false);

        youtubePlayerView.initialize(initializedYouTubePlayer -> {
            initializedYouTubePlayer.addListener(new AbstractYouTubePlayerListener() {
                @Override
                public void onReady() {
//                    initializedYouTubePlayer.cueVideo(asanaData.getAssana_video_id(), 0);
//                        initializedYouTubePlayer.cueVideo("9gWxx1ntHTs", 0);
                    initializedYouTubePlayer.play();
                    AsanaVideoActivity.this.initializedYouTubePlayer = initializedYouTubePlayer;
//                    switchLanguage.setEnabled(true);
//                    initializedYouTubePlayer.loadVideo(asanaData.getAssana_video_id(), 0);

                    loadVideo(initializedYouTubePlayer, asanaData.getAssana_video_id());
                }
            });
            addFullScreenListenerToPlayer(initializedYouTubePlayer);




        }, true);
    }

    private void loadVideo(YouTubePlayer youTubePlayer, String videoId) {
        if(getLifecycle().getCurrentState() == Lifecycle.State.RESUMED)
            youTubePlayer.loadVideo(videoId, ConstUtility.duration);
        else
            youTubePlayer.cueVideo(videoId, ConstUtility.duration);
    }

    private void addFullScreenListenerToPlayer(final YouTubePlayer youTubePlayer) {
        youtubePlayerView.addFullScreenListener(new YouTubePlayerFullScreenListener() {
            @Override
            public void onYouTubePlayerEnterFullScreen() {
                //setFullScreen(youtubePlayerView);
//                youtubePlayerView.enterFullScreen();
//                Toast.makeText(AsanaVideoActivity.this, "full", Toast.LENGTH_SHORT).show();
                detailView.setVisibility(View.GONE);
                toolbarHeader.setVisibility(View.GONE);
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

            }

            @Override
            public void onYouTubePlayerExitFullScreen() {
//                Toast.makeText(AsanaVideoActivity.this, "hide", Toast.LENGTH_SHORT).show();
//                setSmallScreen();
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                detailView.setVisibility(View.VISIBLE);
                toolbarHeader.setVisibility(View.VISIBLE);
            }
        });
    }

    /*public void setFullScreen(YouTubePlayerView youTubePlayerView) {

        this.youtubePlayerView = youTubePlayerView;

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        fullScreenManager.enterFullScreen();

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;
        vp.setLayoutParams(new LinearLayout.LayoutParams(width, 10 * width / 16));


//        getSupportActionBar().hide();
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.translucent));
        }

        if (Build.VERSION.SDK_INT > 11 && Build.VERSION.SDK_INT < 19) { // lower api
            View v = this.getWindow().getDecorView();
            v.setSystemUiVisibility(View.GONE);
        } else if (Build.VERSION.SDK_INT >= 19) {
            //for new api versions.
            View decorView = getWindow().getDecorView();
//            int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
//            decorView.setSystemUiVisibility(uiOptions);
//            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
        }
        isFullScreen = true;

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }
*/
    /*public void szetSmallScreen() {

// Show status bar
        if (youtubePlayerView != null)
            youtubePlayerView.exitFullScreen();
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        fullScreenManager.exitFullScreen();


        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;
        vp.setLayoutParams(new LinearLayout.LayoutParams(width, 9 * width / 16));
//        getSupportActionBar().show();
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_ATTACHED_IN_DECOR);
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        }
        isFullScreen = false;
    }*/

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
}