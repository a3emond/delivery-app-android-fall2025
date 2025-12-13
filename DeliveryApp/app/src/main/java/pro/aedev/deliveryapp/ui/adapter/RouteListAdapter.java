package pro.aedev.deliveryapp.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import pro.aedev.deliveryapp.R;
import pro.aedev.deliveryapp.data.repo.DelivererRepository;
import pro.aedev.deliveryapp.model.Route;

/**
 * Adapter for displaying a list of Routes in a RecyclerView.
 */
public class RouteListAdapter extends RecyclerView.Adapter<RouteListAdapter.ViewHolder> {

    public interface OnRouteClickListener {
        void onRouteClick(Route route);
    }

    private List<Route> items;
    private final OnRouteClickListener listener;

    public RouteListAdapter(List<Route> items, OnRouteClickListener listener) {
        this.items = items;
        this.listener = listener;
    }

    public void setItems(List<Route> routes, DelivererRepository delivererRepo) {
        this.items = routes;
        // no caching of names here; we resolve in bind to keep code simple
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RouteListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_route_row, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RouteListAdapter.ViewHolder holder, int position) {
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

        void bind(Route route, OnRouteClickListener listener) {
            String delivererPart;
            if (route.getDelivererId() == null) {
                delivererPart = "No deliverer";
            } else {
                delivererPart = "Deliverer ID " + route.getDelivererId();
            }

            String label = route.getLabel() != null ? route.getLabel() : "";
            String text = "#" + route.getId() + " – " + label + " – " + delivererPart;
            textLine.setText(text);

            itemView.setOnClickListener(v -> {
                if (listener != null) listener.onRouteClick(route);
            });
        }
    }
}
