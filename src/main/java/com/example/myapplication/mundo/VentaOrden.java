package com.example.myapplication.mundo;

import org.json.JSONObject;

public class VentaOrden
{
    private String tipo;
    private String fecha;
    private String idfacturas;
    private String informacion;
    private double precio_envio;
    private String cliente_mesa;
    private int cantidad_total;
    private String usuarios_nombres;
    private double total;
    private int cantidad;



    public VentaOrden(String tipo, String fecha, String idfacturas, String informacion, double precio_envio, String cliente_mesa, int cantidad_total, String usuarios_nombres, double total) {
        this.tipo = tipo;
        this.fecha = fecha;
        this.idfacturas = idfacturas;
        this.informacion = informacion;
        this.precio_envio = precio_envio;
        this.cliente_mesa = cliente_mesa;
        this.cantidad_total = cantidad_total;
        this.usuarios_nombres = usuarios_nombres;
        this.total = total;
    }

    public VentaOrden(String tipo, String fecha, String idfacturas, String informacion, double precio_envio, String cliente_mesa, int cantidad_total, String usuarios_nombres, double total, int cantidad) {
        this.tipo = tipo;
        this.fecha = fecha;
        this.idfacturas = idfacturas;
        this.informacion = informacion;
        this.precio_envio = precio_envio;
        this.cliente_mesa = cliente_mesa;
        this.cantidad_total = cantidad_total;
        this.usuarios_nombres = usuarios_nombres;
        this.total = total;
        this.cantidad = cantidad;
    }

    public int getCantidad() {
        return cantidad;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getIdfacturas() {
        return "Factura No. "+this.idfacturas;
    }

    public void setIdfacturas(String idfacturas) {
        this.idfacturas = idfacturas;
    }

    public String getInformacion() {
        return informacion;
    }

    public void setInformacion(String informacion) {
        this.informacion = informacion;
    }

    public double getPrecio_envio() {
        return precio_envio;
    }

    public void setPrecio_envio(double precio_envio) {
        this.precio_envio = precio_envio;
    }

    public String getCliente_mesa() {
        return cliente_mesa;
    }

    public void setCliente_mesa(String cliente_mesa) {
        this.cliente_mesa = cliente_mesa;
    }

    public int getCantidad_total() {
        return cantidad_total;
    }

    public void setCantidad_total(int cantidad_total) {
        this.cantidad_total = cantidad_total;
    }

    public String getUsuarios_nombres() {
        return usuarios_nombres;
    }

    public void setUsuarios_nombres(String usuarios_nombres) {
        this.usuarios_nombres = usuarios_nombres;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public double getTotalVenta()
    {
        return this.cantidad+this.getTotal ();
    }
}
