package julianrosser.firebasedemo.list;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ProgressBar;

import butterknife.BindView;
import butterknife.ButterKnife;
import julianrosser.firebasedemo.R;
import julianrosser.firebasedemo.Utils;
import julianrosser.firebasedemo.model.database.FirebaseHelper;
import julianrosser.firebasedemo.model.objects.Dessert;
import julianrosser.firebasedemo.remoteconfig.RemoteConfigHelper;

import static julianrosser.firebasedemo.remoteconfig.RemoteConfigHelper.FEATURE_PROMOTION;
import static julianrosser.firebasedemo.remoteconfig.RemoteConfigHelper.FEATURE_PROMOTION_TOOLBAR_COLOUR;
import static julianrosser.firebasedemo.remoteconfig.RemoteConfigHelper.FEATURE_SHOW_ID;

public class ListActivity extends AppCompatActivity {

    private static final String TAG = ListActivity.class.getSimpleName();

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.fab)
    FloatingActionButton fab;

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    @BindView(R.id.progress_bar)
    ProgressBar progressBar;

    private FirebaseHelper firebaseHelper;
    private DessertAdapter dessertAdapter;
    private RemoteConfigHelper remoteConfigHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        firebaseHelper = new FirebaseHelper();
        remoteConfigHelper = new RemoteConfigHelper(this::setUpList);

        fab.setOnClickListener(view -> onFabPressed());
    }

    private void onFabPressed() {
        // Create new Dessert object
        Dessert dessert = new Dessert(Utils.getRandomDessert(getResources()));
        // Save to table

        // Save with ID

        // Save with callback
        firebaseHelper.saveDessertWithCallback(dessert, () ->
                showMessage(dessert.getName() + " saved!"));
    }

    private void setUpList() {
        checkForForceUpgrade();
        checkForPromotion();

        dessertAdapter = new DessertAdapter(remoteConfigHelper);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(dessertAdapter);
        loadData();

//        showMessage(remoteConfigHelper.getWelcomeMessage());
        if (remoteConfigHelper.getFeatureFlag(FEATURE_SHOW_ID))
            showMessage("Feature 'Dessert ID' enabled!");
    }

    private void checkForPromotion() {
        boolean isPromo = remoteConfigHelper.getFeatureFlag(FEATURE_PROMOTION);
        int color = isPromo ? Color.parseColor(remoteConfigHelper.getStringFromKey(FEATURE_PROMOTION_TOOLBAR_COLOUR)) :
                ContextCompat.getColor(this, R.color.colorPrimary);
        toolbar.setBackgroundColor(color);
        fab.setBackgroundTintList(ColorStateList.valueOf(color));
    }

    private void loadData() {
        firebaseHelper.loadDessertsOnce(retrievedDesserts -> {
            dessertAdapter.setDesserts(retrievedDesserts);
            progressBar.setVisibility(View.GONE);
        }, this::showMessage);
    }

    private void showMessage(String message) {
        Snackbar.make(fab, message, Snackbar.LENGTH_SHORT).show();
    }

    private void checkForForceUpgrade() {
        if (remoteConfigHelper.needsToUpdate()) showForceUpdateDialog();
    }

    private void showForceUpdateDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.dialog_message)
                .setTitle(R.string.dialog_title);
        builder.create().show();
    }

}
