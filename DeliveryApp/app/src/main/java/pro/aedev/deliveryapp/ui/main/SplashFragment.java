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
        binding = null;
    }

    @Override
    public void onViewCreated(@NonNull View view,
                              @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        new Thread(() -> {
            AppDatabase.getInstance(requireContext()).getDb();

            new Handler(Looper.getMainLooper()).postDelayed(() -> {
                if (!isAdded()) return;

                requireActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .setCustomAnimations(
                                android.R.anim.fade_in,
                                android.R.anim.fade_out
                        )
                        .replace(((ViewGroup) requireActivity()
                                .findViewById(android.R.id.content))
                                .getId(), new IntroFragment())
                        .commit();

            }, MIN_DELAY);

        }).start();
    }
}
