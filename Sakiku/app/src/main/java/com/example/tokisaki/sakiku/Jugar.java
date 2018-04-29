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
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.media.AudioManager;
import android.os.Build;
import android.os.Vibrator;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.MotionEvent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Random;

import static android.content.Context.VIBRATOR_SERVICE;

/***
 * Clase que ejecuta los elementos relacionados con el juego , así como el juego en sí
 */
public class Jugar extends Escenas {

    /**
     * instancia de la clase Personaje
     */
    private Personaje corredor;
    /**
     * indica el intervalo de tiempo en el que se actualizan los frames
     */
    private long tiempoMove;
    /**
     * lista con los obstaculos que hay en pantalla
     */
    private ArrayList<Obstaculos> obstaculos;

    private int tickDisparoFrame = 40; // tiempo de refresco de pantalla para los frames del disparo del personaje
    private int puntuacion = 0; // puntuación conseguida

    /**
     * tiempo de refresco de pantalla para el movimento sobre el eje X del personaje
     */
    private int tickMove = 30;
    private int v; // volumen de la música
    private int pasear = 0; // numero que calcula cada cuantos frames suena el sonido de las pisadas
    /**
     * String que se muestra una vez que acaba la carrera
     */
    private String fin;
    /**
     * Instancia de la clase obstaculos
     */
    private Obstaculos obs;
    /**
     * booleana que indica si el personaje esta saltando
     */
    private boolean salto;
    /**
     * booleana que indica si el personaje se esta deslizando
     */
    private boolean desliz;
    /**
     * booleana que indica si se ha finalizado la carrera
     */
    private boolean finCarrera = false;
    /**
     * contador que los contiene 60 segundos que dura la carrera
     */
    private int contador = 60;
    /**
     * contador que le resta una unidad cada segundo a la variable "contador"
     */
    private long segundo;
    /**
     * Posicion del temporizador del juego
     */
    private Point posContador;
    /**
     * Posicion del boton de disparo
     */
    private Point posDisparo;
    /**
     * Posicion del texto fin
     */
    private Point posTextoFin;
    /**
     * Fondo de la pantalla
     */
    private Parallax parallax;
    /**
     * Boton para saltar
     */
    private ImageButton btnSalto;
    /**
     * Boton para deslizarse
     */
    private ImageButton btnDesliz;
    /**
     * Boton de disparo
     */
    private ImageButton btnDisparo;
    /**
     * Boton de disparo dos
     */
    private ImageButton btnDisparoDos;

    /***
     * Constructor de la clase
     * @param numEscena numero de la escena
     * @param context contexto de la aplicación
     * @param anchoPantalla ancho de la pantalla del dispositivo donde se ejecita la aplicación
     * @param altoPantalla alto de la pantalla del dispositivo donde se ejecita la aplicación
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public Jugar(int numEscena, Context context, int anchoPantalla, int altoPantalla) {
        super(numEscena, context, anchoPantalla, altoPantalla);
        inicializar();
        obstaculos = new ArrayList<>();
        corredor = new Personaje(context, altoPantalla, anchoPantalla);
        parallax = new Parallax(context, anchoPantalla, altoPantalla);
        tiempoMove = System.currentTimeMillis();
        segundo = System.currentTimeMillis() + 1000;
        //comenzo = true;
        //v= audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
    }

    /***
     * Inicializa los elementos necesarios para la clase
     */
    private void inicializar() {
        fin = context.getResources().getString(R.string.Final);
        p = new Paint();
        p.setColor(Color.RED);
        p.setTextSize(getPixels(50));
        p.setTypeface(faw);
        l = new Paint();
        l.setColor(Color.RED);
        l.setTextSize(getPixels(30));
        l.setTypeface(letras);

        btnDisparo = new ImageButton(escalaAnchura(BitmapFactory.decodeResource(context.getResources(), R.drawable.senal), getPixels(80)),
                new Point(anchoPantalla - escalaAnchura(BitmapFactory.decodeResource(context.getResources(), R.drawable.senal), getPixels(80)).getWidth(), 0));
        btnDisparoDos = new ImageButton(escalaAnchura(BitmapFactory.decodeResource(context.getResources(), R.drawable.lunafinal), getPixels(80)),
                new Point(anchoPantalla - btnDisparo.imagen.getHeight(), escalaAnchura(BitmapFactory.decodeResource(context.getResources(), R.drawable.lunafinal), getPixels(80)).getHeight()));
        btnDesliz = new ImageButton(escalaAnchura(BitmapFactory.decodeResource(context.getResources(), R.drawable.deslizarse), getPixels(80)),
                new Point(0, altoPantalla - escalaAnchura(BitmapFactory.decodeResource(context.getResources(), R.drawable.deslizarse), getPixels(80)).getHeight()));
        btnSalto = new ImageButton(escalaAnchura(BitmapFactory.decodeResource(context.getResources(), R.drawable.saltar), getPixels(80)),
                new Point(0, altoPantalla - btnDesliz.imagen.getHeight() * 2));
        posContador = new Point(getPixels(20), getPixels(30));
        posTextoFin = new Point(anchoPantalla / 2, altoPantalla / 2);
    }

