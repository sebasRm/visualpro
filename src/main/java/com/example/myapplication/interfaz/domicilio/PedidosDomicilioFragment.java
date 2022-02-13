package com.example.myapplication.interfaz.domicilio;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.Volley;
import com.example.myapplication.R;
import com.example.myapplication.adaptador.AdaptadorListaDomicilio;
import com.example.myapplication.adaptador.Servidor;
import com.example.myapplication.mundo.Cliente;
import com.example.myapplication.mundo.OrdenDomicilio;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PedidosDomicilioFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PedidosDomicilioFragment extends Fragment
{
    protected RequestQueue requestQueue;
    protected JsonRequest jsonRequest;
    Button agregarDomicilio;
    private String idEmpleados;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private AutoCompleteTextView direccionCliente, referenciaCliente ,identificacionCliente;
    private EditText nombreCliente, apellidosCliente,txtPrecioDomicilio;
    private Button botonNuevoDomicilio;
    private Button botonNuevoCliente,botonModificarDomicilio;
    private LinearLayout pnlDomicilio;
    private TextView  lblDomicilio,idCliente, textoEstado;
    private ArrayAdapter<String> adapterDireccion;
    private ArrayAdapter<String> adapterReferencia;
    private ArrayAdapter<String>  adapterCliente;
    private ArrayList<OrdenDomicilio> listaDomicilios;
    private ArrayList<OrdenDomicilio> listaDomiciliosAux;
    private AdaptadorListaDomicilio adaptadorListaDom;
    private RecyclerView rcvDomicilios;
    private SearchView searchBuscarDomicilio;
    private OrdenDomicilio domicilio ;
    private Timer timer;
    public void onStart()
    {
        super.onStart();
        this.timer = new Timer ();
        this.timer.scheduleAtFixedRate(new TimerTask ()
        {
            @Override
            public void run()
            {
               buscarlistaDomicilios ();
                //  buscarMesaDesocupada ();
                System.out.println ("A Kiss after 5 seconds");
            }

        },1,4000);
    }



    public PedidosDomicilioFragment()
    {
        // Required empty public constructor
    }

    public static PedidosDomicilioFragment newInstance(String param1, String param2)
    {
        PedidosDomicilioFragment fragment = new PedidosDomicilioFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        getParentFragmentManager ().setFragmentResultListener("key", this, new FragmentResultListener ()
        {
            @Override
            public void onFragmentResult(@NonNull String key, @NonNull Bundle bundle) {
                // We use a String here, but any type that can be put in a Bundle is supported
                String result = bundle.getString("bundleKey");
                // Do something with the result...
            }
        });


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {

        this.adapterDireccion = new ArrayAdapter<String> ( getContext (), android.R.layout.simple_list_item_1, android.R.id.text1, new ArrayList<String> () );
        this.adapterReferencia = new ArrayAdapter<String> ( getContext (), android.R.layout.simple_list_item_1, android.R.id.text1, new ArrayList<String> () );
        this.adapterCliente= new ArrayAdapter<String> ( getContext (), android.R.layout.simple_list_item_1, android.R.id.text1, new ArrayList<String> () );

        this.requestQueue = Volley.newRequestQueue (getContext ());

        View v= inflater.inflate(R.layout.fragment_pedidos_domicilio, container, false);
        this.rcvDomicilios = v.findViewById (R.id.listaDomicilios);
        this.listaDomicilios = new ArrayList<OrdenDomicilio> ();
        this.listaDomiciliosAux = new ArrayList<OrdenDomicilio> ();
        this.searchBuscarDomicilio=v.findViewById (R.id.searchBuscarDomicilio);

        this.adaptadorListaDom = new AdaptadorListaDomicilio (getContext (), this.listaDomicilios,this);
        this.rcvDomicilios.setLayoutManager (new LinearLayoutManager (getContext ()));
        this.rcvDomicilios.setAdapter (this.adaptadorListaDom);
        this.agregarDomicilio= v.findViewById(R.id.btnAgregarDomicilio);
        recuperarPreferencias();

        this.adaptadorListaDom.setOnclickListener (new View.OnClickListener () {
            @Override
            public void onClick(View v)
            {
                int pos=rcvDomicilios.getChildAdapterPosition (v);
                if (pos>-1)
                {
                    domicilio = listaDomicilios.get (pos);
                    Bundle bundleEnvio = new Bundle ();
                    bundleEnvio.putSerializable ("domicilio", domicilio);
                    getParentFragmentManager ().setFragmentResult ("key", bundleEnvio);
                }
            }
        });

        this.searchBuscarDomicilio.setOnQueryTextListener (new SearchView.OnQueryTextListener ()
        {
            @Override
            public boolean onQueryTextSubmit(String query) {return false; }

            @Override
            public boolean onQueryTextChange(String newText)
            {
                listaDomicilios.clear ();
                if (!newText.isEmpty ())
                {
                    for (OrdenDomicilio m : listaDomiciliosAux)
                    {
                        //String cliente =m.getCliente ().getNombres ()+" "+m.getCliente ().getApellidos ();
                        String cliente =m.getCliente ().getNombres ();
                        String identificaion =m.getCliente ().getIndentificacion ();
                        String telefono =m.getTelefono ();
                        if (cliente.contains (newText)||identificaion.contains (newText)||telefono.contains (newText))
                        {
                            listaDomicilios.add (m);
                        }
                    }
                } else
                {
                    listaDomicilios.addAll (listaDomiciliosAux);
                }
                adaptadorListaDom.notifyDataSetChanged ();

                return false;
            }
        });
        agregarDomicilio.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                LayoutInflater inflater = getLayoutInflater();
                view = inflater.inflate(R.layout.diaglog_nuevo_domicilio, null);
                builder.setView(view);
                final AlertDialog dialog = builder.create();
                dialog.show();

                idCliente = view.findViewById(R.id.idCliente);
                textoEstado = view.findViewById(R.id.textoEstado);
                lblDomicilio = view.findViewById(R.id.lblDomicilio);
                pnlDomicilio = view.findViewById(R.id.pnlDomicilio);
                identificacionCliente = view.findViewById(R.id.txtIdentificacionCliente);
                identificacionCliente.setAdapter (adapterCliente);
                apellidosCliente = view.findViewById(R.id.txtApellidosCliente);
                direccionCliente = view.findViewById(R.id.txtDireccionCliente);
                txtPrecioDomicilio = view.findViewById(R.id.txtPrecioDomicilio);
                direccionCliente.setAdapter (adapterDireccion);
                nombreCliente = view.findViewById(R.id.txtCliente);
                referenciaCliente = view.findViewById(R.id.txtRefenciaCliente);
                referenciaCliente.setAdapter (adapterReferencia);

                botonNuevoDomicilio = view.findViewById(R.id.botonAgregarDomicilio);
                botonNuevoCliente = view.findViewById(R.id.botonRegistrarCliente);
                identificacionCliente.addTextChangedListener(new TextWatcher () {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {}

                    @Override
                    public void afterTextChanged(Editable ide)
                    {
                        String[] s = ide.toString ().split (" ");
                        final String celular=s[0].trim ();

                        if (!celular.isEmpty ())
                        {
                            Map<String,String> params= new HashMap<String, String> ();
                            params.put("buscarCliente",celular);
                            JSONObject parameters = new JSONObject(params);
                            String url=Servidor.HOST +"/consultas/domicilios.php";
                            jsonRequest=new JsonObjectRequest (Request.Method.POST, url, parameters, new Response.Listener<JSONObject> ()
                            {
                                @Override
                                public void onResponse(JSONObject response)
                                {
                                    try
                                    {
                                        Boolean respuesta = response.getBoolean ("respuesta");

                                        if (respuesta.booleanValue ())
                                        {
                                            JSONArray datos = response.getJSONArray ("datos");
                                            if (datos.length()>0)
                                            {

                                                adapterCliente.clear ();
                                                adapterDireccion.clear ();
                                                adapterReferencia.clear ();
                                                int seEncontro= buscarCliente(celular,datos);
                                                if (seEncontro!=-1)
                                                {
                                                    lblDomicilio.setText ("Agregar nuevo domicilio");
                                                    pnlDomicilio.setVisibility (View.VISIBLE);
                                                    botonNuevoCliente.setVisibility (View.GONE);

                                                    botonNuevoDomicilio.setVisibility (View.VISIBLE);
                                                    JSONObject clienteDB = datos.getJSONObject (seEncontro);

                                                    int idclientes = clienteDB.getInt ("idclientes");
                                                    String nombres = clienteDB.getString ("nombres");
                                                    String estado = clienteDB.getString ("estado");

                                                    String telefono = clienteDB.getString ("telefono");
                                                    String direccion = clienteDB.getString ("direccion");
                                                    String referencia = clienteDB.getString ("referencia");
                                                    idCliente.setText (idclientes + "");
                                                    if (nombres.equals ("Sin registrar") ||nombres.equals (""))
                                                        nombreCliente.setHint (nombres);
                                                    else
                                                        nombreCliente.setHint (nombres);
                                                    direccionCliente.setText (!direccion.equals ("null") ? direccion : "");

                                                    referenciaCliente.setText (!referencia.equals ("null") ? referencia : "");
                                                    textoEstado.setText (estado);

                                                    apellidosCliente.setEnabled (false);

                                                    for (int i = 0; i < datos.length (); i++)
                                                    {
                                                        JSONObject registros = datos.getJSONObject (i);

                                                        String nombresReg = registros.getString ("nombres");

                                                        String ide = registros.getString ("identificacion");
                                                        adapterCliente.remove (ide + " " + nombresReg);
                                                        adapterCliente.add (ide + " " + nombresReg);

                                                        direccion = registros.getString ("direccion");
                                                        adapterDireccion.remove (direccion);
                                                        adapterDireccion.add (direccion);

                                                        referencia = registros.getString ("referencia");
                                                        adapterReferencia.remove (referencia);
                                                        adapterReferencia.add (referencia);
                                                    }
                                                }else
                                                {

                                                    nombreCliente.setEnabled (true);
                                                    //apellidosCliente.setEnabled (true);
                                                    botonNuevoDomicilio.setVisibility (View.GONE);
                                                    botonNuevoCliente.setVisibility (View.VISIBLE);
                                                    lblDomicilio.setText ("Agregar nuevo cliente");
                                                    pnlDomicilio.setVisibility (View.GONE);

                                                    idCliente.setText ("");
                                                    nombreCliente.setText ("");
                                                    //apellidosCliente .setText ("");
                                                    direccionCliente.setText ("");
                                                    ;

                                                    referenciaCliente.setText ("");

                                                }
                                                adapterReferencia.notifyDataSetChanged ();
                                                adapterDireccion.notifyDataSetChanged ();
                                                adapterCliente.notifyDataSetChanged ();
                                            }
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

                        }else
                        {
                            lblDomicilio.setText ("Agregar nuevo cliente");

                            pnlDomicilio.setVisibility (View.GONE);

                            botonNuevoDomicilio.setVisibility (View.GONE);
                            botonNuevoCliente.setVisibility (View.GONE);

                            idCliente.setText ("");
                            nombreCliente.setText ("");
                            apellidosCliente .setText ("");
                            direccionCliente.setText ("");;
                            referenciaCliente.setText ("");
                        }
                    }
                });


                botonNuevoCliente.setOnClickListener (new View.OnClickListener () {
                    @Override
                    public void onClick(View v)
                    {
                        String identificacion=identificacionCliente.getText ().toString ();
                  //      String nombre=nombreCliente .getText ().toString ();
                        String apellidos=apellidosCliente.getText ().toString ();

                        if(identificacion.isEmpty()){
                            Toast.makeText(getContext(), "Digite un de telfono valido",Toast.LENGTH_SHORT).show();
                        }
                        /*else if(nombre.isEmpty ())
                        {
                            Toast.makeText(getContext(), "Digite un nombre valido",Toast.LENGTH_SHORT).show();
                        }
                        else if(apellidos.isEmpty ())
                        {
                            Toast.makeText(getContext(), "Digite apellidos validos",Toast.LENGTH_SHORT).show();
                        }*/ else
                        {
                            Map<String,String> params= new HashMap<String, String> ();
                            params.put("registrarCliente","true");
                            params.put("identificacion",identificacion);
                          //  params.put("nombres",nombre);
                            params.put("apellidos","");
                            JSONObject parameters = new JSONObject(params);
                            String url=Servidor.HOST +"/consultas/domicilios.php";
                            jsonRequest=new JsonObjectRequest (Request.Method.POST, url, parameters, new Response.Listener<JSONObject> ()
                            {
                                @Override
                                public void onResponse(JSONObject response)
                                {
                                    try
                                    {
                                        Boolean respuesta = response.getBoolean ("respuesta");

                                        if (respuesta.booleanValue ())
                                        {
                                            Toast.makeText(getContext(), "Usuario registrado exitosamente",Toast.LENGTH_SHORT).show();
                                            JSONArray datos = response.getJSONArray ("datos");
                                            if (datos.length()>0)
                                            {
                                                lblDomicilio.setText ("Agregar nuevo domicilio");
                                                pnlDomicilio.setVisibility (View.VISIBLE);
                                                botonNuevoDomicilio.setVisibility (View.VISIBLE);
                                                botonNuevoCliente.setVisibility (View.GONE);
                                                JSONObject clienteDB = datos.getJSONObject(0);

                                                int idclientes=clienteDB.getInt ("idclientes");
                                                String nombres=clienteDB.getString("nombres");
                                                String apellidos=clienteDB.getString("apellidos");
                                                String estado=clienteDB.getString("estado");
                                                String identificacion=clienteDB.getString("identificacion");

                                                idCliente.setText (idclientes+"");
                                                if (nombres.equals ("Sin registrar")||nombres.equals ("") )
                                                    nombreCliente.setHint (nombres);
                                                else
                                                    nombreCliente.setHint (nombres);
                                                apellidosCliente.setText (apellidos);
                                                textoEstado.setText (estado);;
                                                apellidosCliente.setEnabled (false);
                                                direccionCliente.requestFocus ();

                                            }
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
                    }
                });



                botonNuevoDomicilio.setOnClickListener (new View.OnClickListener ()
                {
                    @Override
                    public void onClick(View v)
                    {
                        String id=idCliente.getText ().toString ();
                        if (!id.isEmpty ())
                        {
                            String estado=textoEstado.getText ().toString ();
                            if (estado.equals ("Sin domicilios"))
                            {
                                String direccion=direccionCliente.getText ().toString ();
                                String cliente=nombreCliente .getText ().toString ();
                                String referencia=referenciaCliente.getText ().toString ();
                                String aux=txtPrecioDomicilio.getText ().toString ();

                                Toast.makeText(getContext(), idEmpleados,Toast.LENGTH_SHORT).show();
                                Map<String,String> params= new HashMap<String, String> ();
                                params.put("crearDomicilio","true");
                                params.put("idEmpleados",idEmpleados);
                                params.put("idCliente",id);
                                params.put("cliente",cliente);
                                params.put("referencia",referencia);
                                params.put("direccion",direccion);
                                params.put("precio",aux.isEmpty ()?"0":aux);
                                JSONObject parameters = new JSONObject(params);
                                String url=Servidor.HOST +"/consultas/domicilios.php";
                                jsonRequest=new JsonObjectRequest (Request.Method.POST, url, parameters, new Response.Listener<JSONObject> ()
                                {
                                    @Override
                                    public void onResponse(JSONObject response)
                                    {
                                        try
                                        {
                                            Boolean respuesta = response.getBoolean ("respuesta");

                                            if (respuesta.booleanValue ())
                                            {
                                                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                                Toast.makeText(getContext(), "Domicilio registrado exitosamente",Toast.LENGTH_SHORT).show();
                                                JSONArray datos = response.getJSONArray ("datos");
                                                if (datos.length()>0)
                                                {
                                                    domicilio=new OrdenDomicilio ();
                                                    NumberFormat nf = NumberFormat.getCurrencyInstance(Locale.getDefault());
                                                    JSONObject pedido = datos.getJSONObject(0);

                                                    int idclientes=pedido.getInt ("idclientes");
                                                    String identificacion=pedido.getString("identificacion");
                                                    String nombres=pedido.getString("nombres");
                                                    String apellidos=pedido.getString("apellidos");
                                                    String estado=pedido.getString("estado");

                                                    Cliente cliente=new Cliente (idclientes,identificacion,nombres,apellidos,estado);

                                                    String factura_fecha = pedido.getString("factura_fecha");
                                                    int factura_idfacturas = pedido.getInt("factura_idfacturas");
                                                    int usuarios_idempleado = pedido.getInt("usuarios_idempleado");
                                                    String usuarios_identificacion = pedido.getString("usuarios_identificacion");
                                                    String usuarios_nombres = pedido.getString("usuarios_nombres");
                                                    String usuarios_apellidos = pedido.getString("usuarios_apellidos");
                                                    String usuarios_telefono = pedido.getString("usuarios_telefono");
                                                    String usuarios_cargo = pedido.getString("usuarios_cargo");


                                                    String factura_telefono = pedido.getString("factura_telefono");
                                                    String factura_direccion = pedido.getString("factura_direccion");
                                                    String factura_referencia = pedido.getString("factura_referencia");
                                                    double factura_precio_envio = pedido.getDouble ("factura_precio_envio");

                                                    domicilio.inicializarDomicilio (cliente,
                                                            format.parse(factura_fecha),
                                                            factura_idfacturas,
                                                            usuarios_idempleado,
                                                            usuarios_identificacion,
                                                            usuarios_nombres,
                                                            usuarios_apellidos,
                                                            usuarios_telefono,
                                                            usuarios_cargo,
                                                            factura_telefono,
                                                            factura_direccion,
                                                            factura_referencia,
                                                            factura_precio_envio);

                                                    listaDomicilios.add (domicilio);
                                                    Bundle bundleEnvio = new Bundle ();
                                                    bundleEnvio.putSerializable ("domicilio", domicilio);
                                                    getParentFragmentManager ().setFragmentResult ("key", bundleEnvio);
                                                    adaptadorListaDom.notifyDataSetChanged ();
                                                }
                                                dialog.dismiss ();
                                            }else
                                            {
                                                String error = response.getString ("error");
                                                Toast.makeText(getContext(), respuesta.booleanValue ()+" Error: "+error,Toast.LENGTH_SHORT).show();
                                            }
                                        } catch (JSONException e)
                                        {
                                            e.printStackTrace ();
                                        } catch (ParseException e) {
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

                            }else
                            {
                                Toast.makeText(getContext(), "El cliente tiene un domicilio pendiente",Toast.LENGTH_SHORT).show();
                            }
                        }else
                        {
                            Toast.makeText(getContext(), "El cliente no es valido",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        return v;
    }

    public int buscarCliente(String identificacion, JSONArray datos) throws JSONException
    {
        int seEncontro=-1;
        for (int i = 0; i < datos.length (); i++)
        {
            JSONObject registros = datos.getJSONObject (i);
            String nombresReg = registros.getString ("nombres");
            String ide = registros.getString ("identificacion");
            adapterCliente.remove (ide + " " + nombresReg);
            adapterCliente.add (ide + " " + nombresReg);

            if (identificacion.equals (ide))
            {
                seEncontro=i;
            }
        }
        return seEncontro;
    }

    private void buscarlistaDomicilios()
    {
        Map<String,String> params= new HashMap<String, String> ();
        params.put("buscarDomicilios","true");
        JSONObject parameters = new JSONObject(params);
        String url= Servidor.HOST +"/consultas/domicilios.php";
        jsonRequest=new JsonObjectRequest (Request.Method.POST, url, parameters, new Response.Listener<JSONObject> ()
        {
            @Override
            public void onResponse(JSONObject response)
            {
                try
                {
                    Boolean respuesta = response.getBoolean ("respuesta");

                    if (respuesta.booleanValue ())
                    {
                        adaptadorListaDom.notifyDataSetChanged ();
                        JSONArray datos = response.getJSONArray ("datos");

                        if (datos.length ()!=listaDomiciliosAux.size ())
                        {
                            if (datos.length ()+1==listaDomiciliosAux.size ())
                            {
                                domicilio=null;
                            }
                            listaDomiciliosAux.clear ();

                            for (int i = 0; i < datos.length(); i++)
                            {
                                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

                                NumberFormat nf = NumberFormat.getCurrencyInstance(Locale.getDefault());
                                JSONObject pedido = datos.getJSONObject(i);

                                int idclientes=pedido.getInt ("idclientes");
                                String identificacion=pedido.getString("identificacion");
                                String nombres=pedido.getString("nombres");
                                String apellidos=pedido.getString("apellidos");
                                String estado=pedido.getString("estado");

                                Cliente cliente=new Cliente (idclientes,identificacion,nombres,apellidos,estado);

                                String factura_fecha = pedido.getString("factura_fecha");
                                int factura_idfacturas = pedido.getInt("factura_idfacturas");
                                int usuarios_idempleado = pedido.getInt("usuarios_idempleado");
                                String usuarios_identificacion = pedido.getString("usuarios_identificacion");
                                String usuarios_nombres = pedido.getString("usuarios_nombres");
                                String usuarios_apellidos = pedido.getString("usuarios_apellidos");
                                String usuarios_telefono = pedido.getString("usuarios_telefono");
                                String usuarios_cargo = pedido.getString("usuarios_cargo");


                                String factura_telefono = pedido.getString("factura_telefono");
                                String factura_direccion = pedido.getString("factura_direccion");
                                String factura_referencia = pedido.getString("factura_referencia");
                                double factura_precio_envio = pedido.getDouble ("factura_precio_envio");


                                OrdenDomicilio aux=new OrdenDomicilio ();
                                aux.inicializarDomicilio (cliente,
                                        format.parse (factura_fecha),
                                        factura_idfacturas,
                                        usuarios_idempleado,
                                        usuarios_identificacion,
                                        usuarios_nombres,
                                        usuarios_apellidos,
                                        usuarios_telefono,
                                        usuarios_cargo,
                                        factura_telefono,
                                        factura_direccion,
                                        factura_referencia,
                                        factura_precio_envio);

                                listaDomiciliosAux.add (aux);
                            }
                            listaDomicilios.clear ();
                            listaDomicilios.addAll (listaDomiciliosAux);
                            adaptadorListaDom.notifyDataSetChanged ();

                        }
                        if (isVisible ())
                        {
                            if (domicilio instanceof OrdenDomicilio)
                            {
                                Bundle bundleEnvio = new Bundle ();
                                bundleEnvio.putSerializable ("domicilio", domicilio);
                                getParentFragmentManager ().setFragmentResult ("key", bundleEnvio);
                            }else
                            {
                                Bundle bundleEnvio = new Bundle ();
                                bundleEnvio.putSerializable ("domicilio", null);
                                getParentFragmentManager ().setFragmentResult ("key", bundleEnvio);
                            }
                        }
                    }else
                    {
                        String error = response.getString ("error");
                        //Toast.makeText(getContext(), respuesta.booleanValue ()+" Error: "+error,Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException | ParseException e)
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

    private void recuperarPreferencias()
    {
        SharedPreferences preferences= getContext ().getSharedPreferences("preferenciasLogin", Context.MODE_PRIVATE);
        boolean sesion=preferences.getBoolean("sesion",false);
        if(sesion)
        {
            this.idEmpleados=preferences.getString("idEmpleados", "No hay nada");

        }
    }
    public void eliminarPedido(final int idFactura, final Cliente cliente, final int posicion)
    {

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_eliminar_mesa, null);
        builder.setView(view);
        final AlertDialog dialog = builder.create();
        dialog.show();

        final TextView input = view.findViewById(R.id.txtEliminarMesa);
        Button botonSi = view.findViewById(R.id.btnSiEliminar);
        Button botonNo = view.findViewById(R.id.btnEliminarNo);
        input.setText("¿Desea eliminar  el docimicilio del cliente '"+ cliente.toString ()+ "'?");
        builder.setView(input);


        botonSi.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {

                final ProgressDialog loading = ProgressDialog.show(getContext(), "Eliminando domicilio...", "Espere por favor...", false, false);
                Map<String, String> params = new HashMap<String, String>();
                params.put("eliminarUnDomicilio", "true");
                params.put("idfactura", idFactura + "");
                params.put("idcliente", cliente.getIdClientes ()+ "");
                JSONObject parameters = new JSONObject(params);
                String url = Servidor.HOST +"/consultas/domicilios.php";
                jsonRequest = new JsonObjectRequest(Request.Method.POST, url, parameters, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response)
                    {
                        try
                        {
                            loading.dismiss();
                            Boolean respuesta = response.getBoolean ("respuesta");
                            if (respuesta.booleanValue ())
                            {
                                Toast.makeText(getContext(), "Domicilio del cliente '" + cliente.toString ()+ "' elminado ", Toast.LENGTH_SHORT).show();
                                adaptadorListaDom.getList().remove (posicion);
                                adaptadorListaDom.notifyDataSetChanged ();
                                Bundle bundleEnvio = new Bundle ();
                                bundleEnvio.putSerializable ("domicilio", null);
                                getParentFragmentManager ().setFragmentResult ("key", bundleEnvio);
                                domicilio=null;
                            }else
                            {
                                String error = response.getString ("error");
                                Toast.makeText(getContext(), respuesta.booleanValue ()+" Error: "+error,Toast.LENGTH_SHORT).show();
                            }
                            dialog.cancel();
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
                int socketTimeout = 0;
                requestQueue.add(jsonRequest);
            }

        });

        botonNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();
            }
        });
        dialog.show();
    }

    public void modificarDomicilio(OrdenDomicilio d)
    {
        this.domicilio=d;
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.diaglog_nuevo_domicilio, null);
        builder.setView(view);
        final AlertDialog dialog = builder.create();
        dialog.show();


        lblDomicilio = view.findViewById(R.id.lblDomicilio);
        pnlDomicilio = view.findViewById(R.id.pnlDomicilio);


        textoEstado = view.findViewById(R.id.textoEstado);
        identificacionCliente = view.findViewById(R.id.txtIdentificacionCliente);
        nombreCliente = view.findViewById(R.id.txtCliente);
        apellidosCliente = view.findViewById(R.id.txtApellidosCliente);
        direccionCliente = view.findViewById(R.id.txtDireccionCliente);
        direccionCliente.setAdapter (adapterDireccion);
        referenciaCliente = view.findViewById(R.id.txtRefenciaCliente);
        referenciaCliente.setAdapter (adapterReferencia);
        txtPrecioDomicilio = view.findViewById(R.id.txtPrecioDomicilio);
        botonModificarDomicilio = view.findViewById(R.id.botonModificarDomicilio);



        lblDomicilio.setText ("Modificar domicilio");
        pnlDomicilio.setVisibility (View.VISIBLE);
        botonModificarDomicilio.setVisibility (View.VISIBLE);

        identificacionCliente.setText ( this.domicilio.getCliente ().getIndentificacion ());
        identificacionCliente.setEnabled (false);

        nombreCliente.setText ( this.domicilio.getCliente ().getNombres ());


        apellidosCliente.setText ( this.domicilio.getCliente ().getApellidos ());
        apellidosCliente.setEnabled (false);

        textoEstado.setText ( this.domicilio.getCliente ().getEstado ());
        textoEstado.setEnabled (false);

        direccionCliente.setText ( this.domicilio.getDireccion ());
        referenciaCliente.setText ( this.domicilio.getReferencia ());
        txtPrecioDomicilio.setText ( this.domicilio.getFactura_precio_envio ()+"");

        direccionCliente.requestFocus ();



        botonModificarDomicilio.setOnClickListener (new View.OnClickListener ()
        {
            @Override
            public void onClick(View v)
            {
                String direccion=direccionCliente.getText ().toString ();
                String cliente=nombreCliente.getText ().toString ();
                String referencia=referenciaCliente.getText ().toString ();
                String aux=txtPrecioDomicilio.getText ().toString ();


                Map<String,String> params= new HashMap<String, String> ();
                params.put("modificarDomicilio","true");

                params.put("idFactura",domicilio.getFactura_idfacturas ()+"");
                params.put("idCliente",domicilio.getCliente ().getIdclientes ()+"");
                params.put("cliente",cliente);
                params.put("referencia",referencia);
                params.put("direccion",direccion);
                params.put("precio",aux.isEmpty ()?"0":aux);
                JSONObject parameters = new JSONObject(params);
                String url=Servidor.HOST +"/consultas/domicilios.php";
                jsonRequest=new JsonObjectRequest (Request.Method.POST, url, parameters, new Response.Listener<JSONObject> ()
                {
                    @Override
                    public void onResponse(JSONObject response)
                    {
                        try
                        {
                            Boolean respuesta = response.getBoolean ("respuesta");

                            if (respuesta.booleanValue ())
                            {
                                JSONArray datos = response.getJSONArray ("datos");
                                if ( datos.length()>0)
                                {
                                    listaDomiciliosAux.clear ();
                                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

                                    NumberFormat nf = NumberFormat.getCurrencyInstance(Locale.getDefault());
                                    JSONObject pedido = datos.getJSONObject(0);

                                    int idclientes=pedido.getInt ("idclientes");
                                    String identificacion=pedido.getString("identificacion");
                                    String nombres=pedido.getString("nombres");
                                    String apellidos=pedido.getString("apellidos");
                                    String estado=pedido.getString("estado");

                                    Cliente cliente=new Cliente (idclientes,identificacion,nombres,apellidos,estado);

                                    String factura_fecha = pedido.getString("factura_fecha");
                                    int factura_idfacturas = pedido.getInt("factura_idfacturas");
                                    int usuarios_idempleado = pedido.getInt("usuarios_idempleado");
                                    String usuarios_identificacion = pedido.getString("usuarios_identificacion");
                                    String usuarios_nombres = pedido.getString("usuarios_nombres");
                                    String usuarios_apellidos = pedido.getString("usuarios_apellidos");
                                    String usuarios_telefono = pedido.getString("usuarios_telefono");
                                    String usuarios_cargo = pedido.getString("usuarios_cargo");


                                    String factura_telefono = pedido.getString("factura_telefono");
                                    String factura_direccion = pedido.getString("factura_direccion");
                                    String factura_referencia = pedido.getString("factura_referencia");
                                    double factura_precio_envio = pedido.getDouble ("factura_precio_envio");
                                    domicilio.inicializarDomicilio (cliente,
                                            format.parse (factura_fecha),
                                            factura_idfacturas,
                                            usuarios_idempleado,
                                            usuarios_identificacion,
                                            usuarios_nombres,
                                            usuarios_apellidos,
                                            usuarios_telefono,
                                            usuarios_cargo,
                                            factura_telefono,
                                            factura_direccion,
                                            factura_referencia,
                                            factura_precio_envio);
                                    Toast.makeText(getContext(), "Domicilio modificado exitosamente",Toast.LENGTH_SHORT).show();
                                    adaptadorListaDom.notifyDataSetChanged ();
                                }
                                dialog.dismiss ();
                            }else
                            {
                                String error = response.getString ("error");
                                Toast.makeText(getContext(), respuesta.booleanValue ()+" Error: "+error,Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException | ParseException e)
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
        });

    }

}