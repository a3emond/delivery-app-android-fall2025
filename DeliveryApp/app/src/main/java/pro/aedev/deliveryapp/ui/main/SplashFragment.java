package pro.aedev.deliveryapp.ui.main;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import pro.aedev.deliveryapp.data.db.AppDatabase;
import pro.aedev.deliveryapp.databinding.FragmentSplashBinding;
import pro.aedev.deliveryapp.util.NavigationHelper;

public class SplashFragment extends Fragment {

    private FragmentSplashBinding binding;
    private static final int MIN_DELAY = 800;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        binding = FragmentSplashBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null; // prevent memory leaks
    }

    @Override
    public void onViewCreated(@NonNull View view,
                              @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Load DB in background thread
        new Thread(() -> {

            AppDatabase.getInstance(requireContext()).getDb(); // just load db instance and do nothing with it. Ensure DB is created.

            new Handler(Looper.getMainLooper()).postDelayed(() -> {
                if (!isAdded()) return;

                NavigationHelper.navigateNoBackstack(requireActivity(), new IntroFragment());

            }, MIN_DELAY);

        }).start();
    }
}
