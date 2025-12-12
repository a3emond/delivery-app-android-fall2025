package pro.aedev.deliveryapp.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import pro.aedev.deliveryapp.R;

public class SubscriptionGridAdapter extends RecyclerView.Adapter<SubscriptionGridAdapter.ViewHolder> {

    public static class Item {
        public final String address;
        public final String productName;
        public final int quantity;
        public final String clientName;

        public Item(String address, String productName, int quantity, String clientName) {
            this.address = address;
            this.productName = productName;
            this.quantity = quantity;
            this.clientName = clientName;
        }
    }

    private List<Item> items;

    public SubscriptionGridAdapter(List<Item> items) {
        this.items = items;
    }

    public void setItems(List<Item> newList) {
        items = newList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public SubscriptionGridAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_subscription_grid, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull SubscriptionGridAdapter.ViewHolder holder, int position) {
        holder.bind(items.get(position));
    }

    @Override
    public int getItemCount() { return items != null ? items.size() : 0; }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView textAddress;
        private final TextView textProduct;
        private final TextView textClient;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            textAddress = itemView.findViewById(R.id.textAddress);
            textProduct = itemView.findViewById(R.id.textProduct);
            textClient = itemView.findViewById(R.id.textClient);
        }

        void bind(Item item) {
            textAddress.setText(item.address);
            textProduct.setText(item.productName + " x" + item.quantity);
            textClient.setText(item.clientName);
        }
    }
}
