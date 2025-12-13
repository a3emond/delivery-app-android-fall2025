package pro.aedev.deliveryapp.ui.adapter;


import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

import pro.aedev.deliveryapp.model.Deliverer;

/**
 * Spinner adapter for deliverers. Displays deliverer ID and name in a single line.
 */
public class DelivererSpinnerAdapter extends ArrayAdapter<Deliverer> {

    public DelivererSpinnerAdapter(@NonNull Context context, @NonNull List<Deliverer> items) {
        super(context, android.R.layout.simple_spinner_dropdown_item, items);
    }

    @NonNull
    @Override
    public View getView(int position,
                        @Nullable View convertView,
                        @NonNull ViewGroup parent) {
        TextView view = (TextView) super.getView(position, convertView, parent);
        Deliverer d = getItem(position);
        view.setText(format(d));
        return view;
    }

    @Override
    public View getDropDownView(int position,
                                @Nullable View convertView,
                                @NonNull ViewGroup parent) {
        TextView view = (TextView) super.getDropDownView(position, convertView, parent);
        Deliverer d = getItem(position);
        view.setText(format(d));
        return view;
    }

    private String format(Deliverer d) {
        if (d == null) return "";
        return "#" + d.getId() + " – " + d.getName();
    }
}
