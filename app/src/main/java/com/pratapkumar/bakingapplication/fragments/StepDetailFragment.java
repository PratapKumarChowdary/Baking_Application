

package com.pratapkumar.bakingapplication.fragments;

import android.annotation.SuppressLint;

import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.pratapkumar.bakingapplication.R;
import com.pratapkumar.bakingapplication.models.Recipe;
import com.pratapkumar.bakingapplication.models.Step;
import com.pratapkumar.bakingapplication.utilities.Constant;
import com.squareup.picasso.Picasso;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.pratapkumar.bakingapplication.utilities.Constant.FAST_FORWARD_INCREMENT;
import static com.pratapkumar.bakingapplication.utilities.Constant.PLAYER_PLAYBACK_SPEED;
import static com.pratapkumar.bakingapplication.utilities.Constant.REWIND_INCREMENT;
import static com.pratapkumar.bakingapplication.utilities.Constant.START_POSITION;


public class StepDetailFragment extends Fragment implements Player.EventListener {


    private Step mStep;
    private int mStepIndex;
    private Recipe mRecipe;

    private String mVideoUrl;
    private String mThumbnailUrl;
    private boolean mHasVideoUrl = false;

    private SimpleExoPlayer mExoPlayer;
    private PlaybackStateCompat.Builder mStateBuilder;
    private static MediaSessionCompat sMediaSession;

    private static final String TAG = StepDetailFragment.class.getSimpleName();

    private long mPlaybackPosition;

    private int mCurrentWindow;

    private boolean mPlayWhenReady;

    @Nullable
    @BindView(R.id.bt_previous)
    ImageButton btPrevious;

    @Nullable
    @BindView(R.id.tv_step_id)
    TextView tvStepId;

    @Nullable
    @BindView(R.id.bt_next)
    ImageButton btNext;

    @Nullable
    @BindView(R.id.tv_description)
    TextView tvDescription;

    @Nullable
    @BindView(R.id.iv_empty)
    ImageView ivEmpty;

    @Nullable
    @BindView(R.id.player_view)
    PlayerView playerView;


