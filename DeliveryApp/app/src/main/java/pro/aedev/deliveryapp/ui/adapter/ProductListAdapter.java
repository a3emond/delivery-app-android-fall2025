package pro.aedev.deliveryapp.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import pro.aedev.deliveryapp.R;
import pro.aedev.deliveryapp.model.Product;

public class ProductListAdapter extends RecyclerView.Adapter<ProductListAdapter.ViewHolder> {

    public interface OnProductClickListener {
        void onProductClick(Product product);
    }

    private List<Product> items;
    private final OnProductClickListener listener;

    public ProductListAdapter(List<Product> items, OnProductClickListener listener) {
        this.items = items;
        this.listener = listener;
    }

    public void setItems(List<Product> products) {
        this.items = products;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ProductListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_product_row, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductListAdapter.ViewHolder holder, int position) {
        holder.bind(items.get(position), listener);
    }

    @Override
    public int getItemCount() {
        return items != null ? items.size() : 0;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView textLine;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            textLine = itemView.findViewById(R.id.textLine);
        }

        void bind(Product product, OnProductClickListener listener) {
            textLine.setText("#" + product.getId() + " – " + product.getName());
            itemView.setOnClickListener(v -> {
                if (listener != null) listener.onProductClick(product);
            });
        }
    }
}
