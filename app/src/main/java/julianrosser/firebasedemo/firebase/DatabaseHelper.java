package julianrosser.firebasedemo.firebase;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import julianrosser.firebasedemo.FirebaseDemo;
import julianrosser.firebasedemo.model.Dessert;

public class DatabaseHelper extends FirebaseCallbacks {

    private final DatabaseReference databaseRef;

    public DatabaseHelper() {
        databaseRef = FirebaseDemo.getInstance().getDatabaseReference();
    }

    /**
     * Save data to Firebase
     */

    // Save single object to table
    public void saveSingleDessert(Dessert dessert) {
        DatabaseReference tableRef = databaseRef.child(FirebaseKeys.TABLE_SINGLE_DESSERT);
        tableRef.setValue(dessert);
    }

    // Save dessert using PushID
    public void saveDessertWithID(Dessert dessert) {
        DatabaseReference tableRef = databaseRef.child(FirebaseKeys.TABLE_MULTIPLE_DESSERTS).push();
        dessert.setId(tableRef.getKey());
        tableRef.setValue(dessert);
    }

    // Save dessert with completion listener
    public void saveDessertWithCallback(Dessert dessert, SaveDessertCallback callback) {
        DatabaseReference tableRef = databaseRef.child(FirebaseKeys.TABLE_MULTIPLE_DESSERTS).push();
        dessert.setId(tableRef.getKey());
        tableRef.setValue(dessert, (databaseError, databaseReference1) -> callback.onSuccess());
    }

    /**
     * Retrieve data from Firebase
     */

    public void loadDessertsOnce(LoadDessertSuccess successCallback, LoadDessertFail failureCallback) {
        DatabaseReference dessertTable = databaseRef.child(FirebaseKeys.TABLE_MULTIPLE_DESSERTS);
        dessertTable.addValueEventListener(new ValueEventListener() {
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

    public void loadDessertsOnChildChange(DessertAdded addCallback, DessertUpdated updateCallback) {
        DatabaseReference dessertTable = databaseRef.child(FirebaseKeys.TABLE_MULTIPLE_DESSERTS);
        dessertTable.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Dessert dessert = dataSnapshot.getValue(Dessert.class);
                addCallback.onAdd(dessert);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Dessert dessert = dataSnapshot.getValue(Dessert.class);
                updateCallback.onUpdated(dessert);
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

}
