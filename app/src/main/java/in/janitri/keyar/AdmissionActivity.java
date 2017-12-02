package in.janitri.keyar;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import cn.pedant.SweetAlert.SweetAlertDialog;
import in.janitri.keyar.model.Patient;

public class AdmissionActivity extends AppCompatActivity {
    private Button admitButton;
    private EditText idEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admission);
        admitButton = (Button) findViewById(R.id.admitButton);
        idEditText = (EditText) findViewById(R.id.idEditText);
        final Context context = this;
        admitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (idEditText.getText().toString().equals("")) {
                    SweetAlertDialog pDialog = new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE);
                    pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                    pDialog.setTitleText("Invalid PCTS ID.");
                    pDialog.setContentText("Please enter a valid PCTS ID.");
                    pDialog.setCancelable(true);
                    pDialog.show();
                    return;
                }

                Patient.savePatient(idEditText.getText().toString());

                new SweetAlertDialog(context, SweetAlertDialog.SUCCESS_TYPE)
                        .setTitleText("Patient admitted")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                sDialog.dismissWithAnimation();
                                finish();
                            }
                        })
                        .show();

            }
        });
    }
}
