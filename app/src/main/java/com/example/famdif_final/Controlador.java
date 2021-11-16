package com.example.famdif_final;

import android.location.Location;

import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

public class Controlador {
    private static Controlador instance;
    private List<Tienda> tiendas = new ArrayList<>();
    private FirebaseUser currentUser;
    //private List<Tienda> tiendas;
    private Tienda selectedShop;
    private Location currentPosition;



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

    void setCurrentUser(FirebaseUser user){
        this.currentUser=user;
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


    /*public List<ItemMyRating> getLastRatings() {
        return lastRatings;
    }

    public void setLastRatings(List<ItemMyRating> lastRatings) {
        this.lastRatings = lastRatings;
    }

    public List<ItemFilterUserResponse> getItemFilterUserResponses() {
        return itemFilterUserResponses;
    }

    public void setItemFilterUserResponses(List<ItemFilterUserResponse> itemFilterUserResponses) {
        this.itemFilterUserResponses = itemFilterUserResponses;
    }

    public List<Suggestion> getSuggestions() {
        return suggestions;
    }

    public void setSuggestions(List<Suggestion> suggestions) {
        this.suggestions = suggestions;
    }
    */
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
}
