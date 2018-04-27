package com.example.tokisaki.sakiku.Escenas;

/**
 * Created by Tokisaki on 15/04/2018.
 */

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PointF;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.MotionEvent;

import com.example.tokisaki.sakiku.UI.BotonImagen;
import com.example.tokisaki.sakiku.Modelos.FondoAnimado;
import com.example.tokisaki.sakiku.Modelos.Obstaculos;
import com.example.tokisaki.sakiku.Modelos.Personaje;
import com.example.tokisaki.sakiku.R;
import com.example.tokisaki.sakiku.Enumerados.eEstadoPersonaje;

import java.util.ArrayList;

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

    /**
     * tiempo de refresco de pantalla para los frames del disparo del personaje
     */
    private int tickDisparoFrame = 40;

    /**
     * puntuación conseguida
     */
    private int puntuacion = 0;

    /**
     * tiempo de refresco de pantalla para el movimento sobre el eje X del personaje
     */
    private int tickMove = 30;

    /**
     * volumen de la música
     */
    private int v;

    /**
     * numero que calcula cada cuantos frames suena el sonido de las pisadas
     */
    private int pasear = 0;

    /**
     * String que se muestra una vez que acaba la carrera
     */
    private String fin;

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
     * Posicion del texto fin
     */
    private Point posTextoFin;

    /**
     * Fondo de la pantalla
     */
    private FondoAnimado fondoAnimado;

    /**
     * BotonImagen para saltar
     */
    private BotonImagen btnSalto;

    /**
     * BotonImagen para deslizarse
     */
    private BotonImagen btnDesliz;

    /**
     * BotonImagen de disparo
     */
    private BotonImagen btnDisparo;

    /**
     * BotonImagen de disparo dos
     */
    private BotonImagen btnDisparoDos;

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
        tiempoMove = System.currentTimeMillis();
        segundo = System.currentTimeMillis() + 1000;
        fondoAnimado = new FondoAnimado(context, anchoPantalla, altoPantalla);
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

        btnDisparo = new BotonImagen(escalaAnchura(BitmapFactory.decodeResource(context.getResources(), R.drawable.senal), getPixels(80)),
                new Point(anchoPantalla - escalaAnchura(BitmapFactory.decodeResource(context.getResources(), R.drawable.senal), getPixels(80)).getWidth(), 0));
        btnDisparoDos = new BotonImagen(escalaAnchura(BitmapFactory.decodeResource(context.getResources(), R.drawable.lunafinal), getPixels(80)),
                new Point(anchoPantalla - btnDisparo.imagen.getHeight(), escalaAnchura(BitmapFactory.decodeResource(context.getResources(), R.drawable.lunafinal), getPixels(80)).getHeight()));
        btnDesliz = new BotonImagen(escalaAnchura(BitmapFactory.decodeResource(context.getResources(), R.drawable.deslizarse), getPixels(80)),
                new Point(0, altoPantalla - escalaAnchura(BitmapFactory.decodeResource(context.getResources(), R.drawable.deslizarse), getPixels(80)).getHeight()));
        btnSalto = new BotonImagen(escalaAnchura(BitmapFactory.decodeResource(context.getResources(), R.drawable.saltar), getPixels(80)),
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
            if (corredor.comprobarVictoria(anchoPantalla)) {
                finCarrera = true;
            } else {
                fondoAnimado.actualizarFisica();
                corredor.desplazarse(true);
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
                        //Aqui por leer en orden fuerza a esperar a la siguiente ejecucion para seguir trabajando con el resto
                        //si en vez de 50 fps , fueses a 1 , verias el resto de disparos quietos 1 segundo entero :S
                        break;
                    }
                    if (obs.detectarColision(corredor)) {
                        obstaculos.remove(obs);
                        corredor.desplazarse(false);
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
        if (!finCarrera) {
            canvas.drawColor(Color.BLUE);
            fondoAnimado.dibujar(canvas);
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
    }

    /***
     * Registra las pulsaciones en pantalla
     * @param event Tipo de pulsación
     * @return numero de la escena actual
     */
    @Override
    public int onTouchEvent(MotionEvent event) {
        int x = (int) event.getX(), y = (int) event.getY();
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                if (!finCarrera) {
                    if (btnDesliz.collider.contains(x, y)) {
                        corredor.setEstado(eEstadoPersonaje.DESLIZANDOSE);
                    } else if (btnSalto.collider.contains(x, y)) {
                        corredor.setEstado(eEstadoPersonaje.SALTANDO);
                    } else if (btnDisparo.collider.contains(x, y)) {
                        obstaculos.add(new Obstaculos(context, new PointF(anchoPantalla, altoPantalla - corredor.getTamañoDePìe()), true));
                    } else if (btnDisparoDos.collider.contains(x, y)) {
                        obstaculos.add(new Obstaculos(context, new PointF(anchoPantalla, altoPantalla - getPixels(40)), false));
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



