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
import com.example.myapplication.mundo.CompraProducto;
import com.example.myapplication.mundo.VentaProducto;

import java.util.ArrayList;

public class AdaptadorVerCompras extends  RecyclerView.Adapter<AdaptadorVerCompras.ViewHolder>
{

    private final InventarioProductosFragment ventana;
    private ArrayList<CompraProducto> list;
    private MenuPlatosFragment buscarPlato;
    private Context contexto;
    private static LayoutInflater inflater = null;
    private View.OnClickListener listener;

    public AdaptadorVerCompras(Context contexto, ArrayList<CompraProducto> lista, InventarioProductosFragment inventarioProductosFragment)
    {
        this.list=lista;
        this.contexto = contexto;
        inflater = LayoutInflater.from(contexto);
        this.ventana=inventarioProductosFragment;
    }


    @NonNull
    @Override
    public AdaptadorVerCompras.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = inflater.inflate(R.layout.fragment_lista_ver_compras, null);
        return new AdaptadorVerCompras.ViewHolder (view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdaptadorVerCompras.ViewHolder holder, final int position) {
        holder.lblFacturaCompra.setText ("Factura No."+this.list.get (position).getIdfacturas ());
        holder.lblFechaCompra.setText (this.list.get (position).getFecha ());

        holder.lblCodigoCompra.setText (this.list.get (position).getCodigoProducto ()+"");
        holder.lblDescripcionCompra.setText (this.list.get (position).getDescripcion ());
        holder.lblCantidadCompra.setText (this.list.get (position).getCantidad ()+"");
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        ConstraintLayout item;
        TextView lblFacturaCompra,lblFechaCompra,lblCodigoCompra, lblDescripcionCompra,lblCantidadCompra;
        public ViewHolder(@NonNull View itemView)
        {
            super(itemView);
            lblFacturaCompra=(TextView) itemView.findViewById(R.id.lblFacturaCompra);
            lblFechaCompra=(TextView) itemView.findViewById(R.id.lblFechaCompra);
            lblCodigoCompra=(TextView) itemView.findViewById(R.id.lblCodigoCompra);
            lblDescripcionCompra=(TextView) itemView.findViewById(R.id.lblDescripcionCompra);
            lblCantidadCompra=(TextView) itemView.findViewById(R.id.lblCantidadCompra);
        }
    }



    public ArrayList<CompraProducto> getList()
    {
        return this.list;
    }


}