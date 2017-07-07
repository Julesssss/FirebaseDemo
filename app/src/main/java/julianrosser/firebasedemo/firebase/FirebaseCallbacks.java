package julianrosser.firebasedemo.firebase;

import java.util.List;

import julianrosser.firebasedemo.model.Dessert;

public class FirebaseCallbacks {

    /**
     * Database
     */
    public interface SaveDessertCallback {
        void onSuccess();
    }

    public interface LoadDessertSuccess {
        void onSuccess(List<Dessert> desserts);
    }

    public interface LoadDessertFail {
        void onFail(String message);
    }

    public interface DessertAdded {
        void onAdd(Dessert dessert);
    }

    public interface DessertUpdated {
        void onUpdated(Dessert dessert);
    }

    /**
     * Remote Config
     */
    public interface RemoteConfigCallback {
        void onCompleted();
    }

}
