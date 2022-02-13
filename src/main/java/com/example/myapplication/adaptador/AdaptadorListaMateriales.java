package com.example.myapplication.adaptador;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.interfaz.plato.DetallePlatoFragment;
import com.example.myapplication.interfaz.plato.MenuPlatosFragment;
import com.example.myapplication.mundo.Material;

import java.util.ArrayList;

public class AdaptadorListaMateriales extends  RecyclerView.Adapter<AdaptadorListaMateriales.ViewHolder> {

    private final DetallePlatoFragment ventana;
    private ArrayList<Material> list;
    private MenuPlatosFragment buscarPlato;
    private Context contexto;
    private static LayoutInflater inflater = null;
    private View.OnClickListener listener;

    public AdaptadorListaMateriales(Context contexto, ArrayList<Material> lista, DetallePlatoFragment inventarioProductosFragment) {
        this.list = lista;
        this.contexto = contexto;
        inflater = LayoutInflater.from(contexto);
        this.ventana = inventarioProductosFragment;
    }


    @NonNull
    @Override
    public AdaptadorListaMateriales.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.fragment_lista_materiales, null);
        return new AdaptadorListaMateriales.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdaptadorListaMateriales.ViewHolder holder, final int position) {
        holder.txtCodigoMaterial.setText(this.list.get(position).getIdMateriales()+"");
        holder.txtProductoMaterial.setText(this.list.get(position).getNombre());
        holder.txtCantidadMaterial.setText(this.list.get(position).getCantidad () + "");
        holder.txtCategoriaMaterial.setText(this.list.get(position).getCategoria () + "");
        holder.estado.setText (this.list.get(position).getEst ());
        holder.modificar.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                ventana.iniciarModificarProducto(list.get (position),v);
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ConstraintLayout item;
        TextView txtCodigoMaterial, txtProductoMaterial, txtCantidadMaterial, txtCategoriaMaterial,estado;
        ImageButton modificar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtCodigoMaterial = (TextView) itemView.findViewById(R.id.txtCodigoMaterial);
            txtProductoMaterial = (TextView) itemView.findViewById(R.id.txtProductoMaterial);
            txtCantidadMaterial = (TextView) itemView.findViewById(R.id.txtCantidadMaterial);
            txtCategoriaMaterial = (TextView) itemView.findViewById(R.id.txtCategoriaMaterial);
            estado= (TextView) itemView.findViewById(R.id.txtEstadoMaterial);
            modificar=(ImageButton) itemView.findViewById(R.id.botonEditarMterial);

        }
    }


    public ArrayList<Material> getList() {
        return this.list;
    }
}