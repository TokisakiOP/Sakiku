package com.example.tokisaki.sakiku;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.PointF;

/**
 * Created by Tokisaki on 29/04/2018.
 */

public class Obstaculo1 extends Obstaculos {

    /**
     * numero del frame actual
     */
    protected int numFrame;
    /**
     * imagen del obstaculo bola de fuego
     */
    private Bitmap bolaFuego;
    /**
     * lista con los frames del obstaculo
     */
    protected Bitmap[] movimientoObstaculo;
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

    /**
     * Constructor de la clase
     * @param context  Contexto de la aplicacion
     * @param posicion Posicion original
     */
    public Obstaculo1(Context context, PointF posicion) {
        super(context, posicion);
        movimientoObstaculo = new Bitmap[numImagenes_obs];
        bolaFuego = BitmapFactory.decodeResource(context.getResources(), R.drawable.fuego);
        anchoFrame = bolaFuego.getWidth() / numImagenesH_obs;
        altoFrame = bolaFuego.getHeight() / numImagenesV_obs;
        for (int i = 0; i < numImagenes_obs; i++) {
            Bitmap frame = Bitmap.createBitmap(bolaFuego, cambioH * anchoFrame, cambioV * altoFrame, anchoFrame, altoFrame);
            frame = escalaAltura(frame, getPixels(40));
            movimientoObstaculo[i] = espejo(frame, true);
            cambioH++;
            if (i == 1 || i == 3) {
                cambioH = 0;
                cambioV++;
            }
        }
        bolaFuego = null;
        frame = movimientoObstaculo[0];
        setRectangulos();
    }

    @Override
    public boolean actualizarFisica() {
        numFrame++;
        if (numFrame >= movimientoObstaculo.length) numFrame = 0;
        frame = movimientoObstaculo[numFrame];
        return super.actualizarFisica();
    }

    @Override
    public boolean detectarColision(Personaje personaje) {
        return super.detectarColision(personaje);
    }
}
