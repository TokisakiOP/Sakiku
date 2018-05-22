package com.example.tokisaki.sakiku;

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
import android.media.AudioManager;
import android.os.Build;
import android.os.Vibrator;
import android.support.annotation.RequiresApi;
import android.view.MotionEvent;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

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
    protected ArrayList<Obstaculos> obstaculos;
    /**
     * puntuación conseguida
     */
    private int puntuacion;
    /**
     * tiempo de refresco de pantalla para el movimento sobre el eje X del personaje
     */
    private int tickMove = 30;
    /**
     * tiempo de refresco de pantalla para el movimento sobre el eje X del personaje
     */
    private int tickAccion = 100;
    /**
     * volumen de la música
     */
    private int v;
    /**
     * numero que calcula cada cuantos frames suena el sonido de las pisadas
     */
    private int pasear;
    /**
     * String que se muestra una vez que acaba la carrera
     */
    private String fin;

    /**
     * booleana que indica si se ha finalizado la carrera
     */
    protected boolean finCarrera;
    /**
     * contador que los contiene 60 segundos que dura la carrera
     */
    private int contador;
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
     * indica si se esta conectado o no al servidor
     */
    protected boolean conectado;
    /**
     * indica si se esta en la clase juego
     */
    protected boolean jugar;
    /**
     * hilo que se va a conectar con el servidor
     */
    protected Cliente myATaskYW;
    /**
     * booleano que indicara si se ha podido encortrar el servidor
     */
    boolean noEncuentra;
    /**
     * booleana que indica si se ha batido un record
     */
    private boolean record;
    /**
     * parametro que ordena de manera inversa una lista
     */
    private Comparator<Integer> comparador = Collections.reverseOrder();
    /**
     * Variable que recogerá "noserver" de strings.xml
     */
    private String noServer;
    /**
     * Variable que recogerá "conectando" de strings.xml
     */
    private String conectando;
    /**
     * Variable que recogerá "primera vez" de strings.xml
     */
    private String primeraVez;

    /***
     * Constructor de la clase
     * @param numEscena numero de la escena
     * @param context contexto de la aplicación
     * @param anchoPantalla ancho de la pantalla del dispositivo donde se ejecita la aplicación
     * @param altoPantalla alto de la pantalla del dispositivo donde se ejecita la aplicación
     * @param info Instancia de la clase Info
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public Jugar(int numEscena, Context context, int anchoPantalla, int altoPantalla,Info info) {
        super(numEscena, context, anchoPantalla, altoPantalla,info);
        conectado = false;
        jugar = true;
        noEncuentra = false;
        record = false;
        puntuacion = 3000;
        inicializar();
        if(info.musica) {
            Inicio.mediaPlayer.stop();
            Inicio.musicaJuego.start();
        }
        corredor = new Personaje(context, altoPantalla, anchoPantalla,info);
        parallax = new Parallax(context, anchoPantalla, altoPantalla);
        tiempoMove = System.currentTimeMillis();
        segundo = System.currentTimeMillis() + 1000;
        contador = 30;
        finCarrera = false;
        obstaculos = new ArrayList<>();
        pasear = 0;
        myATaskYW = new Cliente(context,this,info);
        myATaskYW.execute("conectado");
        v= audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
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
        noServer = context.getResources().getString(R.string.noserver);
        conectando = context.getResources().getString(R.string.conectando);
        primeraVez = context.getResources().getString(R.string.primeravez);
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
        if(conectado) {
            if (contador <= 0) {
                finCarrera = true;
                if(!record){
                    info.records.add(puntuacion);
                    Collections.sort(info.records,comparador);
                    record = true;
                }
            }
            if (!finCarrera) {
                if (corredor.ganador(anchoPantalla)) {
                    finCarrera = true;
                } else {
                    parallax.actualizarFisica();
                    if (System.currentTimeMillis() > segundo) {
                        contador--;
                        segundo = System.currentTimeMillis() + 1000;
                    }
                    if (System.currentTimeMillis() - tiempoMove > tickMove && corredor.estado == eEstadoPersonaje.CORRIENDO) {
                        corredor.actualizarFisica();
                        tiempoMove = System.currentTimeMillis();
                        corredor.movimiento(true,false);
                        pasear++;
                        if (pasear % 4 == 0){
                            if(info.efectos) efectos.play(pasos, v, v, 1, 0, 1);
                        }
                    }else if (System.currentTimeMillis() - tiempoMove > tickAccion && (corredor.estado == eEstadoPersonaje.DESLIZANDOSE
                            || corredor.estado == eEstadoPersonaje.SALTANDO)){
                        if(corredor.estado == eEstadoPersonaje.SALTANDO){
                            corredor.movimiento(true,true);
                        }
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
                            corredor.movimiento(false,false);
                            corredor.setRectangulos();
                            if(info.efectos)efectos.play(golpe, v, v, 1, 0, 1);
                            if(info.vibrar) {
                                Vibrator mVibrator = (Vibrator) context.getSystemService(VIBRATOR_SERVICE);
                                mVibrator.vibrate(300);
                            }
                            puntuacion -= 100;

                        }
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
            if(conectado) {
                if (!finCarrera) {
                    canvas.drawColor(Color.BLUE);
                    parallax.dibujar(canvas);
                    canvas.drawText("" + contador, posContador.x, posContador.y, l);
                    corredor.dibujar(canvas);
                    canvas.drawText(""+puntuacion, anchoPantalla - getPixels(100),0 + getPixels(30), l);
                    for (Obstaculos obs : obstaculos) {
                        obs.dibujar(canvas);
                    }
                    btnDesliz.dibujar(canvas);
                    btnSalto.dibujar(canvas);
                } else {
                    canvas.drawColor(Color.BLACK);
                    canvas.drawText(fin, posTextoFin.x, posTextoFin.y, l);
                }
            }else{
                if(noEncuentra){
                    canvas.drawColor(Color.BLACK);
                    canvas.drawText(noServer, posTextoFin.x, posTextoFin.y, l);
                }else {
                    canvas.drawColor(Color.BLACK);
                    canvas.drawText(conectando, 0 +getPixels(30), posTextoFin.y - getPixels(20), l);
                    canvas.drawText(primeraVez, 0 +getPixels(30), posTextoFin.y + getPixels(20), l);
                }
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
                if(conectado) {
                    if (!finCarrera) {
                        if (btnDesliz.rButton.contains(x, y)) {
                            corredor.setEstado(eEstadoPersonaje.DESLIZANDOSE);
                            if(info.efectos)efectos.play(sonidoDesliz, v, v, 1, 0, 1);
                        } else if (btnSalto.rButton.contains(x, y)) {
                            corredor.setEstado(eEstadoPersonaje.SALTANDO);
                            if(info.efectos)efectos.play(sonidoSalto, v, v, 1, 0, 1);
                        }
                    } else {
                        if(info.musica) {
                            Inicio.musicaJuego.stop();
                            Inicio.mediaPlayer.start();
                        }
                        jugar = false;
                        myATaskYW.cancel(true);
                        return 1;
                    }
                }else {
                    if(info.musica) {
                        Inicio.musicaJuego.stop();
                        Inicio.mediaPlayer.start();
                    }
                    jugar = false;
                    myATaskYW.cancel(true);
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

    protected void crearObstaculos(String obstaculo){
        if(obstaculo.equals("bloque")){
            obstaculos.add((new Obstaculo2(context, new PointF(anchoPantalla, altoPantalla - getPixels(40)))));
            if(info.efectos)efectos.play(sonidoHielo,v,v,1,0,1 );
        }else if(obstaculo.equals("bola")){
            obstaculos.add(new Obstaculo1(context, new PointF(anchoPantalla, altoPantalla - corredor.alturaPersonaje - getPixels(20))));
            if(info.efectos)efectos.play(sonidoFuego,v,v,1,0,1 );
        }
    }

}



