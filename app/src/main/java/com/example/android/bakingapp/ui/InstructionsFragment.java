package com.example.android.bakingapp.ui;

import android.app.Activity;
import android.app.Dialog;
import android.content.pm.ActivityInfo;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.android.bakingapp.R;
import com.example.android.bakingapp.utilities.RecipesData;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;


import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;


public class InstructionsFragment extends Fragment{
    private static final String KEY_VIDEO_URL_STATE = "video_url";
    private static final String KEY_DESCRIPTION_STATE = "description";
    private static final String KEY_FULLSCREEN_STATE = "is_fullscreen";
    private static final String KEY_POSITION_PLAYING_TRACK = "current_video_position";
    private static final String KEY_PLAYING = "is_playing";
    private String mVideoURL;
    private String mDescription;
    private RecipesData.Steps[] mSteps;
    private int mPosition;
    private SimpleExoPlayer mExoPlayer;
    private Dialog mFullScreenDialog;
    private boolean isFullScreen;
    private boolean isPlaying;

    @BindView(R.id.instructions)
    TextView mInstructions;
    @BindView(R.id.playerView)
    SimpleExoPlayerView mPlayerView;
    @BindView(R.id.exo_fullscreen)
    ImageButton mFullScrean;
    @BindView(R.id.exo_fullscreen_exit)
    ImageButton mFullScreenExit;


    public InstructionsFragment() {}

    public void setPosition(int position){
        mPosition = position;
    }

    public void setStepsList(RecipesData.Steps[] steps){
        mSteps = steps;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_instructions, container, false);

        ButterKnife.bind(this, rootView);


        if(savedInstanceState != null) {
            isFullScreen = savedInstanceState.getBoolean(KEY_FULLSCREEN_STATE);
            mVideoURL = savedInstanceState.getString(KEY_VIDEO_URL_STATE);
            mDescription = savedInstanceState.getString(KEY_DESCRIPTION_STATE);
            mInstructions.setText(mDescription);
            initializePlayer(Uri.parse(mVideoURL));
            mExoPlayer.seekTo(savedInstanceState.getLong(KEY_POSITION_PLAYING_TRACK));
            mExoPlayer.setPlayWhenReady(savedInstanceState.getBoolean(KEY_PLAYING));

        }else{
            if(mSteps != null){
                mVideoURL = mSteps[mPosition].getVideoUrl();
                mDescription = mSteps[mPosition].getStepDescription();

                //this add a preview image
                mPlayerView.setDefaultArtwork(BitmapFactory.decodeResource(getResources(), R.drawable.no_video_placeholder));
                mInstructions.setText(mDescription);

                // Initialize the player.
                Timber.d("Position: " + mPosition);
                initializePlayer(Uri.parse(mVideoURL));
                mExoPlayer.setPlayWhenReady(true);
            }
        }

        mFullScrean.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Activity activity = getActivity();
                if (activity != null){
                    isFullScreen = true;
                    if(activity.getResources().getConfiguration().orientation == 2){
                        openFullScreenDialog();
                    }else{
                        activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                    }

                }
            }
        });
        mFullScreenExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Activity activity = getActivity();
                if (activity != null){
                    activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);

                    ((ViewGroup) mPlayerView.getParent()).removeView(mPlayerView);
                    ((FrameLayout) rootView.findViewById(R.id.video_container)).addView(mPlayerView);
                    mFullScreenExit.setVisibility(View.GONE);
                    mFullScrean.setVisibility(View.VISIBLE);
                    mFullScreenDialog.dismiss();

                    isFullScreen = false;
                }
            }
        });
        if(mExoPlayer != null)
        mExoPlayer.addListener(playerListener);
        return rootView;
    }

    /**
     * Save the current state of this fragment
     */
    @Override
    public void onSaveInstanceState(Bundle currentState) {
        Timber.d("onSaveInstanceState: " + mVideoURL);
        currentState.putString(KEY_VIDEO_URL_STATE, mVideoURL);
        currentState.putString(KEY_DESCRIPTION_STATE, mDescription);
        currentState.putBoolean(KEY_FULLSCREEN_STATE, isFullScreen);
        if(mExoPlayer != null){
            currentState.putLong(KEY_POSITION_PLAYING_TRACK, mExoPlayer.getCurrentPosition());
        }
        currentState.putBoolean(KEY_PLAYING, isPlaying);
    }

    /**
     * Initialize ExoPlayer.
     * @param mediaUri The URI of the sample to play.
     */
    private void initializePlayer(Uri mediaUri) {
        if (mExoPlayer == null) {
            // Create an instance of the ExoPlayer.
            TrackSelector trackSelector = new DefaultTrackSelector();
            LoadControl loadControl = new DefaultLoadControl();
            mExoPlayer = ExoPlayerFactory.newSimpleInstance(getContext(), trackSelector, loadControl);
            mPlayerView.setPlayer(mExoPlayer);

            // Prepare the MediaSource.
            String userAgent = Util.getUserAgent(getContext(), getString(R.string.app_name));
            MediaSource mediaSource = new ExtractorMediaSource(mediaUri, new DefaultDataSourceFactory(
                    getContext(), userAgent), new DefaultExtractorsFactory(), null, null);

            mExoPlayer.prepare(mediaSource);
        }
    }


    /**
     * Release ExoPlayer.
     */
    private void releasePlayer() {
        if(mExoPlayer != null){
            mExoPlayer.stop();
            mExoPlayer.release();
            mExoPlayer = null;
            Timber.d("Exoplayer Released");
        }
    }

    /**
     * Release the player when the activity is destroyed.
     */

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        releasePlayer();
    }

    @Override
    public void onResume() {
        super.onResume();
        if(isFullScreen){
            openFullScreenDialog();
        }
    }
    private void openFullScreenDialog(){
        mFullScreenDialog = new Dialog(getContext(), android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        ((ViewGroup) mPlayerView.getParent()).removeView(mPlayerView);
        mFullScreenDialog.addContentView(mPlayerView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        mFullScrean.setVisibility(View.GONE);
        mFullScreenExit.setVisibility(View.VISIBLE);
        mFullScreenDialog.show();
    }

    private Player.EventListener playerListener = new Player.EventListener() {
        @Override
        public void onTimelineChanged(Timeline timeline, Object manifest) {}

        @Override
        public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {}

        @Override
        public void onLoadingChanged(boolean isLoading) {}

        @Override
        public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
            isPlaying = playWhenReady;
        }

        @Override
        public void onRepeatModeChanged(int repeatMode) {}

        @Override
        public void onPlayerError(ExoPlaybackException error) {}

        @Override
        public void onPositionDiscontinuity() {}

        @Override
        public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {}
    };
}
