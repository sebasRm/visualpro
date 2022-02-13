/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.myapplication.mundo;

/**
 *
 * @author Crendon
 */
public class Producto
{

    protected int idProductos;
    protected String nombre;
    protected String codigo;
    protected int cantidad;
    protected String categoria;
    protected boolean estado;

    public Producto(int idProductos, String nombre, String codigo)
    {
        this.idProductos = idProductos;
        this.nombre = nombre;
        this.codigo = codigo;
    }

    public Producto(int idProductos, String nombre, String codigo, int cantidad, String categoria, boolean estado) {
        this.idProductos = idProductos;
        this.nombre = nombre;
        this.codigo = codigo;
        this.cantidad = cantidad;
        this.categoria = categoria;
        this.estado = estado;
    }

    public Producto(String nombre, String codigo)
    {
        this.nombre = nombre;
        this.codigo = codigo;
    }

    public int getIdproductos()
    {
        return idProductos;
    }

    public void setIdproductos(int idproductos)
    {
        this.idProductos = idproductos;
    }

    public String getNombre()
    {
        return nombre;
    }

    public void setNombre(String nombre)
    {
        this.nombre = nombre;
    }

    public String getCodigo()
    {
        return codigo;
    }

    public void setCodigo(String codigo)
    {
        this.codigo = codigo;
    }

    public int getIdProductos() {
        return idProductos;
    }

    public void setIdProductos(int idProductos) {
        this.idProductos = idProductos;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public boolean isEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }

    @Override
    public String toString()
    {
        return super.toString(); //To change body of generated methods, choose Tools | Templates.
    }

}
