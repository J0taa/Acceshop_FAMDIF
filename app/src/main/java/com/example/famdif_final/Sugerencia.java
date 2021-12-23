package com.example.famdif_final;

public class Sugerencia {

    private String autor;
    private String cuerpo;
    private String titulo;
    private String email;

    public Sugerencia(){

    }

    public Sugerencia(String autor, String cuerpo, String titulo, String email){
        this.autor=autor;
        this.cuerpo=cuerpo;
        this.titulo=titulo;
        this.email=email;
    }

    public String getAutor() {
        return autor;
    }

    public String getCuerpo() {
        return cuerpo;
    }

    public String getTitulo() { return titulo; }

    public String getEmail() { return email; }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public void setCuerpo(String cuerpo) {
        this.cuerpo = cuerpo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public void setEmail(String email) { this.email = email; }
}
