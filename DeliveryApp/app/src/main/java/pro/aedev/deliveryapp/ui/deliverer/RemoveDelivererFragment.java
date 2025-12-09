package pro.aedev.deliveryapp.ui.deliverer;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import java.util.List;

import pro.aedev.deliveryapp.R;
import pro.aedev.deliveryapp.data.repo.DelivererRepository;
import pro.aedev.deliveryapp.data.repo.RouteRepository;
import pro.aedev.deliveryapp.databinding.FragmentRemoveDelivererBinding;
import pro.aedev.deliveryapp.model.Deliverer;
import pro.aedev.deliveryapp.model.Route;
import pro.aedev.deliveryapp.ui.adapter.DelivererSpinnerAdapter;

public class RemoveDelivererFragment extends Fragment {

    private FragmentRemoveDelivererBinding binding;
    private DelivererRepository delivererRepo;
    private RouteRepository routeRepo;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentRemoveDelivererBinding.inflate(inflater, container, false);
        delivererRepo = new DelivererRepository(requireContext());
        routeRepo = new RouteRepository(requireContext());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view,
                              @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loadDeliverers();

        binding.btnRemoveDeliverer.setOnClickListener(v -> {
            clearStatus();

            Deliverer selected = (Deliverer) binding.spinnerDeliverers.getSelectedItem();
            if (selected == null) {
                showError(getString(R.string.error_select_deliverer));
                return;
            }

            confirmDelete(selected);
        });

        binding.btnBack.setOnClickListener(v -> {
            requireActivity().getSupportFragmentManager().popBackStack();
        });
    }

    private void confirmDelete(Deliverer d) {
        new AlertDialog.Builder(requireContext())
                .setTitle(R.string.delete_deliverer)
                .setMessage(getString(R.string.confirm_delete_deliverer, d.getId(), d.getName()))
                .setPositiveButton(R.string.confirm, (dialog, which) -> {
                    unassignIfNeeded(d.getId());
                    delivererRepo.delete(d.getId());
                    showSuccess(getString(R.string.deliverer_deleted));
                    loadDeliverers();
                })
                .setNegativeButton(R.string.cancel, null)
                .show();
    }

    private void unassignIfNeeded(int delivererId) {
        List<Route> allRoutes = routeRepo.getAll();
        for (Route r : allRoutes) {
            if (r.getDelivererId() != null && r.getDelivererId() == delivererId) {
                r.setDelivererId(null);
                routeRepo.update(r);
            }
        }
    }

    private void loadDeliverers() {
        List<Deliverer> list = delivererRepo.getAll();
        DelivererSpinnerAdapter adapter = new DelivererSpinnerAdapter(requireContext(), list);
        binding.spinnerDeliverers.setAdapter(adapter);
    }

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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
