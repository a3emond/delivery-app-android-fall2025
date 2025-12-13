package pro.aedev.deliveryapp.ui.deliverer;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import pro.aedev.deliveryapp.R;
import pro.aedev.deliveryapp.data.repo.DelivererRepository;
import pro.aedev.deliveryapp.databinding.FragmentAddDelivererBinding;
import pro.aedev.deliveryapp.model.Deliverer;
import pro.aedev.deliveryapp.ui.adapter.DelivererSpinnerAdapter;

import java.util.List;

public class AddDelivererFragment extends Fragment {

    private FragmentAddDelivererBinding binding;
    private DelivererRepository repo;
    private Deliverer currentSelected;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentAddDelivererBinding.inflate(inflater, container, false);
        repo = new DelivererRepository(requireContext());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view,
                              @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        loadDeliverers(); // Load deliverers into spinner

        // Handle spinner selection
        binding.spinnerDeliverers.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(android.widget.AdapterView<?> parent, View view, int position, long id) {
                Deliverer d = (Deliverer) binding.spinnerDeliverers.getSelectedItem();
                currentSelected = d;
                if (d != null) {
                    binding.editName.setText(d.getName());
                    binding.editAddress.setText(d.getAddress());
                    binding.editPhone.setText(d.getPhone());
                }
                clearStatus();
            }

            @Override
            public void onNothingSelected(android.widget.AdapterView<?> parent) {
                currentSelected = null;
                clearFields();
                clearStatus();
            }
        });

        // Add new deliverer
        binding.btnAddDeliverer.setOnClickListener(v -> {
            clearStatus();
            String name = binding.editName.getText().toString().trim();
            String address = binding.editAddress.getText().toString().trim();
            String phone = binding.editPhone.getText().toString().trim();

            if (TextUtils.isEmpty(name)) {
                showError(getString(R.string.deliverer_name_required));
                return;
            }

            Deliverer d = new Deliverer();
            d.setName(name);
            d.setAddress(address);
            d.setPhone(phone);

            long id = repo.insert(d);
            showSuccess(getString(R.string.deliverer_created, id));
            loadDeliverers();
        });

        // Update selected deliverer
        binding.btnUpdateDeliverer.setOnClickListener(v -> {
            clearStatus();
            if (currentSelected == null) {
                showError(getString(R.string.error_select_deliverer_to_update));
                return;
            }

            String name = binding.editName.getText().toString().trim();
            String address = binding.editAddress.getText().toString().trim();
            String phone = binding.editPhone.getText().toString().trim();

            if (TextUtils.isEmpty(name)) {
                showError(getString(R.string.deliverer_name_required));
                return;
            }

            currentSelected.setName(name);
            currentSelected.setAddress(address);
            currentSelected.setPhone(phone);

            repo.update(currentSelected);

            showSuccess(getString(R.string.deliverer_updated));
            loadDeliverers();
        });

        // Clear / new mode
        binding.btnClearForm.setOnClickListener(v -> {
            binding.spinnerDeliverers.setSelection(-1);
            currentSelected = null;
            clearFields();
            clearStatus();
        });

        // Back
        binding.btnBack.setOnClickListener(v -> {
            requireActivity().getSupportFragmentManager().popBackStack();
        });
    }

    // Load deliverers into spinner
    private void loadDeliverers() {
        List<Deliverer> list = repo.getAll();
        DelivererSpinnerAdapter adapter = new DelivererSpinnerAdapter(requireContext(), list);
        binding.spinnerDeliverers.setAdapter(adapter);
    }

    private void clearFields() {
        binding.editName.setText("");
        binding.editAddress.setText("");
        binding.editPhone.setText("");
    }

    // Status display methods
    private void showError(String msg) {
        binding.textStatus.setTextColor(getResources().getColor(android.R.color.holo_red_dark));
        binding.textStatus.setText(msg);
    }

    private void showSuccess(String msg) {
        binding.textStatus.setTextColor(getResources().getColor(android.R.color.holo_green_dark));
        binding.textStatus.setText(msg);
    }

    private void clearStatus() {
        binding.textStatus.setText("");
    }

    // Clean up binding
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
