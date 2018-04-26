package com.example.tokisaki.sakiku;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.Rect;

/**
 * Boton con imagen
 */
public class BotonImagen {

    /**
     * Imagen del boton
     */
    public Bitmap imagen;

    /**
     * Collider del boton
     */
    public Rect collider;

    /**
     * Posicion de dibujo del boton
     */
    public Point posicion;

    /**
     * Inicializa una instancia de la clase segun parametros recibidos
     *
     * @param imagen   Imagen del boton
     * @param posicion Posicion
     */
    public BotonImagen(Bitmap imagen, Point posicion) {

        this.imagen = imagen;
        this.posicion = posicion;
        collider = new Rect(posicion.x, posicion.y, posicion.x + imagen.getWidth(), posicion.y + imagen.getHeight());
    }

    /**
     * Dibuja el boton sobre un lienzo recibido como parametro
     * @param canvas Lienzo recibido
     */
    public void dibujar(Canvas canvas) {
        canvas.drawBitmap(imagen, posicion.x, posicion.y, null);
    }
}
