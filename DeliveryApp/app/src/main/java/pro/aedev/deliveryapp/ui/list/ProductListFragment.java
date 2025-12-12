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
import pro.aedev.deliveryapp.databinding.FragmentProductListBinding;
import pro.aedev.deliveryapp.model.Client;
import pro.aedev.deliveryapp.model.Deliverer;
import pro.aedev.deliveryapp.model.Product;
import pro.aedev.deliveryapp.model.Route;
import pro.aedev.deliveryapp.model.Subscription;
import pro.aedev.deliveryapp.ui.adapter.ProductDeliveryGridAdapter;
import pro.aedev.deliveryapp.ui.adapter.ProductListAdapter;

public class ProductListFragment extends Fragment implements ProductListAdapter.OnProductClickListener {

    private FragmentProductListBinding binding;

    private ProductRepository productRepo;
    private SubscriptionRepository subscriptionRepo;
    private ClientRepository clientRepo;
    private RouteRepository routeRepo;
    private DelivererRepository delivererRepo;

    private ProductListAdapter productListAdapter;
    private ProductDeliveryGridAdapter deliveryAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        binding = FragmentProductListBinding.inflate(inflater, container, false);

        productRepo = new ProductRepository(requireContext());
        subscriptionRepo = new SubscriptionRepository(requireContext());
        clientRepo = new ClientRepository(requireContext());
        routeRepo = new RouteRepository(requireContext());
        delivererRepo = new DelivererRepository(requireContext());

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view,
                              @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setupProductRecycler();
        setupDeliveryRecycler();
        loadProducts();
    }

    private void setupProductRecycler() {
        binding.recyclerProducts.setLayoutManager(
                new LinearLayoutManager(requireContext()));
        productListAdapter = new ProductListAdapter(new ArrayList<>(), this);
        binding.recyclerProducts.setAdapter(productListAdapter);
    }

    private void setupDeliveryRecycler() {
        int span = getSpanCount();
        binding.recyclerProductDeliveries.setLayoutManager(
                new GridLayoutManager(requireContext(), span));
        deliveryAdapter = new ProductDeliveryGridAdapter(new ArrayList<>());
        binding.recyclerProductDeliveries.setAdapter(deliveryAdapter);
    }

    private int getSpanCount() {
        int o = getResources().getConfiguration().orientation;
        return (o == Configuration.ORIENTATION_LANDSCAPE) ? 3 : 2;
    }

    private void loadProducts() {
        List<Product> products = productRepo.getAll();
        productListAdapter.setItems(products);
    }

    @Override
    public void onProductClick(Product product) {
        if (product == null) {
            deliveryAdapter.setItems(new ArrayList<>());
            return;
        }

        List<ProductDeliveryGridAdapter.Item> items = new ArrayList<>();
        List<Subscription> subs = subscriptionRepo.getAll();

        for (Subscription s : subs) {
            if (s.getProductId() != product.getId()) continue;

            Client c = clientRepo.getById(s.getClientId());
            Route r = routeRepo.getById(s.getRouteId());
            Deliverer d = null;
            if (r != null && r.getDelivererId() != null) {
                d = delivererRepo.getById(r.getDelivererId());
            }

            String address = s.getAddress();
            String clientName = c != null ? c.getName() : "?";
            String delivererName;
            if (r == null) {
                delivererName = "No route";
            } else if (d == null) {
                delivererName = "No deliverer";
            } else {
                delivererName = d.getName();
            }

            items.add(new ProductDeliveryGridAdapter.Item(
                    address,
                    clientName,
                    delivererName,
                    s.getQuantity()
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
