package pro.aedev.deliveryapp.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import pro.aedev.deliveryapp.R;
import pro.aedev.deliveryapp.model.Deliverer;

public class DelivererListAdapter extends RecyclerView.Adapter<DelivererListAdapter.ViewHolder> {

    public interface OnDelivererClickListener {
        void onDelivererClick(Deliverer deliverer);
    }

    private List<Deliverer> items;
    private final OnDelivererClickListener listener;

    public DelivererListAdapter(List<Deliverer> items, OnDelivererClickListener listener) {
        this.items = items;
        this.listener = listener;
    }

    public void setItems(List<Deliverer> newItems) {
        this.items = newItems;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public DelivererListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_deliverer_row, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull DelivererListAdapter.ViewHolder holder, int position) {
        Deliverer d = items.get(position);
        holder.bind(d, listener);
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

        void bind(Deliverer d, OnDelivererClickListener listener) {
            textLine.setText("#" + d.getId() + " – " + d.getName());
            itemView.setOnClickListener(v -> {
                if (listener != null) listener.onDelivererClick(d);
            });
        }
    }
}
