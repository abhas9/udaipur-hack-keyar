package in.janitri.keyar.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by abhastandon on 02/12/17.
 */

public class Patient extends RealmObject {
    @PrimaryKey
    private String id;
}
