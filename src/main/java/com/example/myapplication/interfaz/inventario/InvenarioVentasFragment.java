package com.example.myapplication.interfaz.inventario;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.TableRow;
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
import com.example.myapplication.adaptador.AdaptadorListaVentas;
import com.example.myapplication.adaptador.Servidor;
import com.example.myapplication.mundo.VentaOrden;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link InvenarioVentasFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class InvenarioVentasFragment extends Fragment {



    private ImageButton calendario, inventario;
    private TextView textFechaCalendario,txtTotalVenta;
    private RequestQueue requestQueue;
    private JsonRequest jsonRequest;
    private RecyclerView rcvVentas;
    private AdaptadorListaVentas adaptadorListaVentas;
    private ArrayList<VentaOrden> listaVentaOrdens;

    private VentaOrden ventaOrden;
    public InvenarioVentasFragment() {
        // Required empty public constructor
    }


    public static InvenarioVentasFragment newInstance(String param1, String param2) {
        InvenarioVentasFragment fragment = new InvenarioVentasFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        final NumberFormat nf = NumberFormat.getCurrencyInstance(Locale.getDefault());
        this.requestQueue = Volley.newRequestQueue (getContext ());
        View v= inflater.inflate(R.layout.fragment_invenario_ventas, container, false);
        calendario=v.findViewById(R.id.btnCalendario);
        textFechaCalendario=v.findViewById(R.id.textFechaCalendario);
        txtTotalVenta=v.findViewById(R.id.txtTotalVenta);
        //tblVentasDiarias=v.findViewById (R.id.tblVentasDiarias);

        this.listaVentaOrdens = new ArrayList<VentaOrden> ();
        rcvVentas=v.findViewById(R.id.listaVentas);
        this.adaptadorListaVentas = new AdaptadorListaVentas(getContext(),this.listaVentaOrdens);
        this.rcvVentas.setLayoutManager (new LinearLayoutManager(getContext ()));
        this.rcvVentas.setAdapter (this.adaptadorListaVentas);
        inventario=v.findViewById (R.id.btnInventario);
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

        calendario.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View view)
            {
                final Calendar calendario = Calendar.getInstance();

                int yy = calendario.get(Calendar.YEAR);
                int mm = calendario.get(Calendar.MONTH);
                int dd = calendario.get(Calendar.DAY_OF_MONTH);


                DatePickerDialog datePicker = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener()
                {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth)
                    {
                        String fecha = year +"-"+(monthOfYear+1)+"-"+dayOfMonth;
                        textFechaCalendario.setText(fecha);
                        final ProgressDialog loading = ProgressDialog.show(getContext(), "Consultando ventas...", "Espere por favor...", false, false);
                        Map<String, String> params = new HashMap<String, String> ();
                        params.put("consultarVentasDiarias", "true");
                        params.put("fecha", fecha);
                        JSONObject parameters = new JSONObject(params);
                        String url = Servidor.HOST +"/consultas/inventario.php";
                        jsonRequest = new JsonObjectRequest (Request.Method.POST, url, parameters, new Response.Listener<JSONObject>()
                        {
                            @Override
                            public void onResponse(JSONObject response)
                            {
                                listaVentaOrdens.clear ();
                                try
                                {
                                    loading.dismiss();


                                    Boolean respuesta = response.getBoolean ("respuesta");
                                    if (respuesta.booleanValue ())
                                    {
                                         double totalVenta = 0;
                                        JSONArray datos = response.getJSONArray ("datos");
                                        for (int i = 0; i < datos.length(); i++)
                                        {

                                            JSONObject ventas = datos.getJSONObject(i);
                                            String tipo=  ventas.getString ("tipo");
                                            String fecha= ventas.getString ("factura_fecha");
                                            String idfacturas=ventas.getString ("factura_idfacturas");
                                            String informacion= ventas.getString ("factura_informacion");
                                            double precio_envio=ventas.getDouble ("factura_precio_envio");
                                            String cliente_mesa=ventas.getString ("factura_cliente_mesa");
                                            int cantidad_total=ventas.getInt ("pedidos_cantidad_total");
                                            String usuarios_nombres=ventas.getString ("usuarios_nombres");

                                            double total=ventas.getDouble ("facturas_total");
                                            totalVenta+=total;
                                            VentaOrden ventaOrden = new VentaOrden ( tipo,  fecha,  idfacturas,  informacion,  precio_envio,  cliente_mesa,  cantidad_total,  usuarios_nombres,  total) ;
                                            listaVentaOrdens.add (ventaOrden);

                                        }
                                       txtTotalVenta.setText (nf.format (totalVenta)+"");
                                        adaptadorListaVentas.notifyDataSetChanged ();
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

                datePicker.show();
            }

        });


        return v;
    }
    private void crearZelda(TextView valueView )
    {
        valueView.setSingleLine(false);
        valueView.setMaxLines(20);
        valueView.setPadding(0, 20, 30, 20);
        valueView.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,89,TableRow.LayoutParams.WRAP_CONTENT));
        valueView.setBackgroundColor(Color.parseColor("#f8f8f8"));





    }
}