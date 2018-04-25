package com.example.tokisaki.sakiku;

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
import android.util.Log;
import android.view.WindowManager;

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
     * imagen del obstaculo
     */
    private Bitmap obstaculo;
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

    Paint p;

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
    public Obstaculos(Context context, PointF posicion) {
        this.context = context;
        this.posicion = posicion;
        movimientoObstaculo = new Bitmap[numImagenes_obs];
        obstaculo = BitmapFactory.decodeResource(context.getResources(), R.drawable.fuego);
        anchoFrame = obstaculo.getWidth() / numImagenesH_obs;
        altoFrame = obstaculo.getHeight() / numImagenesV_obs;
        for (int i = 0; i < numImagenes_obs; i++) {
            Bitmap frame = Bitmap.createBitmap(obstaculo, cambioH * anchoFrame, cambioV * altoFrame, anchoFrame, altoFrame);
            ;
            frame = escalaAltura(frame, getPixels(40));
            movimientoObstaculo[i] = frame;
            cambioH++;
            if (i == 1 || i == 3) {
                cambioH = 0;
                cambioV++;
            }
        }
        obstaculo = null;
        this.posicion.y -= movimientoObstaculo[0].getHeight() / 2;


        p = new Paint();
        p.setColor(Color.GREEN);
        p.setStyle(Paint.Style.STROKE);
        p.setStrokeWidth(5);
    }

    /***
     * Actualizamos la física de los elementos en pantalla
     */
    public boolean actualizarFisica(int anchoPantalla) {
        numFrame++;
        if (numFrame >= movimientoObstaculo.length) numFrame = 0;
        moverObstaculo();
        setRectangulos();
        if (posicion.x < 0 - movimientoObstaculo[numFrame].getWidth()) {
            return true;
        }
        return false;
    }

    /***
     * funcion que actualiza el rectangulo de colision
     */
    private void setRectangulos() {
        ancho = movimientoObstaculo[numFrame].getWidth();
        alto = movimientoObstaculo[numFrame].getHeight();
        rectangulo = new RectF(
                posicion.x,
                posicion.y,
                posicion.x + ancho,
                posicion.y + alto
        );
    }

    public boolean detectarColision(Personaje personaje){


        for (RectF collider :personaje.getRectangulos()) {
            if (rectangulo.contains(collider)||rectangulo.intersect(collider)){
                return true;
            }
        }
        return false;
    }

    /***
     * función que mueve la bala
     */
    private void moverObstaculo() {
        posicion.x -= velocidad;
    }

    /***
     * Dibujamos los elementos en pantalla
     * @param canvas Lienzo sobre el que dibujar
     */
    public void dibujar(Canvas canvas) throws Exception {
        canvas.drawBitmap(espejo(movimientoObstaculo[numFrame], true), posicion.x, posicion.y, null);
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
