package pro.aedev.deliveryapp.ui.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

import pro.aedev.deliveryapp.model.Product;

public class ProductSpinnerAdapter extends ArrayAdapter<Product> {

    public ProductSpinnerAdapter(@NonNull Context context, @NonNull List<Product> products) {
        super(context, android.R.layout.simple_spinner_dropdown_item, products);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        TextView v = (TextView) super.getView(position, convertView, parent);
        Product p = getItem(position);
        v.setText(p.getName());
        return v;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        TextView v = (TextView) super.getDropDownView(position, convertView, parent);
        Product p = getItem(position);
        v.setText(p.getName());
        return v;
    }
}
