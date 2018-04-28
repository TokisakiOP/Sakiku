package com.example.tokisaki.sakiku;

/**
 * Created by Tokisaki on 28/04/2018.
 */
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.Rect;

/**
 * Clase creadora del componente ImageButton
 */
public class ImageButton {

    /**
     * Imagen del boton
     */
    public Bitmap imagen;
    /**
     * Rectangulo de colision del boton
     */
    public Rect rButton;
    /**
     * Posicion del boton
     */
    public Point posicion;

    /**
     * constructor de la clase
     * @param imagen imagen para el boton
     * @param posicion posicion donde se situara el boton
     */
    public ImageButton(Bitmap imagen, Point posicion) {

        this.imagen = imagen;
        this.posicion = posicion;
        rButton = new Rect(posicion.x, posicion.y, posicion.x + imagen.getWidth(), posicion.y + imagen.getHeight());
    }

    /***
     * Dibujamos los elementos en pantalla
     * @param canvas Lienzo sobre el que dibujar
     */
    public void dibujar(Canvas canvas) {
        canvas.drawBitmap(imagen, posicion.x, posicion.y, null);
    }
}