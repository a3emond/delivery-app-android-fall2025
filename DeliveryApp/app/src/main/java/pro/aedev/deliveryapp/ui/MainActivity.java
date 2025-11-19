package pro.aedev.deliveryapp.ui;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

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
                    .replace(binding.fragmentContainer.getId(), new SplashFragment())
                    .commit();
        }
    }
}
