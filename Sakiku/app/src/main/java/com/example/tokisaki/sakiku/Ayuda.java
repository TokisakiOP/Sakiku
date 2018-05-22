package com.example.tokisaki.sakiku;

/**
 * Created by Tokisaki on 18/05/2018.
 */

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.view.MotionEvent;

/***
 * Clase que contiene la ayuda del juego
 */
public class Ayuda extends Escenas{

    /**
     * Layout contenedor de texto
     */
    private StaticLayout layoutDescripcion;
    /**
     * Pincel para escritura
     */
    private TextPaint pDescripcion;
    /**
     * Variable que recogerá "ayuda" de strings.xml
     */
    private String ayuda;
    /**
     *  Variable que recogerá "ayudaTexto" de strings.xml
     */
    private String ayudaTexto;
    /**
     * Ancho del contenedor de descripcion
     */
    private int anchoLayoutDescripcion;
    /**
     * Posicion de dibujo de la ayuda
     */
    private Point posAyuda;
    /**
     * Posicion de dibujado de la descripcion
     */
    private Point posDescripcion ;

    /***
     * Constructor de la clase
     * @param numEscena numero de la escena
     * @param context contexto de la aplicación
     * @param anchoPantalla ancho de la pantalla del dispositivo donde se ejecita la aplicación
     * @param altoPantalla alto de la pantalla del dispositivo donde se ejecita la aplicación
     * @param info Instancia de la clase Info
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public Ayuda(int numEscena, Context context, int anchoPantalla, int altoPantalla, Info info) {
        super(numEscena, context, anchoPantalla, altoPantalla,info);
        inicializacion();
    }

    /***
     * Funcion que inicializa todos los elementos de la pantalla
     */
    private void inicializacion(){
        ayuda= context.getResources().getString(R.string.ayuda);
        ayudaTexto = context.getResources().getString(R.string.ayudaTexto);
        pDescripcion = new TextPaint();
        pDescripcion.setColor(Color.RED);
        pDescripcion.setTextSize(getPixels(20));
        pDescripcion.setTextAlign(Paint.Align.LEFT);
        pDescripcion.setTypeface(faw);
        l=new Paint();
        l.setColor(Color.RED);
        l.setTextSize(getPixels(45));
        l.setTypeface(letras);
        p=new Paint();
        p.setColor(Color.RED);
        p.setTextSize(getPixels(30));
        p.setTypeface(faw);
        layoutDescripcion = new StaticLayout(ayudaTexto, pDescripcion,
                anchoPantalla, Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);

        anchoLayoutDescripcion = anchoPantalla / 2;
        posAyuda = new Point(anchoPantalla / 2 - getPixels(75), getPixels(80));
        posDescripcion = new Point(anchoPantalla / 4, getPixels(130));

    }

    /***
     * Dibujamos los elementos en pantalla
     * @param c Lienzo sobre el que dibujar
     */
    public void dibujar (Canvas c){
        c.drawColor(Color.BLACK);
        c.drawBitmap(fondo, 0, 0, null);
        c.drawText(ayuda, posAyuda.x, posAyuda.y, l);
        this.layoutDescripcion = new StaticLayout(ayudaTexto, pDescripcion, anchoLayoutDescripcion, Layout.Alignment.ALIGN_CENTER, 1.0f, 0.0f, false);
        c.translate(posDescripcion.x, posDescripcion.y);
        this.layoutDescripcion.draw(c);
    }

    /***
     * Actualizamos la física de los elementos en pantalla
     */
    public void actualizarFisica(){
        super.actualizarFisica();

    }

    /***
     * Registra las pulsaciones en pantalla
     * @param event Tipo de pulsación
     * @return numero de la escena actual
     */
    public int onTouchEvent(MotionEvent event){
        super.onTouchEvent(event);
        switch (event.getAction()) {
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP:
                return 1 ;
        }
        return numEscena;
    }
}
