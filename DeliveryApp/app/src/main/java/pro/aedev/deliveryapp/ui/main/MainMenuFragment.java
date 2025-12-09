package pro.aedev.deliveryapp.ui.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import pro.aedev.deliveryapp.databinding.FragmentMainMenuBinding;
import pro.aedev.deliveryapp.ui.route.manageview.ManageRoutesFragment;
import pro.aedev.deliveryapp.ui.route.assignview.AssignRouteFragment;
import pro.aedev.deliveryapp.ui.subscription.AddSubscriptionFragment;
import pro.aedev.deliveryapp.ui.subscription.RemoveSubscriptionFragment;
import pro.aedev.deliveryapp.ui.deliverer.AddDelivererFragment;
import pro.aedev.deliveryapp.ui.deliverer.RemoveDelivererFragment;
import pro.aedev.deliveryapp.ui.list.ListFragment;
import pro.aedev.deliveryapp.util.NavigationHelper;

public class MainMenuFragment extends Fragment {

    private FragmentMainMenuBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        binding = FragmentMainMenuBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onViewCreated(@NonNull View view,
                              @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupButtons();
    }

    private void animatePress(View v) {
        v.animate().scaleX(0.96f).scaleY(0.96f).setDuration(70)
                .withEndAction(() ->
                        v.animate().scaleX(1f).scaleY(1f).setDuration(70)
                ).start();
    }

    private void setupButtons() {

        binding.btnManageRoutes.setOnClickListener(v -> {
            animatePress(v);
            NavigationHelper.navigate(requireActivity(), new ManageRoutesFragment());
        });

        binding.btnAssignRoute.setOnClickListener(v -> {
            animatePress(v);
            NavigationHelper.navigate(requireActivity(), new AssignRouteFragment());
        });

        binding.btnAddSubscription.setOnClickListener(v -> {
            animatePress(v);
            NavigationHelper.navigate(requireActivity(), new AddSubscriptionFragment());
        });

        binding.btnRemoveSubscription.setOnClickListener(v -> {
            animatePress(v);
            NavigationHelper.navigate(requireActivity(), new RemoveSubscriptionFragment());
        });

        binding.btnAddDeliverer.setOnClickListener(v -> {
            animatePress(v);
            NavigationHelper.navigate(requireActivity(), new AddDelivererFragment());
        });

        binding.btnRemoveDeliverer.setOnClickListener(v -> {
            animatePress(v);
            NavigationHelper.navigate(requireActivity(), new RemoveDelivererFragment());
        });

        binding.btnList.setOnClickListener(v -> {
            animatePress(v);
            NavigationHelper.navigate(requireActivity(), new ListFragment());
        });

        binding.btnQuit.setOnClickListener(v -> {
            animatePress(v);
            requireActivity().finish();
        });
    }
}
