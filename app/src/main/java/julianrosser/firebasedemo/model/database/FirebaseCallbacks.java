package julianrosser.firebasedemo.model.database;

import java.util.List;

import julianrosser.firebasedemo.model.objects.Dessert;

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

    /**
     * Remote Config
     */

    public interface RemoteConfigCallback {
        void onCompleted();
    }

}
