package pro.aedev.deliveryapp.ui.route.assignview;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import pro.aedev.deliveryapp.R;
import pro.aedev.deliveryapp.data.repo.DelivererRepository;
import pro.aedev.deliveryapp.data.repo.RouteRepository;
import pro.aedev.deliveryapp.databinding.FragmentAssignRouteBinding;
import pro.aedev.deliveryapp.model.Deliverer;
import pro.aedev.deliveryapp.model.Route;
import pro.aedev.deliveryapp.ui.adapter.DelivererSpinnerAdapter;
import pro.aedev.deliveryapp.ui.adapter.RouteSpinnerAdapter;
import pro.aedev.deliveryapp.ui.deliverer.AddDelivererFragment;
import pro.aedev.deliveryapp.util.NavigationHelper;

import java.util.List;

public class AssignRouteFragment extends Fragment {

    private FragmentAssignRouteBinding binding;
    private RouteRepository routeRepo;
    private DelivererRepository delivererRepo;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentAssignRouteBinding.inflate(inflater, container, false);
        routeRepo = new RouteRepository(requireContext());
        delivererRepo = new DelivererRepository(requireContext());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view,
                              @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        loadDeliverers();
        loadRoutes();

        // When route changes, update "currently assigned" info
        binding.spinnerRoutes.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(android.widget.AdapterView<?> parent, View view, int position, long id) {
                updateCurrentAssignmentLabel();
            }

            @Override
            public void onNothingSelected(android.widget.AdapterView<?> parent) {
                binding.textStatus.setText("");
                binding.textCurrentAssignment.setText("");
            }
        });

        // Assign button
        binding.btnAssign.setOnClickListener(v -> {
            clearStatus();

            Route selectedRoute = (Route) binding.spinnerRoutes.getSelectedItem();
            Deliverer selectedDeliverer = (Deliverer) binding.spinnerDeliverers.getSelectedItem();

            if (selectedRoute == null || selectedDeliverer == null) {
                showError(getString(R.string.error_select_route_and_deliverer));
                return;
            }

            selectedRoute.setDelivererId(selectedDeliverer.getId());
            routeRepo.update(selectedRoute);

            showSuccess(getString(R.string.route_assigned_success));
            updateCurrentAssignmentLabel();
        });

        // Unassign button
        binding.btnUnassign.setOnClickListener(v -> {
            clearStatus();

            Route selectedRoute = (Route) binding.spinnerRoutes.getSelectedItem();
            if (selectedRoute == null) {
                showError(getString(R.string.error_select_route));
                return;
            }

            if (selectedRoute.getDelivererId() == null) {
                showError(getString(R.string.error_route_already_unassigned));
                return;
            }

            selectedRoute.setDelivererId(null);
            routeRepo.update(selectedRoute);

            showSuccess(getString(R.string.route_unassigned_success));
            updateCurrentAssignmentLabel();
        });

        // "+" button to navigate to AddDelivererFragment
        binding.btnAddDeliverer.setOnClickListener(v -> {
            NavigationHelper.navigate(requireActivity(), new AddDelivererFragment());
        });

        // Back button
        binding.btnBack.setOnClickListener(v -> {
            requireActivity().getSupportFragmentManager().popBackStack();
        });

        // Initial text
        updateCurrentAssignmentLabel();
    }

    private void loadDeliverers() {
        List<Deliverer> deliverers = delivererRepo.getAll();
        DelivererSpinnerAdapter adapter = new DelivererSpinnerAdapter(requireContext(), deliverers);
        binding.spinnerDeliverers.setAdapter(adapter);
    }

    private void loadRoutes() {
        List<Route> routes = routeRepo.getAll();
        RouteSpinnerAdapter adapter = new RouteSpinnerAdapter(requireContext(), routes);
        binding.spinnerRoutes.setAdapter(adapter);
    }

    private void updateCurrentAssignmentLabel() {
        Route selectedRoute = (Route) binding.spinnerRoutes.getSelectedItem();
        if (selectedRoute == null) {
            binding.textCurrentAssignment.setText("");
            return;
        }

        Integer delivererId = selectedRoute.getDelivererId();
        if (delivererId == null) {
            binding.textCurrentAssignment.setText(getString(R.string.no_deliverer_assigned));
            return;
        }

        Deliverer d = delivererRepo.getById(delivererId);
        if (d == null) {
            binding.textCurrentAssignment.setText(getString(R.string.no_deliverer_assigned));
        } else {
            String label = getString(
                    R.string.current_deliverer_for_route,
                    d.getId(),
                    d.getName()
            );
            binding.textCurrentAssignment.setText(label);
        }
    }

    private void showError(String message) {
        binding.textStatus.setTextColor(getResources().getColor(android.R.color.holo_red_dark));
        binding.textStatus.setText(message);
    }

    private void showSuccess(String message) {
        binding.textStatus.setTextColor(getResources().getColor(android.R.color.holo_green_dark));
        binding.textStatus.setText(message);
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
