package com.example.tokisaki.sakiku;

/**
 * Created by Tokisaki on 18/05/2018.
 */


import java.util.ArrayList;

/***
 * Clase contenedora de los datos que se guardaran y cargaran en el juego
 */
public class Info {

    /**
     * booleana que indica si la vibracion esta activa
     */
    static boolean vibrar;
    /**
     * booleana que indica si la música esta activa
     */
    static boolean musica;
    /**
     * booleana que indica si los efectos de sonido estan activos
     */
    static boolean efectos;
    /**
     * String que indica el personaje elegido
     */
    static String personaje;
    /**
     * String que la ip del servidor
     */
    static String ipServidor;
    /**
     * lista que va a contener las 3 mejores puntuaciones dadas en el juego
     */
    static ArrayList<Integer> records = new ArrayList<>();

}
