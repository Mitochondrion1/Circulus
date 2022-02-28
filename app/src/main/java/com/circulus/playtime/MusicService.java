package com.circulus.playtime;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.os.IBinder;
import android.widget.Toast;

import com.circulus.main.R;
import com.circulus.utility.Store;

/**
 * A service that plays music during gameplay.
 */
public class MusicService extends Service {
    /** The media player object. */
    private MediaPlayer mPlayer;

    /** The broadcast receiver. */
    HeadsetIntentReceiver receiver;

    /** The headset volume value. */
    private int headsetVolume;
    /** The speaker volume value. */
    private int speakerVolume;

    @Override
    public void onCreate() {
        super.onCreate();

        // Create the broadcast receiver, Register the media player and read the volumes from shared preferences
        receiver = new HeadsetIntentReceiver();
        mPlayer = MediaPlayer.create(this, R.raw.project_theme);
        headsetVolume = Store.readInt(getApplicationContext(), R.string.headset_volume_key, 20);
        speakerVolume = Store.readInt(getApplicationContext(), R.string.speaker_volume_key, 100);

        if (null != mPlayer) {
            mPlayer.setLooping(true);

            IntentFilter receiverFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
            receiverFilter.addAction(Intent.ACTION_HEADSET_PLUG);
            registerReceiver(receiver, receiverFilter);
        }
    }

    /**
     * Set the volume to be the headset volume.
     */
    public void setVolumeHeadset(){
        mPlayer.setVolume(headsetVolume / 100f, headsetVolume / 100f);
    }

    /**
     * Set the volume to be the speaker volume.
     */
    public void setVolumeSpeaker(){
        mPlayer.setVolume(speakerVolume / 100f, speakerVolume / 100f);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startid) {

        if (null != mPlayer) {
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
        unregisterReceiver(receiver);
        stopForeground(true);
    }

    /**
     * Update the state for the volume (for when a headset is plugged/unplugged).
     * <p>
     * @param state The state as it is received from the broadcast receiver.
     */
    private void updateState(String state) {
        Toast.makeText(this, state, Toast.LENGTH_LONG).show();
        if (state.equals("Plugged")) {
            setVolumeHeadset();
        }
        else {
            setVolumeSpeaker();
        }
    }

    /**
     * The broadcast receiver that receives a headset plug/unplug broadcast.
     */
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