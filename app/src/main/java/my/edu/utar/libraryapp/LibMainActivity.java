package my.edu.utar.libraryapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;
import android.view.WindowManager;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class LibMainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lib_main);

        // hide the status/system bar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // hide the title/action bar
        getSupportActionBar().hide();

        //Initialize Bottom Navigation View.
        BottomNavigationView navView = findViewById(R.id.bottomLibNav_view);

        //Pass the ID's of Different destinations
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_lib_book, R.id.navigation_lib_room, R.id.navigation_lib_admin )
                .build();

        //Initialize NavController.
        NavController navController = Navigation.findNavController(this, R.id.navHostFragmentLib);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);
    }
}