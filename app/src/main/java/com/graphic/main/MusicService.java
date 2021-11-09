package com.graphic.main;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.widget.Toast;

// The service that plays music while in MainActivity
public class MusicService extends Service {
    private final String TAG = "MusicService";

    private static final int NOTIFICATION_ID = 1;
    private MediaPlayer mPlayer;
    private int mStartID;

    // Register the broadcast receiver
    HeadsetIntentReceiver receiver = new HeadsetIntentReceiver();

    // The volume values
    private int headsetVolume, speakerVolume;

    @Override
    public void onCreate() {
        super.onCreate();

        // Register the media player and read the volumes frm shared preferences
        mPlayer = MediaPlayer.create(this, R.raw.project_theme);
        headsetVolume = Store.readInt(getApplicationContext(), R.string.headset_volume_key, 20);
        speakerVolume = Store.readInt(getApplicationContext(), R.string.speaker_volume_key, 100);

        if (null != mPlayer) {
            mPlayer.setLooping(true);

            IntentFilter receiverFilter = new IntentFilter(
                    Intent.ACTION_HEADSET_PLUG);
            registerReceiver(receiver, receiverFilter);
        }
    }

    // Set the volume to be the headset volume
    public void setVolumeHeadset(){
        mPlayer.setVolume(headsetVolume / 100f, headsetVolume / 100f);
    }

    // Set the volume to be the speaker volume
    public void setVolumeSpeaker(){
        mPlayer.setVolume(speakerVolume / 100f, speakerVolume / 100f);
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
        // Stop playing music
        super.onDestroy();
        mPlayer.stop();
        stopForeground(true);
    }

    private void updateState(String state) {
        // Update the state fo the volume (for when a headset is plugged/unplugged)
        Toast.makeText(this, state, Toast.LENGTH_LONG).show();
        if (state.equals("Plugged")) {
            setVolumeHeadset();
        }
        else {
            setVolumeSpeaker();
        }
    }

    // The broadcast receiver that receives a headset plug/unplug broadcast
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