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
import com.example.myapplication.interfaz.inventario.InventarioProductosFragment;
import com.example.myapplication.interfaz.plato.MenuPlatosFragment;
import com.example.myapplication.mundo.VentaProducto;

import java.util.ArrayList;

public class AdaptadorVerVentas extends  RecyclerView.Adapter<AdaptadorVerVentas.ViewHolder>
{

    private final InventarioProductosFragment ventana;
    private ArrayList<VentaProducto> list;
    private MenuPlatosFragment buscarPlato;
    private Context contexto;
    private static LayoutInflater inflater = null;
    private View.OnClickListener listener;

    public AdaptadorVerVentas(Context contexto, ArrayList<VentaProducto> lista, InventarioProductosFragment inventarioProductosFragment)
    {
        this.list=lista;
        this.contexto = contexto;
        inflater = LayoutInflater.from(contexto);
        this.ventana=inventarioProductosFragment;
    }


    @NonNull
    @Override
    public AdaptadorVerVentas.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = inflater.inflate(R.layout.fragment_lista_ventas_productos, null);
        return new AdaptadorVerVentas.ViewHolder (view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdaptadorVerVentas.ViewHolder holder, final int position) {
        holder.lblFacturaVenta.setText ("Factura No."+this.list.get (position).getIdfacturas ());
        holder.lblFechaVenta.setText (this.list.get (position).getFecha ());

        holder.lblTipoVenta.setText (this.list.get (position).getTipo ()+"");
        holder.lblPlatoVenta.setText (this.list.get (position).getPlato ()+"");
        holder.lblCodigoVenta.setText (this.list.get (position).getCodigoProducto ()+"");
        holder.lblCantidadVenta.setText (this.list.get (position).getCantidad ()+" ");
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        ConstraintLayout item;
        TextView      lblFacturaVenta, lblFechaVenta,lblCodigoVenta,  lblTipoVenta, lblPlatoVenta,  lblCantidadVenta;
        public ViewHolder(@NonNull View itemView)
        {
            super(itemView);
            lblFacturaVenta=(TextView) itemView.findViewById(R.id.lblFacturaVenta);
            lblFechaVenta=(TextView) itemView.findViewById(R.id.lblFechaVenta);
            lblCodigoVenta=(TextView) itemView.findViewById(R.id.lblCodigoVenta);
            lblTipoVenta=(TextView) itemView.findViewById(R.id.lblTipoVenta);
            lblPlatoVenta=(TextView) itemView.findViewById(R.id.lblPlatoVenta);
            lblCantidadVenta=(TextView) itemView.findViewById(R.id.lblCantidadVenta);
        }
    }



    public ArrayList<VentaProducto> getList()
    {
        return this.list;
    }


}