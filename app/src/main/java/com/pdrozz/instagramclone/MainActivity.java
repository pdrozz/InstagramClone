package com.pdrozz.instagramclone;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

public class MainActivity extends AppCompatActivity {

    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar=findViewById(R.id.toolbarMain);
        setSupportActionBar(toolbar);
        BottomNavigationView navView = findViewById(R.id.nav_view);

        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_search, R.id.navigation_add
                , R.id.navigation_notifications, R.id.navigation_user)
                .build();
        final NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);

        navView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            itemSelect(menuItem.getTitle().toString(),navController);
            return false;
        }
    });
    }

    private void itemSelect(String title, NavController navController){
        switch (title){
            case "Meu perfil":
                navController.navigate(R.id.navigation_user);
                break;
            case "Feed":
                navController.navigate(R.id.navigation_home);
                break;
            case "Adicionar":
                navController.navigate(R.id.navigation_add);
                break;
            case "Pesquisar":
                navController.navigate(R.id.navigation_search);
                break;
            case "Notificações":
                navController.navigate(R.id.navigation_notifications);
                break;
        }
    }

}
