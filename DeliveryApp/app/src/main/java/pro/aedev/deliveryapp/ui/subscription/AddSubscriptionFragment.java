package pro.aedev.deliveryapp.ui.subscription;

import android.app.AlertDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.List;

import pro.aedev.deliveryapp.R;
import pro.aedev.deliveryapp.data.repo.ClientRepository;
import pro.aedev.deliveryapp.data.repo.ProductRepository;
import pro.aedev.deliveryapp.data.repo.RouteRepository;
import pro.aedev.deliveryapp.data.repo.SubscriptionRepository;
import pro.aedev.deliveryapp.databinding.FragmentAddSubscriptionBinding;
import pro.aedev.deliveryapp.model.Client;
import pro.aedev.deliveryapp.model.Product;
import pro.aedev.deliveryapp.model.Route;
import pro.aedev.deliveryapp.model.Subscription;
import pro.aedev.deliveryapp.ui.adapter.ClientSpinnerAdapter;
import pro.aedev.deliveryapp.ui.adapter.ProductSpinnerAdapter;
import pro.aedev.deliveryapp.ui.adapter.RouteSpinnerAdapter;

public class AddSubscriptionFragment extends Fragment {

    private FragmentAddSubscriptionBinding binding;

    private ClientRepository clientRepo;
    private ProductRepository productRepo;
    private RouteRepository routeRepo;
    private SubscriptionRepository subscriptionRepo;

    private List<Client> cachedClients;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        binding = FragmentAddSubscriptionBinding.inflate(inflater, container, false);

        clientRepo = new ClientRepository(requireContext());
        productRepo = new ProductRepository(requireContext());
        routeRepo = new RouteRepository(requireContext());
        subscriptionRepo = new SubscriptionRepository(requireContext());

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view,
                              @Nullable Bundle savedInstanceState) {

        loadClients();
        loadProducts();
        loadRoutes();

        binding.btnAddClient.setOnClickListener(v -> showCreateClientDialog());
        binding.btnSave.setOnClickListener(v -> saveSubscription());
        binding.btnBack.setOnClickListener(v -> requireActivity().getSupportFragmentManager().popBackStack());
    }

    // -----------------------------
    // CLIENT CREATION DIALOG
    // -----------------------------
    private void showCreateClientDialog() {

        View dialogView = LayoutInflater.from(requireContext())
                .inflate(R.layout.dialog_create_client, null, false);

        EditText eName = dialogView.findViewById(R.id.editName);
        EditText eAddress = dialogView.findViewById(R.id.editAddress);
        EditText ePhone = dialogView.findViewById(R.id.editPhone);

        new AlertDialog.Builder(requireContext())
                .setTitle("Créer un nouveau client")
                .setView(dialogView)
                .setPositiveButton("Créer", (dialog, which) -> {

                    String name = eName.getText().toString().trim();
                    String address = eAddress.getText().toString().trim();
                    String phone = ePhone.getText().toString().trim();

                    if (TextUtils.isEmpty(name) || TextUtils.isEmpty(address)) {
                        showError("Nom et adresse requis.");
                        return;
                    }

                    Client c = new Client();
                    c.setName(name);
                    c.setAddress(address);
                    c.setPhone(phone);

                    long newId = clientRepo.insert(c);

                    if (newId > 0) {
                        showSuccess("Client créé (#" + newId + ")");
                        reloadClientsAndSelect((int)newId);
                    } else {
                        showError("Erreur lors de la création.");
                    }

                })
                .setNegativeButton("Annuler", null)
                .show();
    }

    // -----------------------------
    // LOADERS
    // -----------------------------
    private void loadClients() {
        cachedClients = clientRepo.getAll();
        ClientSpinnerAdapter adapter = new ClientSpinnerAdapter(requireContext(), cachedClients);
        binding.spinnerClients.setAdapter(adapter);
    }

    private void reloadClientsAndSelect(int newClientId) {
        loadClients();
        for (int i = 0; i < cachedClients.size(); i++) {
            if (cachedClients.get(i).getId() == newClientId) {
                binding.spinnerClients.setSelection(i);
                break;
            }
        }
    }

    private void loadProducts() {
        List<Product> products = productRepo.getAll();
        ProductSpinnerAdapter adapter = new ProductSpinnerAdapter(requireContext(), products);
        binding.spinnerProducts.setAdapter(adapter);
    }

    private void loadRoutes() {
        List<Route> routes = routeRepo.getAll();
        RouteSpinnerAdapter adapter = new RouteSpinnerAdapter(requireContext(), routes);
        binding.spinnerRoutes.setAdapter(adapter);
    }

    // -----------------------------
    // SAVE SUBSCRIPTION
    // -----------------------------
    private void saveSubscription() {

        clearStatus();

        Client client = (Client) binding.spinnerClients.getSelectedItem();
        Product product = (Product) binding.spinnerProducts.getSelectedItem();
        Route route = (Route) binding.spinnerRoutes.getSelectedItem();

        String address = binding.editAddress.getText().toString().trim();
        String qtyStr = binding.editQuantity.getText().toString().trim();
        String startDate = binding.editStartDate.getText().toString().trim();
        String endDate = binding.editEndDate.getText().toString().trim();

        if (client == null) { showError("Sélectionnez un client."); return; }
        if (product == null) { showError("Sélectionnez un produit."); return; }
        if (TextUtils.isEmpty(address)) { showError("Adresse requise."); return; }
        if (TextUtils.isEmpty(qtyStr)) { showError("Quantité requise."); return; }

        int qty;
        try {
            qty = Integer.parseInt(qtyStr);
            if (qty <= 0) throw new NumberFormatException();
        } catch (Exception e) {
            showError("Quantité invalide.");
            return;
        }

        int routeId = (route != null) ? route.getId() : 0;

        Subscription s = new Subscription(
                0,
                client.getId(),
                routeId,
                address,
                startDate,
                endDate,
                product.getId(),
                qty
        );

        long id = subscriptionRepo.insert(s);

        if (id > 0)
            showSuccess("Abonnement #" + id + " ajouté.");
        else
            showError("Erreur lors de l'ajout.");
    }

    // -----------------------------
    // UI HELPERS
    // -----------------------------
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
