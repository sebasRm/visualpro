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
public class Cliente
{

    private int idClientes;
    private String indentificacion;
    private String nombres;
    private String apellidos;
    private String estado;

    public Cliente(int idclientes, String indentificacion, String nombres, String apellidos, String estado)
    {
        this.idClientes = idclientes;
        this.indentificacion = indentificacion;
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.estado=estado;
    }

    public Cliente(String indentificacion, String nombres, String apellidos)
    {
        this.indentificacion = indentificacion;
        this.nombres = nombres;
        this.apellidos = apellidos;
    }

    public int getIdclientes()
    {
        return idClientes;
    }

    public void setIdclientes(int idclientes)
    {
        this.idClientes = idclientes;
    }

    public String getIndentificacion()
    {
        return indentificacion;
    }

    public void setIndentificacion(String indentificacion)
    {
        this.indentificacion = indentificacion;
    }

    public String getNombres()
    {
        return nombres;
    }

    public void setNombres(String nombres)
    {
        this.nombres = nombres;
    }

    public String getApellidos()
    {
        return apellidos;
    }

    public void setApellidos(String apellidos)
    {
        this.apellidos = apellidos;
    }

    public int getIdClientes() {
        return idClientes;
    }

    public void setIdClientes(int idClientes) {
        this.idClientes = idClientes;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    @Override
    public String toString()
    {
        return this.nombres; //To change body of generated methods, choose Tools | Templates.
    }
    
}
