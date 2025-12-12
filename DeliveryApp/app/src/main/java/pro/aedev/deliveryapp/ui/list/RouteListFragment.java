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
import java.util.List;

import pro.aedev.deliveryapp.data.repo.ClientRepository;
import pro.aedev.deliveryapp.data.repo.DelivererRepository;
import pro.aedev.deliveryapp.data.repo.ProductRepository;
import pro.aedev.deliveryapp.data.repo.RouteRepository;
import pro.aedev.deliveryapp.data.repo.SubscriptionRepository;
import pro.aedev.deliveryapp.databinding.FragmentRouteListBinding;
import pro.aedev.deliveryapp.model.Client;
import pro.aedev.deliveryapp.model.Deliverer;
import pro.aedev.deliveryapp.model.Product;
import pro.aedev.deliveryapp.model.Route;
import pro.aedev.deliveryapp.model.Subscription;
import pro.aedev.deliveryapp.ui.adapter.DelivererDeliveryAdapter;
import pro.aedev.deliveryapp.ui.adapter.RouteListAdapter;

public class RouteListFragment extends Fragment implements RouteListAdapter.OnRouteClickListener {

    private FragmentRouteListBinding binding;

    private RouteRepository routeRepo;
    private SubscriptionRepository subscriptionRepo;
    private ClientRepository clientRepo;
    private ProductRepository productRepo;
    private DelivererRepository delivererRepo;

    private RouteListAdapter routeListAdapter;
    private DelivererDeliveryAdapter deliveryAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        binding = FragmentRouteListBinding.inflate(inflater, container, false);

        routeRepo = new RouteRepository(requireContext());
        subscriptionRepo = new SubscriptionRepository(requireContext());
        clientRepo = new ClientRepository(requireContext());
        productRepo = new ProductRepository(requireContext());
        delivererRepo = new DelivererRepository(requireContext());

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view,
                              @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setupRouteRecycler();
        setupDeliveriesRecycler();
        loadRoutes();
    }

    private void setupRouteRecycler() {
        binding.recyclerRoutes.setLayoutManager(
                new LinearLayoutManager(requireContext()));
        routeListAdapter = new RouteListAdapter(new ArrayList<>(), this);
        binding.recyclerRoutes.setAdapter(routeListAdapter);
    }

    private void setupDeliveriesRecycler() {
        int span = getSpanCount();
        binding.recyclerRouteDeliveries.setLayoutManager(
                new GridLayoutManager(requireContext(), span));
        deliveryAdapter = new DelivererDeliveryAdapter(new ArrayList<>());
        binding.recyclerRouteDeliveries.setAdapter(deliveryAdapter);
    }

    private int getSpanCount() {
        int o = getResources().getConfiguration().orientation;
        return (o == Configuration.ORIENTATION_LANDSCAPE) ? 3 : 2;
    }

    private void loadRoutes() {
        List<Route> routes = routeRepo.getAll();
        routeListAdapter.setItems(routes, delivererRepo);
    }

    @Override
    public void onRouteClick(Route route) {
        if (route == null) {
            deliveryAdapter.setItems(new ArrayList<>());
            return;
        }

        List<DelivererDeliveryAdapter.DeliveryItem> items = new ArrayList<>();
        List<Subscription> subs = subscriptionRepo.getAll();

        for (Subscription s : subs) {
            if (s.getRouteId() != route.getId()) continue;

            Client c = clientRepo.getById(s.getClientId());
            Product p = productRepo.getById(s.getProductId());

            String clientName = c != null ? c.getName() : "?";
            String productName = p != null ? p.getName() : "?";

            items.add(new DelivererDeliveryAdapter.DeliveryItem(
                    s.getAddress(),
                    productName,
                    s.getQuantity(),
                    clientName
            ));
        }

        deliveryAdapter.setItems(items);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
