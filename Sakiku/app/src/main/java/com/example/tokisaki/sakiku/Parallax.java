package com.example.tokisaki.sakiku;

/**
 * Created by Tokisaki on 28/04/2018.
 */

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

/**
 * Clase que realizará el efecto parallax en la clase Jugar
 */
public class Parallax {

    /**
     * Velocidad de movimiento del fondo
     */
    private int velocidadFondo;
    /**
     * Imagen del fondo en movimiento
     */
    private Bitmap fondo;
    /**
     * Posicion X de la primera copia del fondo
     */
    private int postI1;
    /**
     * Posicion X de la segunda copia del fondo
     */
    private int postI2;

    /**
     * Constructor de la clase
     * @param context Contexto de la aplicacion
     * @param anchoPantalla ancho de la pantalla
     * @param altoPantalla  alto de la pantalla
     */
    public Parallax(Context context, int anchoPantalla, int altoPantalla) {
        fondo = BitmapFactory.decodeResource(context.getResources(), R.drawable.fondo);
        fondo = Bitmap.createScaledBitmap(fondo, anchoPantalla, altoPantalla, true);
        velocidadFondo = anchoPantalla / 200;
        postI1 = 0;
        postI2 = fondo.getWidth();
    }

    /**
     * Actualizamos la física de los elementos en pantalla
     */
    public void actualizarFisica() {
        postI1 -= velocidadFondo;
        postI2 -= velocidadFondo;
        if (postI1 + fondo.getWidth() < 0) {
            postI1 = postI2 + fondo.getWidth();
        }
        if (postI2 + fondo.getWidth() < 0) {
            postI2 = postI1 + fondo.getWidth();
        }
    }

    /**
     * Dibujamos los elementos en pantalla
     * @param canvas Lienzo sobre el que dibujar
     */
    public void dibujar(Canvas canvas) {
        canvas.drawBitmap(fondo, postI1, 0, null);
        canvas.drawBitmap(fondo, postI2, 0, null);
    }
}