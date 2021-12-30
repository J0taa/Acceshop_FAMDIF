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
                subtipoTienda.add("24h");
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

            case "Educacion y Auxiliares":
                subtipoTienda.add("Academia");
                subtipoTienda.add("Autoescuela");
                subtipoTienda.add("Centro de formación");
                subtipoTienda.add("Copistería");
                subtipoTienda.add("Librería");
                break;

            case "Salud":
                subtipoTienda.add("Clínica auditiva");
                subtipoTienda.add("Clínica dental");
                subtipoTienda.add("Farmacia");
                subtipoTienda.add("Oftalmólogo");
                subtipoTienda.add("Óptica");
                subtipoTienda.add("Parafarmacia");
                break;

            case "Estética y cuidado personal":
                subtipoTienda.add("Centro de estética");
                subtipoTienda.add("Entrenador personal");
                subtipoTienda.add("Gabinete de estética");
                subtipoTienda.add("Gimnasio");
                subtipoTienda.add("Herbolario");
                subtipoTienda.add("Ortopedia");
                subtipoTienda.add("Peluquería");
                subtipoTienda.add("Perfumería");
                subtipoTienda.add("Salón de belleza");
                subtipoTienda.add("Tienda de cosméticos");
                break;

            case "Textil y complementos":
                subtipoTienda.add("Accesotios de moda");
                subtipoTienda.add("Bisutería");
                subtipoTienda.add("Corsetería");
                subtipoTienda.add("Joyería");
                subtipoTienda.add("Mercería");
                subtipoTienda.add("Ropa y Calzado");
                subtipoTienda.add("Sombrerería");
                subtipoTienda.add("Textil");
                subtipoTienda.add("Tienda de bolsos y maletas");
                subtipoTienda.add("Tienda de complementos");
                subtipoTienda.add("Tienda de deportes");
                subtipoTienda.add("Tienda de ropa");
                subtipoTienda.add("Tienda de ropa de cama");
                subtipoTienda.add("Tienda de ropa de hombre");
                subtipoTienda.add("Tienda de ropa de mujer");
                subtipoTienda.add("Tienda de ropa infantil");
                subtipoTienda.add("Tienda de trajes de novia");
                subtipoTienda.add("Tintorería");
                subtipoTienda.add("Zapatería");
                subtipoTienda.add("Zapatería infantil");
                break;

            case "Ocio y entretenimiento":
                subtipoTienda.add("Agencia de viajes");
                subtipoTienda.add("Apuestas");
                subtipoTienda.add("Bazar");
                subtipoTienda.add("Bingo");
                subtipoTienda.add("Loterias y apuestas");
                subtipoTienda.add("Salón de juegos");
                subtipoTienda.add("Tienda de juguetes");
                subtipoTienda.add("Tienda de regalos");
                break;

            case "Bancos, seguros y gestoría":
                subtipoTienda.add("Aseguradora");
                subtipoTienda.add("Asesoría");
                subtipoTienda.add("Bancos");
                subtipoTienda.add("Correduría de seguros");
                break;

            case "Inmobiliario":
                subtipoTienda.add("Constructora");
                subtipoTienda.add("Inmobiliaria");
                subtipoTienda.add("Instalaciones y mantenimiento");
                break;

            case "Hogar y decoración":
                subtipoTienda.add("Artículos para el hogar");
                subtipoTienda.add("Colchonería");
                subtipoTienda.add("Cuchillería");
                subtipoTienda.add("Decoración");
                subtipoTienda.add("Electrodomésticos");
                subtipoTienda.add("Ferretería");
                subtipoTienda.add("Iluminación");
                subtipoTienda.add("Tienda de muebles");
                break;

            case "Tecnología":
                subtipoTienda.add("Fotografía");
                subtipoTienda.add("Proveedor de internet");
                subtipoTienda.add("Tienda de accesorios para móviles");
                subtipoTienda.add("Tienda de cartuchos para impresoras");
                subtipoTienda.add("Tienda de informática");
                subtipoTienda.add("Tienda de telefonía");
                break;

            case "Automoción":
                subtipoTienda.add("Cristales para automóviles");
                subtipoTienda.add("Lavadero");
                subtipoTienda.add("Taller");
                subtipoTienda.add("Tienda de recambios");
                break;

            case "Otros":
                subtipoTienda.add("ONG");
                subtipoTienda.add("Empresa de trabajo temporal");
                subtipoTienda.add("Estanco");
                subtipoTienda.add("Locutorio");
                subtipoTienda.add("Tienda de vaporizadores");
                break;

            case "Cualquiera":
                break;
        }

    }

    public List<String> getSubTiposTienda() {
        return subtipoTienda;
    }

}
