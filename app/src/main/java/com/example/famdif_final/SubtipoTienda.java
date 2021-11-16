package com.example.famdif_final;

import java.util.ArrayList;
import java.util.List;

public class SubtipoTienda {
    private ArrayList<String> subtipoTienda= new ArrayList<>();

    public SubtipoTienda(String tipo){
        switch (tipo){
            case "Alimentación":
                subtipoTienda.add("Carnicería");
                subtipoTienda.add("Charcutería");
                subtipoTienda.add("Comidas preparadas");
                subtipoTienda.add("Carnicería");
                subtipoTienda.add("Frutería");
                subtipoTienda.add("Panadería");
                subtipoTienda.add("Quesería");
                subtipoTienda.add("Suministros de café");
                subtipoTienda.add("Suministros de té");
                subtipoTienda.add("Supermercado");
                subtipoTienda.add("Tienda de bebidas");
                subtipoTienda.add("Tienda de golosinas");
                break;

            case "Restauración":
                subtipoTienda.add("Bar");
                subtipoTienda.add("Cafetería");
                subtipoTienda.add("Confitería");
                subtipoTienda.add("Heladería");
                subtipoTienda.add("Pizzería");
                subtipoTienda.add("Restaurante");
                break;

            case "Cualquiera":
                break;
        }

    }

    public List<String> getSubTiposTienda() {
        return subtipoTienda;
    }

}
