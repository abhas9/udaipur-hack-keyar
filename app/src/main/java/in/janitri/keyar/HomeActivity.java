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
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import java.util.ArrayList;

import cn.pedant.SweetAlert.SweetAlertDialog;
import in.janitri.keyar.model.Patient;
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
    private RecyclerView patientRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private PatientAdapter patientAdapter;
    private Button connectDeviceButton;
    private SweetAlertDialog connectingDialog;
    private WebView tocoWebView;

    private FrameLayout patientListFrameLayout;
    private FrameLayout patientDataFrameLayout;

    private KeyarCallback keyarCallback = new KeyarCallback() {
        @Override
        public void onData(KeyarData keyarData) {
            Log.i(LOG_TAG, keyarData.toString());
            tocoWebView.loadUrl("javascript:addData("+ System.currentTimeMillis() + "," + keyarData.getToco() + ")");
            patientListFrameLayout.setVisibility(View.GONE);
            patientDataFrameLayout.setVisibility(View.VISIBLE);
        }

        @Override
        public void onError(String error) {
            SweetAlertDialog pDialog = new SweetAlertDialog(activity, SweetAlertDialog.ERROR_TYPE);;
            switch (error) {
                case ERROR_PERMISSION:
                    connectingDialog.dismissWithAnimation();
                    ActivityCompat.requestPermissions(activity,
                            new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                            MY_PERMISSIONS_REQUEST);

                    break;
                case ERROR_DEVICE_NOT_FOUND:
                    connectingDialog.dismissWithAnimation();
                    pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                    pDialog.setTitleText("Keyar not found");
                    pDialog.setContentText("Please make sure that Keyar device is on");
                    pDialog.setCancelable(true);
                    pDialog.show();
                    patientAdapter.setReadingData(false);
                    break;
                case ERROR_BLUETOOTH_OFF:
                    connectingDialog.dismissWithAnimation();
                    pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                    pDialog.setTitleText("Turn on your Bluetooth.");
                    pDialog.setCancelable(true);
                    pDialog.show();
                    patientAdapter.setReadingData(false);
                    break;


            }
            Log.e(LOG_TAG, error);
        }

        @Override
        public void onMessage(String message) {
            SweetAlertDialog pDialog;
            switch (message) {
                case MESSAGE_CONNECTION_FAIL:
                    pDialog = new SweetAlertDialog(activity, SweetAlertDialog.ERROR_TYPE);
                    pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                    pDialog.setTitleText("Keyar not found");
                    pDialog.setContentText("Please make sure that Keyar device is on");
                    pDialog.setCancelable(true);
                    pDialog.show();
                    patientAdapter.setReadingData(false);
                    break;
                case MESSAGE_CONNECTION_SUCCESS:
                    connectingDialog.dismissWithAnimation();
                    pDialog = new SweetAlertDialog(activity, SweetAlertDialog.SUCCESS_TYPE);
                    pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                    pDialog.setTitleText("Keyar connected");
                    pDialog.setContentText("Keyar connected successfully");
                    pDialog.setCancelable(true);
                    pDialog.show();
                    break;
                case MESSAGE_READ_DATA_FAIL:
                    pDialog = new SweetAlertDialog(activity, SweetAlertDialog.WARNING_TYPE);
                    pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                    pDialog.setTitleText("Keyar disconnected");
                    pDialog.setContentText("Keyar device is now disconnected or turned off");
                    pDialog.setCancelable(true);
                    pDialog.show();
                    patientAdapter.setReadingData(false);
                    // connectDeviceButton.setVisibility(View.VISIBLE);
                    break;
                case MESSAGE_READ_DATA_START:
                    patientAdapter.setReadingData(true);
                    break;
            }
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
        initLayout();
    }

    void initLayout() {
        connectDeviceButton = (Button) findViewById(R.id.connectDeviceButton);
        addPatientFrameLayout = (FrameLayout) findViewById(R.id.addPatientFrameLayout);
        patientRecyclerView = (RecyclerView) findViewById(R.id.patientRecyclerView);

        patientListFrameLayout  = (FrameLayout) findViewById(R.id.patientListFrameLayout);
        patientDataFrameLayout  = (FrameLayout) findViewById(R.id.patientDataFrameLayout);;

        final Context context = this;
        addPatientFrameLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, AdmissionActivity.class);
                startActivity(intent);
            }
        });

        patientRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(context);
        patientRecyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter
        patientAdapter = new PatientAdapter(context);
        patientRecyclerView.setAdapter(patientAdapter);

        tocoWebView = new WebView(this);
        WebSettings webSettings = tocoWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        WebView.setWebContentsDebuggingEnabled(true);
        tocoWebView.loadUrl("file:///android_asset/www/realtime.html");
        LinearLayout fhrGraphLinearLayout = (LinearLayout) findViewById(R.id.fhrGraphLinearLayout);
        fhrGraphLinearLayout.addView(tocoWebView);

        refreshLayout();

    }

    @Override
    public void onResume() {
        super.onResume();
        refreshLayout();
    }

    private void refreshLayout() {
        ArrayList<Patient> patients = Patient.getAllPatients();
        patientAdapter.replacePatients(patients);
        keyarDevice = KeyarDevice.getInstance(this);
        keyarDevice.setKeyarCallback(keyarCallback);

        if (patientAdapter.getReadingData()) {
            connectDeviceButton.setVisibility(View.GONE);
        }

        connectDeviceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                keyarDevice.connectDevice();
                connectingDialog = new SweetAlertDialog(activity, SweetAlertDialog.PROGRESS_TYPE);
                connectingDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                connectingDialog.setTitleText("Searching for device");
                connectingDialog.setCancelable(false);
                connectingDialog.show();
                connectDeviceButton.setVisibility(View.GONE);
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
