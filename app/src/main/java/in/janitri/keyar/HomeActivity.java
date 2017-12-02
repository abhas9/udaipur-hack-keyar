package in.janitri.keyar;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;

import cn.pedant.SweetAlert.SweetAlertDialog;
import in.janitri.keyarhardware.KeyarCallback;
import in.janitri.keyarhardware.KeyarData;
import in.janitri.keyarhardware.KeyarDevice;
import in.janitri.keyarhardware.KeyarTestActivity;

public class HomeActivity extends AppCompatActivity {
    private static String LOG_TAG = KeyarTestActivity.class.getName();
    private KeyarDevice keyarDevice;
    private Activity activity;
    private final int MY_PERMISSIONS_REQUEST = 1;
    private FrameLayout addPatientFrameLayout;
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
                case ERROR_BLUETOOTH_OFF:
                    SweetAlertDialog pDialog = new SweetAlertDialog(activity, SweetAlertDialog.ERROR_TYPE);
                    pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                    pDialog.setTitleText("Your Bluetooth is off. Please turn on your bluetooth to use Keyar");
                    pDialog.setCancelable(true);
                    pDialog.show();

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

//            static String ERROR_BLUETOOTH_OFF = "bluetooth_off";
//            static String ERROR_DEVICE_NOT_FOUND = "device_not_found";
//            static String ERROR_PERMISSION = "error_permission";

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
        initLayout();
    }

    void initLayout() {
        addPatientFrameLayout = (FrameLayout) findViewById(R.id.addPatientFrameLayout);
        final Context context = this;
        addPatientFrameLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, AdmissionActivity.class);
                startActivity(intent);
            }
        });
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
