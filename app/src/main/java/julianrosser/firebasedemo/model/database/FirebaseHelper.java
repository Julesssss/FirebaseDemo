package julianrosser.firebasedemo.model.database;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import julianrosser.firebasedemo.FirebaseDemo;
import julianrosser.firebasedemo.model.objects.Dessert;

public class FirebaseHelper extends FirebaseCallbacks {

    private final DatabaseReference databaseReference;

    public FirebaseHelper() {
        databaseReference = FirebaseDemo.getInstance().getDatabaseReference();
    }

    /**
     * Save data to Firebase
     */

    // Save single object to table
    public void saveSingleDessert(Dessert dessert) {
        DatabaseReference tableReference = databaseReference.child(FirebaseKeys.TABLE_SINGLE_DESSERT);
        tableReference.setValue(dessert);
    }

    // Save dessert using PushID
    public void saveDessertWithID(Dessert dessert) {
        DatabaseReference tableReference = databaseReference.child(FirebaseKeys.TABLE_MULTIPLE_DESSERTS).push();
        dessert.setId(tableReference.getKey());
        tableReference.setValue(dessert);
    }

    // Save dessert with completion listener
    public void saveDessertWithCallback(Dessert dessert, SaveDessertCallback callback) {
        DatabaseReference tableReference = databaseReference.child(FirebaseKeys.TABLE_MULTIPLE_DESSERTS).push();
        dessert.setId(tableReference.getKey());
        tableReference.setValue(dessert, (databaseError, databaseReference1) -> callback.onSuccess());
    }

    /**
     * Retrieve data from Firebase
     */

    public void loadDessertsFromFirebase(LoadDessertSuccess successCallback, LoadDessertFail failureCallback) {
        DatabaseReference dessertTable = databaseReference.child(FirebaseKeys.TABLE_MULTIPLE_DESSERTS);
        dessertTable.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Dessert> desserts = new ArrayList<>();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    Dessert dessert = ds.getValue(Dessert.class);
                    desserts.add(dessert);
                }
                successCallback.onSuccess(desserts);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                failureCallback.onFail(databaseError.getMessage());
            }
        });

    }
}
