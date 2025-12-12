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

import java.util.ArrayList;
import java.util.List;

import pro.aedev.deliveryapp.data.repo.ClientRepository;
import pro.aedev.deliveryapp.data.repo.ProductRepository;
import pro.aedev.deliveryapp.data.repo.SubscriptionRepository;
import pro.aedev.deliveryapp.databinding.FragmentSubscribersListBinding;
import pro.aedev.deliveryapp.model.Client;
import pro.aedev.deliveryapp.model.Product;
import pro.aedev.deliveryapp.model.Subscription;
import pro.aedev.deliveryapp.ui.adapter.SubscriptionGridAdapter;

public class SubscribersListFragment extends Fragment {

    private FragmentSubscribersListBinding binding;

    private SubscriptionRepository subscriptionRepo;
    private ClientRepository clientRepo;
    private ProductRepository productRepo;

    private SubscriptionGridAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        binding = FragmentSubscribersListBinding.inflate(inflater, container, false);

        subscriptionRepo = new SubscriptionRepository(requireContext());
        clientRepo = new ClientRepository(requireContext());
        productRepo = new ProductRepository(requireContext());

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view,
                              @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        int span = getSpanCount();
        binding.recyclerSubscribers.setLayoutManager(
                new GridLayoutManager(requireContext(), span));

        adapter = new SubscriptionGridAdapter(new ArrayList<>());
        binding.recyclerSubscribers.setAdapter(adapter);

        loadSubscriptions();
    }

    private int getSpanCount() {
        return getResources().getConfiguration().orientation ==
                Configuration.ORIENTATION_LANDSCAPE ? 3 : 2;
    }

    private void loadSubscriptions() {
        List<Subscription> subs = subscriptionRepo.getAll();
        List<SubscriptionGridAdapter.Item> items = new ArrayList<>();

        for (Subscription s : subs) {

            Client c = clientRepo.getById(s.getClientId());
            Product p = productRepo.getById(s.getProductId());

            items.add(new SubscriptionGridAdapter.Item(
                    s.getAddress(),
                    (p != null ? p.getName() : "?"),
                    s.getQuantity(),
                    (c != null ? c.getName() : "?")
            ));
        }

        adapter.setItems(items);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
