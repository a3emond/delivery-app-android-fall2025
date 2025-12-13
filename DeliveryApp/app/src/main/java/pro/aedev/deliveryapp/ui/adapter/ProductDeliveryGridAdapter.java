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
 * Adapter for displaying product delivery information in a grid layout.
 */
public class ProductDeliveryGridAdapter extends RecyclerView.Adapter<ProductDeliveryGridAdapter.ViewHolder> {

    public static class Item {
        public final String address;
        public final String clientName;
        public final String delivererName;
        public final int quantity;

        public Item(String address, String clientName, String delivererName, int quantity) {
            this.address = address;
            this.clientName = clientName;
            this.delivererName = delivererName;
            this.quantity = quantity;
        }
    }

    private List<Item> items;

    public ProductDeliveryGridAdapter(List<Item> items) {
        this.items = items;
    }

    public void setItems(List<Item> newItems) {
        this.items = newItems;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ProductDeliveryGridAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_product_delivery_grid, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductDeliveryGridAdapter.ViewHolder holder, int position) {
        holder.bind(items.get(position));
    }

    @Override
    public int getItemCount() {
        return items != null ? items.size() : 0;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView textAddress;
        private final TextView textClient;
        private final TextView textDeliverer;
        private final TextView textQuantity;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            textAddress = itemView.findViewById(R.id.textAddress);
            textClient = itemView.findViewById(R.id.textClient);
            textDeliverer = itemView.findViewById(R.id.textDeliverer);
            textQuantity = itemView.findViewById(R.id.textQuantity);
        }

        void bind(Item item) {
            textAddress.setText(item.address);
            textClient.setText("Subscriber: " + item.clientName);
            textDeliverer.setText("Deliverer: " + item.delivererName);
            textQuantity.setText("Qty: " + item.quantity);
        }
    }
}
