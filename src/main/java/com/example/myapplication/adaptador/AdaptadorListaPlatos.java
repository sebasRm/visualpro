package com.example.myapplication.adaptador;

import android.content.ClipData;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.myapplication.R;
import com.example.myapplication.interfaz.plato.MenuPlatosFragment;
import com.example.myapplication.mundo.Plato;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class AdaptadorListaPlatos extends  RecyclerView.Adapter<AdaptadorListaPlatos.ViewHolder>
{

    private Plato plato;
    private ArrayList<Plato> list;
    private MenuPlatosFragment buscarPlato;
    private Context contexto;
    private static LayoutInflater  inflater = null;

    public AdaptadorListaPlatos(Context contexto, ArrayList<Plato> lista)
    {
        this.list=lista;
        this.contexto = contexto;
        inflater =LayoutInflater.from(contexto);
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {

        View view = inflater.inflate(R.layout.fragment_lista_menu_platos, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position)
    {
        NumberFormat nf = NumberFormat.getCurrencyInstance(Locale.getDefault());
        plato = this.list.get(position);
        holder.txtNombrePlato.setText(list.get(position).getNombre ());
        holder.txtPrecioPlato.setText(String.valueOf(nf.format (list.get(position).getPrecio())));
        String image = list.get (position).getImage ();
        if (!image.isEmpty ())
        {
            Glide.get(contexto).clearMemory();
            Glide.with (contexto)
                    .load (image )
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .fitCenter()
                    .into (holder.imgPlatos);
        }
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
    }
    public ArrayList<Plato> getList()
    {
        return this.list;
    }
    @Override
    public int getItemCount() {
            return list.size();
    }
    public void setFragment(MenuPlatosFragment buscarPlato)
    {
        this.buscarPlato=buscarPlato;
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        ConstraintLayout item;
        TextView txtNombrePlato,txtPrecioPlato,txtCategoriaPlato,txtDescripcionPlato;
        ImageView imgPlatos;
        public ViewHolder(@NonNull View itemView)
        {
            super(itemView);
            txtNombrePlato=(TextView) itemView.findViewById(R.id.txtNombrePlatoMenu);
            txtPrecioPlato=(TextView) itemView.findViewById(R.id.txtPrecioMenu);

            //txtDescripcionPlato=(TextView) itemView.findViewById(R.id.txtPrecioMenu);
            imgPlatos=(ImageView) itemView.findViewById(R.id.imgPlatosMenu);
            item=(ConstraintLayout) itemView.findViewById (R.id.itemPlato);
        }
    }



}
