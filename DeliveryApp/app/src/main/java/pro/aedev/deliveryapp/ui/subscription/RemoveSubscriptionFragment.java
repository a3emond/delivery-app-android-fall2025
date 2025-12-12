package pro.aedev.deliveryapp.ui.subscription;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.List;

import pro.aedev.deliveryapp.data.repo.ClientRepository;
import pro.aedev.deliveryapp.data.repo.ProductRepository;
import pro.aedev.deliveryapp.data.repo.RouteRepository;
import pro.aedev.deliveryapp.data.repo.SubscriptionRepository;
import pro.aedev.deliveryapp.databinding.FragmentRemoveSubscriptionBinding;
import pro.aedev.deliveryapp.model.Client;
import pro.aedev.deliveryapp.model.Product;
import pro.aedev.deliveryapp.model.Route;
import pro.aedev.deliveryapp.model.Subscription;
import pro.aedev.deliveryapp.ui.adapter.SubscriptionSpinnerAdapter;

public class RemoveSubscriptionFragment extends Fragment {

    private FragmentRemoveSubscriptionBinding binding;

    private SubscriptionRepository subscriptionRepo;
    private ClientRepository clientRepo;
    private RouteRepository routeRepo;
    private ProductRepository productRepo;

    private List<Subscription> cachedSubscriptions;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        binding = FragmentRemoveSubscriptionBinding.inflate(inflater, container, false);

        subscriptionRepo = new SubscriptionRepository(requireContext());
        clientRepo = new ClientRepository(requireContext());
        routeRepo = new RouteRepository(requireContext());
        productRepo = new ProductRepository(requireContext());

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view,
                              @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        loadSubscriptions();

        binding.btnDisplay.setOnClickListener(v -> displaySelectedSubscription());
        binding.btnDelete.setOnClickListener(v -> deleteSelectedSubscription());
        binding.btnBack.setOnClickListener(v -> requireActivity().getSupportFragmentManager().popBackStack());
    }


    // ---------------------------------------------------------
    // LOAD SUBSCRIPTIONS FOR SPINNER
    // ---------------------------------------------------------
    private void loadSubscriptions() {
        cachedSubscriptions = subscriptionRepo.getAll();

        SubscriptionSpinnerAdapter adapter =
                new SubscriptionSpinnerAdapter(requireContext(), cachedSubscriptions);

        binding.spinnerSubscriptions.setAdapter(adapter);
    }


    // ---------------------------------------------------------
    // DISPLAY SELECTED SUBSCRIPTION DETAILS
    // ---------------------------------------------------------
    private void displaySelectedSubscription() {
        clearStatus();

        Subscription s = (Subscription) binding.spinnerSubscriptions.getSelectedItem();
        if (s == null) {
            showError("No subscription selected.");
            return;
        }

        Client c = clientRepo.getById(s.getClientId());
        Product p = productRepo.getById(s.getProductId());
        Route r = routeRepo.getById(s.getRouteId());

        binding.textDetails.setText(
                "Subscription #" + s.getId() + "\n\n" +
                        "Client: " + (c != null ? c.getName() : "?") + "\n" +
                        "Address: " + s.getAddress() + "\n" +
                        "Product: " + (p != null ? p.getName() : "?") + "\n" +
                        "Quantity: " + s.getQuantity() + "\n" +
                        "Route: " + (r != null ? r.getId() : 0) + "\n" +
                        "Start: " + (s.getStartDate() != null ? s.getStartDate() : "-") + "\n" +
                        "End: " + (s.getEndDate() != null ? s.getEndDate() : "-")
        );
    }


    // ---------------------------------------------------------
    // DELETE SELECTED SUBSCRIPTION
    // ---------------------------------------------------------
    private void deleteSelectedSubscription() {
        clearStatus();

        Subscription s = (Subscription) binding.spinnerSubscriptions.getSelectedItem();
        if (s == null) {
            showError("No subscription selected.");
            return;
        }

        int count = subscriptionRepo.delete(s.getId());

        if (count > 0) {
            showSuccess("Subscription #" + s.getId() + " deleted.");
            loadSubscriptions();
            binding.textDetails.setText("");
        } else {
            showError("Failed to delete subscription.");
        }
    }


    // ---------------------------------------------------------
    // UI HELPERS
    // ---------------------------------------------------------
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
