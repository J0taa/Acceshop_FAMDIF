package com.example.famdif_final;

public class Tienda {
    private String id;
    private String nombre;
    private String tipo;
    private String subtipo;
    private String direccion;
    private String clasificacion;
    private String latitud;
    private String longitud;
    private String creacion;
    private String acceso;
    private String puertaAcceso;

    public Tienda(){}

    public Tienda(String id, String nombre, String tipo, String subtipo, String direccion, String clasificacion, String latitud, String longitud, String creacion, String acceso, String puertaAcceso) {
        this.id = id;
        this.nombre = nombre;
        this.tipo = tipo;
        this.subtipo = subtipo;
        this.direccion = direccion;
        this.clasificacion = clasificacion;
        this.latitud = latitud;
        this.longitud = longitud;
        this.creacion = creacion;
        this.acceso = acceso;
        this.puertaAcceso = puertaAcceso;
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

    public String getCreacion() {
        return creacion;
    }


    public String getAcceso() {
        return acceso;
    }

    public String getPuertaAcceso() {
        return puertaAcceso;
    }

    public String getId() {
        return id;
    }
}


