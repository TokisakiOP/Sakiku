package com.example.tokisaki.sakiku;

/**
 * Created by Tokisaki on 15/04/2018.
 */

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.DisplayMetrics;
import android.view.WindowManager;

/***
 * Clase que gestiona loe elementos relacionados con el personaje que controlamos
 */
public class Personaje {

    /**
     * contexto de la aplicación
     */
    private Context context;
    /**
     * número de frames de la acción de saltar
     */
    private int numImagenes_salto = 10;
    /**
     * número de frames de la acción de correr
     */
    private int numImagenes_run = 10;
    /**
     * número de frames de la acción de deslizarse
     */
    private int numImagenes_desliz = 10;
    /**
     * numero de frames horizontales a recortar de la accion correr
     */
    private int numImagenesH_run = 3;
    /**
     * numero de frames verticales a recortar de la accion correr
     */
    private int numImagenesV_run = 4;
    /**
     * // numero de frames horizontales a recortar de la accion saltar
     */
    private int numImagenesH_salto = 4;
    /**
     * numero de frames verticales a recortar de la accion saltar
     */
    private int numImagenesV_salto = 3;
    /**
     * numero de frames horizontales a recortar de la accion deslizarse
     */
    private int numImagenesH_desliz = 3;
    /**
     * numero de frames verticales a recortar de la accion deslizarse
     */
    private int numImagenesV_desliz = 4;
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
     * cambia la fila del recorte del frame
     */
    private int cambioV = 0;
    /**
     * posicion en el eje X donde esta situado el personaje
     */
    protected int posX = 0;
    /**
     * limite Y variable donde pisa el personaje
     */
    protected int suelo;
    /**
     * limite Y fijo donde pisa el personaje
     */
    protected int fijo;
    /**
     * numero del frame actual
     */
    protected int numFrame;
    /**
     * alto del cuadrado de colisión
     */
    protected int alto;
    /**
     * ancho del cuadrado de colisión
     */
    private int ancho;
    /**
     * lista con los frames de la accion movimiento horizontal
     */
    protected Bitmap[] movimientoRun;
    /**
     * lista con los frames de la accion muerte
     */
    protected Bitmap[] movimientoSalto;
    /**
     * lista con los frames de la accion disparo horizontal
     */
    protected Bitmap[] movimientoDesliz;
    /**
     * frame con el recorte actual de salto
     */
    private Bitmap salto;
    /**
     * frame con el recorte actual de desliz
     */
    private Bitmap desliz;
    /**
     * frame con el recorte actual de correr
     */
    private Bitmap run;
    /**
     * Array con los cuadrados de colisión
     */
    protected RectF[] rectangulos;
    //protected boolean muriendo; // booleano que indica si el personaje esta en la acción de morirse

    public eEstadoPersonaje estado;

    Paint p;

    public Personaje(Context context, int altoPantalla, int anchoPantalla) {
        this.context = context;
        rectangulos = new RectF[2];
        suelo = altoPantalla;
        fijo = altoPantalla;
        posX = anchoPantalla / 2;
        inicializarElementos();
        p = new Paint();
        p.setColor(Color.RED);
        p.setStyle(Paint.Style.STROKE);
        p.setStrokeWidth(5);
        estado = eEstadoPersonaje.CORRIENDO;
    }

