package in.janitri.keyar;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import in.janitri.keyar.model.Patient;
import android.support.v7.widget.RecyclerView;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by abhastandon on 02/12/17.
 */

public class PatientAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context c;
    ArrayList<Patient> patients;
    private boolean readingData;
    private String attachedToPatientId;
    private OnDeviceAttached onDeviceAttached;

    public void setOnDeviceAttached(OnDeviceAttached onDeviceAttached) {
        this.onDeviceAttached = onDeviceAttached;
    }

    public PatientAdapter(Context c) {
        this.c = c;
        this.patients = new ArrayList<Patient>();
        readingData = false;
        attachedToPatientId = "";
    }

    public void setReadingData(boolean readingData) {
        this.readingData = readingData;
        notifyDataSetChanged();
    }

    public boolean getReadingData() {
        return readingData;
    }

    public void setAttachedToPatientId(String patientId) {
        this.attachedToPatientId = patientId;
        notifyDataSetChanged();
    }

    public String getAttachedToPatientId() {
        return attachedToPatientId;
    }


    public void replacePatients(ArrayList<Patient> patients) {
        this.patients = patients;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(c).inflate(R.layout.adapter_patient,parent,false);
        return new PatientsHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof PatientsHolder) {
            Patient patient = patients.get(position);
            ((PatientsHolder) holder).initializeFromPatient(patient, onDeviceAttached);
        }
    }
    @Override
    public int getItemCount() {
        return patients.size();
    }

    public class PatientsHolder extends RecyclerView.ViewHolder {
        private final TextView idTextView;
        private final Button attachDeviceButton;

        public PatientsHolder(View itemView) {
            super(itemView);
            idTextView = itemView.findViewById(R.id.idTextView);
            attachDeviceButton = itemView.findViewById(R.id.attachDeviceButton);
        }

        public void initializeFromPatient(final Patient patient, final OnDeviceAttached onDeviceAttached) {
            idTextView.setText(patient.getId().toUpperCase());
            if (readingData) {
                attachDeviceButton.setVisibility(View.VISIBLE);
            } else {
                attachDeviceButton.setVisibility(View.GONE);
            }
            attachDeviceButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (onDeviceAttached != null) {
                        onDeviceAttached.deviceAttached(patient.getId());
                    }
                }
            });
        }
    }

}