package pro.aedev.deliveryapp.ui.route.manageview;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.List;

import pro.aedev.deliveryapp.R;
import pro.aedev.deliveryapp.data.repo.RouteRepository;
import pro.aedev.deliveryapp.databinding.FragmentManageRouteBottomSectionBinding;
import pro.aedev.deliveryapp.model.Route;
import pro.aedev.deliveryapp.ui.adapter.RouteSpinnerAdapter;

public class ManageRouteFragmentBottomSection extends Fragment {

    private FragmentManageRouteBottomSectionBinding binding;
    private RouteRepository repo;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentManageRouteBottomSectionBinding.inflate(inflater, container, false);
        repo = new RouteRepository(requireContext());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loadRoutes();

        binding.btnDeleteRoute.setOnClickListener(v -> {
            Route selected = (Route) binding.spinnerRoutes.getSelectedItem();
            if (selected == null) return;

            new AlertDialog.Builder(requireContext())
                    .setTitle(R.string.delete_route)
                    .setMessage(getString(R.string.are_you_sure_you_want_to_delete_route_id,
                            selected.getId(), selected.getLabel()))
                    .setPositiveButton(R.string.confirm, (dialog, which) -> {
                        repo.delete(selected.getId());
                        loadRoutes();
                        Toast.makeText(requireContext(), R.string.route_deleted, Toast.LENGTH_SHORT).show();
                    })
                    .setNegativeButton(R.string.cancel, null)
                    .show();
        });
    }

    void loadRoutes() {
        List<Route> routes = repo.getAll();

        RouteSpinnerAdapter adapter = new RouteSpinnerAdapter(requireContext(), routes);
        binding.spinnerRoutes.setAdapter(adapter);
    }

    void selectRoute(int id) {
        for (int i = 0; i < binding.spinnerRoutes.getCount(); i++) {
            Route r = (Route) binding.spinnerRoutes.getItemAtPosition(i);
            if (r.getId() == id) {
                binding.spinnerRoutes.setSelection(i);
                break;
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
