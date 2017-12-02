package in.janitri.keyar;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import in.janitri.keyarhardware.KeyarCallback;
import in.janitri.keyarhardware.KeyarData;
import in.janitri.keyarhardware.KeyarDevice;
import in.janitri.keyarhardware.KeyarTestActivity;

public class HomeActivity extends AppCompatActivity {
    private static String LOG_TAG = KeyarTestActivity.class.getName();
    private KeyarDevice keyarDevice;
    private Activity activity;
    private final int MY_PERMISSIONS_REQUEST = 1;
    private KeyarCallback keyarCallback = new KeyarCallback() {
        @Override
        public void onData(KeyarData keyarData) {
            Log.i(LOG_TAG, keyarData.toString());
        }

        @Override
        public void onError(String error) {
            switch (error) {
                case ERROR_PERMISSION:
                    ActivityCompat.requestPermissions(activity,
                            new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                            MY_PERMISSIONS_REQUEST);
                    break;

            }
            Log.e(LOG_TAG, error);
        }

        @Override
        public void onMessage(String message) {
//            Handle
//            static String MESSAGE_CONNECTION_SUCCESS = "service_get_socket_ok";
//            static String MESSAGE_CONNECTION_FAIL = "service_get_socket_fail";
//            static String MESSAGE_READ_DATA_FAIL = "service_read_data_fail"; // When device is switched off
//            static String MESSAGE_READ_DATA_START = "read_data_start";

            Log.i(LOG_TAG, message);
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = this;
        setContentView(R.layout.activity_home);
        keyarDevice = KeyarDevice.getInstance(this);
        keyarDevice.setKeyarCallback(keyarCallback);
        keyarDevice.connectDevice();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    keyarDevice.connectDevice();
                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

        }
    }
}