package pro.aedev.deliveryapp.ui;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import pro.aedev.deliveryapp.R;
import pro.aedev.deliveryapp.databinding.ActivityMainBinding;
import pro.aedev.deliveryapp.ui.main.SplashFragment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, new SplashFragment())
                    .commit();
        }
    }

}
// The first fragment is added programmatically rather than via android:name in XML.
// If a fragment is auto-attached by the layout system, it is created outside the
// app’s navigation flow, causing the FragmentManager to track it differently.
// Initializing it manually ensures the navigation controller owns every fragment
// from the start, preventing mismatched states, broken transitions, and back
// stack inconsistencies.

