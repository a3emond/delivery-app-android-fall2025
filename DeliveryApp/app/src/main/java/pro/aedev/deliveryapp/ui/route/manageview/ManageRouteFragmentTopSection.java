package pro.aedev.deliveryapp.ui.route.manageview;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import pro.aedev.deliveryapp.R;
import pro.aedev.deliveryapp.data.repo.RouteRepository;
import pro.aedev.deliveryapp.databinding.FragmentManageRouteTopSectionBinding;
import pro.aedev.deliveryapp.model.Route;

public class ManageRouteFragmentTopSection extends Fragment {

    private FragmentManageRouteTopSectionBinding binding;
    private RouteRepository repo;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentManageRouteTopSectionBinding.inflate(inflater, container, false);
        repo = new RouteRepository(requireContext());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.btnAddRoute.setOnClickListener(v -> showAddDialog());
    }

    private void showAddDialog() {
        EditText input = new EditText(requireContext());
        input.setHint(R.string.default_route_label);

        new AlertDialog.Builder(requireContext())
                .setTitle(R.string.add_route)
                .setView(input)
                .setPositiveButton(R.string.confirm, (dialog, which) -> {
                    String label = input.getText().toString().trim();
                    if (label.isEmpty()) {
                        Toast.makeText(requireContext(), R.string.route_label_required, Toast.LENGTH_SHORT).show();
                        return;
                    }

                    Route r = new Route(0, label, null);
                    long id = repo.insert(r);

                    Toast.makeText(
                            requireContext(),
                            getString(R.string.route_created_id, id),
                            Toast.LENGTH_SHORT
                    ).show();

                    ManageRoutesFragment parent = (ManageRoutesFragment) getParentFragment();
                    if (parent != null && parent.bottom != null) {
                        parent.bottom.loadRoutes();
                        parent.bottom.selectRoute((int) id);
                    }
                })
                .setNegativeButton(R.string.cancel, null)
                .show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
