package pro.aedev.deliveryapp.ui.list;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import pro.aedev.deliveryapp.data.db.AppDatabase;
import pro.aedev.deliveryapp.data.repo.ClientRepository;
import pro.aedev.deliveryapp.data.repo.DelivererRepository;
import pro.aedev.deliveryapp.data.repo.ProductRepository;
import pro.aedev.deliveryapp.data.repo.RouteRepository;
import pro.aedev.deliveryapp.data.repo.SubscriptionRepository;
import pro.aedev.deliveryapp.databinding.FragmentDelivererListBinding;
import pro.aedev.deliveryapp.model.Client;
import pro.aedev.deliveryapp.model.Deliverer;
import pro.aedev.deliveryapp.model.Product;
import pro.aedev.deliveryapp.model.Route;
import pro.aedev.deliveryapp.model.Subscription;
import pro.aedev.deliveryapp.ui.adapter.DelivererDeliveryAdapter;
import pro.aedev.deliveryapp.ui.adapter.DelivererListAdapter;

public class DelivererListFragment extends Fragment
        implements DelivererListAdapter.OnDelivererClickListener {

    private FragmentDelivererListBinding binding;

    private DelivererRepository delivererRepo;
    private RouteRepository routeRepo;
    private SubscriptionRepository subscriptionRepo;
    private ProductRepository productRepo;
    private ClientRepository clientRepo;

    private DelivererListAdapter delivererListAdapter;
    private DelivererDeliveryAdapter deliveryAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        binding = FragmentDelivererListBinding.inflate(inflater, container, false);

        delivererRepo = new DelivererRepository(requireContext());
        routeRepo = new RouteRepository(requireContext());
        subscriptionRepo = new SubscriptionRepository(requireContext());
        productRepo = new ProductRepository(requireContext());
        clientRepo = new ClientRepository(requireContext());

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view,
                              @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setupDeliverersRecycler();
        setupDeliveriesRecycler();

        loadDeliverers();
    }

    private void setupDeliverersRecycler() {
        binding.recyclerDeliverers.setLayoutManager(
                new LinearLayoutManager(requireContext())
        );
        delivererListAdapter = new DelivererListAdapter(new ArrayList<>(), this);
        binding.recyclerDeliverers.setAdapter(delivererListAdapter);
    }

    private void setupDeliveriesRecycler() {
        int span = getSpanCount();
        binding.recyclerDeliveries.setLayoutManager(
                new GridLayoutManager(requireContext(), span)
        );
        deliveryAdapter = new DelivererDeliveryAdapter(new ArrayList<>());
        binding.recyclerDeliveries.setAdapter(deliveryAdapter);
    }

    private int getSpanCount() {
        int orientation = getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            return 3; // choice C
        }
        return 2; // portrait
    }

    private void loadDeliverers() {
        List<Deliverer> deliverers = delivererRepo.getAll();
        delivererListAdapter.setItems(deliverers);
    }

    @Override
    public void onDelivererClick(Deliverer deliverer) {
        if (deliverer == null) {
            deliveryAdapter.setItems(new ArrayList<>());
            return;
        }
        List<DelivererDeliveryAdapter.DeliveryItem> items =
                buildDeliveriesForDeliverer(deliverer.getId());
        deliveryAdapter.setItems(items);
    }

    private List<DelivererDeliveryAdapter.DeliveryItem> buildDeliveriesForDeliverer(int delivererId) {

        List<Route> allRoutes = routeRepo.getAll();
        List<Subscription> allSubs = subscriptionRepo.getAll();

        // 1) Find all route IDs for this deliverer
        Set<Integer> routeIds = new HashSet<>();
        for (Route r : allRoutes) {
            Integer dId = r.getDelivererId();
            if (dId != null && dId == delivererId) {
                routeIds.add(r.getId());
            }
        }

        List<DelivererDeliveryAdapter.DeliveryItem> result = new ArrayList<>();

        // 2) For each subscription on those routes, build delivery item
        for (Subscription s : allSubs) {
            if (!routeIds.contains(s.getRouteId())) continue;

            Client c = clientRepo.getById(s.getClientId());
            Product p = productRepo.getById(s.getProductId());

            String clientName = (c != null) ? c.getName() : "?";
            String productName = (p != null) ? p.getName() : "?";

            DelivererDeliveryAdapter.DeliveryItem item =
                    new DelivererDeliveryAdapter.DeliveryItem(
                            s.getAddress(),
                            productName,
                            s.getQuantity(),
                            clientName
                    );
            result.add(item);
        }

        return result;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
