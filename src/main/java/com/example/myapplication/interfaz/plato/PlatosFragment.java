
package com.example.myapplication.interfaz.plato;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.SearchView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.example.myapplication.Abtract.InterfazFragamen;
import com.example.myapplication.R;
import com.example.myapplication.adaptador.AdaptadorListaPlatosMenu;
import com.example.myapplication.adaptador.Servidor;
import com.example.myapplication.adaptador.VolleySingleton;
import com.example.myapplication.mundo.Plato;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PlatosFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PlatosFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    protected RequestQueue requestQueue;
    protected JsonRequest jsonRequest;
    private SearchView buscarPlato;
    private RecyclerView listaPlatos;
    private Activity actividad;
    private InterfazFragamen interfazFragamen;

    private ArrayList<Plato> platosMenu;
    private AdaptadorListaPlatosMenu adaptadorListaPlatosMenu;


    RecyclerView recyclerView;

    AgregarPlatoFragment agregarPlatoFragment;


    public PlatosFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PlatosFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PlatosFragment newInstance(String param1, String param2) {
        PlatosFragment fragment = new PlatosFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_platos, container, false);





        ImageButton agregarPlato = v.findViewById(R.id.botonAgregarPlato);
        this.listaPlatos = v.findViewById(R.id.listaPlatosCarta);
        this.buscarPlato = v.findViewById(R.id.buscar_plato);

        this.requestQueue =  VolleySingleton.getInstance(getContext ()).getRequestQueue();
        this.platosMenu = new ArrayList<Plato>();


        this.listaPlatos.setLayoutManager(new GridLayoutManager(getContext(),4));
        this.adaptadorListaPlatosMenu = new AdaptadorListaPlatosMenu (getContext (),platosMenu);
        adaptadorListaPlatosMenu.notifyDataSetChanged ();
        listaPlatos.setAdapter(adaptadorListaPlatosMenu);


        adaptadorListaPlatosMenu.setOnclickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                int numeroMesa = platosMenu.get(listaPlatos.getChildAdapterPosition(v)).getIdplato();
                // Toast.makeText(getContext (), numeroMesa, Toast.LENGTH_SHORT).show();
                interfazFragamen.enviarPlato(platosMenu.get(listaPlatos.getChildAdapterPosition(v)));
            }
        });


        this.buscarPlato.setOnQueryTextListener (new SearchView.OnQueryTextListener ()
        {
            @Override
            public boolean onQueryTextSubmit(String query) { return false;  }
            @Override
            public boolean onQueryTextChange(final String platoNombre)
            {
                cargarPlatos(platoNombre);
                return false;
            }
        });


        agregarPlato.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment agregarPlatoFragment = new AgregarPlatoFragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.nav_host_fragment, agregarPlatoFragment);
                transaction.addToBackStack(null);
                // Commit a la transacción
                transaction.commit();
            }
        });




        cargarPlatos("");


        return v;
    }


    public void cargarPlatos(String platoNombre)
    {
        Map<String,String> params= new HashMap<String, String>();
        params.put("buscarPlatosTodos",platoNombre);
        JSONObject parameters = new JSONObject(params);
        String url=Servidor.HOST +"/consultas/platos.php";
        jsonRequest=new JsonObjectRequest(Request.Method.POST, url, parameters, new Response.Listener<JSONObject> ()
        {
            @Override
            public void onResponse(JSONObject response)
            {
                platosMenu.clear();
                try
                {
                    Boolean respuesta = response.getBoolean ("respuesta");
                    if (respuesta.booleanValue ())
                    {
                        JSONArray datos = response.getJSONArray ("datos");
                        for (int i = 0; i < datos.length(); i++)
                        {
                            JSONObject plato = datos.getJSONObject(i);
                            int idPlato=plato.getInt ("idplatos");
                            String categoria=plato.getString("categoria");
                            String nombre=plato.getString("nombre");
                            String descripcion=plato.getString("descripcion");
                            Double precio=plato.getDouble("precio");
                            String estado=plato.getString ("estado");
                            String image=plato.getString ("imagen");
                            Plato m=new Plato( idPlato, categoria,  nombre, descripcion,precio,image,estado);
                            platosMenu.add (m);
                        }
                        adaptadorListaPlatosMenu.notifyDataSetChanged();
                    }else
                    {
                        String error = response.getString ("error");
                        Toast.makeText(getContext(), respuesta.booleanValue ()+" Error: "+error,Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e)
                {
                    e.printStackTrace ();
                }
            }
        }, new Response.ErrorListener ()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                error.printStackTrace ();
            }
        });
        requestQueue.add(jsonRequest);
    }

    @Override
    public void onAttach(Context context)
    {
        super.onAttach(context);
        if (context instanceof Activity)
        {
            this.actividad = (Activity) context;
            this.interfazFragamen = (InterfazFragamen) this.actividad;
        }
    }

    @Override
    public void onDetach()
    {
        super.onDetach();
    }
}