package pro.aedev.deliveryapp.util;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import pro.aedev.deliveryapp.R;

public class NavigationHelper {

    public static void navigate(FragmentActivity activity, Fragment fragment) {
        activity.getSupportFragmentManager()
                .beginTransaction()
                .setCustomAnimations(
                        android.R.anim.fade_in,
                        android.R.anim.fade_out,
                        android.R.anim.fade_in,
                        android.R.anim.fade_out
                )
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit();
    }

    public static void navigateNoBackstack(FragmentActivity activity, Fragment fragment) {
        activity.getSupportFragmentManager()
                .beginTransaction()
                .setCustomAnimations(
                        android.R.anim.fade_in,
                        android.R.anim.fade_out
                )
                .replace(R.id.fragment_container, fragment)
                .commit();
    }
}
