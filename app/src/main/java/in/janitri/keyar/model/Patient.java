package in.janitri.keyar.model;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.RealmResults;
import io.realm.annotations.PrimaryKey;

/**
 * Created by abhastandon on 02/12/17.
 */

public class Patient extends RealmObject {

    @PrimaryKey
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public static void savePatient(String id) {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        Patient patient = realm.createObject(Patient.class, id); // Create a new object
        realm.commitTransaction();
    }

    public static ArrayList<Patient> getAllPatients() {
        Realm realm = Realm.getDefaultInstance();
        final RealmResults<Patient> patients = realm.where(Patient.class).findAll();
        return new ArrayList(patients);
    }
}
