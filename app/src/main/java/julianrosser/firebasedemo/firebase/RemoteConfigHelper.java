package julianrosser.firebasedemo.firebase;

import android.util.Log;

import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;

import julianrosser.firebasedemo.BuildConfig;
import julianrosser.firebasedemo.R;
import julianrosser.firebasedemo.firebase.FirebaseCallbacks.RemoteConfigCallback;

public class RemoteConfigHelper {

    private static final String TAG = "Firebase";

    // Remote config Keys
    public static final String FORCE_UPDATE = "force_update";
    public static final String WELCOME_MESSAGE = "message";
    public static final String FEATURE_SHOW_ID = "feature_show_id";
    public static final String FEATURE_PROMOTION = "feature_promotion";
    public static final String FEATURE_PROMOTION_TOOLBAR_COLOUR = "colour_toolbar_promotion";

    private final FirebaseRemoteConfig remoteConfigRef;

    public RemoteConfigHelper(RemoteConfigCallback callback) {
        remoteConfigRef = FirebaseRemoteConfig.getInstance();
        // Set developer mode
        FirebaseRemoteConfigSettings configSettings = new FirebaseRemoteConfigSettings.Builder()
                .setDeveloperModeEnabled(BuildConfig.DEBUG).build();
        remoteConfigRef.setConfigSettings(configSettings);
        // Set up defaults
        remoteConfigRef.setDefaults(R.xml.remote_config_defaults);
        // Callback to activity
        updateWithLatest(callback);
    }

    private void updateWithLatest(RemoteConfigCallback callback) {
        remoteConfigRef.fetch(0).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                remoteConfigRef.activateFetched();
                callback.onCompleted();
            } else {
                Log.i(TAG, "Fetch Failed: " + task.getException().getMessage());
            }
        });
    }

    /**
     * Get predefined String
     */
    public String getWelcomeMessage() {
        return remoteConfigRef.getString(WELCOME_MESSAGE);
    }

    /**
     * Get String from Key
     */
    public String getStringFromKey(String stringKey) {
        return remoteConfigRef.getString(stringKey);
    }

    /**
     * Get feature flag status from Key
     */
    public boolean getFeatureFlag(String featureFlagKey) {
        return remoteConfigRef.getBoolean(featureFlagKey);
    }

    /**
     * Check version for force update status
     */
    public boolean needsToUpdate() {
        boolean b = remoteConfigRef.getBoolean(FORCE_UPDATE);
        Log.d(TAG, "Force update: " + b);
        return remoteConfigRef.getBoolean(FORCE_UPDATE);
    }
}
