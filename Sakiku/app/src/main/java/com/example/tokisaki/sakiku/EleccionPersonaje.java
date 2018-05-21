package com.example.tokisaki.sakiku;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.MotionEvent;

import static com.example.tokisaki.sakiku.Inicio.jugando;

/**
 * Created by Tokisaki on 19/05/2018.
 */

public class EleccionPersonaje extends Escenas {
    /**
     * pincel para las fuentes externas
     */
    private Paint s;
    /**
     * icono para el aventurero
     */
    private String aventurero;
    /**
     * icono para el ninja
     */
    private String ninja;
    /**
     *  icono para el robot
     */
    private String robot;
    /**
     * Variable que recogerá "eleccion" de strings.xml
     */
    private String titulo;
    /**
     * Rectangulo de pulsación para aventurero
     */
    private Rect avent;
    /**
     * Rectangulo de pulsación para ninja
     */
    private Rect nin;
    /**
     * Rectangulo de pulsación para robot
     */
    private Rect rob;
    /**
     * Posición left de los rectangulos
     */
    private int posBx;
    /**
     * Posición top y bottom de los rectangulos
     */
    private int posBf;
    /**
     * Posición right de los rectangulos
     */
    private int poxBy;
    /**
     * Posicion del titulo
     */
    private Point posTitulo;
    /**
     * Posicion de aventurero
     */
    private Point posAventurero;
    /**
     * Posicion de ninja
     */
    private Point posNinja;
    /**
     * Posicion robot
     */
    private Point posRobot;

    /***
     * Constructor de la clase
     * @param numEscena numero de la escena
     * @param context contexto de la aplicación
     * @param anchoPantalla ancho de la pantalla del dispositivo donde se ejecita la aplicación
     * @param altoPantalla alto de la pantalla del dispositivo donde se ejecita la aplicación
     * @param info Instancia de la clase Info
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public EleccionPersonaje(int numEscena, Context context, int anchoPantalla, int altoPantalla, Info info) {
        super(numEscena, context, anchoPantalla, altoPantalla,info);
        int size = getPixels(50);
        posBx = posBf = poxBy = altoPantalla / 2 - getPixels(75);
        posBx = anchoPantalla / 2 - getPixels(25);
        posBf = anchoPantalla / 2 + getPixels(25);
        inicializarRects();
        if(jugando)Inicio.musicaJuego.stop();
        Inicio.mediaPlayer.start();

    }

    /***
     * funcion que inicializa los iconos
     */
    private void inicializarRects() {
        titulo = context.getResources().getString(R.string.eleccion);
        aventurero = context.getResources().getString(R.string.indiana);
        ninja = context.getResources().getString(R.string.ninja);
        robot = context.getResources().getString(R.string.robot);
        avent = new Rect(posBx, poxBy, posBf, poxBy + getPixels(50));
        poxBy += getPixels(40) * 2;
        nin = new Rect(posBx, poxBy, posBf, poxBy + getPixels(50));
        poxBy += getPixels(40) * 2;
        rob = new Rect(posBx,poxBy,posBf,poxBy+getPixels(50));
        p = new Paint();
        p.setColor(Color.RED);
        p.setTextSize(avent.height());
        p.setTypeface(faw);
        l = new Paint();
        l.setColor(Color.RED);
        l.setTextSize(getPixels(40));
        l.setTypeface(letras);
        s = new Paint();
        s.setColor(Color.RED);
        s.setTextSize(getPixels(30));
        s.setTypeface(letras);

        posTitulo = new Point(anchoPantalla / 2 - getPixels(125), getPixels(75));
        posAventurero = new Point(avent.left + getPixels(3), avent.bottom - getPixels(8));
        posNinja = new Point(nin.left, nin.bottom - getPixels(8));
        posRobot = new Point(rob.left, rob.bottom - getPixels(8));
    }

    /***
     * Dibujamos los elementos en pantalla
     * @param c Lienzo sobre el que dibujar
     */
    public void dibujar(Canvas c) {
        c.drawColor(Color.BLACK);
        c.drawBitmap(fondo, 0, 0, null);
        c.drawText(titulo, posTitulo.x, posTitulo.y, l);
        c.drawRect(avent, pBoton);
        c.drawRect(nin, pBoton);
        c.drawRect(rob, pBoton);
        c.drawText(aventurero, posAventurero.x, posAventurero.y, p);
        c.drawText(ninja, posNinja.x, posNinja.y, p);
        c.drawText(robot, posRobot.x, posRobot.y, p);
        c.drawText(info.personaje, 0, altoPantalla - getPixels(30), l);
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
        int x = (int) event.getX(), y = (int) event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP:
                if (avent.contains(x, y)) {
                    info.personaje = "aventurero";
                    SharedPreferences preferencias = context.getSharedPreferences("saves",0);
                    SharedPreferences.Editor editor = preferencias.edit();
                    editor.putString("character",info.personaje);
                    editor.apply();
                    return 1;
                } else if (nin.contains(x, y)) {
                    info.personaje = "ninja";
                    SharedPreferences preferencias = context.getSharedPreferences("saves",0);
                    SharedPreferences.Editor editor = preferencias.edit();
                    editor.putString("character",info.personaje);
                    editor.apply();
                    return 1;
                }else if (rob.contains((int) event.getX(), (int) event.getY())) {
                    info.personaje = "robot";
                    SharedPreferences preferencias = context.getSharedPreferences("saves",0);
                    SharedPreferences.Editor editor = preferencias.edit();
                    editor.putString("character",info.personaje);
                    editor.apply();
                    return 1;
                }
        }
        return numEscena;
    }
}
