package com.mcode.tempsigna;

/**
 * Created by MH on 06/10/2015.
 */
public class Centro {

    protected String nombreCentro;
    protected String temperatura_s1;
    protected int control;

    public String getTemperatura_s2() {
        return temperatura_s2;
    }

    public void setTemperatura_s2(String temperatura_s2) {
        this.temperatura_s2 = temperatura_s2;
    }

    protected String temperatura_s2;
    protected String humedadCentro;


    protected String fecha;
    protected long idCentro;

    public String getEnergia() {
        return energia;
    }

    public String getCompresor() {
        return compresor;
    }

    public String getBomba() {
        return bomba;
    }

    public void setEnergia(String energia) {
        this.energia = energia;
    }

    public void setCompresor(String compresor) {
        this.compresor = compresor;
    }

    public void setBomba(String bomba) {
        this.bomba = bomba;
    }

    protected String energia;
    protected String compresor;
    protected String bomba;


    public Centro(String nombreCentro, String temperatura_s1, String temperatura_s2, String humedadCentro, String fecha,
                  int control, String energia
            ,String bomba, String compresor) {
        this.nombreCentro = nombreCentro;
        this.temperatura_s1 = temperatura_s1;
        this.temperatura_s2 = temperatura_s2;
        this.humedadCentro = humedadCentro;
        this.fecha = fecha;
        this.control = control;
        this.energia=energia;
        this.compresor=compresor;
        this.bomba=bomba;
        //this.idCentro = idCentro;

    }

    public String getNombreCentro() {
        return nombreCentro;
    }

    public int getControl() {
        return control;
    }

    public void setNombreCentro(String nombreCentro) {
        this.nombreCentro = nombreCentro;
    }

    public String getHumedadCentro() {
        return humedadCentro;
    }

    public void setHumedadCentro(String humedadCentro) {
        this.humedadCentro = humedadCentro;
    }

    public String getTemperatura_s1() {
        return temperatura_s1;
    }

    public void setTemperatura_s1(String temperatura_s1) {
        this.temperatura_s1 = temperatura_s1;
    }

    public void setIdCentro(long idCentro) {
        this.idCentro = idCentro;
    }

    public long getIdCentro() {
        return idCentro;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }
}
