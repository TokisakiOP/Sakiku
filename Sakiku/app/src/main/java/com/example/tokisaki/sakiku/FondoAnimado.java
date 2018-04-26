package com.example.tokisaki.sakiku;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

/**
 * Fondo animado de juego
 */
public class FondoAnimado {

    /**
     * Velocidad de movimiento del fondo
     */
    private int velocidadFondo;

    /**
     * Bitmap del fondo
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
     * Inicializa una instancia de la clase segun los parametros recibidos
     *
     * @param context       Contexto de la aplicacion
     * @param anchoPantalla ancho de la pantalla
     * @param altoPantalla  alto de la pantalla
     */
    public FondoAnimado(Context context, int anchoPantalla, int altoPantalla) {
        fondo = BitmapFactory.decodeResource(context.getResources(), R.drawable.fondo);
        fondo = Bitmap.createScaledBitmap(fondo, anchoPantalla, altoPantalla, true);
        velocidadFondo = anchoPantalla / 200;
        postI1 = 0;
        postI2 = fondo.getWidth();
    }

    /**
     * Actualiza el estado del fondo
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
     * Dado un lienzo recibido como parametro , dibuja los elementos
     * de la clase en el mismo
     *
     * @param canvas Lienzo recibido
     */
    public void dibujar(Canvas canvas) {
        canvas.drawBitmap(fondo, postI1, 0, null);
        canvas.drawBitmap(fondo, postI2, 0, null);
    }
}
