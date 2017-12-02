package in.janitri.keyar;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 * Created by abhastandon on 17/08/16.
 */

public class AlertSound extends Service {
    MediaPlayer alertMediaPlayer;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public void onCreate() {
        alertMediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.genmed);
        alertMediaPlayer.setVolume(100, 100);
        alertMediaPlayer.setLooping(false);
    }

    public void onDestroy() {
        alertMediaPlayer.stop();
    }

    /*
        For backward compatibility:
        https://developer.android.com/reference/android/app/Service.html#onStartCommand(android.content.Intent, int, int)
     */
    @Override
    public void onStart(Intent intent, int startId) {
        alertMediaPlayer.start();
    }

    String alertValue;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
            alertMediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.genmed);


            alertMediaPlayer.start();

        }
        return START_STICKY;
    }

}
