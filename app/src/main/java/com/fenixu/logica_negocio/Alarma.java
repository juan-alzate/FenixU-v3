package com.fenixu.logica_negocio;

import java.util.Calendar;

public class Alarma {

    private int id;
    private String titulo;
    private String fecha;
    private String hora;

    public Alarma() {
        this.id = -1;
        this.titulo = "AlarmaDefault";
        this.fecha = ""+Calendar.getInstance().get(Calendar.DAY_OF_MONTH)+"/"+
                        Calendar.getInstance().get(Calendar.MONTH)+"/"+
                        Calendar.getInstance().get(Calendar.YEAR);
        this.hora = ""+Calendar.getInstance().get(Calendar.HOUR_OF_DAY)+":"+
                        Calendar.getInstance().get(Calendar.MINUTE);
    }

    public Alarma(String titulo, String fecha, String hora) {
        this.titulo = titulo;
        this.fecha = fecha;
        this.hora = hora;
    }

    public Alarma(int id, String titulo, String fecha, String hora) {
        this.id = id;
        this.titulo = titulo;
        this.fecha = fecha;
        this.hora = hora;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public String toString(){
        return "idAlarma: "+ Integer.toString(this.id)+" tituloAlarma: "+ this.titulo+
                " fechaAlarma: "+ this.fecha+ " horaAlarma: "+this.hora;
    }
}
