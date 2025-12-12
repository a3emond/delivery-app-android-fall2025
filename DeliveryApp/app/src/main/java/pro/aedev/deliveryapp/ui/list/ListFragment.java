package pro.aedev.deliveryapp.ui.list;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import pro.aedev.deliveryapp.databinding.FragmentListBinding;

public class ListFragment extends Fragment {

    private FragmentListBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        binding = FragmentListBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view,
                              @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.btnDeliverers.setOnClickListener(v ->
                showChild(new DelivererListFragment()));

        binding.btnSubscribers.setOnClickListener(v ->
                showChild(new SubscribersListFragment()));

        binding.btnRoutes.setOnClickListener(v ->
                showChild(new RouteListFragment()));

        binding.btnProducts.setOnClickListener(v ->
                showChild(new ProductListFragment()));

        binding.btnBack.setOnClickListener(v -> {
            requireActivity().getSupportFragmentManager().popBackStack();
        });

        // Default view: deliverers list
        if (savedInstanceState == null) {
            showChild(new DelivererListFragment());
        }
    }

    private void showChild(Fragment fragment) {
        getChildFragmentManager()
                .beginTransaction()
                .replace(binding.rightContainer.getId(), fragment)
                .commit();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
