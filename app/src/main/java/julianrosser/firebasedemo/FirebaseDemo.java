package julianrosser.firebasedemo;

import android.app.Application;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FirebaseDemo extends Application {

    private static FirebaseDemo sInstance;
    private FirebaseDatabase database;

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
    }

    public static FirebaseDemo getInstance() {
        return sInstance;
    }

    public synchronized DatabaseReference getDatabaseReference() {
        if (database == null) {
            database = FirebaseDatabase.getInstance();
            database.setPersistenceEnabled(true);
        }
        return database.getReference();
    }
}
