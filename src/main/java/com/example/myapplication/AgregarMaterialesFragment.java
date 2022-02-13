package com.example.myapplication;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.example.myapplication.Abtract.InterfazFragamen;
import com.example.myapplication.adaptador.AdaptadorListaAgregarMaterial;
import com.example.myapplication.adaptador.AdaptadorListaMateriales;
import com.example.myapplication.adaptador.Servidor;
import com.example.myapplication.adaptador.VolleySingleton;
import com.example.myapplication.mundo.Material;
import com.example.myapplication.mundo.Plato;
import com.example.myapplication.mundo.Producto;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class AgregarMaterialesFragment extends Fragment {

    private final String CARPETA_RAIZ = "misImagenesPrueba/";
    private final String RUTA_IMAGEN = CARPETA_RAIZ + "misFotos";
    private final int COD_SELECCIONA = 10;
    private final int COD_FOTO = 20;
    private RequestQueue requestQueue;
    private JsonRequest jsonRequest;
    private ImageView imagenPlato;
    private EditText nombrePlato;
    private EditText precioPlato;
    private Spinner categoriaPalto;
    private Spinner estadoPlato;
    private EditText descripcionPlato;
    private Button buscarImage;
    private Button modificarUnPlato, agregarMateriales;
    private Bitmap bitmap;
    private String path;
    private Uri miPath;
    private String selectedImagePath = null;
    private Activity actividad;
    private InterfazFragamen interfazFragamen;
    private Plato platos = null;
    private String rutaImg;
    private RecyclerView rcvMaterialesAgregar;
    private RecyclerView rcvMateriales;
    private AdaptadorListaAgregarMaterial adaptadorListaProductos;
    private AdaptadorListaMateriales adaptadorListaMateriales;
    private ArrayList<Producto> listProductos;
    private ArrayList<Producto> listProductosAux;
    private ArrayList<Material> listMaterialesProductos;
    private Button btnAgregarPM;
    Bundle objetoPlato;
    TextView txtNombreDelPlato;

    public AgregarMaterialesFragment() {
        // Required empty public constructor
    }


    public static AgregarMaterialesFragment newInstance(String param1, String param2) {
        AgregarMaterialesFragment fragment = new AgregarMaterialesFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        objetoPlato = getArguments();

        if(objetoPlato !=null) {


            this.platos = (Plato) objetoPlato.getSerializable ("plato");
           // String nombreplato= platos.getNombre();
            //txtNombreDelPlato.setText(nombreplato);

        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_agregar_materiales, container, false);


        this.requestQueue = VolleySingleton.getInstance(getContext()).getRequestQueue();
        this.nombrePlato= view.findViewById(R.id.txtModnombrePlato);
        this.precioPlato = (EditText) view.findViewById(R.id.txtModPrecionPlato);

        this.descripcionPlato = (EditText) view.findViewById(R.id.txtModDescripcion);
        this.categoriaPalto = (Spinner) view.findViewById(R.id.spnModCategoria);
        this.estadoPlato = (Spinner) view.findViewById(R.id.spnestado);
        this.buscarImage =(Button) view.findViewById(R.id.btnModBuscarImagen);
        this.modificarUnPlato =(Button) view.findViewById(R.id.btnModificar);
        this.imagenPlato =(ImageView) view.findViewById(R.id.imgModPlato);
        this.agregarMateriales=(Button) view.findViewById(R.id.btnAgregarMateriales);
        this.listProductos = new ArrayList<>();
        this.listProductosAux=new ArrayList<> ();
        this.listMaterialesProductos=new ArrayList<> ();


        this.adaptadorListaProductos = new AdaptadorListaAgregarMaterial (getContext(),this.listProductos,this);
        rcvMaterialesAgregar=view.findViewById(R.id.rcvMaterialesAgregar);
        rcvMaterialesAgregar.setLayoutManager (new LinearLayoutManager (getContext ()));
        rcvMaterialesAgregar.setAdapter (adaptadorListaProductos);
        btnAgregarPM=view.findViewById(R.id.btnAgregarPM);







        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        if(objetoPlato !=null) {

            Toast.makeText(getContext(), platos.getNombre()+ "", Toast.LENGTH_SHORT).show();


            conusltarMateriales();

        }
    }

    public void conusltarMateriales() {

        final ProgressDialog loading = ProgressDialog.show(getContext(), "Consultado productos...", "Espere por favor...", false, false);
        Map<String, String> params = new HashMap<String, String> ();
        params.put("consultasListaProductos", "true");
        JSONObject parameters = new JSONObject(params);
        String url = Servidor.HOST +"/consultas/inventario.php";
        jsonRequest = new JsonObjectRequest (Request.Method.POST, url, parameters, new Response.Listener<JSONObject>()
        {
            @Override
            public void onResponse(JSONObject response)
            {
                listProductos.clear ();
                try
                {
                    loading.dismiss();

                    Boolean respuesta = response.getBoolean ("respuesta");
                    if (respuesta.booleanValue ())
                    {
                        JSONArray datos = response.getJSONArray ("datos");
                        for (int i = 0; i < datos.length(); i++)
                        {
                            JSONObject producto = datos.getJSONObject(i);

                            int idProductos= producto.getInt ("idproductos");
                            String nombre= producto.getString ("nombre");
                            String codigo= producto.getString ("codigo");
                            Producto p=new Producto( idProductos, nombre, codigo);
                            listProductos.add (p);
                        }

                        adaptadorListaProductos.notifyDataSetChanged ();

                        btnAgregarPM.setOnClickListener(new View.OnClickListener() {

                            @Override
                            public void onClick(View v) {


                                listProductosAux.clear();
                                for (Producto producto : listProductos) {
                                    if (producto.isEstado()) {
                                        listProductosAux.add(producto);
                                    }
                                }

                                String data = new Gson().toJson(listProductosAux);
                                //Toast.makeText(getContext(), platos.getIdplato() + "", Toast.LENGTH_SHORT).show();
                                HashMap<String, String> params = new HashMap<String, String>();
                                params.put("registrarMaterialesPlato", "true");
                                params.put("productos", data);
                                params.put("idplato", platos.getIdplato() + "");
                                JSONObject parameters = new JSONObject(params);

                                String url = Servidor.HOST + "/consultas/inventario.php";

                                jsonRequest = new JsonObjectRequest(Request.Method.POST, url, parameters, new Response.Listener<JSONObject>() {
                                    @Override
                                    public void onResponse(JSONObject response) {
                                        try {
                                            Boolean respuesta = response.getBoolean("respuesta");

                                            if (respuesta.booleanValue()) {
                                                Toast.makeText(getContext(), "Materiales agregados al plato"+platos.getNombre() + "", Toast.LENGTH_SHORT).show();

                                            } else {
                                                String error = response.getString("error");
                                                Toast.makeText(getContext(), respuesta.booleanValue() + " Error: " + error, Toast.LENGTH_SHORT).show();
                                            }
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }, new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        error.printStackTrace();
                                    }
                                });
                                requestQueue.add(jsonRequest);
                                //adaptadorListaProductos.notifyDataSetChanged ();
                            }
                        });

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


