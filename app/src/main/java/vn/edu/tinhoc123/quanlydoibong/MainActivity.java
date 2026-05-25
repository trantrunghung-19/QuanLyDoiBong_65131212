package vn.edu.tinhoc123.quanlydoibong;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import vn.edu.tinhoc123.quanlydoibong.fragments.CoachFragment;
import vn.edu.tinhoc123.quanlydoibong.fragments.HomeFragment;
import vn.edu.tinhoc123.quanlydoibong.fragments.TeamFragment;

public class MainActivity extends AppCompatActivity {
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, new HomeFragment())
                .commit();
        }

        bottomNavigationView.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = null;
            int id = item.getItemId();
            if (id == R.id.nav_home) {
                selectedFragment = new HomeFragment();
            } else if (id == R.id.nav_team) {
                selectedFragment = new TeamFragment();
            } else if (id == R.id.nav_coach) {
                selectedFragment = new CoachFragment();
            }

            if (selectedFragment != null) {
                getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, selectedFragment)
                    .commit();
            }
            return true;
        });
    }

    public void navigateToTeamTab() {
        bottomNavigationView.setSelectedItemId(R.id.nav_team);
    }
}