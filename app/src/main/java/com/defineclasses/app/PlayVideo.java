package com.defineclasses.app;

import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Point;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.defineclasses.app.Adapter.All_Course_Adapter;
import com.defineclasses.app.Model.Course_Model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class PlayVideo extends Fragment {
    TextView textView;
    VideoView videoView;
    MediaController mediaController;
    boolean isVisible = true;
    ProgressBar progressBar;
    RelativeLayout mediaLayout;
    ImageButton back, zoomButton, backwardBTN, forwardBTN, playBTN, pauseBTN;
    LinearLayout seekbarLayout;
    SeekBar seekBar;
    double current_pos, total_duration;
    TextView current, total;
    ConstraintLayout fullControllerLayout;
    Handler mHandler, handler;
    LinearLayout heading_layout;
    String videoName;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.play_video_fragment, null);
        back=view.findViewById(R.id.backTo);
        textView = view.findViewById(R.id.play_topic_name);
        videoView = view.findViewById(R.id.topic_video);
        heading_layout = view.findViewById(R.id.heading_layout);
        progressBar = view.findViewById(R.id.progressVideoLoading);
        progressBar.setVisibility(View.VISIBLE);
        fullControllerLayout = view.findViewById(R.id.video_controller_layout);
        playBTN = view.findViewById(R.id.playVideo);
        pauseBTN = view.findViewById(R.id.pauseVideo);
        zoomButton = view.findViewById(R.id.zoomButton);
        seekBar = view.findViewById(R.id.seekbar);
        seekbarLayout = view.findViewById(R.id.seekbarController);
        current = view.findViewById(R.id.current_duration);
        total = view.findViewById(R.id.total_duration);
        mediaLayout = view.findViewById(R.id.mediaController);
        mHandler = new Handler();
        handler = new Handler();
        videoName=getArguments().getString("topic_video");
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.RIGHT;
        params.rightMargin = 80;
        Log.e("Video", getArguments().getString("topic_video"));
        seekbarLayout.setVisibility(View.GONE);

        zoomButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("Message", "Zoom Button");
                if (getActivity().getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
                    getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                    zoomButton.setBackgroundResource(R.drawable.ic_fullscreen_exit);
                    heading_layout.setVisibility(View.GONE);
                } else {
                    getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                    zoomButton.setBackgroundResource(R.drawable.ic_fullscreen);
                    heading_layout.setVisibility(View.VISIBLE);
                }
            }
        });
        Log.e("Course ID Play",getArguments().getString("course_id"));
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getArguments() != null) {
                    Bundle args = new Bundle();
                    args.putString("course_name", getArguments().getString("course_name"));
                    args.putString("course_id", getArguments().getString("course_id"));
                    args.putString("course_description", getArguments().getString("course_description"));
                    args.putString("course_banner", getArguments().getString("course_banner"));
                    args.putString("course_duration", getArguments().getString("course_duration"));
                    args.putString("course_fee", getArguments().getString("course_fee"));
                    args.putString("course_lectures", getArguments().getString("course_lectures"));
                    Fragment myFragment = new Course_Page();
                    myFragment.setArguments(args);
                    getActivity().getSupportFragmentManager()
                            .beginTransaction()
                            .setCustomAnimations(
                                    R.anim.enter_left_to_right,
                                    R.anim.exit_left_to_right,
                                    R.anim.enter_right_to_left,
                                    R.anim.exit_right_to_left)
                            .replace(R.id.fragment_container, myFragment).addToBackStack(null).commit();
                }
            }
        });
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                setVideoProgress();
                mp.start();
                progressBar.setVisibility(View.GONE);
                mediaLayout.setVisibility(View.GONE);
                seekbarLayout.setVisibility(View.GONE);
                mp.setOnInfoListener(new MediaPlayer.OnInfoListener() {
                    @Override
                    public boolean onInfo(MediaPlayer mp, int what, int extra) {
                        if (MediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START == what) {
                            progressBar.setVisibility(View.GONE);
                            mediaLayout.setVisibility(View.VISIBLE);
                            seekbarLayout.setVisibility(View.VISIBLE);
                        }
                        if (MediaPlayer.MEDIA_INFO_BUFFERING_START == what) {
                            progressBar.setVisibility(View.VISIBLE);
                            mediaLayout.setVisibility(View.GONE);
                            seekbarLayout.setVisibility(View.GONE);
                        }
                        if (MediaPlayer.MEDIA_INFO_BUFFERING_END == what) {
                            progressBar.setVisibility(View.GONE);
                            mediaLayout.setVisibility(View.GONE);
                            seekbarLayout.setVisibility(View.GONE);
                        }
                        return false;
                    }
                });
            }
        });
        mediaLayout.setVisibility(View.GONE);
        pauseBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (videoView.isPlaying()) {
                    videoView.pause();
                    pauseBTN.setVisibility(View.GONE);
                    playBTN.setVisibility(View.VISIBLE);
                } else {
                    pauseBTN.setVisibility(View.VISIBLE);
                    playBTN.setVisibility(View.GONE);
                }
            }
        });
        playBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!videoView.isPlaying()) {
                    videoView.start();
                    pauseBTN.setVisibility(View.VISIBLE);
                    playBTN.setVisibility(View.GONE);
                } else {
                    pauseBTN.setVisibility(View.GONE);
                    playBTN.setVisibility(View.VISIBLE);
                }
            }
        });
        playVideo();
        hideLayout();
        return view;
    }

    public void hideLayout() {
        final Handler handler = new Handler();
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                mediaLayout.setVisibility(View.GONE);
                seekbarLayout.setVisibility(View.GONE);
                isVisible = false;
            }
        };

        handler.postDelayed(runnable, 5000);

        fullControllerLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mHandler.removeCallbacks(runnable);
                if (isVisible) {
                    mediaLayout.setVisibility(View.GONE);
                    seekbarLayout.setVisibility(View.GONE);
                    isVisible = false;
                } else {
                    mediaLayout.setVisibility(View.VISIBLE);
                    seekbarLayout.setVisibility(View.VISIBLE);
                    mHandler.postDelayed(runnable, 5000);
                    isVisible = true;
                }
            }
        });

    }

    public void playVideo() {
        try {
            textView.setText(getArguments().getString("topic_name"));
            Log.e("Video Id",getArguments().getString("topic_id"));
            String videoUrl = "https://defineclasses.com//admin/" + getArguments().getString("topic_video");
            Uri video = Uri.parse(videoUrl);
            mediaController = new MediaController(getContext());
            mediaController.setAnchorView(videoView);
            videoView.setVideoURI(video);
            videoView.getRotation();
            videoView.start();
        } catch (Exception e) {
            // TODO: handle exception
            Toast.makeText(getContext(), "Error connecting", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            setPlayerViewLayoutParamsForLandScape();
            heading_layout.setVisibility(View.GONE);
        } else {
            setPlayerViewLayoutParamsForPortrait();
            heading_layout.setVisibility(View.VISIBLE);
        }

    }

    private void setPlayerViewLayoutParamsForLandScape() {
        ConstraintLayout.LayoutParams lp = new ConstraintLayout.LayoutParams(
                ConstraintLayout.LayoutParams.MATCH_PARENT, ConstraintLayout.LayoutParams.MATCH_PARENT);
        zoomButton.setBackgroundResource(R.drawable.ic_fullscreen_exit);
        videoView.setLayoutParams(lp);

    }

    private void setPlayerViewLayoutParamsForPortrait() {
        Display display = getActivity().getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        Double doubleHeight = width / 1.5;
        Integer height = doubleHeight.intValue();

        ConstraintLayout.LayoutParams lp = new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT, height);
        zoomButton.setBackgroundResource(R.drawable.ic_fullscreen);
        videoView.setLayoutParams(lp);
    }

    public void setVideoProgress() {
        //get the video duration
        current_pos = videoView.getCurrentPosition();
        total_duration = videoView.getDuration();
        total.setText(timeConversion((long) total_duration));
        current.setText(timeConversion((long) current_pos));
        //display video duration
        seekBar.setMax((int) total_duration);
        final Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    current_pos = videoView.getCurrentPosition();
                    seekBar.setProgress((int) current_pos);
                    current.setText(timeConversion((long) current_pos));
                    handler.postDelayed(this, 1000);
                } catch (IllegalStateException ed) {
                    ed.printStackTrace();
                }
            }
        };
        handler.postDelayed(runnable, 1000);

        //seekbar change listner
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                current_pos = seekBar.getProgress();
                videoView.seekTo((int) current_pos);
            }
        });
    }

    public String timeConversion(long value) {
        String songTime;
        int dur = (int) value;
        int hrs = (dur / 3600000);
        int mns = (dur / 60000) % 60000;
        int scs = dur % 60000 / 1000;

        if (hrs > 0) {
            songTime = String.format("%02d:%02d:%02d", hrs, mns, scs);
        } else {
            songTime = String.format("%02d:%02d", mns, scs);
        }
        return songTime;
    }
}

