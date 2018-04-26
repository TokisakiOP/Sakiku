package com.example.tokisaki.sakiku.Modelos;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import com.example.tokisaki.sakiku.R;

/**
 * Created by Tokisaki on 15/04/2018.
 */

public class Obstaculos {

    /**
     * Contexto de la aplicacion
     */
    private Context context;

    /**
     * Posición del obstaculo
     */
    protected PointF posicion;

    /**
     * Rectangulo del colisión
     */
    private RectF rectangulo;

    /**
     * imagen del obstaculo bola de fuego
     */
    private Bitmap bolaFuego;

    /**
     * imagen del obstaculo bloque de hielo
     */
    private Bitmap hielo;

    /**
     * Lista con los frames del obstaculo
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
     * ancho del frame a recortar
     */
    private int anchoFrame;

    /**
     * alto del frame a recortar
     */
    private int altoFrame;

    /**
     * cambia la columna del recorte del frame
     */
    private int cambioH = 0;

    /**
     * cambia la columna del recorte del frame
     */
    private int cambioV = 0;

    /**
     * numero del frame actual
     */
    protected int numFrame;

    /**
     * alto del rectangulo de colision
     */
    private int alto;

    /**
     * ancho del rectangulo de colision
     */
    private int ancho;

    /**
     * velocidad de movimiento del obstaculo
     */
    private int velocidad = 8;

    /**
     * indica si el obstaculo es bola de fuego o bloque de hielo
     */
    private boolean bola;

    /**
     * Pincel para dibujador de colliders
     */
    Paint p;

    /**
     * Imagen actual del objeto
     */
    Bitmap frameActual;

    /***
     * Reescala una imagen
     * @param bitmapAux imagen a reescalar
     * @param nuevoAlto nuevo alto de la imagen despues del rescalado
     * @return la imagen reescalada
     */
    public Bitmap escalaAltura(Bitmap bitmapAux, int nuevoAlto) {
        if (nuevoAlto == bitmapAux.getHeight()) return bitmapAux;
        return bitmapAux.createScaledBitmap(bitmapAux, (bitmapAux.getWidth() * nuevoAlto) / bitmapAux.getHeight(),
                nuevoAlto, true);
    }

    /***
     * Transforma pixeles en dps
     * @param dp dps a transformar
     * @return dp transformados
     */
    int getPixels(float dp) {
        DisplayMetrics metrics = new DisplayMetrics();
        ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().
                getMetrics(metrics);
        return (int) (dp * metrics.density);
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

    /***
     * Constructor de la clase
     * @param context cotexto de la aplicación
     * @param posicion posicion de la bala
     */
    public Obstaculos(Context context, PointF posicion, boolean bola) {
        this.context = context;
        this.posicion = posicion;
        this.bola = bola;
        if (bola) {
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
            frameActual = movimientoObstaculo[0];
            this.posicion.y -= frameActual.getHeight() / 2;
        } else {
            hielo = BitmapFactory.decodeResource(context.getResources(), R.drawable.box);
            hielo = escalaAltura(hielo, getPixels(40));
        }
        p = new Paint();
        p.setColor(Color.GREEN);
        p.setStyle(Paint.Style.STROKE);
        p.setStrokeWidth(5);
    }

    private void animar(Bitmap[] animacion) {
        numFrame++;
        if (numFrame >= animacion.length) numFrame = 0;
        frameActual = animacion[numFrame];
    }

    /***
     * Actualizamos la física de los elementos en pantalla
     */
    public boolean actualizarFisica(int anchoPantalla) {
        if (bola) {
            animar(movimientoObstaculo);
        }
        moverObstaculo();
        setRectangulos();
        if (bola) {
            if (posicion.x < 0 - frameActual.getWidth()) {
                return true;
            }
            return false;
        } else {
            if (posicion.x < 0 - hielo.getWidth()) {
                return true;
            }
            return false;
        }
    }

    /***
     * funcion que actualiza el rectangulo de colision
     */
    private void setRectangulos() {
        rectangulo = new RectF(posicion.x, posicion.y, posicion.x + frameActual.getWidth(), posicion.y + frameActual.getHeight());
    }

    /**
     * Comprueba si esta colisionando con el personaje recibido como parametro
     *
     * @param personaje Personaje recibido
     * @return Devuelve true si colisiona , de otra manera false
     */
    public boolean detectarColision(Personaje personaje) {
        for (RectF collider : personaje.getRectangulos()) {
            if (rectangulo.contains(collider) || rectangulo.intersect(collider)) {
                return true;
            }
        }
        return false;
    }

    /***
     * función que mueve el obstaculo
     */
    private void moverObstaculo() {
        posicion.x -= velocidad;
    }

    /***
     * Dibujamos los elementos en pantalla
     * @param canvas Lienzo sobre el que dibujar
     */
    public void dibujar(Canvas canvas) {
        canvas.drawBitmap(frameActual, posicion.x, posicion.y, null);
        canvas.drawRect(rectangulo, p);
    }

    /***
     * Devuelve la posición del rectangulo de colisión de la flecha
     * @return posición del rectangulo
     */
    protected RectF getRectangulo() {
        return rectangulo;
    }
}
