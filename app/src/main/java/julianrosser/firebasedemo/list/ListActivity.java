package julianrosser.firebasedemo.list;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import butterknife.BindView;
import butterknife.ButterKnife;
import julianrosser.firebasedemo.R;
import julianrosser.firebasedemo.Utils;
import julianrosser.firebasedemo.model.database.FirebaseHelper;
import julianrosser.firebasedemo.model.objects.Dessert;

public class ListActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.fab)
    FloatingActionButton fab;

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    private FirebaseHelper firebaseHelper;
    private DessertAdapter dessertAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        dessertAdapter = new DessertAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(dessertAdapter);

        firebaseHelper = new FirebaseHelper();

        fab.setOnClickListener(view -> {
            Dessert dessert = new Dessert(Utils.getRandomDessert(getResources()));
//            firebaseHelper.saveSingleDessert(dessert);
//            firebaseHelper.saveDessertWithID(dessert);
            firebaseHelper.saveDessertWithCallback(dessert, () ->
                    Snackbar.make(view, dessert.getName() + " created!", Snackbar.LENGTH_SHORT).show());
        });

        loadData();
    }

    private void loadData() {
        firebaseHelper.loadDessertsFromFirebase(retrievedDesserts -> dessertAdapter.setDesserts(retrievedDesserts), this::showMessage);
    }

    private void showMessage(String message){
        Snackbar.make(fab, message, Snackbar.LENGTH_SHORT).show();
    }

}
