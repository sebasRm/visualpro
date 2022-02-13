package com.example.myapplication.adaptador;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.AgregarMaterialesFragment;
import com.example.myapplication.R;
import com.example.myapplication.interfaz.inventario.InventarioProductosFragment;
import com.example.myapplication.interfaz.plato.DetallePlatoFragment;
import com.example.myapplication.interfaz.plato.MenuPlatosFragment;
import com.example.myapplication.mundo.Producto;

import java.util.ArrayList;

import static android.content.Context.INPUT_METHOD_SERVICE;

public class AdaptadorListaAgregarMaterial extends  RecyclerView.Adapter<AdaptadorListaAgregarMaterial.ViewHolder> {

    private final AgregarMaterialesFragment ventana;
    private ArrayList<Producto> list;
    private MenuPlatosFragment buscarPlato;
    private Context contexto;
    private static LayoutInflater inflater = null;
    private View.OnClickListener listener;

    public AdaptadorListaAgregarMaterial(Context contexto, ArrayList<Producto> lista, AgregarMaterialesFragment inventarioProductosFragment) {
        this.list = lista;
        this.contexto = contexto;
        inflater = LayoutInflater.from(contexto);
        this.ventana = inventarioProductosFragment;
    }


    @NonNull
    @Override
    public AdaptadorListaAgregarMaterial.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.fragment_lista_agregar_materiales, null);
        return new AdaptadorListaAgregarMaterial.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final AdaptadorListaAgregarMaterial.ViewHolder holder, final int position) {
        holder.txtCodigoAgregar.setText(this.list.get(position).getIdproductos ()+"");
        holder.txtProductoAgregar.setText(this.list.get(position).getNombre());
        holder.edtCantidadAgregar.requestFocus(); //Asegurar que editText tiene focus
        InputMethodManager imm = (InputMethodManager) contexto.getSystemService(INPUT_METHOD_SERVICE);
        imm.showSoftInput( holder.edtCantidadAgregar, InputMethodManager.SHOW_FORCED);
        holder.edtCantidadAgregar.addTextChangedListener(new TextWatcher () {
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

        holder.checkBoxSelecciono.setOnCheckedChangeListener (new CompoundButton.OnCheckedChangeListener () {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                list.get(position).setEstado (isChecked);
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
        Spinner spinnerTipoAgregar;
        CheckBox checkBoxSelecciono;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtCodigoAgregar = (TextView) itemView.findViewById(R.id.txtCodigoAgregar);
            txtProductoAgregar = (TextView) itemView.findViewById(R.id.txtProductoAgregar);
            edtCantidadAgregar = (EditText) itemView.findViewById(R.id.edtCantidadAgregar);




            spinnerTipoAgregar = (Spinner) itemView.findViewById(R.id.spinnerTipoAgregar);
            checkBoxSelecciono = (CheckBox) itemView.findViewById(R.id.checkBoxSelecciono);
        }
    }


    public ArrayList<Producto> getList() {
        return this.list;
    }
}
