package com.pdrozz.instagramclone.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.pdrozz.instagramclone.FirebaseHelperManager.FirebaseAuthManager;
import com.pdrozz.instagramclone.R;
import com.squareup.picasso.Picasso;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private FirebaseAuthManager manager=new FirebaseAuthManager(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar=findViewById(R.id.toolbarMain);
        setSupportActionBar(toolbar);
        getSupportActionBar().setElevation(4);
        BottomNavigationView navView = findViewById(R.id.nav_view);

        isLogado();

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

    private void isLogado(){
        FirebaseUser user=manager.getCurrentUser();
        if (user==null){
            finish();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_sair,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.item_sair:

                manager.logout();
                startActivity(new Intent(this,LoginActivity.class));
                finish();

                try{
                    File profile_image=new File(getFilesDir(),"profile_image.jpeg");
                    boolean profile_pic_exists=profile_image.delete();
                    if (profile_pic_exists){
                        profile_image.deleteOnExit();
                    }

                }catch (Exception e){
                    e.printStackTrace();
                }

                break;
        }

        return super.onOptionsItemSelected(item);
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
