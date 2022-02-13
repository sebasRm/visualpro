package com.example.myapplication;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.example.myapplication.adaptador.AdaptadorListaModificarMaterial;
import com.example.myapplication.adaptador.Servidor;
import com.example.myapplication.adaptador.VolleySingleton;
import com.example.myapplication.mundo.Material;
import com.example.myapplication.mundo.Producto;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DetallesMaterialesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DetallesMaterialesFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private RequestQueue requestQueue;
    private JsonRequest jsonRequest;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private Material material;
    private RecyclerView rcvMateriales;
    private AdaptadorListaModificarMaterial adaptadorListaMateriales;
    private ArrayList<Material> listMaterialesProductos;

    public DetallesMaterialesFragment()
    {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DetallesMaterialesFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DetallesMaterialesFragment newInstance(String param1, String param2) {
        DetallesMaterialesFragment fragment = new DetallesMaterialesFragment();
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
        View view= inflater.inflate(R.layout.fragment_detalles, container, false);
        Bundle objetoPlato = getArguments();

        if(objetoPlato !=null)
        {
            this.requestQueue = VolleySingleton.getInstance(getContext()).getRequestQueue();
            this.material = (Material) objetoPlato.getSerializable ("material");
            this.rcvMateriales=view.findViewById(R.id.rcvModificarMateriales);
            this.listMaterialesProductos=new ArrayList<> ();
            this.listMaterialesProductos.add (material);
            this.adaptadorListaMateriales = new AdaptadorListaModificarMaterial (getContext(),this.listMaterialesProductos,this);
            this.rcvMateriales.setLayoutManager (new LinearLayoutManager (getContext ()));
            this.rcvMateriales.setAdapter (adaptadorListaMateriales);
            this.adaptadorListaMateriales.notifyDataSetChanged ();

        }
        return view;
    }


    public void modificarMaterial(Material material)
    {
        final ProgressDialog loading = ProgressDialog.show(getContext(), "Modificando material...", "Espere por favor...", false, false);
        Map<String, String> params = new HashMap<String, String> ();
        params.put("modificarMaterial", "true");
        params.put("idmaterial", material.getIdMateriales ()+"");
        params.put("categoria", material.getCategoria ()+"");
        params.put("cantidad", material.getCantidad ()+"");
        params.put("estado", material.getEst ()+"");


        JSONObject parameters = new JSONObject(params);
        String url = Servidor.HOST +"/consultas/inventario.php";
        jsonRequest = new JsonObjectRequest (Request.Method.POST, url, parameters, new Response.Listener<JSONObject>()
        {
            @Override
            public void onResponse(JSONObject response)
            {

                    loading.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                loading.dismiss();
            }
        });
        requestQueue.add(jsonRequest);
    }
}