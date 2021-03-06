package com.example.tokisaki.sakiku;

/**
 * Created by Tokisaki on 15/04/2018.
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
 * Escena que contiene los créditos del juego
 */
public class Creditos extends Escenas {

    /**
     * Layout contenedor de texto
     */
    private StaticLayout layoutDescripcion;
    /**
     * Pincel para escritura
     */
    private TextPaint pDescripcion;
    /**
     * Variable que recogerá "creditos" de strings.xml
     */
    private String creditos;
    /**
     * Variable que recogerá "creditosTexto" de strings.xml
     */
    private String creditosTexto;
    /**
     * Ancho del contenedor de descripcion
     */
    private int anchoLayoutDescripcion;
    /**
     * Posicion de dibujo de la cabecera Creditos
     */
    private Point posCreditos ;
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
    public Creditos(int numEscena, Context context, int anchoPantalla, int altoPantalla, Info  info) {
        super(numEscena, context, anchoPantalla, altoPantalla,info);
        inicializar();
    }

    /***
     * función que inicializa los elementos de la pantalla
     */
    private void inicializar() {
        creditos = context.getResources().getString(R.string.creditosTitulo);
        creditosTexto = context.getResources().getString(R.string.creditosTexto);
        this.pDescripcion = new TextPaint();
        this.pDescripcion.setColor(Color.RED);
        this.pDescripcion.setTextSize(getPixels(20));
        this.pDescripcion.setTextAlign(Paint.Align.LEFT);
        pDescripcion.setTypeface(faw);

        l = new Paint();
        l.setColor(Color.RED);
        l.setTextSize(getPixels(45));
        l.setTypeface(letras);
        p = new Paint();
        p.setColor(Color.RED);
        p.setTextSize(getPixels(40));
        p.setTypeface(faw);

        anchoLayoutDescripcion = anchoPantalla / 2;
        posCreditos = new Point(anchoPantalla / 2 - getPixels(75), getPixels(80));
        posDescripcion = new Point(anchoPantalla / 4, getPixels(130));
    }

    /***
     * Dibujamos los elementos en pantalla
     * @param c Lienzo sobre el que dibujar
     */
    public void dibujar(Canvas c) {
        c.drawColor(Color.BLACK);
        c.drawBitmap(fondo, 0, 0, null);
        c.drawText(creditos, posCreditos.x, posCreditos.y, l);
        this.layoutDescripcion = new StaticLayout(creditosTexto, pDescripcion, anchoLayoutDescripcion, Layout.Alignment.ALIGN_CENTER, 1.0f, 0.0f, false);
        c.translate(posDescripcion.x, posDescripcion.y);
        this.layoutDescripcion.draw(c);
    }

    /***
     * Actualizamos la física de los elementos en pantalla
     */
    public void actualizarFisica() {
        super.actualizarFisica();
    }

    /***
     * Registra las pulsaciones en pantalla
     * @param event Tipo de pulsación
     * @return numero de la escena actual
     */
    public int onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);
        if (event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_POINTER_UP)
            return 1;
        return numEscena;
    }
}

