package com.example.myapplication.interfaz.plato;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.myapplication.Abtract.InterfazFragamen;
import com.example.myapplication.AgregarMaterialesFragment;
import com.example.myapplication.R;
import com.example.myapplication.adaptador.AdaptadorListaAgregarMaterial;
import com.example.myapplication.adaptador.AdaptadorListaMateriales;
import com.example.myapplication.adaptador.AdaptadorListaProductos;
import com.example.myapplication.adaptador.Servidor;
import com.example.myapplication.adaptador.VolleySingleton;
import com.example.myapplication.interfaz.MainActivity;
import com.example.myapplication.interfaz.pedido.PedidosFragment;
import com.example.myapplication.mundo.Material;
import com.example.myapplication.mundo.Plato;
import com.example.myapplication.mundo.Producto;
import com.example.myapplication.mundo.VentaProducto;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DetallePlatoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DetallePlatoFragment extends Fragment
{
    private final String CARPETA_RAIZ = "misImagenesPrueba/";
    private final String RUTA_IMAGEN = CARPETA_RAIZ + "misFotos";
    private final int COD_SELECCIONA = 10;
    private final int COD_FOTO = 20;
    private RequestQueue requestQueue;
    private JsonRequest jsonRequest;
    private ImageView imagenPlato;
    private EditText nombrePlato;
    private EditText precioPlato;
    private Spinner categoriaPalto;
    private Spinner estadoPlato;
    private EditText descripcionPlato;
    private Button buscarImage;
    private Button modificarUnPlato, agregarMateriales;
    private Bitmap bitmap;
    private String path;
    private Uri miPath;
    private String selectedImagePath = null;
    private Activity actividad;
    private InterfazFragamen interfazFragamen;
    private Plato platos = null;
    private String rutaImg;
    private RecyclerView rcvMaterialesAgregar;
    private RecyclerView rcvMateriales;
    private AdaptadorListaAgregarMaterial adaptadorListaProductos;
    private AdaptadorListaMateriales adaptadorListaMateriales;
    private ArrayList<Producto> listProductos;
    private ArrayList<Producto> listProductosAux;
    private ArrayList<Material> listMaterialesProductos;
    private ImageButton btnAgregarPM;
    public DetallePlatoFragment()
    {
        // Required empty public constructor
    }

    public static DetallePlatoFragment newInstance(String param1, String param2)
    {
        DetallePlatoFragment fragment = new DetallePlatoFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

   @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState)
    {
        View view= inflater.inflate(R.layout.fragment_detalle_plato, container, false);
        this.requestQueue = VolleySingleton.getInstance(getContext()).getRequestQueue();
        this.nombrePlato= view.findViewById(R.id.txtModnombrePlato);
        this.precioPlato = (EditText) view.findViewById(R.id.txtModPrecionPlato);
        this.descripcionPlato = (EditText) view.findViewById(R.id.txtModDescripcion);
        this.categoriaPalto = (Spinner) view.findViewById(R.id.spnModCategoria);
        this.estadoPlato = (Spinner) view.findViewById(R.id.spnestado);
        this.buscarImage =(Button) view.findViewById(R.id.btnModBuscarImagen);
        this.modificarUnPlato =(Button) view.findViewById(R.id.btnModificar);
        this.imagenPlato =(ImageView) view.findViewById(R.id.imgModPlato);
        this.agregarMateriales=(Button) view.findViewById(R.id.btnAgregarMateriales);
        this.listProductos = new ArrayList<> ();
        this.listProductosAux=new ArrayList<> ();
        this.listMaterialesProductos=new ArrayList<> ();
      //  this.adaptadorListaProductos = new AdaptadorListaAgregarMaterial (getContext(),this.listProductos,this);
        this.adaptadorListaMateriales = new AdaptadorListaMateriales (getContext(),this.listMaterialesProductos,this);

        rcvMateriales=view.findViewById(R.id.rcvMateriales);
        rcvMateriales.setLayoutManager (new LinearLayoutManager (getContext ()));
        rcvMateriales.setAdapter (adaptadorListaMateriales);

        this.agregarMateriales.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                interfazFragamen.enviarPlatoDos(platos);


            }
        });

        this.buscarImage.setOnClickListener (new View.OnClickListener ()
        {
            @Override
            public void onClick(View v)
            {
                final CharSequence[] opciones={"Tomar Foto","Cargar Imagen","Cancelar"};
                final AlertDialog.Builder alertOpciones=new AlertDialog.Builder(getContext ());
                alertOpciones.setTitle("Seleccione una Opción");
                alertOpciones.setItems(opciones, new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i)
                    {
                        if (opciones[i].equals("Tomar Foto"))
                        {
                            tomarFotografia();

                        }else  if (opciones[i].equals("Cargar Imagen"))
                        {
                            cargarImagen();
                        }else
                        {
                            dialogInterface.dismiss();
                        }
                    }
                });
                alertOpciones.show ();
            }
        });
        this.modificarUnPlato.setOnClickListener (new View.OnClickListener ()
        {
            @Override
            public void onClick(final View v)
            {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                LayoutInflater inflater = getLayoutInflater();
                View view = inflater.inflate(R.layout.dialog_eliminar_mesa, null);
                builder.setView(view);
                final AlertDialog dialog = builder.create();
                dialog.show();

                final TextView input = view.findViewById(R.id.txtEliminarMesa);
                Button botonSi = view.findViewById(R.id.btnSiEliminar);
                Button botonNo = view.findViewById(R.id.btnEliminarNo);
                input.setText("¿Desea modificar el plato : '"+platos.getNombre ()+"'?");
                builder.setView(input);

                botonSi.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View view)
                    {
                        modificarPlato(v);
                        dialog.cancel();
                    }
                });

                botonNo.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View view)
                    {
                        dialog.cancel();
                    }
                });
            }
        });
        if(validaPermisos())
        {
            buscarImage.setEnabled(true);
        }else
        {
            buscarImage.setEnabled(false);
        }
        Bundle objetoPlato = getArguments();

        if(objetoPlato !=null)
        {
            final ProgressDialog loading = ProgressDialog.show(getContext (),"Cargando plato...","Espere por favor...",false,false);

            this.platos = (Plato) objetoPlato.getSerializable ("plato");
            this.nombrePlato.setText (platos.getNombre ());
            this.precioPlato.setText (platos.getPrecio () + "");
            this.descripcionPlato.setText (platos.getDescripcion());

            this.categoriaPalto.setSelection (platos.getPosicionCategoria ());

            this.estadoPlato.setSelection (platos.getPosicionEstado ());

            this.rutaImg=platos.getImage ();
            if (!this.rutaImg.isEmpty ())
            {
                Glide.get(getContext ()).clearMemory();
                Glide.with (getContext ())
                        .asBitmap ()
                        .load (platos.getImage ())
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .skipMemoryCache(true)
                        .fitCenter()
                        .into (new SimpleTarget<Bitmap> () {
                            @Override
                            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                bitmap = resource;
                                imagenPlato.setImageBitmap (bitmap);
                                loading.dismiss ();
                            }
                        });
            }
        }
        conusltarMateriales();
       return view;
    }

    public void cargarImagen()
    {
        Intent intent=new Intent(Intent.ACTION_GET_CONTENT, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.createChooser(intent,"Seleccione la Aplicación");
        intent.setType("image/*");
        startActivityForResult(intent,COD_SELECCIONA);
    }

    public void conusltarMateriales()
    {
        HashMap<String, String> params = new HashMap<String, String> ();
        params.put ("consultarMaterialesPlato", "true");

        params.put ("idplato", platos.getIdplato ()+"");
        JSONObject parameters = new JSONObject (params);
        String url = Servidor.HOST +"/consultas/inventario.php";

        jsonRequest = new JsonObjectRequest (Request.Method.POST, url, parameters, new Response.Listener<JSONObject> ()
        {
            @Override
            public void onResponse(JSONObject response)
            {
                listMaterialesProductos.clear ();
                try
                {
                    Boolean respuesta = response.getBoolean ("respuesta");

                    if (respuesta.booleanValue ())
                    {
                        JSONArray datos = response.getJSONArray ("datos");
                        for (int i = 0; i < datos.length(); i++)
                        {
                            JSONObject material = datos.getJSONObject(i);

                            int idMaterial= material.getInt ("idmateriales");
                            int idProductos= material.getInt ("idproductos");
                            int cantidad= material.getInt ("cantidad");
                            String categoria= material.getString ("categoria");
                            String nombre= material.getString ("nombre");
                            String estado= material.getString ("estado");

                           Material m= new  Material( idMaterial, idProductos, nombre,   cantidad, categoria,estado );

                            listMaterialesProductos.add (m);
                        }
                    }else
                    {
                        String error = response.getString ("error");
                        Toast.makeText(getContext(), respuesta.booleanValue ()+" Error: "+error,Toast.LENGTH_SHORT).show();
                    }
                    adaptadorListaMateriales.notifyDataSetChanged ();
                } catch (JSONException e) {
                    e.printStackTrace ();
                }
            }
        }, new Response.ErrorListener () {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace ();
            }
        });
        requestQueue.add (jsonRequest);
        //adaptadorListaProductos.notifyDataSetChanged ();
    }
    public void modificarPlato(final View v)
    {
        try
        {
            String archivoImg="";
            String nombre=this.nombrePlato.getText().toString ();
            double precio= Double.parseDouble(precioPlato.getText().toString());
            Object select = this.categoriaPalto.getSelectedItem ();

            Object selectEstado = this.estadoPlato.getSelectedItem ();
            String descripcion = this.descripcionPlato.getText ().toString ();
            if (this.platos.equals (new Plato( select.toString(),  nombre,  descripcion,  precio,this.rutaImg,selectEstado.toString ())))
            {
                Toast.makeText(getContext(), "No se detectaron cambios",Toast.LENGTH_SHORT).show();
            }else if (bitmap==null)
            {
                Toast.makeText(getContext(), "Seleccione una imagen valida",Toast.LENGTH_SHORT).show();
            }else if (!(select instanceof  Object))
            {
                Toast.makeText(getContext(), "Seleccione una categoria valida",Toast.LENGTH_SHORT).show();
            }else if (nombre.isEmpty ())
            {
                Toast.makeText(getContext(), "Escriba un nombre de plato valida",Toast.LENGTH_SHORT).show();
            }else if (precio<0)
            {
                Toast.makeText(getContext(), "Escriba un precio de plato valida ",Toast.LENGTH_SHORT).show();
            }else if (descripcion.isEmpty ())
            {
                Toast.makeText(getContext(), "Escriba una descripcon del plato valida",Toast.LENGTH_SHORT).show();
            }else if (!(selectEstado instanceof  Object))
            {
                Toast.makeText(getContext(), "Seleccione una estado valida",Toast.LENGTH_SHORT).show();
            }else
            {
                final ProgressDialog loading = ProgressDialog.show(getContext (),"Actualizando cambios...","Espere por favor...",false,false);

                 String URL= Servidor.HOST +"/consultas/platos.php";

                RequestQueue servicio= Volley.newRequestQueue(getContext());
                Map<String,String> params= new HashMap<String, String> ();

                params.put("idplato",this.platos.getIdplato ()+"");

                params.put("imagenRuta",this.platos.getImage ());
                params.put("categoria",select.toString());
                params.put("nombre",nombre);
                params.put("precio",precio+"");
                params.put("descripcion",descripcion);
                params.put("estado",selectEstado.toString ());
                params.put("modificarPlato","true");
                archivoImg=convertirImageToString (bitmap);
                params.put("imagen",archivoImg);
                JSONObject parameters = new JSONObject(params);

                JsonRequest jsonRequest=new JsonObjectRequest (Request.Method.POST, URL, parameters, new Response.Listener<JSONObject> ()
                {
                    @Override
                    public void onResponse(JSONObject response)
                    {
                        loading.dismiss();
                        if (response!=null)
                        {
                            try
                            {
                                Boolean respuesta = response.getBoolean ("respuesta");

                                if (respuesta.booleanValue ()) {
                                    Toast.makeText (getContext (), "Plato modificado con exito", Toast.LENGTH_SHORT).show ();
                                    limpiar ();
                                    FragmentManager manager = getActivity ().getSupportFragmentManager ();
                                    FragmentTransaction trans = manager.beginTransaction ();
                                    trans.commit();
                                    manager.popBackStack();
                                }else
                                {
                                    String error = response.getString ("error");
                                    Toast.makeText(getContext(), respuesta.booleanValue ()+" Error: "+error,Toast.LENGTH_SHORT).show();
                                }
                            }catch (JSONException e)
                            {
                                e.printStackTrace ();
                            }
                        }
                    }
                }, new Response.ErrorListener ()
                {
                    @Override
                    public void onErrorResponse(VolleyError error){error.printStackTrace (); loading.dismiss(); }
                });
                servicio.add(jsonRequest);
            }
        }catch (Exception e)
        {
            Toast.makeText(getContext(), "Escriba un precio de plato valida",Toast.LENGTH_SHORT).show();
        }
    }

    private String convertirImageToString(Bitmap bitmap)
    {
        ByteArrayOutputStream array=new ByteArrayOutputStream();
        bitmap.compress (Bitmap.CompressFormat.WEBP, 10, array);
        byte[] imagenByte=array.toByteArray ();
        String imagenString= Base64.encodeToString (imagenByte, Base64.DEFAULT);
        return imagenString;
    }

    private void tomarFotografia()
    {
        File fileImagen=new File(Environment.getExternalStorageDirectory(),RUTA_IMAGEN);
        boolean isCreada=fileImagen.exists();
        String nombreImagen="";
        if(isCreada==false)
        {
            isCreada=fileImagen.mkdirs();
        }

        if(isCreada==true)
        {
            nombreImagen=(System.currentTimeMillis()/1000)+".jpg";
        }

        path=Environment.getExternalStorageDirectory()+
                File.separator+RUTA_IMAGEN+File.separator+nombreImagen;

        File imagen=new File(path);

        Intent intent=null;
        intent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        ////
        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.N)
        {
            String authorities=getContext().getPackageName()+".provider";
            Uri imageUri= FileProvider.getUriForFile(getContext (),authorities,imagen);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        }else
        {
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(imagen));
        }
        startActivityForResult(intent,COD_FOTO);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == MainActivity.RESULT_OK)
        {
            switch (requestCode)
            {
                case COD_SELECCIONA:
                    miPath=data.getData();
                    imagenPlato.setImageURI (miPath);
                    try
                    {
                        bitmap=MediaStore.Images.Media.getBitmap (getContext ().getContentResolver (),miPath);
                        imagenPlato.setImageBitmap(bitmap);
                        rutaImg="Se cambio";
                    } catch (IOException e)
                    {
                        e.printStackTrace ();
                    }
                    break;
                case COD_FOTO:
                    MediaScannerConnection.scanFile(getContext (), new String[]{path}, null, new MediaScannerConnection.OnScanCompletedListener()
                    {
                        @Override
                        public void onScanCompleted(String path, Uri uri)
                        {
                            Log.i("Ruta de almacenamiento","Path: "+path);
                        }
                    });
                    rutaImg="Se cambio";
                    bitmap= BitmapFactory.decodeFile(path);
                    imagenPlato.setImageBitmap(bitmap);
                    break;
            }
        }

    }
    public String getPath(Uri uri)
    {
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = getActivity().getContentResolver().query(uri, projection, null, null, null);
        if (cursor == null) return null;
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String s=cursor.getString(column_index);
        cursor.close();
        return s;
    }

    private boolean validaPermisos()
    {
        if(Build.VERSION.SDK_INT<Build.VERSION_CODES.M)
        {
            return true;
        }
        if((getContext().checkSelfPermission(CAMERA)== PackageManager.PERMISSION_GRANTED)
                &&(getContext().checkSelfPermission(WRITE_EXTERNAL_STORAGE)==PackageManager.PERMISSION_GRANTED))
        {
            return true;
        }

        if((shouldShowRequestPermissionRationale(CAMERA))
                || (shouldShowRequestPermissionRationale(WRITE_EXTERNAL_STORAGE)))
        {
            cargarDialogoRecomendacion();
        }else
        {
            requestPermissions(new String[]{WRITE_EXTERNAL_STORAGE,CAMERA},100);
        }

        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode==100)
        {
            if(grantResults.length==2
                    && grantResults[0]==PackageManager.PERMISSION_GRANTED
                    && grantResults[1]==PackageManager.PERMISSION_GRANTED)
            {
                buscarImage.setEnabled(true);
            }else
            {
                solicitarPermisosManual();
            }
        }

    }

    private void solicitarPermisosManual()
    {
        final CharSequence[] opciones={"si","no"};
        final AlertDialog.Builder alertOpciones=new AlertDialog.Builder(getContext ());
        alertOpciones.setTitle("¿Desea configurar los permisos de forma manual?");
        alertOpciones.setItems(opciones, new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialogInterface, int i)
            {
                if (opciones[i].equals("si"))
                {
                    Intent intent=new Intent();
                    intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    Uri uri=Uri.fromParts("package",getContext().getPackageName(),null);
                    intent.setData(uri);
                    startActivity(intent);
                }else
                {
                    Toast.makeText(getContext().getApplicationContext(),"Los permisos no fueron aceptados",Toast.LENGTH_SHORT).show();
                    dialogInterface.dismiss();
                }
            }
        });

        alertOpciones.show();
    }
    @Override
    public void onAttach(Context context)
    {
        super.onAttach(context);
        if (context instanceof Activity)
        {
            this.actividad = (Activity) context;
            this.interfazFragamen = (InterfazFragamen) this.actividad;
        }
    }

    @Override
    public void onDetach()
    {
        super.onDetach();
    }

    private void cargarDialogoRecomendacion()
    {
        AlertDialog.Builder dialogo=new AlertDialog.Builder(getContext ());
        dialogo.setTitle("Permisos Desactivados");
        dialogo.setMessage("Debe aceptar los permisos para el correcto funcionamiento de la App");

        dialogo.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i)
            {
                requestPermissions(new String[]{WRITE_EXTERNAL_STORAGE,CAMERA},100);
            }
        });
        dialogo.show();
    }

    public void limpiar()
    {
        nombrePlato.setText("");
        precioPlato.setText("");
    }


    public void iniciarModificarProducto(Material material, View v) {

        interfazFragamen.enviarMateriales (material);

    }
}