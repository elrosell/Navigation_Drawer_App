package com.example.navigation_drawer_app;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout cajonDeNavegacion;
    private NavigationView vistaDeNavegacion;
    private MaterialToolbar barraDeHerramientas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        cajonDeNavegacion = findViewById(R.id.drawer_layout);
        vistaDeNavegacion = findViewById(R.id.nav_view);
        barraDeHerramientas = findViewById(R.id.top_app_bar);

        ViewCompat.setOnApplyWindowInsetsListener(cajonDeNavegacion, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        setSupportActionBar(barraDeHerramientas);
        ActionBarDrawerToggle alternador = new ActionBarDrawerToggle(
                this,
                cajonDeNavegacion,
                barraDeHerramientas,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        cajonDeNavegacion.addDrawerListener(alternador);
        alternador.syncState();

        vistaDeNavegacion.setNavigationItemSelectedListener(item -> {
            Fragment fragmento;
            String titulo;
            int idElemento = item.getItemId();
            if (idElemento == R.id.nav_profile) {
                fragmento = new ProfileFragment();
                titulo = getString(R.string.menu_profile);
            } else if (idElemento == R.id.nav_settings) {
                fragmento = new SettingsFragment();
                titulo = getString(R.string.menu_settings);
            } else if (idElemento == R.id.nav_botonesradio) {
                startActivity(new Intent(this, BotonesradioActivity.class));
                cajonDeNavegacion.closeDrawer(GravityCompat.START);
                return true;
            } else if (idElemento == R.id.nav_calcu_act) {
                startActivity(new Intent(this, CalcuActActivity.class));
                cajonDeNavegacion.closeDrawer(GravityCompat.START);
                return true;
            } else {
                fragmento = new HomeFragment();
                titulo = getString(R.string.menu_home);
            }

            reemplazarFragmento(fragmento, titulo);
            cajonDeNavegacion.closeDrawer(GravityCompat.START);
            return true;
        });

        if (savedInstanceState == null) {
            vistaDeNavegacion.setCheckedItem(R.id.nav_home);
            reemplazarFragmento(new HomeFragment(), getString(R.string.menu_home));
        }
    }

    private void reemplazarFragmento(Fragment fragmento, String titulo) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.content_frame, fragmento)
                .commit();
        setTitle(titulo);
    }

    @Override
    public void onBackPressed() {
        if (cajonDeNavegacion.isDrawerOpen(GravityCompat.START)) {
            cajonDeNavegacion.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}