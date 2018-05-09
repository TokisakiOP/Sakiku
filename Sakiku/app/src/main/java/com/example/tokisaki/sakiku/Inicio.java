package com.example.tokisaki.sakiku;

/**
 * Created by Tokisaki on 15/04/2018.
 */

import android.content.Context;
import android.graphics.Canvas;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/***
 * Clase que hereda de SurfaceView y que manejará todos los cambios en la aplicacion
 */
public class Inicio extends SurfaceView implements SurfaceHolder.Callback {
    /***
     * La clase hilo va a ser la que maneje el Thread de la aplicacion
     */
    class Hilo extends Thread {
        public Hilo() {
        }

        @Override
        public void run() {
            long tiempoDormido = 0; //Tiempo que va a dormir el hilo
            final int FPS = 50; // Nuestro objetivo
            final int TPS = 1000000000; //Ticks en un segundo para la función usada nanoTime()
            final int FRAGMENTO_TEMPORAL = TPS / FPS; // Espacio de tiempo en el que haremos todo de forma repetida
            long tiempoReferencia = System.nanoTime();// Tomamos un tiempo de referencia actual en nanosegundos más preciso que currenTimeMillis()
            while (funcionando) {
                Canvas c = null;
                try {
                    c = surfaceHolder.lockCanvas();
                    synchronized (surfaceHolder) {
                        try {
                            escenaActual.actualizarFisica();
                            escenaActual.dibujar(c);
                        } catch (Exception e) {

                        }
                    }
                } finally {
                    if (c != null) {
                        surfaceHolder.unlockCanvasAndPost(c);
                    }
                }
                tiempoReferencia += FRAGMENTO_TEMPORAL;
                tiempoDormido = tiempoReferencia - System.nanoTime();
                if (tiempoDormido > 0) {
                    try {
                        Thread.sleep(tiempoDormido / 1000000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    /**
     * variable que contiene la música que se escucha en los menus
     */
    protected static MediaPlayer mediaPlayer = null;
    /**
     * variable que contiene la música que se escucha mientras se juega
     */
    protected static MediaPlayer musicaJuego = null;
    /**
     * valor de la nueva escena recogida por el onTouch
     */
    private static int nuevaEscena;
    /**
     * booleano que indica si el hilo esta en funcionamiento
     */
    protected static boolean funcionando = false;
    /**
     * variable que maneja el holder
     */
    private SurfaceHolder surfaceHolder;
    /**
     * variable que contiene el contexto de la aplicacion
     */
    private Context context;
    /**
     * instancia de la clase Hilo
     */
    private Hilo hilo;
    /**
     * instancia de la clase escena
     */
    protected  static Escenas escenaActual;
    /**
     * manejador del audio
     */
    private AudioManager audioManager;
    /**
     * ancho de la pantalla del móvil en que se esta ejecutando la aplicación
     */
    private int anchoPantalla = 1;
    /**
     * alto de la pantalla del móvil en que se esta ejecutando la aplicación
     */
    private int altoPantalla = 1;
    /**
     * volumen de la música
     */
    private int v;
    /**
     * booleano que indica si se esta jugando
     */
    boolean jugando = false;


    /***
     * Constructor de la clase
     * @param context Contexto de la aplicación
     */
    public Inicio(Context context) {
        super(context);
        this.context = context;
        this.surfaceHolder = getHolder();
        this.surfaceHolder.addCallback(this);
        hilo = new Hilo();
        setFocusable(true);
        // creoMusica();

    }

    /***
     * Funcion que inicializa la música de la aplicacion
     */
    /*protected void creoMusica() {
        audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        v = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        //mediaPlayer = MediaPlayer.create(context, R.raw.soundtrack);
        mediaPlayer.setVolume(v / 2, v / 2);
        //musicaJuego = MediaPlayer.create(context, R.raw.jugar);
        musicaJuego.setVolume(v / 2, v / 2);
    }*/

    /***
     * Registra las pulsaciones en pantalla
     * @param event Tipo de pulsación
     * @return numero de la escena actual
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        nuevaEscena = escenaActual.onTouchEvent(event);
        //mediaPlayer.pause();
        if (nuevaEscena != escenaActual.getNumEscena()) {
            switch (nuevaEscena) {
                case 1:
                    eleccionEscena(1);
                    break;
                case 2:
                    eleccionEscena(2);
                    break;
                case 3:
                    eleccionEscena(3);
                    break;
            }
            //audioManager.playSoundEffect(AudioManager.FX_KEYPRESS_SPACEBAR);
        }
        return true;
    }

    /***
     * funcion que se llama cuando la aplicación pasa a segundo plano
     */
    protected void enPausa() {
        funcionando = false;
        /*if(jugando)musicaJuego.pause();
        else mediaPlayer.pause();*/
        try {
            hilo.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * +
     * funcion que se llama cuando se vuelve a conectar con la aplicación
     */
    protected void vuelta() {
        funcionando = true;
        hilo = new Hilo();
        hilo.start();
    }


    /***
     * Funcion en la que se escogerá la escena de la aplicación
     * @param escena numero de escena a escoger
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    protected void eleccionEscena(int escena) {
        switch (escena) {
            case 1:
                jugando = false;
                escenaActual = new Principal(1, context, anchoPantalla, altoPantalla);
                break;
            case 2:
                jugando = true;
                escenaActual = new Jugar(2, context, anchoPantalla, altoPantalla);
                break;
            case 3:
                jugando = false;
                escenaActual = new Creditos(3, context, anchoPantalla, altoPantalla);
                break;
        }
    }


    /***
     * Función que se llama cuando se crea la surface
     * @param surfaceHolder surface creada
     */
    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {

    }

    /***
     * Función que se llama cuando se crea la surface
     * @param surfaceHolder surface del cambio
     * @param i pixeles que forman la surface
     * @param i1 alto de la pantalla donde se situa la surface
     * @param i2 alto de la pantalla donde se situa la surface
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {
        anchoPantalla = i1;
        altoPantalla = i2;
        eleccionEscena(1);
        if (!funcionando) {
            funcionando = true;
            if (hilo.getState() == Thread.State.NEW)
                hilo.start();
            if (hilo.getState() == Thread.State.TERMINATED) {
                hilo = new Hilo();
                hilo.start();
            }
        }
    }


    /***
     * Función que se llama cuando se destruye la surface
     * @param surfaceHolder surface a destruir
     */
    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        funcionando = false;
        try {
            hilo.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}


