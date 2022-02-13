package com.example.myapplication.adaptador;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.interfaz.plato.MenuPlatosFragment;
import com.example.myapplication.mundo.VentaOrden;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class AdaptadorListaVentas extends  RecyclerView.Adapter<AdaptadorListaVentas.ViewHolder>  implements View.OnClickListener
{

    private ArrayList<VentaOrden> list;
    private MenuPlatosFragment buscarPlato;
    private Context contexto;
    private static LayoutInflater  inflater = null;
    private View.OnClickListener listener;

    public AdaptadorListaVentas(Context contexto, ArrayList<VentaOrden> lista)
    {
        this.list=lista;
        this.contexto = contexto;
        inflater = LayoutInflater.from(contexto);
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = inflater.inflate(R.layout.fragment_lista_ventas, null);
        view.setOnClickListener(this);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        NumberFormat nf = NumberFormat.getCurrencyInstance(Locale.getDefault());
        holder.tipoDeOrdenVenta.setText(list.get(position).getTipo ());
        holder.fechaVenta.setText(list.get(position).getFecha ()+"");
        holder.facturaVenta.setText(list.get(position).getIdfacturas()+"");
        holder.facturaInformacionVenta.setText(list.get(position).getInformacion ()+"");
        holder.totalVenta.setText(list.get(position).getTotal ()==0?"Sin pagar":nf.format (list.get(position).getTotal ()));
        holder.clienteMesaVenta.setText(list.get(position).getCliente_mesa ());
        holder.empleadoVenta.setText(list.get(position).getUsuarios_nombres ());
        holder.precioDeEnvioVenta.setText(nf.format (list.get(position).getPrecio_envio()));
        holder.numeroDePlatos.setText(list.get(position).getCantidad_total ()+"");



    }

    @Override
    public int getItemCount() {
        return list.size();
    }
    public void setFragment(MenuPlatosFragment buscarPlato)
    {
        this.buscarPlato=buscarPlato;
    }

    @Override
    public void onClick(View v)
    {
        if(listener!=null)
        {
            listener.onClick(v);
        }
    }
    public void setOnclickListener(View.OnClickListener listener)
    {
        this.listener = listener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        ConstraintLayout item;
        TextView tipoDeOrdenVenta,fechaVenta,facturaVenta,facturaInformacionVenta,precioDeEnvioVenta,clienteMesaVenta,numeroDePlatos,empleadoVenta,totalVenta;

        public ViewHolder(@NonNull View itemView)
        {
            super(itemView);
            tipoDeOrdenVenta=(TextView) itemView.findViewById(R.id.tipoDeOrdenVenta);
            fechaVenta=(TextView) itemView.findViewById(R.id.fechaVenta);
            facturaVenta=(TextView) itemView.findViewById(R.id.facturaVenta);
            facturaInformacionVenta=(TextView) itemView.findViewById(R.id.facturaInformacionVenta);
            precioDeEnvioVenta=(TextView) itemView.findViewById (R.id.precioDeEnvioVenta);
            clienteMesaVenta=(TextView) itemView.findViewById(R.id.clienteMesaVenta);
            numeroDePlatos=(TextView) itemView.findViewById(R.id.numeroDePlatos);
            empleadoVenta=(TextView) itemView.findViewById(R.id.empleadoVenta);
            totalVenta=(TextView) itemView.findViewById(R.id.totalVenta);
            //item=(ConstraintLayout) itemView.findViewById(R.id.constrain_domicilio);

        }
    }



    public ArrayList<VentaOrden> getList()
    {
        return this.list;
    }


}