    /***
     * Actualizamos la física de los elementos en pantalla
     */
    public void actualizarFisica() {
        if (contador <= 0) {
            finCarrera = true;
        }
        if (!finCarrera) {
            if (corredor.ganador(anchoPantalla)) {
                finCarrera = true;
            } else {
                parallax.actualizarFisica();
                corredor.movimiento(true);
                if (System.currentTimeMillis() > segundo) {
                    contador--;
                    segundo = System.currentTimeMillis() + 1000;
                }
                if (System.currentTimeMillis() - tiempoMove > tickMove) {
                    corredor.actualizarFisica();
                    tiempoMove = System.currentTimeMillis();
                }
                for (Obstaculos obs : obstaculos) {
                    if (obs.actualizarFisica()) {
                        obstaculos.remove(obs);
                        break;
                    }
                    if (obs.detectarColision(corredor)) {
                        obstaculos.remove(obs);
                        corredor.movimiento(false);
                        corredor.setRectangulos();
                    }
                }
            }
        }
    }

    /***
     * Dibujamos los elementos en pantalla
     * @param canvas Lienzo sobre el que dibujar
     */
    public void dibujar(Canvas canvas) {
        try {
            if (!finCarrera) {
                canvas.drawColor(Color.BLUE);
                parallax.dibujar(canvas);
                canvas.drawText("" + contador, posContador.x, posContador.y, l);
                btnDisparo.dibujar(canvas);
                btnDisparoDos.dibujar(canvas);
                corredor.dibujar(canvas);
                for (Obstaculos obs : obstaculos) {
                    obs.dibujar(canvas);
                }
                btnDesliz.dibujar(canvas);
                btnSalto.dibujar(canvas);
            } else {
                canvas.drawColor(Color.BLACK);
                canvas.drawText(fin, posTextoFin.x, posTextoFin.y, l);
            }
        } catch (Exception e) {
        }
    }

    /***
     * Registra las pulsaciones en pantalla
     * @param event Tipo de pulsación
     * @return numero de la escena actual
     */
    @Override
    public int onTouchEvent(MotionEvent event) {
        int accion = event.getActionMasked();
        int x = (int) event.getX(), y = (int) event.getY();
        switch (accion) {
            case MotionEvent.ACTION_DOWN:
                if (!finCarrera) {
                    if (btnDesliz.rButton.contains(x, y)) {
                        corredor.setEstado(eEstadoPersonaje.DESLIZANDOSE);
                    } else if (btnSalto.rButton.contains(x, y)) {
                        corredor.setEstado(eEstadoPersonaje.SALTANDO);
                    } else if (btnDisparo.rButton.contains(x, y)) {
                        obstaculos.add(new Obstaculo1(context, new PointF(anchoPantalla, altoPantalla - corredor.alturaPersonaje - getPixels(20))));
                    } else if (btnDisparoDos.rButton.contains(x, y)) {
                        obstaculos.add((new Obstaculo2(context, new PointF(anchoPantalla, altoPantalla - getPixels(40)))));
                    }
                } else {
                    return 1;
                }
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:
                break;
        }
        return numEscena;
    }
}



