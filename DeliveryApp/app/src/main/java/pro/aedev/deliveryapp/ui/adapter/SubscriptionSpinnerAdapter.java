package pro.aedev.deliveryapp.ui.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

import pro.aedev.deliveryapp.model.Subscription;

/**
 * Adapter for displaying Subscription items in a Spinner. (single-line text representation subscription ID and client ID)
 */
public class SubscriptionSpinnerAdapter extends ArrayAdapter<Subscription> {

    public SubscriptionSpinnerAdapter(@NonNull Context ctx, @NonNull List<Subscription> items) {
        super(ctx, android.R.layout.simple_spinner_dropdown_item, items);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        TextView v = (TextView) super.getView(position, convertView, parent);
        Subscription s = getItem(position);
        v.setText(format(s));
        return v;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        TextView v = (TextView) super.getDropDownView(position, convertView, parent);
        Subscription s = getItem(position);
        v.setText(format(s));
        return v;
    }

    private String format(Subscription s) {
        return "#" + s.getId() + " – Client " + s.getClientId();
    }
}
