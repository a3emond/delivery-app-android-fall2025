package pro.aedev.deliveryapp.ui.main;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import pro.aedev.deliveryapp.databinding.FragmentIntroBinding;
import pro.aedev.deliveryapp.util.NavigationHelper;

public class IntroFragment extends Fragment {

    private FragmentIntroBinding binding;

    private float startX;
    private static final float SWIPE_THRESHOLD = 120f;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        binding = FragmentIntroBinding.inflate(inflater, container, false);
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

        setupSwipeListener();
        startSwipeArrowAnimation();
    }

    @SuppressLint("ClickableViewAccessibility")
    private void setupSwipeListener() {
        binding.introRoot.setOnTouchListener((v, event) -> {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    startX = event.getX();
                    return true;

                case MotionEvent.ACTION_UP:
                    float endX = event.getX();
                    float deltaX = startX - endX;

                    if (deltaX > SWIPE_THRESHOLD) {
                        navigateToMenu();
                    }
                    return true;
            }
            return false;
        });
    }

    private void startSwipeArrowAnimation() {
        if (binding == null) return;

        binding.swipeArrow.animate()
                .translationX(-20f)
                .alpha(0.2f)
                .setDuration(600)
                .withEndAction(() -> {
                    if (binding != null)
                        binding.swipeArrow.animate()
                                .translationX(0f)
                                .alpha(0.5f)
                                .setDuration(600)
                                .withEndAction(this::startSwipeArrowAnimation)
                                .start();
                }).start();
    }

    private void navigateToMenu() {
        if (!isAdded()) return;

        NavigationHelper.navigate(requireActivity(), new MainMenuFragment());
    }
}
