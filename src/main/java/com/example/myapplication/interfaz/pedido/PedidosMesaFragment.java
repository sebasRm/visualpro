package com.example.myapplication.interfaz.pedido;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.example.myapplication.R;
import com.example.myapplication.adaptador.AdaptadorListaMesa;
import com.example.myapplication.adaptador.AdaptadorListaMesaDesocupada;
import com.example.myapplication.adaptador.Servidor;
import com.example.myapplication.adaptador.VolleySingleton;
import com.example.myapplication.mundo.Mesa;
import com.example.myapplication.mundo.OrdenPedido;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

/**

 * Use the {@link PedidosMesaFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PedidosMesaFragment extends Fragment implements View.OnDragListener
{
    protected RequestQueue requestQueue;
    protected JsonRequest jsonRequest;
    private SearchView buscarMesa;
    private RecyclerView listaMesas, mesasDesocupadas;
    private AdaptadorListaMesa adaptadorListaMesa;
    private AdaptadorListaMesaDesocupada adaptadorListaMesaDesocupada;
    private ArrayList<Mesa> mesasDes;
    private ArrayList<Mesa> mesasDesAux;
    private ArrayList<OrdenPedido> mesas;
    private ArrayList<OrdenPedido> mesasAux;
    private OrdenPedido ordenPedido;
    private int cantMesas;
    private int cantMesasDes;
    private Activity actividad;
    ImageButton agregarMesa;
    private Timer timer;


    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;
    private String idEmpleados;

    public PedidosMesaFragment()
    {
        // Required empty public constructor
    }

    @Override
    public void onStart()
    {
        super.onStart();
        this.timer = new Timer();
        this.timer.scheduleAtFixedRate(new TimerTask ()
        {
            @Override
            public void run()
            {
                buscarlista ();
                //  buscarMesaDesocupada ();
               System.out.println ("A Kiss after 5 seconds");
            }

        },1,4000);
    }

    public static PedidosMesaFragment newInstance(String param1, String param2)
    {
        PedidosMesaFragment fragment = new PedidosMesaFragment ();
        Bundle args = new Bundle ();
        args.putString (ARG_PARAM1, param1);
        args.putString (ARG_PARAM2, param2);
        fragment.setArguments (args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate (savedInstanceState);
        if (getArguments () != null)
        {
            mParam1 = getArguments ().getString (ARG_PARAM1);
            mParam2 = getArguments ().getString (ARG_PARAM2);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate (R.layout.fragment_pedidos_mesa, container, false);

        this.agregarMesa = v.findViewById (R.id.AgregarPedido);
        this.buscarMesa = v.findViewById (R.id.searchBuscarDomicilio);
        this.listaMesas = v.findViewById (R.id.listaPlatosMesas);
        this.mesasDesocupadas = v.findViewById (R.id.listaMesasDesocupadas);
        this.requestQueue = VolleySingleton.getInstance(getContext()).getRequestQueue();
        this.mesasDes = new ArrayList<Mesa> ();
        this.mesasDesAux = new ArrayList<Mesa> ();
        this.mesas = new ArrayList<OrdenPedido> ();
        this.mesasAux = new ArrayList<OrdenPedido> ();
        this.adaptadorListaMesa = new AdaptadorListaMesa (getContext (), this.mesas, this);
        this.listaMesas.setLayoutManager (new LinearLayoutManager (getContext ()));
        this.mesasDesocupadas.setLayoutManager (new GridLayoutManager (getContext (), 3));
        this.adaptadorListaMesaDesocupada = new AdaptadorListaMesaDesocupada (getContext (), this.mesasDes,this);
        this.mesasDesocupadas.setAdapter (adaptadorListaMesaDesocupada);
        this.listaMesas.setAdapter(adaptadorListaMesa);
        this.listaMesas.setLayoutManager (new GridLayoutManager (getContext (), 3));
        recuperarPreferencias();


        this.buscarMesa.setOnQueryTextListener (new SearchView.OnQueryTextListener ()
        {
            @Override
            public boolean onQueryTextSubmit(String query) {return false; }

            @Override
            public boolean onQueryTextChange(String newText)
            {
                mesas.clear ();
                listaMesas.setAdapter (adaptadorListaMesa);

                if (!newText.isEmpty ())
                {
                    for (OrdenPedido m : mesasAux)
                    {
                        if (m.getMesa ().getNumero ().contains (newText))
                        {
                            mesas.add (m);
                        }
                    }
                } else
                {
                    mesas.addAll (mesasAux);
                }
                return false;
            }
        });
        this.adaptadorListaMesa.setOnclickListener (new View.OnClickListener ()
        {
            @Override
            public void onClick(View v)
            {
               int pos=listaMesas.getChildAdapterPosition (v);
                if (pos>-1)
                {
                    ordenPedido = mesas.get (listaMesas.getChildAdapterPosition (v));
                    Bundle bundleEnvio = new Bundle ();
                    bundleEnvio.putSerializable ("mesa", ordenPedido);
                    getParentFragmentManager ().setFragmentResult ("key", bundleEnvio);
                }
            }
        });
        this. mesasDesocupadas.setOnDragListener(this);
        this.listaMesas.setOnDragListener(this);

        return v;

    }


    //Modificar php y procedimiento
    public void buscarlista()
    {
        Map<String,String> params= new HashMap<String, String> ();
        params.put("buscarOrdenPedidos","Mes");
        JSONObject parameters = new JSONObject(params);
        String url=Servidor.HOST +"/consultas/pedidos.php";
        jsonRequest=new JsonObjectRequest (Request.Method.POST, url, parameters, new Response.Listener<JSONObject> ()
        {
            @Override
            public void onResponse(JSONObject response)
            {
                try
                {
                    Boolean respuesta = response.getBoolean ("respuesta");
                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    if (respuesta.booleanValue ())
                    {
                        JSONArray datos = response.getJSONArray ("datos");
                        cantMesas=contarMesasOcupadas(datos);
                        if ( datos.length()-cantMesas!= cantMesasDes)
                        {
                            mesasDesAux.clear ();
                            mesasAux.clear();
                            for (int i = 0; i < datos.length(); i++)
                            {
                                JSONObject pedido = datos.getJSONObject(i);
                                int id=pedido.getInt ("mesas_idmesas");
                                String numero=pedido.getString("mesas_numero");
                                String codigoQR=pedido.getString("mesas_codigoQR");
                                String estado=pedido.getString("estado");
                                Mesa m=new Mesa( id,  numero,  codigoQR, estado);




                                if(estado.equals("Ocupada")){

                                    String factura_fecha = pedido.getString("factura_fecha");
                                    int factura_idfacturas = pedido.getInt("factura_idfacturas");
                                    double factura_pagado = pedido.getDouble("factura_pagado");
                                    double factura_IVA = pedido.getDouble("factura_IVA");
                                    int usuarios_idempleado = pedido.getInt("usuarios_idempleado");
                                    String usuarios_identificacion = pedido.getString("usuarios_identificacion");
                                    String usuarios_nombres = pedido.getString("usuarios_nombres");
                                    String usuarios_apellidos = pedido.getString("usuarios_apellidos");
                                    String usuarios_telefono = pedido.getString("usuarios_telefono");
                                    String usuarios_cargo = pedido.getString("usuarios_cargo");

                                    OrdenPedido  aux=new OrdenPedido ();

                                    aux.inicializarPedidos(m,
                                            factura_pagado,
                                            factura_IVA,
                                            format.parse(factura_fecha),
                                            factura_idfacturas,
                                            usuarios_idempleado,
                                            usuarios_identificacion,
                                            usuarios_nombres,
                                            usuarios_apellidos,
                                            usuarios_telefono,
                                            usuarios_cargo);
                                    mesasAux.add(aux);
                                }else{
                                    mesasDesAux.add (m);
                                }
                            }
                            mesasDes.clear ();
                            mesas.clear();
                            mesas.addAll(mesasAux);
                            mesasDes.addAll (mesasDesAux);
                            adaptadorListaMesaDesocupada.notifyDataSetChanged ();
                            adaptadorListaMesa.notifyDataSetChanged ();
                            cantMesasDes = mesasDesAux.size ();
                        }
                        if (isVisible ())
                        {
                            if (ordenPedido instanceof  OrdenPedido)
                            {
                                Bundle bundleEnvio = new Bundle ();
                                bundleEnvio.putSerializable ("mesa", ordenPedido);
                                getParentFragmentManager ().setFragmentResult ("key", bundleEnvio);
                            }else
                            {
                                Bundle bundleEnvio = new Bundle ();
                                bundleEnvio.putSerializable ("mesa", null);
                                getParentFragmentManager ().setFragmentResult ("key", bundleEnvio);
                            }
                        }
                    }else
                    {
                        String error = response.getString ("error");
                        Toast.makeText(getContext(), respuesta.booleanValue ()+" Error: "+error,Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException  | ParseException e)
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

    private int contarMesasOcupadas(JSONArray datos) throws JSONException
    {
        int contador=0;
        for (int i = 0; i < datos.length(); i++)
        {
            JSONObject mesa = datos.getJSONObject(i);
            String estado=mesa.getString("estado");
            if(estado.equals("Ocupada"))
            {
                contador++;
            }
        }
        return contador;
    }

    public void onAttach(Context context)
    {
        super.onAttach(context);
        if (context instanceof Activity)
        {
            this.actividad = (Activity) context;
            // this.interfazFragamen = (InterfazFragamen) this.actividad;
        }
    }

    @Override
    public void onDetach()
    {
        super.onDetach();
    }

    @Override
    public boolean onDrag(View v, DragEvent event)
    {
        int action = event.getAction();
        switch (action)
        {
            case DragEvent.ACTION_DRAG_STARTED:
                break;
            case DragEvent.ACTION_DRAG_ENTERED:
                //    v.setBackgroundColor(Color.LTGRAY);
                break;
            case DragEvent.ACTION_DRAG_EXITED:
                // v.setBackgroundColor(Color.YELLOW);
                break;
            case DragEvent.ACTION_DROP:
                int positionFuente = -1, posicionDestion=-1;
                View viewSource = (View) event.getLocalState();
                RecyclerView recyclerView= (RecyclerView) viewSource.getParent ();
                positionFuente = (int) viewSource.getTag ();
                if((v instanceof  RecyclerView)
                    && (recyclerView instanceof  RecyclerView)
                    &&  v.getId() == R.id.listaMesasDesocupadas
                    && recyclerView.getId ()== R.id.listaPlatosMesas)
                {

                    positionFuente = (int) viewSource.getTag ();

                  //  positionFuente = (int) viewSource.getTag ();

                    final AdaptadorListaMesa adaptadorListaMesa= (AdaptadorListaMesa) recyclerView.getAdapter();
                    ordenPedido = adaptadorListaMesa.getList().get(positionFuente);

                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    LayoutInflater inflater = getLayoutInflater();
                    View view = inflater.inflate(R.layout.dialog_eliminar_mesa, null);
                    builder.setView(view);
                    final AlertDialog dialog = builder.create();
                    dialog.show();

                    final TextView input = view.findViewById(R.id.txtEliminarMesa);
                    Button botonSi = view.findViewById(R.id.btnSiEliminar);
                    Button botonNo = view.findViewById(R.id.btnEliminarNo);
                    input.setText("¿Desea eliminar  el pedido de la mesa " + ordenPedido.getMesa ().getIdmesa() + "?");
                    builder.setView(input);


                    final int finalPositionFuente = positionFuente;
                    botonSi.setOnClickListener(new View.OnClickListener()
                    {
                        @Override
                        public void onClick(View view) {
                            final ProgressDialog loading = ProgressDialog.show(getContext(), "Eliminando pedido...", "Espere por favor...", false, false);
                            Map<String, String> params = new HashMap<String, String>();
                            params.put("eliminarUnPedido", "true");
                            params.put("idfactura", ordenPedido.getFactura_idfacturas () + "");
                            params.put("idmesa", ordenPedido.getMesa ().getIdmesa() + "");

                          //  Toast.makeText(getContext(),ordenPedido.getFactura_idfacturas () + " - "+ordenPedido.getMesa ().getIdmesa() ,Toast.LENGTH_SHORT).show();
                            JSONObject parameters = new JSONObject(params);
                            String url = Servidor.HOST +"/consultas/pedidos.php";
                            jsonRequest = new JsonObjectRequest(Request.Method.POST, url, parameters, new Response.Listener<JSONObject>()
                            {
                                @Override
                                public void onResponse(JSONObject response)
                                {
                                    try
                                    {
                                        loading.dismiss();
                                        Boolean respuesta = response.getBoolean ("respuesta");
                                        if (respuesta.booleanValue ())
                                        {
                                            Toast.makeText(getContext(), "Pedido elminado de la " + ordenPedido.getMesa ().getIdmesa(), Toast.LENGTH_SHORT).show();
                                            adaptadorListaMesa.getList().remove (finalPositionFuente);
                                            adaptadorListaMesa.notifyDataSetChanged ();
                                            Bundle bundleEnvio = new Bundle ();
                                            bundleEnvio.putSerializable ("mesa", null);
                                            getParentFragmentManager ().setFragmentResult ("key", bundleEnvio);
                                            ordenPedido =null;
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

                }/*else if((RecyclerView.getAdapter () instanceof AdaptadorListaMesaDesocupada)&&v.getId ()== R.id.itemMesaOcupada)
                {
                    final Mesa mesaOrigen = adaptadorListaMesa.getList ().get (positionFuente);
                    //final Mesa mesaDestino = adaptadorListaMesaDesocupada.getList ().get (posicionDestion);
                   // final ProgressDialog loading = ProgressDialog.show(getContext (),"Creando pedido...","Espere por favor...",false,false);
                    Map<String, String> params = new HashMap<String, String>();
                    params.put ("cambiarMesaPedio", "true");
                    Toast.makeText (getContext (), "Pedido elminado de la ", Toast.LENGTH_SHORT).show ();
                    params.put ("idmesaOrigen", mesaOrigen.getIdmesa ()+"");
                  //  params.put ("idmesaDestion", mesaDestino.getIdmesa ()+"");
                    JSONObject parameters = new JSONObject (params);
                    String url = "http://192.168.1.27/consultas/pedidos.php";
                    jsonRequest = new JsonObjectRequest(Request.Method.POST, url, parameters, new Response.Listener<JSONObject> ()
                    {
                        @Override
                        public void onResponse(JSONObject response)
                        {
                            /*loading.dismiss ();
                            Toast.makeText (getContext (), "Pedido elminado de la "+ mesa.getIdmesa (), Toast.LENGTH_SHORT).show ();
                            adaptadorListaMesaDesocupada.getList ().add (mesa);
                            adaptadorListaMesa.getList ().remove (finalPositionFuente1);
                            adaptadorListaMesaDesocupada.notifyDataSetChanged ();
                            adaptadorListaMesa.notifyDataSetChanged ();
                            Bundle bundleEnvio = new Bundle ();

                            bundleEnvio.putSerializable ("mesa", null);
                            getParentFragmentManager ().setFragmentResult ("key", bundleEnvio);

                            dialog.cancel();
                        }
                    }, new Response.ErrorListener ()
                    {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            error.printStackTrace ();
                            //loading.dismiss ();
                        }
                    });
                    int socketTimeout = 0;
                    //requestQueue.add (jsonRequest);

                }*/ else if((v instanceof  RecyclerView)
                        && (recyclerView instanceof  RecyclerView)
                        &&  v.getId() == R.id.listaPlatosMesas
                        && recyclerView.getId ()== R.id.listaMesasDesocupadas)

                {

                    positionFuente = (int) viewSource.getTag ();

                   // positionFuente = (int) viewSource.getTag ();

                    final AdaptadorListaMesaDesocupada adaptadorListaMesaDesocupada= (AdaptadorListaMesaDesocupada) recyclerView.getAdapter();
                    final Mesa mesaDesocupada =  adaptadorListaMesaDesocupada.getList().get(positionFuente);
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext ());
                    LayoutInflater inflater= getLayoutInflater();
                    View view = inflater.inflate(R.layout.dialog_agregar_mesa,null);
                    builder.setView(view);
                    final AlertDialog dialog = builder.create();
                    dialog.show ();


                    final TextView input = view.findViewById(R.id.txtAgregarMesa);
                    input.setText ("¿Desea realizar un pedido en la "+ mesaDesocupada.getNumero()+"?");
                    Button botonSiAgregar =view.findViewById(R.id.botonSiAgregar);
                    Button botonNoAgregar =view.findViewById(R.id.botonNoAgregar);

                    final int finalPositionFuente1 = positionFuente;
                    botonSiAgregar.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view)
                        {
                            final ProgressDialog loading = ProgressDialog.show(getContext (),"Creando pedido...","Espere por favor...",false,false);
                            Map<String, String> params = new HashMap<String, String>();
                            params.put ("crearPedido", "true");
                            params.put ("idmesa", mesaDesocupada.getIdmesa ()+"");
                            params.put ("idempleado", idEmpleados+"");
                            JSONObject parameters = new JSONObject (params);
                            String url = Servidor.HOST +"/consultas/pedidos.php";
                            jsonRequest = new JsonObjectRequest(Request.Method.POST, url, parameters, new Response.Listener<JSONObject> ()
                            {
                                @Override
                                public void onResponse(JSONObject response)
                                {
                                    try
                                    {
                                        loading.dismiss ();
                                        Boolean respuesta = response.getBoolean ("respuesta");
                                        if (respuesta.booleanValue ())
                                        {
                                            Toast.makeText (getContext (), "Pedido creado en la mesa "+ mesaDesocupada.getIdmesa (), Toast.LENGTH_SHORT).show ();
                                            JSONArray datos = response.getJSONArray ("datos");

                                            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

                                            for (int i = 0; i < datos.length(); i++)
                                            {
                                                JSONObject pedido = datos.getJSONObject(i);
                                                int id=pedido.getInt ("mesas_idmesas");
                                                String numero=pedido.getString("mesas_numero");
                                                String codigoQR=pedido.getString("mesas_codigoQR");
                                                String estado=pedido.getString("estado");
                                                Mesa m=new Mesa( id,  numero,  codigoQR, estado);


                                                    String factura_fecha = pedido.getString("factura_fecha");
                                                    int factura_idfacturas = pedido.getInt("factura_idfacturas");
                                                    double factura_pagado = pedido.getDouble("factura_pagado");
                                                    double factura_IVA = pedido.getDouble("factura_IVA");
                                                    int usuarios_idempleado = pedido.getInt("usuarios_idempleado");
                                                    String usuarios_identificacion = pedido.getString("usuarios_identificacion");
                                                    String usuarios_nombres = pedido.getString("usuarios_nombres");
                                                    String usuarios_apellidos = pedido.getString("usuarios_apellidos");
                                                    String usuarios_telefono = pedido.getString("usuarios_telefono");
                                                    String usuarios_cargo = pedido.getString("usuarios_cargo");
                                                ordenPedido=new OrdenPedido ();
                                                    ordenPedido.inicializarPedidos(m,
                                                            factura_pagado,
                                                            factura_IVA,
                                                            format.parse(factura_fecha),
                                                            factura_idfacturas,
                                                            usuarios_idempleado,
                                                            usuarios_identificacion,
                                                            usuarios_nombres,
                                                            usuarios_apellidos,
                                                            usuarios_telefono,
                                                            usuarios_cargo);
                                            }


                                            Bundle bundleEnvio = new Bundle ();
                                            bundleEnvio.putSerializable ("mesa", ordenPedido);
                                            getParentFragmentManager ().setFragmentResult ("key", bundleEnvio);
                                            adaptadorListaMesaDesocupada.getList().remove (finalPositionFuente1);
                                            adaptadorListaMesaDesocupada.notifyDataSetChanged ();

                                        }else
                                        {
                                            String error = response.getString ("error");
                                            Toast.makeText(getContext(), respuesta.booleanValue ()+" Error: "+error,Toast.LENGTH_SHORT).show();
                                        }

                                        dialog.cancel();
                                    }catch (JSONException | ParseException e )
                                    {
                                        e.printStackTrace ();
                                    }

                                }
                            }, new Response.ErrorListener ()
                            {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    error.printStackTrace ();
                                    loading.dismiss ();
                                }
                            });
                            requestQueue.add (jsonRequest);
                        }
                    });
                    botonNoAgregar.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialog.cancel();
                        }
                    });


                    dialog.show ();

                }
                onStart();
                break;
            case DragEvent.ACTION_DRAG_ENDED:
                v.setBackgroundColor(0);
                break;
            default:
                break;
        }
        View vw = (View) event.getLocalState();
        vw.setVisibility(View.VISIBLE);
        return true;
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

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStop()
    {
        super.onStop();
        this.ordenPedido =null;
        if(this.timer != null){
            this.timer.cancel();
        }
    }


}