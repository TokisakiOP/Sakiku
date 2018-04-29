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
    protected Context context;
    /**
     * Rectangulo de colisión
     */
    public RectF rectangulo;
    /**
     * velocidad de movimiento del obstaculo
     */
    protected int velocidad = 8;
    /**
     * Pincel para dibujar los rectangulos
     */
    public Paint p;
    /**
     * Imagen actual del objeto
     */
    Bitmap frame;
    /**
     * Posicion del obstaculo
     */
    PointF posicion;

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

    /**
     * Constructor de la clase
     *
     * @param context  Contexto de la aplicacion
     * @param posicion Posicion inicial
     */
    public Obstaculos(Context context, PointF posicion) {
        this.context = context;
        this.posicion = posicion;
        p = new Paint();
        p.setColor(Color.GREEN);
        p.setStyle(Paint.Style.STROKE);
        p.setStrokeWidth(5);
    }

    /**
     * Actualizamos la física de los elementos en pantalla
     * @return devuelve true en caso de que el obsaculo este dentro de la pantalla, si esta fuera de los límites de loa misma devuelve false
     */
    public boolean actualizarFisica() {
        moverObstaculo();
        if (posicion.x < 0 - frame.getWidth()) {
            return true;
        }
        return false;
    }

    /***
     * Dibujamos los elementos en pantalla
     * @param canvas Lienzo sobre el que dibujar
     */
    public void dibujar(Canvas canvas) {
        canvas.drawBitmap(frame, posicion.x, posicion.y, null);
        canvas.drawRect(rectangulo, p);
    }

    /***
     * función que mueve el obstaculo
     */
    private void moverObstaculo() {
        posicion.x -= velocidad;
        setRectangulos();
    }

    /***
     * funcion que actualiza el rectangulo de colision
     */
    protected void setRectangulos() {
        rectangulo = new RectF(posicion.x, posicion.y, posicion.x + frame.getWidth(), posicion.y + frame.getHeight());

    }

    /**
     * Comprueba si hay colision entre el obstaculo y el personaje pasado como parametro
     * @param personaje Personaje a comprobar
     * @return Devuelve true si colisiona en caso contrario false
     */
    public boolean detectarColision(Personaje personaje) {
        for (RectF r : personaje.getRectangulos()) {
            if (rectangulo.contains(r) || rectangulo.intersect(r)) {
                return true;
            }
        }
        return false;
    }
}