    public StepDetailFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_step_detail, container, false);
        ButterKnife.bind(StepDetailFragment.this,view);

        if (savedInstanceState != null) {
            mStep = savedInstanceState.getParcelable(Constant.SAVE_STEP);
            mStepIndex = savedInstanceState.getInt(Constant.STATE_STEP_INDEX);
            mPlaybackPosition = savedInstanceState.getLong(Constant.STATE_PLAYBACK_POSITION);
            mCurrentWindow = savedInstanceState.getInt(Constant.STATE_CURRENT_WINDOW);
            mPlayWhenReady = savedInstanceState.getBoolean(Constant.STATE_PLAY_WHEN_READY);
        } else {
            mCurrentWindow = C.INDEX_UNSET;
            mPlaybackPosition = C.TIME_UNSET;
            mPlayWhenReady = true;
        }


        if(mStep != null) {
            setCorrectDescription(mStep);
            handleMediaUrl();

        } else {

        }

        initializeMediaSession();

        getRecipeData();

        onNextButtonClick();
        onPreviousButtonClick();
        setStepId();
        hideButtonAtBeginningEnd();

        return view;
    }

    private void initializeMediaSession() {

        // Create a MediaSessionCompat
        sMediaSession = new MediaSessionCompat(Objects.requireNonNull(getContext()), TAG);

        // Enable callbacks from MediaButtons and TransportControls
        sMediaSession.setFlags(
                MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS |
                        MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS);

        // Do not let MediaButtons restart the player when the app is not visible
        sMediaSession.setMediaButtonReceiver(null);

        // Set an initial PlaybackState with ACTION_PLAY, so media buttons can start the player
        mStateBuilder = new PlaybackStateCompat.Builder()
                .setActions(
                        PlaybackStateCompat.ACTION_PLAY |
                                PlaybackStateCompat.ACTION_PAUSE |
                                PlaybackStateCompat.ACTION_REWIND |
                                PlaybackStateCompat.ACTION_FAST_FORWARD |
                                PlaybackStateCompat.ACTION_PLAY_PAUSE);

        sMediaSession.setPlaybackState(mStateBuilder.build());

        // MySessionCallback has methods that handle callbacks from a media controller
        sMediaSession.setCallback(new MySessionCallback());

        // Start the Media Session since the fragment is active
        sMediaSession.setActive(true);
    }


    private String getDescriptionWithCorrectStepId(Step step) {
        int stepId = step.getmId();
        String description = step.getmDescription();

        if (stepId != mStepIndex) {
            stepId = mStepIndex;

            int periodIndex = description.indexOf(getString(R.string.period));
            description = stepId + description.substring(periodIndex);
            step.setmDescription(description);
        }
        return description;
    }

    private void setCorrectDescription(Step step) {
        String descriptionWithCorrectStepId = getDescriptionWithCorrectStepId(step);
        String replacedDescription = replaceString(descriptionWithCorrectStepId);
        tvDescription.setText(replacedDescription);
    }


    private void onNextButtonClick() {
       btNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StepDetailFragment stepDetailFragment = new StepDetailFragment();
                if (mStepIndex < mRecipe.getmSteps().size() - 1) {
                    mStepIndex++;
                    stepDetailFragment.setStep(mRecipe.getmSteps().get(mStepIndex));
                    stepDetailFragment.setStepIndex(mStepIndex);
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    fragmentManager.beginTransaction()
                            .replace(R.id.step_detail_container, stepDetailFragment)
                            .commit();
                }
            }
        });
    }


    private void onPreviousButtonClick() {
      btPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StepDetailFragment stepDetailFragment = new StepDetailFragment();
                if (mStepIndex > 0) {
                    mStepIndex--;
                    stepDetailFragment.setStep(mRecipe.getmSteps().get(mStepIndex));
                    stepDetailFragment.setStepIndex(mStepIndex);
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    fragmentManager.beginTransaction()
                            .replace(R.id.step_detail_container, stepDetailFragment)
                            .commit();
                }
            }
        });
    }


    private void hideButtonAtBeginningEnd() {
        int lastStep = mRecipe.getmSteps().size() -1;
        if (mStepIndex == lastStep) {
           btNext.setVisibility(View.INVISIBLE);
        } else if (mStepIndex == 0) {
           btPrevious.setVisibility(View.INVISIBLE);
        }
    }


    private void setStepId() {
        if (!isTwoPane()) {
            int lastStep = mRecipe.getmSteps().size() - 1;
            String currentStepOfLast = getString(R.string.step) + String.valueOf(mStepIndex)
                    + getString(R.string.space) + getString(R.string.of)
                    + getString(R.string.space) + String.valueOf(lastStep);
            tvStepId.setText(currentStepOfLast);
        }
    }


    private void getRecipeData() {
        Intent intent = getActivity().getIntent();
        if (intent != null) {
            if (intent.hasExtra(Constant.RECIPE)) {
                // Get the recipe from the intent
                Bundle b = intent.getBundleExtra(Constant.RECIPE);
                mRecipe = b.getParcelable(Constant.RECIPE);
            }
        }
    }


    private void handleMediaUrl() {
        mVideoUrl = mStep.getmVideoUrl();
        mThumbnailUrl = mStep.getmVideoUrl();

        if (mThumbnailUrl.contains(getResources().getString(R.string.mp4))) {
            mVideoUrl = mThumbnailUrl;
        }

        if (!mVideoUrl.isEmpty()) {
            mHasVideoUrl = true;
        } else if (!mThumbnailUrl.isEmpty()) {
         playerView.setVisibility(View.GONE);
            Picasso.get()
                    .load(mThumbnailUrl)
                    .error(R.drawable.cake)
                    .placeholder(R.drawable.cake)
                    .into(ivEmpty);
        } else {
           playerView.setVisibility(View.GONE);
           ivEmpty.setImageResource(R.drawable.cake);
        }
    }


    public void setStep(Step step) {
        mStep = step;
    }


    public void setStepIndex(int stepIndex) {
        mStepIndex = stepIndex;
    }


    private void initializePlayer(boolean hasVideoUrl) {
        // Check if the step of the recipe has a video
        if (hasVideoUrl) {
            if (mExoPlayer == null) {
                DefaultRenderersFactory defaultRenderersFactory = new DefaultRenderersFactory(getContext());
                TrackSelector trackSelector = new DefaultTrackSelector();
                LoadControl loadControl = new DefaultLoadControl();
                mExoPlayer = ExoPlayerFactory.newSimpleInstance(
                        defaultRenderersFactory, trackSelector, loadControl);

               playerView.setPlayer(mExoPlayer);

               mExoPlayer.setPlayWhenReady(mPlayWhenReady);
            }

            mExoPlayer.addListener(this);

            Uri mediaUri = Uri.parse(mVideoUrl);
            MediaSource mediaSource = buildMediaSource(mediaUri);

            boolean haveStartPosition = mCurrentWindow != C.INDEX_UNSET;
            if (haveStartPosition) {
                mExoPlayer.seekTo(mCurrentWindow, mPlaybackPosition);
            }
            mExoPlayer.prepare(mediaSource, !haveStartPosition, false);
        }
    }


    private MediaSource buildMediaSource(Uri mediaUri) {
        String userAgent = Util.getUserAgent(this.getContext(), getString(R.string.app_name));
        return new ExtractorMediaSource.Factory(new DefaultHttpDataSourceFactory(userAgent))
                .createMediaSource(mediaUri);
    }

    @Override
    public void onStart() {
        super.onStart();
        if (Util.SDK_INT > Build.VERSION_CODES.M) {
            initializePlayer(mHasVideoUrl);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        hideSystemUi();
        if (Util.SDK_INT <= Build.VERSION_CODES.M || mExoPlayer == null) {
            initializePlayer(mHasVideoUrl);
        }
    }


    private boolean isTwoPane() {
       return tvStepId == null;
    }


    private boolean isSinglePaneLand() {
        return getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE
                && !isTwoPane();
    }


    @SuppressLint("InlinedApi")
    private void hideSystemUi() {
        if (isSinglePaneLand()) {
            int flagFullScreen = View.SYSTEM_UI_FLAG_LOW_PROFILE
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;

           playerView.setSystemUiVisibility(flagFullScreen);
           ivEmpty.setSystemUiVisibility(flagFullScreen);
        }
    }

    @Override
    public void onPause() {
        super.onPause();

        if (Util.SDK_INT <= Build.VERSION_CODES.M) {
            releasePlayer();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (Util.SDK_INT > Build.VERSION_CODES.M) {
            releasePlayer();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        sMediaSession.setActive(false);
    }


    private void releasePlayer() {
        if (mExoPlayer != null) {
            updateCurrentPosition();
            mExoPlayer.release();
            mExoPlayer = null;
        }
    }


    private void updateCurrentPosition() {
        if (mExoPlayer != null) {
            mPlaybackPosition = mExoPlayer.getCurrentPosition();
            mCurrentWindow = mExoPlayer.getCurrentWindowIndex();
            mPlayWhenReady = mExoPlayer.getPlayWhenReady();
        }
    }


    private String replaceString(String target) {
        if (target.contains(getString(R.string.question_mark))) {
            target = target.replace(getString(R.string.question_mark), getString(R.string.degree));
        }
        return target;
    }

    private void hideButtonWhenPlaying() {
        if (isTwoPane() | isSinglePaneLand()){
           btPrevious.setVisibility(View.GONE);
           btNext.setVisibility(View.GONE);
        }
    }


    private void showButtonWhenPausedEnded() {
        if (isTwoPane() | isSinglePaneLand()) {
         btPrevious.setVisibility(View.VISIBLE);
         btNext.setVisibility(View.VISIBLE);
        }
        hideButtonAtBeginningEnd();
    }


    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(Constant.SAVE_STEP, mStep);
        outState.putInt(Constant.STATE_STEP_INDEX, mStepIndex);

        updateCurrentPosition();
        outState.putLong(Constant.STATE_PLAYBACK_POSITION, mPlaybackPosition);
        outState.putInt(Constant.STATE_CURRENT_WINDOW, mCurrentWindow);
        outState.putBoolean(Constant.STATE_PLAY_WHEN_READY, mPlayWhenReady);
    }


    @Override
    public void onTimelineChanged(Timeline timeline, Object manifest, int reason) {

    }

    @Override
    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

    }

    @Override
    public void onLoadingChanged(boolean isLoading) {

    }


    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
        if (playbackState == Player.STATE_READY && playWhenReady) {
            mStateBuilder.setState(PlaybackStateCompat.STATE_PLAYING,
                    mExoPlayer.getCurrentPosition(), PLAYER_PLAYBACK_SPEED);

            hideButtonWhenPlaying();
        } else if (playbackState == Player.STATE_READY) {
            mStateBuilder.setState(PlaybackStateCompat.STATE_PAUSED,
                    mExoPlayer.getCurrentPosition(), PLAYER_PLAYBACK_SPEED);

            showButtonWhenPausedEnded();
        } else if (playbackState == Player.STATE_ENDED) {
            showButtonWhenPausedEnded();
        }
        sMediaSession.setPlaybackState(mStateBuilder.build());

    }

    @Override
    public void onRepeatModeChanged(int repeatMode) {

    }

    @Override
    public void onShuffleModeEnabledChanged(boolean shuffleModeEnabled) {

    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {

    }

    @Override
    public void onPositionDiscontinuity(int reason) {

    }

    @Override
    public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {

    }

    @Override
    public void onSeekProcessed() {

    }

    private class MySessionCallback extends MediaSessionCompat.Callback {
        @Override
        public void onPlay() {
            mExoPlayer.setPlayWhenReady(true);
        }

        @Override
        public void onPause() {
            mExoPlayer.setPlayWhenReady(false);
        }

        @Override
        public void onRewind() {
            mExoPlayer.seekTo(Math.max(mExoPlayer.getCurrentPosition() - REWIND_INCREMENT, START_POSITION));
        }

        @Override
        public void onFastForward() {
            long duration = mExoPlayer.getDuration();
            mExoPlayer.seekTo(Math.min(mExoPlayer.getCurrentPosition() + FAST_FORWARD_INCREMENT, duration));
        }
    }
}