    /***
     * Función que inicializa los elementos necesarios en la clase
     */
    public void inicializarElementos() {
        movimientoRun = new Bitmap[numImagenes_run];
        movimientoSalto = new Bitmap[numImagenes_salto];
        movimientoDesliz = new Bitmap[numImagenes_desliz];
        run = BitmapFactory.decodeResource(context.getResources(), R.drawable.correr);
        anchoFrame = run.getWidth() / numImagenesH_run;
        altoFrame = run.getHeight() / numImagenesV_run;
        for (int i = 0; i < numImagenes_run; i++) {
            Bitmap frame = Bitmap.createBitmap(run, cambioH * anchoFrame, cambioV * altoFrame, anchoFrame, altoFrame);
            frame = escalaAltura(frame, getPixels(100));
            movimientoRun[i] = frame;
            cambioH++;
            if (i == 2 || i == 5 || i == 8) {
                cambioH = 0;
                cambioV++;
            }
        }
        run = null;
        salto = BitmapFactory.decodeResource(context.getResources(), R.drawable.salto);
        cambioH = 0;
        cambioV = 0;
        anchoFrame = salto.getWidth() / numImagenesH_salto;
        altoFrame = salto.getHeight() / numImagenesV_salto;
        for (int i = 0; i < numImagenes_salto; i++) {
            Bitmap frame = Bitmap.createBitmap(salto, cambioH * anchoFrame, cambioV * altoFrame, anchoFrame, altoFrame);
            ;
            frame = escalaAltura(frame, getPixels(100));
            movimientoSalto[i] = frame;
            cambioH++;
            if (i == 3 || i == 7) {
                cambioH = 0;
                cambioV++;
            }
        }
        salto = null;
        desliz = BitmapFactory.decodeResource(context.getResources(), R.drawable.desliz);
        cambioH = 0;
        cambioV = 0;
        anchoFrame = desliz.getWidth() / numImagenesH_desliz;
        altoFrame = desliz.getHeight() / numImagenesV_desliz;
        for (int i = 0; i < numImagenes_desliz; i++) {
            Bitmap frame = Bitmap.createBitmap(desliz, cambioH * anchoFrame, cambioV * altoFrame, anchoFrame, altoFrame);
            ;
            frame = escalaAltura(frame, getPixels(80));
            movimientoDesliz[i] = frame;
            cambioH++;
            if (i == 2 || i == 5 || i == 8) {
                cambioH = 0;
                cambioV++;
            }
        }
        desliz = null;
    }

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
     * Función que actualiza los rectangulos de colisión
     * @param estado enum que indica que accion se esta ejecutando
     */
    public void setRectangulos(eEstadoPersonaje estado) {
        switch (estado) {
            case CORRIENDO:
                ancho = movimientoRun[numFrame].getWidth();
                alto = movimientoRun[numFrame].getHeight();
                break;
            case DESLIZANDOSE:
                ancho = movimientoDesliz[numFrame].getWidth();
                alto = movimientoDesliz[numFrame].getHeight();
                break;
            case SALTANDO:
                ancho = movimientoSalto[numFrame].getWidth();
                alto = movimientoSalto[numFrame].getHeight();
                break;
        }
        float x = posX;
        float y = suelo;
        rectangulos[0] = new RectF(
                (int) x + ancho / 3,
                (int) y - (alto - alto / 8),
                (int) x + (ancho - ancho / 3),
                (int) y - alto / 2
        );
        rectangulos[1] = new RectF(
                (int) x + ancho / 6,
                (int) y - alto / 2,
                (int) x + (ancho - ancho / 6),
                (int) y
        );
    }

    /***
     * Actualizamos la física de los elementos en pantalla
     */
    public void actualizarFisica(eEstadoPersonaje estado) {
        this.estado = estado;
        switch (estado) {
            /*case "avanceX":
                posX += getPixels(3f);
                setRectangulos("movimientoX");
                break;*/
            /*case "retrocesoX":
                posX-=getPixels(3f);
                setRectangulos("movimientoXespejo");
                break;*/
            case CORRIENDO:
                numFrame++;
                if (numFrame >= movimientoRun.length) numFrame = 0;
                setRectangulos(eEstadoPersonaje.CORRIENDO);
                break;
            case SALTANDO:
                if (numFrame <= 5) {
                    suelo -= getPixels(10);
                } else {
                    suelo += getPixels(10);
                }
                numFrame++;
                if (numFrame >= movimientoSalto.length) numFrame = 0;
                setRectangulos(eEstadoPersonaje.SALTANDO);
                break;
            case DESLIZANDOSE:
                numFrame++;
                if (numFrame >= movimientoDesliz.length) numFrame = 0;
                setRectangulos(eEstadoPersonaje.DESLIZANDOSE);
                break;
        }
    }


    /***
     * Dibujamos los elementos en pantalla
     * @param canvas Lienzo sobre el que dibujar
     */
    public void dibujar(Canvas canvas) throws Exception {
        switch (estado) {
            case CORRIENDO:
                canvas.drawBitmap(movimientoRun[numFrame], posX, (suelo - movimientoRun[numFrame].getHeight()), null);
                break;
            case SALTANDO:
                canvas.drawBitmap(movimientoSalto[numFrame], posX, (suelo - movimientoSalto[numFrame].getHeight()), null);
                break;
            case DESLIZANDOSE:
                canvas.drawBitmap(movimientoDesliz[numFrame], posX, (suelo - movimientoDesliz[numFrame].getHeight()), null);
                break;
        }
        canvas.drawRect(rectangulos[0], p);
        canvas.drawRect(rectangulos[1], p);
    }

    /***
     * función que devuelve el rectangulo de colisión del personaje
     * @return rectangulo de colisión
     */
    protected RectF[] getRectangulos() {
        return rectangulos;
    }
}
