package pro.aedev.deliveryapp.ui.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

import pro.aedev.deliveryapp.model.Client;

/**
 * Adapter for displaying Client objects in a Spinner in a single line format. (id – name)
 */
public class ClientSpinnerAdapter extends ArrayAdapter<Client> {

    public ClientSpinnerAdapter(@NonNull Context ctx, @NonNull List<Client> items) {
        super(ctx, android.R.layout.simple_spinner_dropdown_item, items);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        TextView v = (TextView) super.getView(position, convertView, parent);
        Client c = getItem(position);
        v.setText("#" + c.getId() + " – " + c.getName());
        return v;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        TextView v = (TextView) super.getDropDownView(position, convertView, parent);
        Client c = getItem(position);
        v.setText("#" + c.getId() + " – " + c.getName());
        return v;
    }
}
