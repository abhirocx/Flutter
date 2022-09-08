package com.np.namasteyoga.ui.eventModule.eventVideo

import android.os.Bundle
import androidx.lifecycle.Lifecycle
import com.np.namasteyoga.R
import com.np.namasteyoga.base.BaseActivity
import com.np.namasteyoga.comman.CommonString
import com.np.namasteyoga.utils.ConstUtility
import com.np.namasteyoga.utils.IntentUtils
import com.np.namasteyoga.utils.Logger
import com.pierfrancescosoffritti.youtubeplayer.player.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.youtubeplayer.player.YouTubePlayer
import com.pierfrancescosoffritti.youtubeplayer.player.YouTubePlayerFullScreenListener
import kotlinx.android.synthetic.main.activity_event_video.*

class EventVideoActivity : BaseActivity<EventVideoViewModel>(EventVideoViewModel::class) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        intent?.run {
            if (hasExtra(IntentUtils.SHARE_STRING)) {
                val videoId = getStringExtra(IntentUtils.SHARE_STRING)
                videoId?.run {
                    Logger.Debug(this)
                    setVideo(this)
                }
            }
        }
    }

    private fun setVideo(s: String) {
        lifecycle.addObserver(youtubePlayerView)
        youtubePlayerView.playerUIController.run {
            showFullscreenButton(true)
            showYouTubeButton(false)
            showPlayPauseButton(true)
            showMenuButton(false)
            showUI(true)
            showCustomAction1(false)
            showVideoTitle(false)
        }

        youtubePlayerView.addFullScreenListener(object :YouTubePlayerFullScreenListener{
            override fun onYouTubePlayerEnterFullScreen() {
                onBackPressed()
            }

            override fun onYouTubePlayerExitFullScreen() {
                onBackPressed()
            }

        })
        youtubePlayerView.initialize({
            it.addListener(object : AbstractYouTubePlayerListener(){
                override fun onReady() {
                    it.play()
                    loadVideo(it, s)
                }
            })

        }, true)
    }

    private fun loadVideo(youTubePlayer: YouTubePlayer, videoId: String) {
        if (lifecycle.currentState == Lifecycle.State.RESUMED)
            youTubePlayer.loadVideo(
                videoId, ConstUtility.duration
            ) else
            youTubePlayer.cueVideo(videoId, ConstUtility.duration)
    }

    override fun layout(): Int = R.layout.activity_event_video

    override fun tag(): String = TAG

    companion object {
        private const val TAG = "EventVideoActivity:: = "
    }

    override val title: String
        get() = CommonString.Empty
    override val isShowTitle: Boolean
        get() = false
}