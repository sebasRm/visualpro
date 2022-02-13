package com.example.myapplication.interfaz.inventario;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
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
import com.example.myapplication.adaptador.AdaptadorListaProductos;
import com.example.myapplication.adaptador.AdaptadorVerCompras;
import com.example.myapplication.adaptador.AdaptadorVerVentas;
import com.example.myapplication.adaptador.Servidor;
import com.example.myapplication.adaptador.VolleySingleton;
import com.example.myapplication.mundo.CompraProducto;
import com.example.myapplication.mundo.Inventario;
import com.example.myapplication.mundo.VentaProducto;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link InventarioProductosFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class InventarioProductosFragment extends Fragment {

   ImageButton btnProductos,btnFechaInventario,btnFechaSurtir,btnHistorico,btnVentas;
   TextView nombreProducto, stockMin,txtFechaInventario,lblNombreProductoSurtir,  lblIdProductoSurtir,lblFechaSurtir,lblNombreProductoModificar,lblIdProductoModifcar;
   Button registrarProducto, btnResgistrarSurtir,btnModificarProducto;
   EditText  editTextTextPersonName,txtStockProductoModificar, txtNombreProductoModificar,edtCantidadProducto,edtDescripcionProducto;

    private RequestQueue requestQueue;
    private JsonRequest jsonRequest;
    private RecyclerView rcvInventario;
    private RecyclerView rcvVentas;
    private RecyclerView rcvCompras;


    private AdaptadorListaProductos adaptadorListaInventario;
    private AdaptadorVerVentas adaptadorVerVentas;
    private AdaptadorVerCompras adaptadorVerCompras;
    private ArrayList<Inventario> listaInventario;
    private ArrayList<VentaProducto> listaVentas;
    private ArrayList<CompraProducto> listaCompras;

    private String fecha;

    public InventarioProductosFragment()
    {
        // Required empty public constructor
    }


    public static InventarioProductosFragment newInstance(String param1, String param2) {
        InventarioProductosFragment fragment = new InventarioProductosFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        if (getArguments() != null)
        {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_inventario_productos, container, false);
        this.fecha="";
        this.requestQueue = VolleySingleton.getInstance(getContext()).getRequestQueue();
        this.listaInventario = new ArrayList<> ();
        this.listaVentas=new ArrayList<> ();
        this.listaCompras=new ArrayList<> ();

        this.rcvInventario=v.findViewById(R.id.listaInventario);
        this.adaptadorListaInventario = new AdaptadorListaProductos (getContext(),this.listaInventario,this);
        this.adaptadorVerVentas= new AdaptadorVerVentas (getContext(),this.listaVentas,this);
        this.adaptadorVerCompras= new AdaptadorVerCompras (getContext(),this.listaCompras,this);
        this.rcvInventario.setLayoutManager (new LinearLayoutManager (getContext ()));
        this.rcvInventario.setAdapter (this.adaptadorListaInventario);

        btnProductos= v.findViewById(R.id.btnProductos);
        btnFechaInventario= v.findViewById(R.id.btnFechaInventario);
        txtFechaInventario= v.findViewById(R.id.txtFechaInventario);



        btnVentas=v.findViewById(R.id.btnVentas);
        btnVentas.setOnClickListener(new View.OnClickListener() {
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
        btnHistorico= v.findViewById(R.id.btnHistorico);
        btnHistorico.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                consultarInventario("");
            }
        });
        consultarInventario("");
        btnFechaInventario.setOnClickListener (new View.OnClickListener ()
        {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v)
            {
                final Calendar calendario = Calendar.getInstance ();

                int yy = calendario.get (Calendar.YEAR);
                int mm = calendario.get (Calendar.MONTH);
                int dd = calendario.get (Calendar.DAY_OF_MONTH);


                DatePickerDialog datePicker = new DatePickerDialog (getActivity (), new DatePickerDialog.OnDateSetListener ()
                {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth)
                    {
                        fecha = year + "-" + ((monthOfYear+1)<10?"0"+(monthOfYear + 1):(monthOfYear + 1)+"");
                        txtFechaInventario.setText (fecha);
                        final ProgressDialog loading = ProgressDialog.show(getContext(), "Consultando inventario...", "Espere por favor...", false, false);
                        Map<String, String> params = new HashMap<String, String> ();
                        params.put("consultarListaInventario", "true");
                        params.put("fecha", fecha);
                        JSONObject parameters = new JSONObject(params);
                        String url = Servidor.HOST +"/consultas/inventario.php";
                        jsonRequest = new JsonObjectRequest (Request.Method.POST, url, parameters, new Response.Listener<JSONObject>()
                        {
                            @Override
                            public void onResponse(JSONObject response)
                            {
                                listaInventario.clear ();
                                try
                                {
                                    loading.dismiss();

                                    Boolean respuesta = response.getBoolean ("respuesta");
                                    if (respuesta.booleanValue ())
                                    {
                                        JSONArray datos = response.getJSONArray ("datos");

                                        for (int i = 0; i < datos.length(); i++)
                                        {

                                            JSONObject ventas = datos.getJSONObject(i);
                                            int idProductos=  ventas.getInt ("productos_idproductos");
                                            String nombre= ventas.getString ("productos_nombre");
                                            int stock_minimo=ventas.getInt ("productos_stock_minimo");
                                            int entrada_total= ventas.getInt ("entrada_total");
                                            int salida_total=ventas.getInt ("salida_total");
                                            int  stock=ventas.getInt ("stock");
                                             Inventario inventario=new Inventario( idProductos,  nombre,  stock_minimo,  entrada_total,  salida_total,  stock);
                                            listaInventario.add (inventario);
                                        }
                                        adaptadorListaInventario.notifyDataSetChanged ();
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
                        int socketTimeout = 0;
                        requestQueue.add(jsonRequest);
                    }

                }, yy, mm, dd);
                //datePicker.set
                datePicker.show();
            }
        });
        btnProductos.setOnClickListener (new View.OnClickListener ()
        {
            @Override
            public void onClick(View view)
            {

                AlertDialog.Builder builder = new AlertDialog.Builder (getContext ());
                LayoutInflater inflater = getLayoutInflater ();
                view = inflater.inflate (R.layout.dialog_registrar_producto, null);
                builder.setView (view);
                final AlertDialog dialog = builder.create ();
                dialog.show ();

                nombreProducto = view.findViewById (R.id.txtNombreProducto);
                stockMin = view.findViewById (R.id.txtStockMin);
                registrarProducto = view.findViewById (R.id.btnRegistrarProducto);


                registrarProducto.setOnClickListener (new View.OnClickListener ()
                {
                    @Override
                    public void onClick(View v)
                    {
                        try
                        {
                            String nombre=nombreProducto.getText ().toString ();
                            int stock=Integer.parseInt (stockMin.getText ().toString ().isEmpty ()?"0":stockMin.getText ().toString ());
                            if (nombre.isEmpty ())
                            {
                                Toast.makeText(getContext(), " Digite nombre del producto",Toast.LENGTH_SHORT).show();
                            }else if (stock<=0)
                            {
                                Toast.makeText(getContext(), "El stock  minimo no debe ser menor a uno",Toast.LENGTH_SHORT).show();
                            }else
                            {
                                final ProgressDialog loading = ProgressDialog.show(getContext(), "Registrando producto...", "Espere por favor...", false, false);
                                Map<String, String> params = new HashMap<String, String> ();
                                params.put("registarProducto", "true");
                                params.put("nombre", nombre);
                                params.put("stock", stock+"");
                                JSONObject parameters = new JSONObject(params);
                                String url = Servidor.HOST +"/consultas/inventario.php";
                                jsonRequest = new JsonObjectRequest (Request.Method.POST, url, parameters, new Response.Listener<JSONObject>()
                                {
                                    @Override
                                    public void onResponse(JSONObject response)
                                    {
                                        listaInventario.clear ();
                                        try
                                        {
                                            loading.dismiss();

                                            Boolean respuesta = response.getBoolean ("respuesta");
                                            if (respuesta.booleanValue ())
                                            {
                                                consultarInventario(fecha);
                                                Toast.makeText(getContext(), " Producto registrado exitosamente",Toast.LENGTH_SHORT).show();
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
                        }catch (Exception error)
                        {
                            Toast.makeText(getContext(), "Stock ingresado no es valido",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
        return v;
    }


    public void iniciarSurtirProducto(final Inventario  inventario, View view)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder (getContext ());
        LayoutInflater inflater = getLayoutInflater ();
        view = inflater.inflate (R.layout.dialog_surtir_producto, null);
        builder.setView (view);
        final AlertDialog dialog = builder.create ();
        dialog.show ();

        lblNombreProductoSurtir = view.findViewById (R.id.lblNombreProductoSurtir);
        lblIdProductoSurtir = view.findViewById (R.id.lblIdProductoSurtir);
        lblNombreProductoSurtir.setText (inventario.getNombre ());
        lblIdProductoSurtir.setText (inventario.getIdproductos ()+"");

        editTextTextPersonName = view.findViewById (R.id.editTextPrecioProducto);
        btnFechaSurtir = view.findViewById (R.id.btnFechaSurtir);
        lblFechaSurtir = view.findViewById (R.id.lblFechaSurtir);

        edtCantidadProducto=view.findViewById(R.id.edtCantidadProducto);
        edtDescripcionProducto=view.findViewById(R.id.edtDescripcionProducto);

        btnResgistrarSurtir = view.findViewById (R.id.btnResgistrarSurtir);

        btnFechaSurtir.setOnClickListener (new View.OnClickListener ()
        {
            @Override
            public void onClick(View v) {
                final Calendar calendario = Calendar.getInstance ();

                int yy = calendario.get (Calendar.YEAR);
                int mm = calendario.get (Calendar.MONTH);
                int dd = calendario.get (Calendar.DAY_OF_MONTH);


                DatePickerDialog datePicker = new DatePickerDialog (getActivity (), new DatePickerDialog.OnDateSetListener ()
                {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth)
                    {
                        String fechaCompra = year + "-" + ((monthOfYear+1)<10?"0"+(monthOfYear + 1):(monthOfYear + 1))+"-"+((dayOfMonth+1)<10?"0"+(dayOfMonth):(dayOfMonth));
                        lblFechaSurtir.setText (fechaCompra);

                    }

                }, yy, mm, dd);
                //datePicker.set
                datePicker.show();
            }
        });

        btnResgistrarSurtir.setOnClickListener (new View.OnClickListener ()
        {
            @Override
            public void onClick(View v)
            {
                try
                {

                    double precio=Double.parseDouble (editTextTextPersonName.getText ().toString ().isEmpty ()?"0":editTextTextPersonName.getText ().toString ());
                    String fecha=lblFechaSurtir.getText ().toString ();
                   int cantidad =Integer.parseInt ( edtCantidadProducto.getText ().toString ().isEmpty ()?"0": edtCantidadProducto.getText ().toString ());
                    String descripcion=edtDescripcionProducto.getText ().toString ();
                    if (precio<=0)
                    {
                        Toast.makeText(getContext(), "El precio del prodcuto no debe ser menor a uno",Toast.LENGTH_SHORT).show();

                    }else if (fecha.equals ("Fecha"))
                    {
                        Toast.makeText(getContext(), "Seleccione una fecha valida",Toast.LENGTH_SHORT).show();
                    }else if (cantidad<=0)
                    {
                        Toast.makeText(getContext(), "Cantidad de prodcutos no debe ser menor a uno",Toast.LENGTH_SHORT).show();
                    }else if (descripcion.isEmpty ())
                    {
                        Toast.makeText(getContext(), "Ingrese una descripción de compra",Toast.LENGTH_SHORT).show();
                    }else
                    {
                        final ProgressDialog loading = ProgressDialog.show(getContext(), "Surtiendo producto...", "Espere por favor...", false, false);
                        Map<String, String> params = new HashMap<String, String> ();
                        params.put("surtirProducto", "true");
                        params.put("idproductos", lblIdProductoSurtir.getText ().toString ());
                        params.put("precio", precio+"");
                        params.put("fecha", fecha+"");
                        params.put("cantidad", cantidad+"");
                        params.put("descripcion",descripcion);
                        params.put("factura", "1");
                        JSONObject parameters = new JSONObject(params);
                        String url = Servidor.HOST +"/consultas/inventario.php";
                        jsonRequest = new JsonObjectRequest (Request.Method.POST, url, parameters, new Response.Listener<JSONObject>()
                        {
                            @Override
                            public void onResponse(JSONObject response)
                            {
                                listaInventario.clear ();
                                try
                                {
                                    loading.dismiss();

                                    Boolean respuesta = response.getBoolean ("respuesta");
                                    if (respuesta.booleanValue ())
                                    {
                                        consultarInventario("");
                                        Toast.makeText(getContext(), " Producto surtido exitosamente",Toast.LENGTH_SHORT).show();
                                        dialog.dismiss ();
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
                }catch (Exception error)
                {
                    Toast.makeText(getContext(), "Valor númerico ingresado no es valido",Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    public void iniciarVerVentas(int idproductos, View view)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder (getContext ());
        LayoutInflater inflater = getLayoutInflater ();
        view = inflater.inflate (R.layout.dialog_ver_ventas, null);
        builder.setView (view);
        this.rcvVentas=view.findViewById(R.id.listaVerVentas);
        this.rcvVentas.setLayoutManager (new LinearLayoutManager (getContext ()));
        this.rcvVentas.setAdapter (this.adaptadorVerVentas);
        final AlertDialog dialog = builder.create ();
        final ProgressDialog loading = ProgressDialog.show(getContext(), "Consultado ventas...", "Espere por favor...", false, false);
        Map<String, String> params = new HashMap<String, String> ();
        params.put("consultasVentaProducto", "true");
        params.put("idProducto",idproductos+"");
        Toast.makeText (getContext(),fecha ,Toast.LENGTH_SHORT).show();
        params.put("fecha", fecha);
        JSONObject parameters = new JSONObject(params);
        String url = Servidor.HOST +"/consultas/inventario.php";
        jsonRequest = new JsonObjectRequest (Request.Method.POST, url, parameters, new Response.Listener<JSONObject>()
        {
            @Override
            public void onResponse(JSONObject response)
            {
                listaVentas.clear ();
                try
                {
                    loading.dismiss();

                    Boolean respuesta = response.getBoolean ("respuesta");
                    if (respuesta.booleanValue ())
                    {
                        JSONArray datos = response.getJSONArray ("datos");
                        for (int i = 0; i < datos.length(); i++)
                        {
                            JSONObject ventas = datos.getJSONObject(i);

                             String idfacturas=  ventas.getString ("facturas_idfacturas");
                             String fecha=  ventas.getString ("facturas_fecha");
                             String codigoProducto=  ventas.getString ("productos_idproductos");
                             String nombreProducto=  ventas.getString ("productos_nombre");
                             String tipo=  ventas.getString ("tipo");
                             String plato=  ventas.getString ("platos_nombre");
                             int cantidad=  ventas.getInt ("recursos_cantidad");

                            VentaProducto v=new VentaProducto( idfacturas,  fecha,  codigoProducto,  nombreProducto,  tipo,  plato,  cantidad);

                            listaVentas.add (v);
                        }
                        dialog.dismiss ();
                        dialog.show ();
                        adaptadorVerVentas.notifyDataSetChanged ();
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

    public void iniciarVerCompras(int idproductos, View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder (getContext ());
        LayoutInflater inflater = getLayoutInflater ();
        view = inflater.inflate (R.layout.dialog_ver_compras, null);
        builder.setView (view);
        this.rcvCompras=view.findViewById(R.id.rcvCompras );
        this.rcvCompras.setLayoutManager (new LinearLayoutManager (getContext ()));
        this.rcvCompras.setAdapter (this.adaptadorVerCompras);
        final AlertDialog dialog = builder.create ();
        final ProgressDialog loading = ProgressDialog.show(getContext(), "Consultado compras...", "Espere por favor...", false, false);
        Map<String, String> params = new HashMap<String, String> ();
        params.put("consultasComprasProducto", "true");
        params.put("idProducto",idproductos+"");
        params.put("fecha", fecha);
        JSONObject parameters = new JSONObject(params);
        String url = Servidor.HOST +"/consultas/inventario.php";
        jsonRequest = new JsonObjectRequest (Request.Method.POST, url, parameters, new Response.Listener<JSONObject>()
        {
            @Override
            public void onResponse(JSONObject response)
            {
                listaCompras.clear ();
                try
                {
                    loading.dismiss();

                    Boolean respuesta = response.getBoolean ("respuesta");
                    if (respuesta.booleanValue ())
                    {
                        JSONArray datos = response.getJSONArray ("datos");
                        for (int i = 0; i < datos.length(); i++) {
                            JSONObject compra = datos.getJSONObject (i);

                             String idfacturas=  compra.getString ("factura");;
                             String fecha=  compra.getString ("fecha");;
                             String codigoProducto=  compra.getString ("productos_idproductos");;
                             String descripcion=  compra.getString ("descripcion");;
                             int cantidad=  compra.getInt ("cantidad");;
                            CompraProducto compraProducto=new  CompraProducto( idfacturas,  fecha,  codigoProducto,  descripcion,  cantidad);

                            listaCompras.add (compraProducto);
                        }
                        dialog.dismiss ();
                        dialog.show ();
                        adaptadorVerCompras.notifyDataSetChanged ();
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

    public void iniciarModificarProducto(final Inventario inventario, View view)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder (getContext ());
        LayoutInflater inflater = getLayoutInflater ();
        view = inflater.inflate (R.layout.dialog_modificar_producto, null);
        builder.setView (view);
        final AlertDialog dialog = builder.create ();
        dialog.show ();

        lblNombreProductoModificar= view.findViewById (R.id.lblNombreProductoModificar);
        lblIdProductoModifcar= view.findViewById (R.id.lblIdProductoModifcar);
        lblNombreProductoModificar.setText (inventario.getNombre ());
        lblIdProductoModifcar.setText (inventario.getIdproductos ()+"");
        btnModificarProducto= view.findViewById (R.id.btnModificarProducto);

        txtStockProductoModificar= view.findViewById (R.id.txtStockProductoModificar);
        txtNombreProductoModificar= view.findViewById (R.id.txtNombreProductoModificar);

        btnModificarProducto.setOnClickListener (new View.OnClickListener ()
        {
            @Override
            public void onClick(View v)
            {
                try
                {
                    String nombre=txtNombreProductoModificar.getText ().toString ();
                    int stock=Integer.parseInt (txtStockProductoModificar.getText ().toString ().isEmpty ()?"0":txtStockProductoModificar.getText ().toString ());
                    if (nombre.isEmpty ())
                    {
                        Toast.makeText(getContext(), " Digite nombre del producto",Toast.LENGTH_SHORT).show();
                    }else if (stock<=0)
                    {
                        Toast.makeText(getContext(), "El stock  minimo no debe ser menor a uno",Toast.LENGTH_SHORT).show();
                    }else
                    {
                        final ProgressDialog loading = ProgressDialog.show(getContext(), "Registrando producto...", "Espere por favor...", false, false);
                        Map<String, String> params = new HashMap<String, String> ();
                        params.put("modificarProducto", "true");
                        params.put("idProducto", inventario.getIdproductos ()+"");
                        params.put("nombre", nombre);
                        params.put("stock", stock+"");
                        JSONObject parameters = new JSONObject(params);
                        String url = Servidor.HOST +"/consultas/inventario.php";
                        jsonRequest = new JsonObjectRequest (Request.Method.POST, url, parameters, new Response.Listener<JSONObject>()
                        {
                            @Override
                            public void onResponse(JSONObject response)
                            {
                                listaInventario.clear ();
                                try
                                {
                                    loading.dismiss();

                                    Boolean respuesta = response.getBoolean ("respuesta");
                                    if (respuesta.booleanValue ())
                                    {
                                        consultarInventario("");
                                        Toast.makeText(getContext(), " Producto modificado exitosamente",Toast.LENGTH_SHORT).show();
                                        dialog.dismiss ();
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
                }catch (Exception error)
                {
                    Toast.makeText(getContext(), "Stock ingresado no es valido",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void consultarInventario(String fecha)
    {
        final ProgressDialog loading = ProgressDialog.show(getContext(), "Consultando inventario...", "Espere por favor...", false, false);
        Map<String, String> params = new HashMap<String, String> ();
        params.put("consultarListaInventario", "true");
        params.put("fecha", fecha);
        JSONObject parameters = new JSONObject(params);
        String url = Servidor.HOST +"/consultas/inventario.php";
        jsonRequest = new JsonObjectRequest (Request.Method.POST, url, parameters, new Response.Listener<JSONObject>()
        {
            @Override
            public void onResponse(JSONObject response)
            {
                listaInventario.clear ();
                try
                {
                    loading.dismiss();

                    Boolean respuesta = response.getBoolean ("respuesta");
                    if (respuesta.booleanValue ())
                    {
                        JSONArray datos = response.getJSONArray ("datos");

                        for (int i = 0; i < datos.length(); i++)
                        {
                            JSONObject ventas = datos.getJSONObject(i);
                            int idProductos=  ventas.getInt ("productos_idproductos");
                            String nombre= ventas.getString ("productos_nombre");
                            int stock_minimo=ventas.getInt ("productos_stock_minimo");
                            int entrada_total= ventas.getInt ("entrada_total");
                            int salida_total=ventas.getInt ("salida_total");
                            int  stock=ventas.getInt ("stock");
                            Inventario inventario=new Inventario( idProductos,  nombre,  stock_minimo,  entrada_total,  salida_total,  stock);
                            listaInventario.add (inventario);
                        }
                        adaptadorListaInventario.notifyDataSetChanged ();
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