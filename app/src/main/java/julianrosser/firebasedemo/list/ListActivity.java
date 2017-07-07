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
import julianrosser.firebasedemo.firebase.DatabaseHelper;
import julianrosser.firebasedemo.firebase.RemoteConfigHelper;
import julianrosser.firebasedemo.model.Dessert;

import static julianrosser.firebasedemo.firebase.RemoteConfigHelper.FEATURE_PROMOTION;
import static julianrosser.firebasedemo.firebase.RemoteConfigHelper.FEATURE_PROMOTION_TOOLBAR_COLOUR;
import static julianrosser.firebasedemo.firebase.RemoteConfigHelper.FEATURE_SHOW_ID;

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

    private DessertAdapter dessertAdapter;
    private DatabaseHelper databaseHelper;
    private RemoteConfigHelper remoteConfigHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        databaseHelper = new DatabaseHelper();
        remoteConfigHelper = new RemoteConfigHelper(this::setUpList);

        fab.setOnClickListener(view -> onFabPressed());
    }

    private void onFabPressed() {
        // Create new Dessert object
        Dessert dessert = Dessert.newRandom(this);

        // Save to table
//        databaseHelper.saveSingleDessert(dessert);

        // Save with ID
//        databaseHelper.saveDessertWithID(dessert);

        // Save with callback
        databaseHelper.saveDessertWithCallback(dessert, () ->
                showMessage(dessert.getName() + " saved!"));
    }

    private void setUpList() {
        checkForForceUpgrade();
        checkForPromotion();

        // Set up Dessert list
        dessertAdapter = new DessertAdapter(remoteConfigHelper);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(dessertAdapter);
        loadData();

        showMessage(remoteConfigHelper.getWelcomeMessage());
        if (remoteConfigHelper.getFeatureFlag(FEATURE_SHOW_ID))
            showMessage("Feature 'Dessert ID' enabled!");
    }

    private void loadData() {
        // Load node on change
        databaseHelper.loadDessertsOnce(retrievedDesserts -> {
            dessertAdapter.setDesserts(retrievedDesserts);
            progressBar.setVisibility(View.GONE);
        }, this::showMessage);

        // Load node child which has been modified
        databaseHelper.loadDessertsOnChildChange(newDessert -> {
            // dessertAdapter.addDesert(newDessert);
        }, updatedDessert -> {
            // dessertAdapter.updateDessert(updatedDessert);

        });
    }

    private void showMessage(String message) {
        Snackbar.make(fab, message, Snackbar.LENGTH_SHORT).show();
    }

    private void checkForPromotion() {
        boolean isPromo = remoteConfigHelper.getFeatureFlag(FEATURE_PROMOTION);
        int color = isPromo ? Color.parseColor(remoteConfigHelper.getStringFromKey(FEATURE_PROMOTION_TOOLBAR_COLOUR)) :
                ContextCompat.getColor(this, R.color.colorPrimary);
        toolbar.setBackgroundColor(color);
        fab.setBackgroundTintList(ColorStateList.valueOf(color));
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
