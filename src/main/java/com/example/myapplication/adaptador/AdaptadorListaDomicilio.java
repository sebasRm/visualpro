package com.example.myapplication.adaptador;

import android.content.ClipData;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.interfaz.domicilio.PedidosDomicilioFragment;
import com.example.myapplication.interfaz.plato.MenuPlatosFragment;
import com.example.myapplication.mundo.OrdenDomicilio;

import java.util.ArrayList;

public class AdaptadorListaDomicilio extends  RecyclerView.Adapter<AdaptadorListaDomicilio.ViewHolder>  implements View.OnClickListener
{
    private final PedidosDomicilioFragment pdf;
    private ArrayList<OrdenDomicilio> list;
    private MenuPlatosFragment buscarPlato;
    private Context contexto;
    private static LayoutInflater  inflater = null;
    private View.OnClickListener listener;

    public AdaptadorListaDomicilio(Context contexto, ArrayList<OrdenDomicilio> lista, PedidosDomicilioFragment pdf)
    {
        this.list=lista;
        this.contexto = contexto;
        this.pdf=pdf;
        inflater = LayoutInflater.from(contexto);
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = inflater.inflate(R.layout.fragment_lista_domicilios, null);
        view.setOnClickListener(this);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        holder.lblCidentificacion.setText(list.get(position).getCliente().getIndentificacion());
        holder.lblCNombreCliente.setText(list.get(position).getCliente ().getNombres ()+"");
        holder.lblCApellidoCliente.setText(list.get(position).getCliente ().getApellidos ()+"");
        holder.lblCDireccionCliente.setText(list.get(position).getDireccion ()+"");
        holder.lblCTelefonoCliente.setText(list.get(position).getFactura_precio_envio()+"");
        holder.lblReferenciaCliente.setText(list.get(position).getReferencia ()+"");

        holder.item.setTag (position);
        holder.item.setOnLongClickListener (new View.OnLongClickListener ()
        {
            @Override
            public boolean onLongClick(View view)
            {
                ClipData data = ClipData.newPlainText("", "");
                View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(view);
                view.startDrag(data, shadowBuilder, view, 0);
                view.setVisibility(View.INVISIBLE);
                return true;
            }
        });
        long l = System.currentTimeMillis ();
        long l1 = list.get (position).getFactura_fecha ().getTime ();
        long min=((l-l1)/1000)/60;
        long seg=((l-l1)/1000)%60;
        holder.lblTiempo.setText (min+":"+seg);

        holder.btnEliminarDomicilio.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick(View v)
            {
                pdf.eliminarPedido(list.get(position).getFactura_idfacturas (),list.get(position).getCliente (),position);

            }
        });

        holder.btnModificarDomicilio.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick(View v)
            {
                pdf.modificarDomicilio(list.get (position));
            }
        });

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
        TextView lblCidentificacion,lblCNombreCliente,lblCApellidoCliente,lblCDireccionCliente,lblCTelefonoCliente,lblReferenciaCliente,lblTiempo;
        ImageButton btnEliminarDomicilio,btnModificarDomicilio;

        ImageView imgPlatos;
        public ViewHolder(@NonNull View itemView)
        {
            super(itemView);
            lblCidentificacion=(TextView) itemView.findViewById(R.id.lblCidentificacion);
            lblCNombreCliente=(TextView) itemView.findViewById(R.id.lblCNombreCliente);
            lblCApellidoCliente=(TextView) itemView.findViewById(R.id.lblCApellidoCliente);
            lblCDireccionCliente=(TextView) itemView.findViewById(R.id.lblCDireccionCliente);
            lblCTelefonoCliente=(TextView) itemView.findViewById (R.id.lblCTelefonoCliente);
            lblReferenciaCliente=(TextView) itemView.findViewById(R.id.lblReferenciaCliente);
            lblTiempo=(TextView) itemView.findViewById(R.id.lblTiempo);
            btnEliminarDomicilio=(ImageButton) itemView.findViewById(R.id.btnEliminarPedidoCliente);
            btnModificarDomicilio=(ImageButton) itemView.findViewById(R.id.btnDomicilioModificar);
            item=(ConstraintLayout) itemView.findViewById(R.id.constrain_domicilio);

        }
    }



    public ArrayList<OrdenDomicilio> getList()
    {
        return this.list;
    }


}