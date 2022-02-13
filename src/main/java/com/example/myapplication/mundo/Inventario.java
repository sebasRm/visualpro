package com.example.myapplication.mundo;

public class Inventario
{
    private int idproductos;
    private String nombre;
    private int stock_minimo;
    private int  entrada_total;
    private int  salida_total;
    private int  stock;


    public Inventario(int idproductos, String nombre, int stock_minimo, int entrada_total, int salida_total, int stock) {
        this.idproductos = idproductos;
        this.nombre = nombre;
        this.stock_minimo = stock_minimo;
        this.entrada_total = entrada_total;
        this.salida_total = salida_total;
        this.stock = stock;
    }

    public int getIdproductos() {
        return idproductos;
    }

    public void setIdproductos(int idproductos) {
        this.idproductos = idproductos;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getStock_minimo() {
        return stock_minimo;
    }

    public void setStock_minimo(int stock_minimo) {
        this.stock_minimo = stock_minimo;
    }

    public int getEntrada_total() {
        return entrada_total;
    }

    public void setEntrada_total(int entrada_total) {
        this.entrada_total = entrada_total;
    }

    public int getSalida_total() {
        return salida_total;
    }

    public void setSalida_total(int salida_total) {
        this.salida_total = salida_total;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }
}
