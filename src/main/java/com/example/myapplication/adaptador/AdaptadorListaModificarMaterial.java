package com.example.myapplication.adaptador;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.AgregarMaterialesFragment;
import com.example.myapplication.DetallesMaterialesFragment;
import com.example.myapplication.R;
import com.example.myapplication.interfaz.plato.MenuPlatosFragment;
import com.example.myapplication.mundo.Material;
import com.example.myapplication.mundo.Producto;

import java.util.ArrayList;

import static android.content.Context.INPUT_METHOD_SERVICE;

public class AdaptadorListaModificarMaterial extends  RecyclerView.Adapter<AdaptadorListaModificarMaterial.ViewHolder> {

    private final DetallesMaterialesFragment ventana;
    private ArrayList<Material> list;
    private MenuPlatosFragment buscarPlato;
    private Context contexto;
    private static LayoutInflater inflater = null;
    private View.OnClickListener listener;

    public AdaptadorListaModificarMaterial(Context contexto, ArrayList<Material> lista, DetallesMaterialesFragment inventarioProductosFragment) {
        this.list = lista;
        this.contexto = contexto;
        inflater = LayoutInflater.from(contexto);
        this.ventana = inventarioProductosFragment;
    }


    @NonNull
    @Override
    public AdaptadorListaModificarMaterial.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.fragment_lista_modificar_materiales, null);
        return new AdaptadorListaModificarMaterial.ViewHolder(view);
    }


    public void onBindViewHolder(@NonNull final AdaptadorListaModificarMaterial.ViewHolder holder, final int position) {
        holder.txtCodigoAgregar.setText(this.list.get(position).getIdproductos ()+"");
        holder.txtProductoAgregar.setText(this.list.get(position).getNombre());
        holder.edtCantidadAgregar.requestFocus(); //Asegurar que editText tiene focus
        InputMethodManager imm = (InputMethodManager) contexto.getSystemService(INPUT_METHOD_SERVICE);
        imm.showSoftInput( holder.edtCantidadAgregar, InputMethodManager.SHOW_FORCED);

        holder.edtCantidadAgregar.setText (this.list.get(position).getCantidad ()+"");
        holder.spinnerTipoAgregar.setSelection (this.list.get(position).getPosicionCategoria ());
        holder.estadoMaterial.setSelection (this.list.get(position).getPosicionEstado ());
        holder.edtCantidadAgregar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                try{
                    int cantidad =Integer.parseInt (s.toString ().isEmpty ()?"0":s.toString ());
                    list.get(position).setCantidad (cantidad);

                }catch (Exception e)
                {

                }
            }

        });


        holder.spinnerTipoAgregar.setOnItemSelectedListener (new AdapterView.OnItemSelectedListener () {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                String categoria = (String) holder.spinnerTipoAgregar.getItemAtPosition(pos);
                list.get(position).setCategoria (categoria);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



        holder.estadoMaterial.setOnItemSelectedListener (new AdapterView.OnItemSelectedListener () {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                String estado = (String) holder.estadoMaterial.getItemAtPosition(pos);
                list.get(position).setEst (estado);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

     holder.modificar.setOnClickListener (new View.OnClickListener () {
         @Override
         public void onClick(View v)
         {
            ventana.modificarMaterial (list.get(position));
         }
     });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ConstraintLayout item;

        TextView txtCodigoAgregar, txtProductoAgregar;
        EditText edtCantidadAgregar;
        Spinner spinnerTipoAgregar,estadoMaterial;
        ImageButton modificar;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtCodigoAgregar = (TextView) itemView.findViewById(R.id.txtCodigoModificar);
            txtProductoAgregar = (TextView) itemView.findViewById(R.id.txtProductoModificar);
            edtCantidadAgregar = (EditText) itemView.findViewById(R.id.edtCantidadModificar);
            spinnerTipoAgregar = (Spinner) itemView.findViewById(R.id.spinnerTipoModificar);
            estadoMaterial = (Spinner) itemView.findViewById(R.id.estadoMaterial);
            modificar= (ImageButton) itemView.findViewById(R.id.btnModificarMaterial);
        }
    }


    public ArrayList<Material> getList() {
        return this.list;
    }
}
