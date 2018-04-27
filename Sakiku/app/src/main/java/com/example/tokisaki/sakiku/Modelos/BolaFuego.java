package com.example.tokisaki.sakiku.Modelos;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.PointF;
import android.util.Log;

import com.example.tokisaki.sakiku.R;

public class BolaFuego extends Obstaculo {

    /**
     * numero del frame actual
     */
    protected int numFrame;

    /**
     * imagen del obstaculo bola de fuego
     */
    private Bitmap bolaFuego;

    /**
     * Lista con los frames del obstaculo
     */
    protected Bitmap[] aBolaFuego;

    /**
     * numero de frames horizontales a recortar del movimiento del obstaculo
     */
    private int numImagenesH_obs = 2;

    /**
     * numero de frames verticales a recortar del movimiento del obstaculo
     */
    private int numImagenesV_obs = 3;

    /**
     * número de frames del recorrido del obstaculo
     */
    private int numImagenes_obs = 5;

    /**
     * cambia la columna del recorte del frame
     */
    private int cambioH = 0;

    /**
     * cambia la columna del recorte del frame
     */
    private int cambioV = 0;

    /**
     * ancho del frame a recortar
     */
    private int anchoFrame;

    /**
     * alto del frame a recortar
     */
    private int altoFrame;

    /**
     * Inicializa una instancia de la clase segun parametros recibidos
     *
     * @param context  Contexto de la aplicacion
     * @param posicion Posicion original
     */
    public BolaFuego(Context context, PointF posicion) {
        super(context, posicion);
        generarAnimacion();
        setRectangulos();
    }

    @Override
    public boolean actualizarFisica() {
        animar(aBolaFuego);
        return super.actualizarFisica();
    }

    /**
     * Dada una animacion recibida como parametro ,
     * coge uno de los frames de la misma como frame actual
     *
     * @param animacion Animacion recibida
     */
    private void animar(Bitmap[] animacion) {
        numFrame++;
        if (numFrame >= animacion.length) numFrame = 0;
        frameActual = animacion[numFrame];
    }

    @Override
    public boolean detectarColision(Personaje personaje) {
        return super.detectarColision(personaje);
    }

    /**
     * Genera la animacion del obstaculo
     */
    private void generarAnimacion() {
        aBolaFuego = new Bitmap[numImagenes_obs];
        bolaFuego = BitmapFactory.decodeResource(context.getResources(), R.drawable.fuego);
        anchoFrame = bolaFuego.getWidth() / numImagenesH_obs;
        altoFrame = bolaFuego.getHeight() / numImagenesV_obs;
        for (int i = 0; i < numImagenes_obs; i++) {
            Bitmap frame = Bitmap.createBitmap(bolaFuego, cambioH * anchoFrame, cambioV * altoFrame, anchoFrame, altoFrame);
            frame = escalaAltura(frame, getPixels(40));
            aBolaFuego[i] = espejo(frame, true);
            cambioH++;
            if (i == 1 || i == 3) {
                cambioH = 0;
                cambioV++;
            }
        }
        bolaFuego = null;
        frameActual = aBolaFuego[0];
    }

    /***
     * Función que aplica visión de espejo a una imagen
     * @param imagen imagen a aplicar la visión
     * @param horizontal Booleano que indica si se plicara vertical o horizontalmente
     * @return imagen transformada
     */
    public Bitmap espejo(Bitmap imagen, Boolean horizontal) {
        Matrix matrix = new Matrix();
        if (horizontal) matrix.preScale(-1, 1);
        else matrix.preScale(1, -1);
        return Bitmap.createBitmap(imagen, 0, 0, imagen.getWidth(),
                imagen.getHeight(), matrix, false);
    }
}
