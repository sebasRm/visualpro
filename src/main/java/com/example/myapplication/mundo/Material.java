package com.example.myapplication.mundo;

import java.io.Serializable;

public class Material extends  Producto implements Serializable
{
    private  int idMateriales;
    private String est;

    public Material(int idMateriales,int idProductos, String nombre,  int cantidad, String categoria, String estado) {
        super (idProductos, nombre, "", cantidad, categoria, false);
        this.idMateriales=idMateriales;
        this.est=estado;
    }

    public int getPosicionEstado()
    {
        if (this.est.equals ("Activo"))
        {
            return 0;
        }else {
            return 1;
        }
    }
    public int getPosicionCategoria()
    {
        if(this.categoria.equals("Pedido"))
        {
            return 0;
        }else if(this.categoria.equals("Domicilio")){
            return 1;
        }else {
            return 2;
        }
    }
    public int getIdMateriales() {
        return idMateriales;
    }

    public void setIdMateriales(int idMateriales) {
        this.idMateriales = idMateriales;
    }

    public String getEst() {
        return est;
    }

    public void setEst(String est) {
        this.est = est;
    }
}
