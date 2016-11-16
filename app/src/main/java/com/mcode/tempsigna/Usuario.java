package com.mcode.tempsigna;

/**
 * Created by Systema on 22/07/2016.
 */
public class Usuario {
    private int id;
    private String nusuario;
    private int pass;
    private String fecha;


    public Usuario(int id, String nusuario, int pass, String fecha) {
        this.id = id;
        this.nusuario = nusuario;
        this.pass = pass;
        this.fecha = fecha;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNusuario() {
        return nusuario;
    }

    public void setNusuario(String nusuario) {
        this.nusuario = nusuario;
    }

    public int getPass() {
        return pass;
    }

    public void setPass(int pass) {
        this.pass = pass;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }
}
