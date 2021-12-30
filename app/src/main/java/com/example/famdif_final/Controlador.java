package com.example.famdif_final;

import android.location.Location;

import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

public class Controlador {
    private static Controlador instance;
    private List<Tienda> tiendas = new ArrayList<>();
    private List<Usuario> usuarios =new ArrayList<>();
    private List<UsuarioFiltrado> usuariosBorrar = new ArrayList<>();
    private FirebaseUser currentUser;
    //private List<Tienda> tiendas;
    private Tienda selectedShop;
    private Location currentPosition;
    private String usuario;
    private String usuarioBorrar;
    private String sugerenciasUsuario;
    private String nombreUsuarioActual;
    private String nombreTiendaValorar="";
    private String direccionTiendaValorar="";
    private int sugerenciasTotal;


    private double latitud;
    private double longitud;
    //private List<Suggestion> suggestions;
    //private List<ItemMyRating> lastRatings;
    //private List<ItemFilterUserResponse> itemFilterUserResponses;
    private Tienda lastShop;
    private String lastId, lastName, lastAddress;
    private int admin=0;

    private Controlador() {
    }

    public static Controlador getInstance() {
        if (instance == null) {
            instance = new Controlador();
        }
        return instance;
    }

    public static void setInstance(Controlador instance) {
        Controlador.instance = instance;
    }

    public double getLatitud() {
        return latitud;
    }

    public double getLongitud() {
        return longitud;
    }

    FirebaseUser getCurrentUser(){
        return currentUser;
    }

    public void setCurrentUser(FirebaseUser user){
        this.currentUser=user;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getUsuario() {
        return usuario;
    }

    public int getSugerenciasTotal() {
        return sugerenciasTotal;
    }

    public void setSugerenciasTotal(int sugerenciasTotal) {
        this.sugerenciasTotal = sugerenciasTotal;
    }

    /*public User getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }

    public boolean isCurrentUserAdmin() {
        if (currentUser == null || !currentUser.isAdmin()) {
            return false;
        } else {
            return true;
        }
    }*/

    public List<Tienda> getShops() {
        return tiendas;
    }

    public void setShops(List<Tienda> tiendas) {
        this.tiendas = tiendas;
    }

    public Tienda getSelectedShop() {
        return selectedShop;
    }

    public void setSelectedShop(Tienda selectedShop) {
        this.selectedShop = selectedShop;
    }

    public Location getCurrentPosition() {
        return currentPosition;
    }

    public void setCurrentPosition(Location currentPosition) {
        this.currentPosition = currentPosition;
    }

    public List<Usuario> getUsuarios() {
        return usuarios;
    }

    public void setUsuarios(List<Usuario> usuarios) {
        this.usuarios = usuarios;
    }

    public String getUsuarioBorrar() {
        return usuarioBorrar;
    }

    public void setUsuarioBorrar(String usuarioBorrar) {
        this.usuarioBorrar = usuarioBorrar;
    }


    public Tienda getLastShop() {
        return lastShop;
    }

    public void setLastShop(Tienda tienda) {
        this.lastShop = tienda;
    }


    public String getLastId() {
        return lastId;
    }

    public void setLastId(String lastId) {
        this.lastId = lastId;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getLastAddress() {
        return lastAddress;
    }

    public void setLastAddress(String lastAddress) {
        this.lastAddress = lastAddress;
    }

    public int getAdmin(){
        return admin;
    }

    public void setAdmin(int adm){
        admin=adm;
    }

    public void setLatitud(double latitud) {
        this.latitud = latitud;
    }

    public void setLongitud(double latitud) {
        this.latitud = latitud;
    }

    public List<UsuarioFiltrado> getUsuariosBorrar() {
        return usuariosBorrar;
    }

    public void setUsuariosBorrar(List<UsuarioFiltrado> usuariosBorrar) {
        this.usuariosBorrar = usuariosBorrar;
    }


    public String getSugerenciasUsuario() {
        return sugerenciasUsuario;
    }

    public void setSugerenciasUsuario(String sugerenciasUsuario) {
        this.sugerenciasUsuario = sugerenciasUsuario;
    }

    public void setNombreUsuarioActual(String nombreUsuarioActual) {
        this.nombreUsuarioActual = nombreUsuarioActual;
    }

    public String getNombreUsuarioActual() {
        return nombreUsuarioActual;
    }

    public String getNombreTiendaValorar() {
        return nombreTiendaValorar;
    }

    public void setNombreTiendaValorar(String nombreTiendaValorar) {
        this.nombreTiendaValorar = nombreTiendaValorar;
    }

    public String getDireccionTiendaValorar() {
        return direccionTiendaValorar;
    }

    public void setDireccionTiendaValorar(String direccionTiendaValorar) {
        this.direccionTiendaValorar = direccionTiendaValorar;
    }
}
