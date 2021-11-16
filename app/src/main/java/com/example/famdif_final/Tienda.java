package com.example.famdif_final;

import java.util.Date;

public class Tienda {
    private String id;
    private String nombre;
    private String tipo;
    private String subtipo;
    private String direccion;
    private String clasificacion;
    private String latitud;
    private String longitud;
    private Date fechaRegistro;
    private Date fechaModificacion;

    public Tienda(){}

    public Tienda(String id, String nombre, String tipo, String subtipo, String direccion, String clasificacion, String latitud, String longitud, Date fechaRegistro, Date fechaModificacion) {
        this.id = id;
        this.nombre = nombre;
        this.tipo = tipo;
        this.subtipo = subtipo;
        this.direccion = direccion;
        this.clasificacion = clasificacion;
        this.latitud = latitud;
        this.longitud = longitud;
        this.fechaRegistro = fechaRegistro;
        this.fechaModificacion = fechaModificacion;
    }

    public String getNombre() {
        return nombre;
    }

    public String getTipo() {
        return tipo;
    }

    public String getSubtipo() {
        return subtipo;
    }

    public String getDireccion() {
        return direccion;
    }

    public String getClasificacion() { return clasificacion;}

    public String getLatitud() {
        return latitud;
    }

    public String getLongitud() {
        return longitud;
    }

    public Date getFechaRegistro() {
        return fechaRegistro;
    }

    public Date getFechaModificacion() {
        return fechaModificacion;
    }

    public String getId() {
        return id;
    }
}


