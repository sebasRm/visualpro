package com.example.myapplication.adaptador;

import android.content.ClipData;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.interfaz.pedido.PedidosMesaFragment;
import com.example.myapplication.mundo.Mesa;
import com.example.myapplication.mundo.OrdenPedido;

import java.util.ArrayList;

public class AdaptadorListaMesa extends  RecyclerView.Adapter<AdaptadorListaMesa.ViewHolder> implements View.OnClickListener
{
    private final PedidosMesaFragment fragmentEvet;
    private Mesa proyecto;
    private ArrayList<OrdenPedido> list;
    private PedidosMesaFragment buscarMesa;
    private Context contexto;
    private static LayoutInflater  inflater = null;
    private View.OnClickListener listener;

    public AdaptadorListaMesa(Context conexto, ArrayList<OrdenPedido> lista, PedidosMesaFragment fragmentEvet)
    {
        this.list=lista;
        this.contexto = conexto;
        this.fragmentEvet=fragmentEvet;
        inflater = (LayoutInflater ) conexto.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = inflater.inflate(R.layout.fragment_lista_pedidos_mesa, null);
        view.setOnClickListener(this);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position)
    {
        proyecto = this.list.get(position).getMesa ();

        holder.txtNumeroMesa.setText(String.valueOf(proyecto.getNumero()));
        holder.item.setTag (position);
        //holder.item.setOnDragListener (fragmentEvet);
        holder.item.setOnLongClickListener (new View.OnLongClickListener ()
        {


            @Override
            public boolean onLongClick(View view)
            {
                fragmentEvet.onStop();
                ClipData data = ClipData.newPlainText("", "");
                View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(view);
                view.startDrag(data, shadowBuilder, view, 0);
                view.setVisibility(View.INVISIBLE);
                return true;
            }
        });
    }

    @Override
    public int getItemCount()
    {
        return list.size();
    }

    public void setFragment(PedidosMesaFragment buscarMesa)
    {
        this.buscarMesa=buscarMesa;
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

    public ArrayList<OrdenPedido> getList()
    {
        return this.list;
    }

    public class ViewHolder  extends RecyclerView.ViewHolder
    {
        ConstraintLayout item;
        TextView txtNumeroMesa;

        public ViewHolder (@NonNull View itemView)
        {
            super(itemView);
            txtNumeroMesa=(TextView) itemView.findViewById(R.id.txtNombrePlato);
            item=(ConstraintLayout) itemView.findViewById (R.id.itemMesaOcupada);
        }
    }


}
