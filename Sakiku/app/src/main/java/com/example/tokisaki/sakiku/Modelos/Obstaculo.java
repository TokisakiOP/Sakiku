package com.example.tokisaki.sakiku.Modelos;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.RectF;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;

public class Obstaculo {

    /**
     * Contexto de la aplicacion
     */
    protected Context context;

    /**
     * Rectangulo del colisión
     */
    public RectF rectangulo;

    /**
     * velocidad de movimiento del obstaculo
     */
    protected int velocidad = 8;

    /**
     * Pincel para dibujador de colliders
     */
    public Paint p;

    /**
     * Imagen actual del objeto
     */
    Bitmap frameActual;

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
     * Ininicializa uns instancia de la clase Obstaculo segun parametros recibidos
     *
     * @param context  Contexto de la aplicacion
     * @param posicion Posicion inicial
     */
    public Obstaculo(Context context, PointF posicion) {
        this.context = context;
        this.posicion = posicion;
        p = new Paint();
        p.setColor(Color.GREEN);
        p.setStyle(Paint.Style.STROKE);
        p.setStrokeWidth(5);
    }

    /**
     * Actualiza el estado de obstaculo , devolviendo true
     * si este ha llegado al final de la pantalla , si no false
     *
     * @return True si llego al final o false si no
     */
    public boolean actualizarFisica() {
        moverObstaculo();
        if (posicion.x < 0 - frameActual.getWidth()) {
            return true;
        }
        return false;
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
}
