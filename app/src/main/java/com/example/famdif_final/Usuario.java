package com.example.famdif_final;

public class Usuario {
    private String nombre;
    private String email;
    private String admin;

    public Usuario(String nombre, String email, String admin){
        this.admin=admin;
        this.email=email;
        this.nombre=nombre;
    }

    public Usuario(){}

    public String getEmail() {
        return email;
    }

    public String getAdmin() {
        return admin;
    }

    public String getNombre() {
        return nombre;
    }

    public void setAdmin(String admin) {
        this.admin = admin;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
