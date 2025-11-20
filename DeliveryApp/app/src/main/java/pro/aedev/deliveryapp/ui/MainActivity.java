package pro.aedev.deliveryapp.ui;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import pro.aedev.deliveryapp.R;
import pro.aedev.deliveryapp.databinding.ActivityMainBinding;
import pro.aedev.deliveryapp.ui.main.SplashFragment;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, new SplashFragment())
                    .commit();
        }
    }
}
