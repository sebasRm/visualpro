package com.example.myapplication.mundo;

public class CompraProducto
{
    private String idfacturas;
    private String fecha;
    private String codigoProducto;
    private String descripcion;
    private int cantidad;

    public CompraProducto(String idfacturas, String fecha, String codigoProducto, String descripcion, int cantidad) {
        this.idfacturas = idfacturas;
        this.fecha = fecha;
        this.codigoProducto = codigoProducto;
        this.descripcion = descripcion;
        this.cantidad = cantidad;
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

    public String getDescripcion() {
        return descripcion;
    }

    public int getCantidad() {
        return cantidad;
    }
}
