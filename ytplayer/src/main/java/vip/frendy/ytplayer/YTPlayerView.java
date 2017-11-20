package vip.frendy.ytplayer;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.SeekBar;

import vip.frendy.ytplayer.extension.HandlerExt;
import vip.frendy.ytplayer.interfaces.IYTJSListener;
import vip.frendy.ytplayer.model.PlaylistItems;

import static vip.frendy.ytplayer.Contants.DEBUG;

/**
 * Created by frendy on 2017/11/17.
 */

public class YTPlayerView<T> extends LinearLayout implements IYTJSListener, View.OnClickListener {
    private static String TAG = "YTPlayerView";

    private LinearLayout mButtonLayout;
    private Button mLoad, mStop, mClear;
    private ImageButton mPlayPause;
    private PlayerState mState = PlayerState.ENDED;
    private enum PlayerState {
        ENDED, PLAYING, PAUSED, BUFFERING, CUED
    };

    private final static int MAX = 1000;
    private SeekBar mSeekBar;
    private float totalVideoDuration;

    protected YTWebView mWebView;
    //播放单个视频
    private String mVideoId = "";

    public YTPlayerView(Context context) {
        super(context);
        init(context);
    }

    public YTPlayerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public YTPlayerView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    // 继承时可替换布局
    protected int getLayoutResId() {
        return R.layout.yt_player_view;
    }

    protected void init(Context context) {
        LayoutInflater.from(context).inflate(getLayoutResId(), this);

        mButtonLayout = findViewById(R.id.buttonLayout);
        mLoad = findViewById(R.id.load);
        mLoad.setOnClickListener(this);
        mPlayPause = findViewById(R.id.play_pause);
        mPlayPause.setOnClickListener(this);
        mStop = findViewById(R.id.stop);
        mStop.setOnClickListener(this);
        mClear = findViewById(R.id.clear);
        mClear.setOnClickListener(this);

        mSeekBar = findViewById(R.id.seek_bar);
        mSeekBar.setMax(MAX);

        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                int progress = seekBar.getProgress();
                double secs = (progress * totalVideoDuration) / 1000;
                secs = Math.ceil(secs);
                mWebView.seekVideo(secs);

                if(DEBUG) Log.d(TAG, "onStopTrackingTouch :: progress = " + progress +  "-- secs = " + secs);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                if(DEBUG) Log.d(TAG, "onStartTrackingTouch :: progress = " + seekBar.getProgress() );
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }
        });

        mWebView = findViewById(R.id.web_view);
        mWebView.init(this);
    }

    public void setVideoId(String id) {
        mVideoId = id;
    }

    public void loadDefault() {
        mWebView.loadDefault(mVideoId);
    }

    // 展开
    public void rollout() {
        mSeekBar.setVisibility(VISIBLE);
        mButtonLayout.setVisibility(VISIBLE);
    }

    // 收起
    public void rollup() {
        mSeekBar.setVisibility(GONE);
        mButtonLayout.setVisibility(GONE);
    }

    public boolean isRollup() {
        return mButtonLayout.getVisibility() == GONE;
    }


    @Override
    public void onYouTubeIframeAPIReady() {

    }

    @Override
    public void updateVideoDuration(String duration) {
        try {
            changeSeekBar(Float.parseFloat(duration));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateTotalVideoDuration(String duration) {
        try {
            totalVideoDuration = Float.parseFloat(duration);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onVideoEnd() {
        mState = PlayerState.ENDED;
        setSeekBar("ENDED");
    }

    @Override
    public void onVideoPlaying() {
        mState = PlayerState.PLAYING;
        updatePlayPuaseButton(mState);
    }

    @Override
    public void onVideoPaused() {
        mState = PlayerState.PAUSED;
        updatePlayPuaseButton(mState);
    }

    @Override
    public void onVideoBuffering() {
        mState = PlayerState.BUFFERING;
    }

    @Override
    public void onVideoCued() {
        mState = PlayerState.CUED;
    }


    private void changeSeekBar(float time){
        float progress =  (time/totalVideoDuration) * 1000;
        final double d = Math.ceil(progress);

        if(DEBUG) Log.d(TAG, "changeSlider progress = " + d);

        HandlerExt.postToUI(new Runnable() {
            @Override
            public void run() {
                mSeekBar.setProgress((int)d);
            }
        });
    }

    private void setSeekBar(final String flag){
        HandlerExt.postToUI(new Runnable() {
            @Override
            public void run() {
                if(flag.equals("ENDED"))
                    mSeekBar.setProgress(MAX);
            }
        });
    }

    private void updatePlayPuaseButton(final PlayerState state) {
        if(mPlayPause == null) return;

        HandlerExt.postToUI(new Runnable() {
            @Override
            public void run() {
                if(state == PlayerState.PLAYING) {
                    mPlayPause.setImageResource(android.R.drawable.ic_media_pause);
                } else if(state == PlayerState.PAUSED) {
                    mPlayPause.setImageResource(android.R.drawable.ic_media_play);
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.load && mVideoId != null) {
            mWebView.loadDefault(mVideoId);
        } else if(view.getId() == R.id.play_pause) {
            if(mState == PlayerState.PLAYING || mState == PlayerState.BUFFERING) {
                mWebView.pauseVideo();
            } else {
                mWebView.playVideo();
            }
        } else if(view.getId() == R.id.stop) {
            mWebView.stopVideo();
        } else if(view.getId() == R.id.clear) {
            mWebView.clearVideo();
        }
    }

    protected String getVideoId(T video) {
        if(video instanceof String) {
            return (String) video;
        } else if(video instanceof PlaylistItems) {
            return ((PlaylistItems) video).getSnippet().getResourceId().getVideoId();
        } else {
            return "";
        }
    }
}
