package com.example.myapplication.interfaz;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.Abtract.InterfazFragamen;
import com.example.myapplication.AgregarMaterialesFragment;
import com.example.myapplication.DetallesMaterialesFragment;
import com.example.myapplication.R;
import com.example.myapplication.interfaz.pedido.PedidosFragment;
import com.example.myapplication.interfaz.pedido.PlatosMesaFragment;
import com.example.myapplication.interfaz.plato.DetallePlatoFragment;
import com.example.myapplication.interfaz.plato.PlatosFragment;
import com.example.myapplication.mundo.Material;
import com.example.myapplication.mundo.Plato;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.io.Serializable;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, InterfazFragamen {
    Button cerrarSesion;
    AppBarConfiguration mAppBarConfiguration;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;
    NavigationView navigationView;
    TextView nick,nombreUsuario,apellidoUsuario,cargoUsuario;
    ImageButton menu,agregarPedido,pedidos;
    Plato plato;


    private PlatosMesaFragment platosMesaFragment;
    private PlatosFragment platosFragment;
    private DetallePlatoFragment detallePlatoFragment;
    private AgregarMaterialesFragment agregarMaterialesFragment;
    private DetallesMaterialesFragment detallesMaterialesFragment;
    private String rolUsuario;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.nav_host_fragment,new HomeFragment());
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();

        cerrarSesion=findViewById(R.id.botonCerrarSesion);
        cerrarSesion.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                SharedPreferences preferences= getSharedPreferences("preferenciasLogin", Context.MODE_PRIVATE);
                preferences.edit().clear().commit();
                Intent intent= new Intent(getApplicationContext(), loginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        nick=findViewById(R.id.textView3);
        nombreUsuario=findViewById(R.id.txtNombreUsuario);
        apellidoUsuario=findViewById(R.id.txtApellidoUsuario);
        cargoUsuario=findViewById(R.id.txtCargoUsuario);

        recuperarPreferencias();
   //     enviarPlato(plato);

        actionBarDrawerToggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.open,R.string.close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.setDrawerIndicatorEnabled(true);
        actionBarDrawerToggle.syncState();

        menu=findViewById(R.id.BotonInicioMenu);

        menu.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                irMenu();
            }
        });

        agregarPedido = findViewById(R.id.botonInicioPlatos);

        agregarPedido.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
               irPlatos();
            }
        });

        pedidos=findViewById(R.id.botonInicioMesas);
        pedidos.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                irPedidos();
            }
        });




    }

    private void irMenu()
    {
        drawerLayout.closeDrawer(GravityCompat.START);
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.nav_host_fragment,new HomeFragment());
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();


    }

    private void irPlatos()
    {
        if(rolUsuario.equals("Admin"))
        {
            drawerLayout.closeDrawer(GravityCompat.START);
            fragmentManager = getSupportFragmentManager();
            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.nav_host_fragment,new PlatosFragment());
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();

        }
        else
        {
            Toast.makeText(MainActivity.this, "No tiene permisos para acceder a Menu", Toast.LENGTH_SHORT).show();
        }

    }
    private void irPedidos() {
        drawerLayout.closeDrawer(GravityCompat.START);
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.nav_host_fragment,new PedidosFragment ());
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();

    }

    private void recuperarPreferencias()
    {
        SharedPreferences preferences= getSharedPreferences("preferenciasLogin", Context.MODE_PRIVATE);
        boolean sesion=preferences.getBoolean("sesion",false);
        if(sesion)
        {
            this.rolUsuario=preferences.getString("rol", "No hay nada");
            String cargo=preferences.getString("cargo", "No hay nada");
            String apellido=preferences.getString("apellidos", "No hay nada");
            String nombre=preferences.getString("nombres", "No hay nada");
            String nickUsuario=preferences.getString("nick", "No hay nada");
            nick.setText (nickUsuario);
            nombreUsuario.setText(nombre);
            apellidoUsuario.setText(apellido);
            cargoUsuario.setText(cargo);
        }
    }

    @Override
    public boolean onSupportNavigateUp()
    {
       NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
       // NavController navControllerDos = Navigation.findNavController(this, R.id.platosFragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration) || super.onSupportNavigateUp();
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item)
    {
        drawerLayout.closeDrawer(GravityCompat.START);
        if(item.getItemId() == R.id.nav_home)
        {
            fragmentManager = getSupportFragmentManager();
            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.nav_host_fragment,new HomeFragment());
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();

        }
        return false;
    }




    public void enviarPlato(Plato plato)
    {
        detallePlatoFragment = new DetallePlatoFragment();
        Bundle bundleEnvio = new Bundle();
        bundleEnvio.putSerializable("plato", plato);
        detallePlatoFragment.setArguments(bundleEnvio);

        //CArgar fragment en el activity
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.nav_host_fragment, detallePlatoFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    public void enviarPlatoDos(Plato plato)
    {
        agregarMaterialesFragment = new AgregarMaterialesFragment();
        Bundle bundleEnvio = new Bundle();
        bundleEnvio.putSerializable("plato", plato);
        agregarMaterialesFragment.setArguments(bundleEnvio);

        //CArgar fragment en el activity
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.nav_host_fragment, agregarMaterialesFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    public void enviarPlatoTres(Plato plato)
    {
        detallesMaterialesFragment = new DetallesMaterialesFragment();
        Bundle bundleEnvio = new Bundle();
        bundleEnvio.putSerializable("plato", plato);
        detallesMaterialesFragment.setArguments(bundleEnvio);

        //CArgar fragment en el activity
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.nav_host_fragment, detallesMaterialesFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }


    public void enviarMateriales(Material material)
    {
        detallesMaterialesFragment = new DetallesMaterialesFragment();
        Bundle bundleEnvio = new Bundle();
        bundleEnvio.putSerializable("material", material);
        detallesMaterialesFragment.setArguments(bundleEnvio);

        //CArgar fragment en el activity
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.nav_host_fragment, detallesMaterialesFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
}