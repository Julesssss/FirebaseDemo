package julianrosser.firebasedemo.list;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import julianrosser.firebasedemo.R;
import julianrosser.firebasedemo.model.Dessert;
import julianrosser.firebasedemo.firebase.RemoteConfigHelper;

public class DessertAdapter extends RecyclerView.Adapter<DessertAdapter.DessertHolder> {

    private List<Dessert> desserts;
    private boolean featureShowIDEnable;


    public DessertAdapter(RemoteConfigHelper remoteConfigHelper) {
        this.desserts = new ArrayList<>();
        featureShowIDEnable = remoteConfigHelper.getFeatureFlag(RemoteConfigHelper.FEATURE_SHOW_ID);
    }

    @Override
    public DessertHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.dessert_list_item, parent, false);
        return new DessertHolder(view);
    }

    @Override
    public void onBindViewHolder(DessertHolder holder, int position) {
        Dessert dessert = desserts.get(position);
        holder.setTag(dessert);
        holder.setName(dessert.getName());
        if (featureShowIDEnable)
            holder.setId(dessert.getId());
    }

    @Override
    public int getItemCount() {
        return desserts.size();
    }

    public void setDesserts(List<Dessert> retrievedDesserts) {
        desserts = retrievedDesserts;
        notifyDataSetChanged();
    }

    /**
     * View Holder
     */
    class DessertHolder extends RecyclerView.ViewHolder {

        private View view;

        @BindView(R.id.text_name)
        TextView textName;

        @BindView(R.id.text_id)
        TextView textId;

        DessertHolder(View itemView) {
            super(itemView);
            view = itemView;
            ButterKnife.bind(this, view);
        }

        void setName(String text) {
            textName.setText(text);
        }

        void setId(String text) {
            textId.setVisibility(View.VISIBLE);
            textId.setText(text);
        }

        void setTag(Dessert dessert) {
            view.setTag(dessert);
        }
    }
}
