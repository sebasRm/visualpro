package com.example.myapplication.mundo;

public class VentaProducto
{
    private String idfacturas;
    private String fecha;
    private String codigoProducto;
    private String nombreProducto;
    private String tipo;
    private String plato;
    private int cantidadl;

    public VentaProducto(String idfacturas, String fecha, String codigoProducto, String nombreProducto, String tipo, String plato, int cantidadl) {
        this.idfacturas = idfacturas;
        this.fecha = fecha;
        this.codigoProducto = codigoProducto;
        this.nombreProducto = nombreProducto;
        this.tipo = tipo;
        this.plato = plato;
        this.cantidadl = cantidadl;
    }

    public VentaProducto(String idfacturas, String fecha, String codigoProducto, String tipo, String plato, int cantidadl) {
        this.idfacturas = idfacturas;
        this.fecha = fecha;
        this.codigoProducto = codigoProducto;
        this.tipo = tipo;
        this.plato = plato;
        this.cantidadl = cantidadl;
    }

    public String getIdfacturas() {
        return idfacturas;
    }

    public String getFecha() {
        return fecha;
    }

    public String getCodigoProducto() {
        return codigoProducto;
    }

    public String getTipo() {
        return tipo;
    }

    public String getPlato() {
        return plato;
    }

    public int getCantidad() {
        return cantidadl;
    }

    public String getNombreProducto() {
        return nombreProducto;
    }


}
