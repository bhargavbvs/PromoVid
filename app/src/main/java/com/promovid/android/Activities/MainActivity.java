package com.promovid.android.Activities;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import java.text.DateFormat;

import android.support.annotation.Nullable;
import android.text.format.DateUtils;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.promovid.android.App;
import com.promovid.android.Constants;
import com.promovid.android.Entities.YoutubeChannelDetailsEntity;
import com.promovid.android.Entities.YoutubeIdsEntity;
import com.promovid.android.Events.OnVideoIdsDownloadedEvent;
import com.promovid.android.Net.SnucklsClient;
import com.promovid.android.Net.YoutubeClient;
import com.promovid.android.R;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.text.SimpleDateFormat;
import java.util.SimpleTimeZone;
import java.util.concurrent.TimeUnit;
import javax.inject.Inject;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class MainActivity extends YouTubeBaseActivity implements YouTubePlayer.OnInitializedListener {

    @Inject
    SnucklsClient snucklsClient;
    @Inject
    YoutubeClient youtubeClient;

    YoutubeIdsEntity mYoutubeIdsEntity = new YoutubeIdsEntity();
    YoutubeChannelDetailsEntity mYoutubeChannelDetailsEntity = new YoutubeChannelDetailsEntity();
    private static final int RECOVERY_DIALOG_REQUEST = 1;
    EventBus myEventBus = EventBus.getDefault();
    int video_count = 0;

    private YouTubePlayer mYouTubePlayer;
    private YouTubePlayerView youTubeView;
    private TextView textChannelName;
    private TextView textChannelViews;
    private TextView textVideoName;
    private TextView textVideoPostedOn;
    private TextView textProgressBar;
    private Subscription subscription;
    private DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

    int pStatus = 30;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        App.getNetComponent().inject(this);
        setContentView(R.layout.activity_main);

        //setting the id's
        youTubeView = (YouTubePlayerView) findViewById(R.id.youtube_view);
        textChannelName = (TextView) findViewById(R.id.textChannelName);
        textChannelViews = (TextView) findViewById(R.id.textChannelViews);
        textVideoName = (TextView) findViewById(R.id.textVideoName);
        textVideoPostedOn = (TextView) findViewById(R.id.textVideoPostedOn);

        //Getting the youtube videos id's using Retrofit
        provideYoutubeVideoIds();

        //Setting a event listener when the video id's are downloaded
        myEventBus.register(this);

        //Initializing the progress bar to show time
        Resources res = getResources();
        Drawable drawable = res.getDrawable(R.drawable.circular);
        final ProgressBar mProgress = (ProgressBar) findViewById(R.id.circularProgressbar);
        mProgress.setProgress(0);   // Main Progress
        mProgress.setSecondaryProgress(100); // Secondary Progress
        mProgress.setMax(100); // Maximum Progress
        mProgress.setProgressDrawable(drawable);
        textProgressBar = (TextView) findViewById(R.id.textProgressBar);

        // Timer to change the video after every 30 seconds
        subscription = rx.Observable.interval(1, TimeUnit.SECONDS, Schedulers.io())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Long>() {
                    @Override
                    public void call(Long aLong) {
                        if (mYouTubePlayer != null) {
                            pStatus = (30000 - mYouTubePlayer.getCurrentTimeMillis()) / 1000;
                            mProgress.setProgress((pStatus*10)/3);
                            textProgressBar.setText(pStatus +"");
                            if (mYouTubePlayer.getCurrentTimeMillis() > 30000) {
                                video_count++;
                                //Checking if the video_count is less than the actual no.of videos
                                if (video_count < mYoutubeIdsEntity.getData().size()) {
                                    provideYoutubeVideoChannelDetails(video_count);
                                    mYouTubePlayer.loadVideo(mYoutubeIdsEntity.getData().get(video_count));
                                }else {
                                    //Removing subscribers after the video sequence ended.
                                    mYouTubePlayer.release();
                                    subscription.unsubscribe();
                                }
                            }
                        }
                    }
                });
    }

    private void provideYoutubeVideoIds() {
        final Observable<YoutubeIdsEntity> youtubeIds = snucklsClient.getYoutubeIds(Constants.SNUCKLS_KEY);

        youtubeIds.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<YoutubeIdsEntity>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.i("Not Working", "Retrofit is not getting the video details!!");
                    }

                    @Override
                    public void onNext(YoutubeIdsEntity youtubeIdsEntity) {
                        MainActivity.this.mYoutubeIdsEntity = youtubeIdsEntity;
                        EventBus.getDefault().post(new OnVideoIdsDownloadedEvent("Got VideoId's!"));
                        provideYoutubeVideoChannelDetails(video_count);
                    }
                });
    }

    private void provideYoutubeVideoChannelDetails(int count) {
        final Observable<YoutubeChannelDetailsEntity> youtubeChannelDetails = youtubeClient.getYoutubeChannelDetails(Constants.API_KEY, "statistics,snippet",
                mYoutubeIdsEntity.getData().get(count));
        youtubeChannelDetails.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<YoutubeChannelDetailsEntity>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.i("Not Working", "Retrofit is not getting the video details!!");
                    }

                    @Override
                    public void onNext(YoutubeChannelDetailsEntity youtubeChannelDetailsEntity) {
                        mYoutubeChannelDetailsEntity = youtubeChannelDetailsEntity;
                        updateVideoDetailsUI();
                    }
                });
    }

    private void updateVideoDetailsUI() {
        //Setting the channel and video details every time
        textChannelName.setText(mYoutubeChannelDetailsEntity.getItems().get(0).getSnippet().getChannelTitle());
        textVideoPostedOn.setText(parseDate(mYoutubeChannelDetailsEntity.getItems().get(0).getSnippet().getPublishedAt()));
        textVideoName.setText(mYoutubeChannelDetailsEntity.getItems().get(0).getSnippet().getTitle());
        textChannelViews.setText(mYoutubeChannelDetailsEntity.getItems().get(0).getStatistics().getViewCount() + " Views");
    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
        if (!b) {
            this.mYouTubePlayer = youTubePlayer;
            //Loading the first video
            video_count = 0;
            mYouTubePlayer.loadVideo(mYoutubeIdsEntity.getData().get(video_count));
        }
    }

    @Subscribe
    public void onEvent(OnVideoIdsDownloadedEvent event) {
        //Initializing the youtubeView after getting videoId's
        youTubeView.initialize(Constants.API_KEY, this);
    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
        if (youTubeInitializationResult.isUserRecoverableError()) {
            youTubeInitializationResult.getErrorDialog(this, RECOVERY_DIALOG_REQUEST).show();
        } else {
            String errorMessage = String.format(
                    getString(R.string.error_player), youTubeInitializationResult.toString());
            Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RECOVERY_DIALOG_REQUEST) {
            // Retry initialization if user performed a recovery action
            youTubeView.initialize(Constants.API_KEY, this);
        }
    }

    @Nullable
    private CharSequence parseDate(String date) {
        dateFormat.setTimeZone(new SimpleTimeZone(1, "GMT"));
        try {
            return DateUtils.getRelativeTimeSpanString((dateFormat.
                    parse(date).getTime()));
        } catch (java.text.ParseException e) {
            // Should never happen
            e.printStackTrace();
            return null;
        }
    }


    @Override
    protected void onDestroy() {
        //Removing the subscribers
        subscription.unsubscribe();
        super.onDestroy();
    }
}
