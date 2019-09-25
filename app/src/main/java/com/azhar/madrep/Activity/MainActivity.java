package com.azhar.madrep.Activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.azhar.madrep.Fragment.DokterFragment;
import com.azhar.madrep.Fragment.HomeFragment;
import com.azhar.madrep.Fragment.KunjunganFragment;
import com.azhar.madrep.Fragment.ObatFragment;
import com.azhar.madrep.Fragment.ProfileFragment;
import com.azhar.madrep.R;

import butterknife.BindView;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.frameFragment)
    FrameLayout frameFragment;
    private TextView mTextMessage;

    public BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment = null;
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    fragment = new HomeFragment();
                    break;
                case R.id.nav_profile:
                    fragment = new ProfileFragment();
                    break;
                case R.id.nav_dokter:
                    fragment = new DokterFragment();
                    break;
                case R.id.nav_kunjungan:
                    fragment = new KunjunganFragment();
                    break;
                case R.id.nav_obat:
                    fragment = new ObatFragment();
                    break;
            }
            return loadFragment(fragment);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        /*
        ActionBar actionBar;

        actionBar = getSupportActionBar();
        assert actionBar != null;

        actionBar.setTitle("Home");
*/


        loadFragment(new HomeFragment());
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }
    public boolean loadFragment(Fragment fragment) {
        if (fragment != null){
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.frameFragment, fragment)
                    .commit();
            return true;
        }
        return false;
    }

}
