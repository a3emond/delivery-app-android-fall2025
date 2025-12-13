package pro.aedev.deliveryapp.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import pro.aedev.deliveryapp.R;

/**
 * Adapter for displaying delivery items in a RecyclerView for deliverers.
 */
public class DelivererDeliveryAdapter extends RecyclerView.Adapter<DelivererDeliveryAdapter.ViewHolder> {

    public static class DeliveryItem {
        public final String address;
        public final String productName;
        public final int quantity;
        public final String clientName;

        public DeliveryItem(String address, String productName, int quantity, String clientName) {
            this.address = address;
            this.productName = productName;
            this.quantity = quantity;
            this.clientName = clientName;
        }
    }

    private List<DeliveryItem> items;

    public DelivererDeliveryAdapter(List<DeliveryItem> items) {
        this.items = items;
    }

    public void setItems(List<DeliveryItem> newItems) {
        this.items = newItems;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public DelivererDeliveryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_delivery_grid, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull DelivererDeliveryAdapter.ViewHolder holder, int position) {
        DeliveryItem item = items.get(position);
        holder.bind(item);
    }

    @Override
    public int getItemCount() {
        return items != null ? items.size() : 0;
    }

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

        void bind(DeliveryItem item) {
            textAddress.setText(item.address);
            textProduct.setText(item.productName + " x" + item.quantity);
            textClient.setText(item.clientName);
        }
    }
}
