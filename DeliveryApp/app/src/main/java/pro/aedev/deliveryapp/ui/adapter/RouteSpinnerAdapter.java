package pro.aedev.deliveryapp.ui.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

import pro.aedev.deliveryapp.model.Route;

/**
 * Adapter for displaying Route objects in a Spinner. Each item shows the route ID and label in a single line.
 */
public class RouteSpinnerAdapter extends ArrayAdapter<Route> {

    public RouteSpinnerAdapter(@NonNull Context context, @NonNull List<Route> routes) {
        super(context, android.R.layout.simple_spinner_dropdown_item, routes);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        TextView view = (TextView) super.getView(position, convertView, parent);
        Route r = getItem(position);
        view.setText(format(r));
        return view;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        TextView view = (TextView) super.getDropDownView(position, convertView, parent);
        Route r = getItem(position);
        view.setText(format(r));
        return view;
    }

    private String format(Route r) {
        return "#" + r.getId() + "  –  " + r.getLabel();
    }
}