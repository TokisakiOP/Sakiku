package com.example.tokisaki.sakiku;

/**
 * Created by Tokisaki on 15/04/2018.
 */

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Typeface;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.WindowManager;

import java.util.ArrayList;

/***
 * Clase contenedora de todos los elementos comunes de las escenas
 */
public class Escenas {

    /**
     * fuente externa
     */
    protected Typeface letras;
    /**
     * fuente externa
     */
    protected Typeface faw;
    /**
     * pincel para el dibujo de las fuentes externa
     */
    protected Paint p;
    /**
     * pincel para el dibujo de las fuentes externa
     */
    protected Paint l;
    /**
     * contexto de la aplicacion
     */
    protected  Context context;
    /**
     * instancia de la clase inicio
     */
    protected Inicio music;
    /**
     * pincel para texto
     */
    protected Paint pTexto;
    /**
     * pincel para botones/rectangulos
     */
    protected Paint pBoton;
    /**
     * parametro que nos permitirá asignar los sonidos
     */
    protected SoundPool efectos;
    /**
     * imagen de fonde de las escenas secundarias
     */
    Bitmap fondo;
    /**
     * parametro que nos permite acceder al sonido
     */
    protected AudioManager audioManager;
    /**
     * número de escena
     */
    protected int numEscena;
    /**
     * maximo de sonidos simultaneos en la aplicación
     */
    final protected int maxSonidosSimultaneos = 10;
    /**
     * ancho de la pantalla del dispositivo donde se ejecita la aplicación
     */
    protected int anchoPantalla;
    /**
     * alto de la pantalla del dispositivo donde se ejecita la aplicación
     */
    protected int altoPantalla;
    /**
     * sonido de salto del personaje
     */
    protected int sonidoSalto;
    /**
     * sonido del desliz del personaje
     */
    protected int sonidoDesliz;
    /**
     * sonido de comienzo de la carrera
     */
    protected int sonidoStart;
    /**
     * sonido de salida de una bola de fuego
     */
    protected int sonidoFuego;
    /**
     * sonido de salida de un bloque de hielo
     */
    protected int sonidoHielo;
    /**
     * sonido de pisadas
     */
    protected int pasos;


    /***
     * Constructor de la clase
     * @param numEscena numero de la escena
     * @param context contexto de la aplicación
     * @param anchoPantalla ancho de la pantalla del dispositivo donde se ejecita la aplicación
     * @param altoPantalla alto de la pantalla del dispositivo donde se ejecita la aplicación
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public Escenas(int numEscena, Context context, int anchoPantalla, int altoPantalla) {
        this.context = context;
        this.numEscena = numEscena;
        this.anchoPantalla = anchoPantalla;
        this.altoPantalla = altoPantalla;
        inicializarPaints();
        inicializarMusica();
        music = new Inicio(context);
        fondo = BitmapFactory.decodeResource(context.getResources(), R.drawable.portada);
        fondo = Bitmap.createScaledBitmap(fondo, anchoPantalla, altoPantalla, true);
        faw = Typeface.createFromAsset(context.getAssets(), "fontawesome-webfont.ttf");
        letras = Typeface.createFromAsset(context.getAssets(), "fontawesome-webfont.ttf");
    }

    /***
     * inicializa los pinceles que se usaran en las escenas
     */
    private void inicializarPaints() {
        pTexto = new Paint();
        pTexto.setColor(Color.RED);
        pTexto.setTextSize(getPixels(50));
        pBoton = new Paint();
        pBoton.setColor(Color.WHITE);
        pBoton.setAlpha(100);
    }

    /***
     * Inicializa la musica y los efectos que su usaran en el juego
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void inicializarMusica() {
        audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        if ((android.os.Build.VERSION.SDK_INT) >= 21) {
            SoundPool.Builder spb = new SoundPool.Builder();
            spb.setAudioAttributes(new AudioAttributes.Builder().setUsage(AudioAttributes.USAGE_MEDIA)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION).build());
            spb.setMaxStreams(maxSonidosSimultaneos);
            this.efectos = spb.build();
        } else {
            this.efectos = new SoundPool(maxSonidosSimultaneos, AudioManager.STREAM_MUSIC, 0);
        }
        sonidoDesliz = efectos.load(context,R.raw.desliz,1);
        sonidoFuego = efectos.load(context,R.raw.fuego,1);
        sonidoHielo = efectos.load(context,R.raw.hielo,1);
        sonidoSalto = efectos.load(context,R.raw.salto,1);
        sonidoStart = efectos.load(context,R.raw.start,1);
        pasos = efectos.load(context,R.raw.pisadas,1);
    }

    /***
     * delvuelve el numero de escena
     * @return numero de la escena actual
     */
    protected int getNumEscena() {
        return numEscena;
    }

    /***
     * Dibujamos los elementos en pantalla
     * @param c Lienzo sobre el que dibujar
     */
    public void dibujar(Canvas c) {

    }

    /***
     * Actualizamos la física de los elementos en pantalla
     */
    public void actualizarFisica() {

    }

    /***
     * Registra las pulsaciones en pantalla
     * @param event Tipo de pulsación
     * @return numero de la escena actual
     */
    public int onTouchEvent(MotionEvent event) {
        return numEscena;
    }

    /***
     * Reescala una imagen
     * @param bitmapAux imagen a reescalar
     * @param nuevoAncho nuevo ancho de la imagen despues del rescalado
     * @return la imagen reescalada
     */
    public Bitmap escalaAnchura(Bitmap bitmapAux, int nuevoAncho) {
        //Bitmap bitmapAux=BitmapFactory.decodeResource(context.getResources(), res);
        if (nuevoAncho == bitmapAux.getWidth()) return bitmapAux;
        return bitmapAux.createScaledBitmap(bitmapAux, nuevoAncho, (bitmapAux.getHeight() * nuevoAncho) /
                bitmapAux.getWidth(), true);
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
}




