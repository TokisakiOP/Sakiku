package com.example.tokisaki.sakiku.Botones;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.Rect;

public class BotonImagen {

    public Bitmap imagen;
    public Rect collider;
    public Point posicion;
    public String texto;

    public BotonImagen(Bitmap imagen, Point posicion) {

        this.imagen = imagen;
        this.posicion = posicion;
        collider = new Rect(posicion.x, posicion.y, posicion.x + imagen.getWidth(), posicion.y + imagen.getHeight());
    }

    public void dibujar(Canvas canvas) {
        canvas.drawBitmap(imagen, posicion.x, posicion.y, null);
    }
}
