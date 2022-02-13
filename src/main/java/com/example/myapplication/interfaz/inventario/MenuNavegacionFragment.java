package com.example.myapplication.interfaz.inventario;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.example.myapplication.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MenuNavegacionFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MenuNavegacionFragment extends Fragment {

    ImageButton inventario,ventas;


    public MenuNavegacionFragment() {
        // Required empty public constructor
    }


    public static MenuNavegacionFragment newInstance(String param1, String param2) {
        MenuNavegacionFragment fragment = new MenuNavegacionFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_menu_navegacion, container, false);
        inventario=v.findViewById(R.id.btnIrInventario);
        ventas=v.findViewById(R.id.btnIrVentas);

        inventario.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick(View v) {

                Fragment inventarioProductosFragment = new InventarioProductosFragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.nav_host_fragment, inventarioProductosFragment);
                transaction.addToBackStack(null);

                // Commit a la transacción
                transaction.commit();
            }
        });

        ventas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment invenarioVentasFragment = new InvenarioVentasFragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.nav_host_fragment, invenarioVentasFragment);
                transaction.addToBackStack(null);

                // Commit a la transacción
                transaction.commit();
            }
        });
        return v;
    }
}