package com.graphic.main;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.IBinder;
import android.widget.Toast;

public class MusicService extends Service {
    private final String TAG = "MusicService";

    private static final int NOTIFICATION_ID = 1;
    private MediaPlayer mPlayer;
    private int mStartID;

    HeadsetIntentReceiver receiver = new HeadsetIntentReceiver();

    @Override
    public void onCreate() {
        super.onCreate();

        mPlayer = MediaPlayer.create(this, R.raw.project_theme);

        if (null != mPlayer) {
            mPlayer.setLooping(true);

            IntentFilter receiverFilter = new IntentFilter(
                    Intent.ACTION_HEADSET_PLUG);
            registerReceiver(receiver, receiverFilter);
        }

        // Create a notification area notification so the user
        // can get back to the MusicServiceClient

        final Intent notificationIntent = new Intent(getApplicationContext(),
                MainActivity.class);
        final PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                notificationIntent, 0);

        final Notification notification = new Notification.Builder(
                getApplicationContext())
                .setSmallIcon(android.R.drawable.ic_media_play)
                .setOngoing(true).setContentTitle("Music Playing")
                .setContentText("Click to Access Music Player")
                .setContentIntent(pendingIntent).build();

        // Put this Service in a foreground state, so it won't
        // readily be killed by the system
        //startForeground(NOTIFICATION_ID, notification);
    }

    public void setLawVolume(){
        mPlayer.setVolume(0.2f, 0.2f);
    }

    public void resetLawVolume(){
        mPlayer.setVolume(1f, 1f);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startid) {

        if (null != mPlayer) {
            // ID for this start command
            mStartID = startid;

            if (mPlayer.isPlaying()) {
                // Rewind to beginning of song
                mPlayer.seekTo(0);
            }
            else {
                // Start playing song
                mPlayer.start();
            }
        }

        // Don't automatically restart this Service if it is killed
        return START_NOT_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPlayer.stop();
        stopForeground(true);
    }

    private void updateState(String state) {
        Toast.makeText(this, state, Toast.LENGTH_LONG).show();
        if(state.equals("Plugged")) {
            setLawVolume();
        }
        else {
            resetLawVolume();
        }
    }

    public class HeadsetIntentReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(Intent.ACTION_HEADSET_PLUG)) {
                int state = intent.getIntExtra("state", -1);
                switch (state) {
                    case (0):
                        updateState("Unplugged");
                        break;
                    case (1):
                        updateState("Plugged");
                        break;
                    default:
                        updateState("Error");
                }
            }
        }
    }
}