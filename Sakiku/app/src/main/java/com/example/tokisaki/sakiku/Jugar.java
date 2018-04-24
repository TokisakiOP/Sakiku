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
     * Fondo de pantalla
     */
    private Bitmap fondo;

    /**
     * imagen para el boton de saltar
     */
    private Bitmap btnSalto;

    /**
     * imagen para el boton de deslizarse
     */
    private Bitmap btnDesliz;

    /**
     * lista con los obstaculos que hay en pantalla
     */
    private ArrayList<Obstaculos> obstaculos; // lista con las balas que hay en pantalla

    /**
     * Posicion del fondo para hacer el efecto parallax
     */
    private int postI1;

    /**
     * Posicion del fondo para hacer el efecto parallax
     */
    private int postI2;
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
     * pincel para las fuentes externas
     */
    private Paint p;

    /**
     * pincel para las fuentes externas
     */
    private Paint l;

    private Obstaculos obs;
    private boolean salto;
    private boolean desliz;
    private boolean finCarrera = false;
    private int contador = 60;
    private long segundo;

    private Bitmap disparo; // imagen para el botón de disparar

    /**
     * Posicion del temporizador del juego
     */
    private Point posContador;

    /**
     * Posicion del boton de disparo
     */
    private Point posDisparo;

    /**
     * Posicion del boton de deslizarse
     */
    private Point posBotonDeslizar;

    /**
     * Posicion del boton de saltar
     */
    private Point posBotonSalto;

    /**
     * Posicion del texto fin
     */
    private Point posTextoFin;

    /**
     * Collider boton de salto
     */
    private Rect btnSaltoRect;

    /**
     * Collider boton deslizarse
     */
    private Rect btnDeslizarseRect;

    /**
     * Collider boton disparo
     */
    private Rect btnDisparoRect;

    /**
     * Velocidad a la que se mueve el fondo
     */
    private int velocidadFondo;


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
        postI1 = 0;
        postI2 = fondo.getWidth();
        corredor = new Personaje(context, altoPantalla, anchoPantalla);
        tiempoMove = System.currentTimeMillis();
        segundo = System.currentTimeMillis() + 1000;
        //comenzo = true;
        //v= audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
    }

    /***
     * Inicializa los elementos necesarios para la clase
     */
    private void inicializar() {
        btnSalto = BitmapFactory.decodeResource(context.getResources(), R.drawable.saltar);
        btnSalto = escalaAnchura(btnSalto, getPixels(80));
        btnDesliz = BitmapFactory.decodeResource(context.getResources(), R.drawable.deslizarse);
        btnDesliz = escalaAnchura(btnDesliz, getPixels(80));
        disparo = BitmapFactory.decodeResource(context.getResources(), R.drawable.senal);
        disparo = escalaAnchura(disparo, getPixels(80));
        fondo = BitmapFactory.decodeResource(context.getResources(), R.drawable.fondo);
        fondo = Bitmap.createScaledBitmap(fondo, anchoPantalla, altoPantalla, true);
        fin = context.getResources().getString(R.string.Final);
        velocidadFondo = getPixels(2);
        p = new Paint();
        p.setColor(Color.RED);
        p.setTextSize(getPixels(50));
        p.setTypeface(faw);
        l = new Paint();
        l.setColor(Color.RED);
        l.setTextSize(getPixels(30));
        l.setTypeface(letras);
        posDisparo = new Point(anchoPantalla - disparo.getWidth(), 0);
        posContador = new Point(getPixels(20), getPixels(30));
        posBotonDeslizar = new Point(0, altoPantalla - btnDesliz.getHeight());
        posBotonSalto = new Point(0, altoPantalla - (btnDesliz.getHeight() + btnSalto.getHeight()));
        posTextoFin = new Point(anchoPantalla / 2, altoPantalla / 2);
        btnSaltoRect = new Rect(posBotonSalto.x, posBotonSalto.y, posBotonSalto.x + btnSalto.getWidth(), posBotonSalto.y + btnSalto.getHeight());
        btnDeslizarseRect = new Rect(posBotonDeslizar.x, posBotonDeslizar.y, posBotonDeslizar.x + btnDesliz.getWidth(), posBotonDeslizar.y + btnDesliz.getHeight());
        btnDisparoRect = new Rect(posDisparo.x, posDisparo.y, posDisparo.x + disparo.getWidth(), posDisparo.y + disparo.getHeight());
    }

    /***
     * Función que realiza la deteccion de colisiones en el juego
     */
   /* private void detectaColision() {
        if (bolas.size() >= 1) {
            for (int i = flechas.size() - 1; i >= 0; i--) {
                for (int j = bolas.size() - 1; j >= 0; j--) {
                    if (flechas.get(i).getRectangulo().intersect(bolas.get(j).getRectangulo())) {
                        if (bolas.get(j) instanceof Bola1) {
                            Bola1 b1 = new Bola1(this.context, bolas.get(j).posicion.x - 50, bolas.get(j).posicion.y, anchoPantalla, altoPantalla, 2,multiplicador);
                            Bola1 b2 = new Bola1(this.context, bolas.get(j).posicion.x + 50, bolas.get(j).posicion.y, anchoPantalla, altoPantalla, 2,multiplicador);
                            b1.direccion = -1;
                            b2.direccion = 1;
                            dobles.add(b1);
                            dobles.add(b2);
                            bolas.remove(j);
                            if(info.efectos)efectos.play(sonidoExplosion,v,v,1,0,1 );
                            break;
                        }
                        if (bolas.get(j) instanceof Bola2) {
                            bolas.get(j).vida--;
                            if (bolas.get(j).vida >= 1) {
                                bolas.get(j).eleccionCara(2);
                                if(info.efectos)efectos.play(sonidoExplosion,v,v,1,0,1 );
                            } else {
                                bolas.remove(j);
                                puntuacion += 100;
                                if(info.efectos)efectos.play(sonidoExplosionFinal,v,v,1,0,1 );
                            }
                            flechas.remove(i);
                            break;
                        }
                        if (bolas.get(j) instanceof Bola3) {
                            bolas.remove(j);
                            flechas.remove(i);
                            puntuacion += 200;
                            if(info.efectos)efectos.play(sonidoExplosionFinal,v,v,1,0,1 );
                        }
                    }
                }
            }
            if (dobles.size() >= 1) {
                for (int i = flechas.size() - 1; i >= 0; i--) {
                    for (int j = dobles.size() - 1; j >= 0; j--) {
                        if (flechas.get(i).getRectangulo().intersect(dobles.get(j).getRectangulo())) {
                            if (dobles.get(j).vida > 1) {
                                Bola1 b1 = new Bola1(this.context, dobles.get(j).posicion.x - 50, dobles.get(j).posicion.y, anchoPantalla, altoPantalla, 1,multiplicador);
                                Bola1 b2 = new Bola1(this.context, dobles.get(j).posicion.x + 50, dobles.get(j).posicion.y, anchoPantalla, altoPantalla, 1,multiplicador);
                                b1.direccion = -1;
                                b2.direccion = 1;
                                dobles.add(b1);
                                dobles.add(b2);
                                dobles.remove(j);
                                flechas.remove(i);
                                if(info.efectos)efectos.play(sonidoExplosion,v,v,1,0,1 );
                            } else {
                                dobles.remove(j);
                                flechas.remove(i);
                                if(info.efectos)efectos.play(sonidoExplosionFinal,v,v,1,0,1 );
                            }
                            break;
                        }
                    }
                }
            }

            for (Bolas b : bolas){
                if(b.getRectangulo().intersect(robot.getRectangulos()[0]) || b.getRectangulo().intersect(robot.getRectangulos()[1])){
                    muertePersonaje();
                    break;
                }
            }
            for (Bola1 b : dobles){
                if(b.getRectangulo().intersect(robot.getRectangulos()[0]) || b.getRectangulo().intersect(robot.getRectangulos()[1])){
                    muertePersonaje();
                    break;
                }
            }
        }
    }*/

    /***
     * Actualizamos la física de los elementos en pantalla
     */
    public void actualizarFisica() {
        if (contador <= 0) {
            finCarrera = true;
        }
        if (!finCarrera) {
            postI1 -= velocidadFondo;
            postI2 -= velocidadFondo;
            if (postI1 + fondo.getWidth() < 0) {
                postI1 = postI2 + fondo.getWidth();
            }

            if (postI2 + fondo.getWidth() < 0) {
                postI2 = postI1 + fondo.getWidth();
            }
            if (System.currentTimeMillis() > segundo) {
                contador--;
                segundo = System.currentTimeMillis() + 1000;
            }
            if (System.currentTimeMillis() - tiempoMove > tickMove) {
                if (salto) {
                    corredor.actualizarFisica(eEstadoPersonaje.SALTANDO);
                    if (corredor.numFrame == corredor.movimientoSalto.length - 1) {
                        salto = false;
                        corredor.suelo = corredor.fijo;
                    }
                } else if (desliz) {
                    corredor.actualizarFisica(eEstadoPersonaje.DESLIZANDOSE);
                    if (corredor.numFrame == corredor.movimientoDesliz.length - 1) {
                        desliz = false;
                    }
                } else {
                    corredor.actualizarFisica(eEstadoPersonaje.CORRIENDO);
                }
                tiempoMove = System.currentTimeMillis();
            }

            for (Obstaculos obs : obstaculos) {
                if (obs.actualizarFisica(anchoPantalla)) {
                    obstaculos.remove(obs);
                    //Aqui por leer en orden fuerza a esperar a la siguiente ejecucion para seguir trabajando con el resto
                    //si en vez de 50 fps , fueses a 1 , verias el resto de disparos quietos 1 segundo entero :S
                    break;
                }
                if (obs.detectarColision(corredor)) {
                    corredor.setRectangulos(corredor.estado);
                    finCarrera=true;
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
                canvas.drawBitmap(fondo, postI1, 0, null);
                canvas.drawBitmap(fondo, postI2, 0, null);
                canvas.drawText("" + contador, posContador.x, posContador.y, l);

                canvas.drawBitmap(disparo, posDisparo.x, posDisparo.y, null);

                if (salto) {
                    corredor.dibujar(canvas, "salto");
                } else if (desliz) {
                    corredor.dibujar(canvas, "desliz");
                } else {
                    corredor.dibujar(canvas, "run");
                }
                canvas.drawBitmap(btnDesliz, posBotonDeslizar.x, posBotonDeslizar.y, null);
                canvas.drawBitmap(btnSalto, posBotonSalto.x, posBotonSalto.y, null);
                for (Obstaculos obs : obstaculos) {
                    obs.dibujar(canvas);
                }
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
                    if (btnDeslizarseRect.contains(x, y)) {
                        desliz = true;
                        corredor.numFrame = 0;
                    } else if (btnSaltoRect.contains(x, y)) {
                        salto = true;
                        corredor.numFrame = 0;
                    } else if (btnDisparoRect.contains(x, y)) {
                        obstaculos.add(new Obstaculos(context, new PointF(anchoPantalla, altoPantalla - corredor.movimientoRun[0].getHeight())));
                    }
                } else {
                    return 1;
                }
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                /*
                    if (event.getPointerCount() > 0) {
                        for (int i = 0; i < event.getPointerCount(); i++) {
                            if (i > 0) {
                                float x2 = event.getX(i);
                                float y2 = event.getY(i);
                                if (x2 > anchoPantalla - disparo.getWidth() && x < anchoPantalla &&
                                        y2 > altoPantalla - disparo.getHeight()) {
                                    avance = false;
                                    retroceso = false;
                                    dispararLat = true;
                                    robot.numFrame = 0;
                                }
                            }
                        }
                }*/
                break;
            case MotionEvent.ACTION_MOVE:
                   /* for (int i = 0; i < event.getPointerCount(); i++) {
                        x = event.getX(i);
                        y = event.getY(i);
                        if (retroceso) {
                            if (x < 0 || x > btnRetrocede.getWidth() || y < altoPantalla - btnRetrocede.getHeight()) {
                                retroceso = false;
                                derecha = false;
                            }
                        } else if (avance) {
                            if (x < btnRetrocede.getWidth() || x > btnRetrocede.getWidth() + btnAvz.getWidth()
                                    || y < altoPantalla - btnRetrocede.getHeight()) {
                                avance = false;
                                derecha = true;
                            }
                        } else if (!avance || !retroceso) {
                            if (x > 0 && x < btnRetrocede.getWidth() && y > altoPantalla - btnRetrocede.getHeight()) {
                                retroceso = true;
                                derecha = false;
                            }
                            if (x > btnRetrocede.getWidth() && x < btnRetrocede.getWidth() + btnAvz.getWidth()
                                    && y > altoPantalla - btnRetrocede.getHeight()) {
                                derecha = false;
                                avance = true;
                            }

                        } else if (!retroceso) {
                            if (x > btnRetrocede.getWidth() && x < btnRetrocede.getWidth() + btnAvz.getWidth()
                                    && y > altoPantalla - btnAvz.getHeight()) {
                                avance = true;
                            }
                        }
                    }*/

                break;
            case MotionEvent.ACTION_UP:

                    /*if (avance) {
                        derecha = true;
                    } else if (retroceso) {
                        derecha = false;
                    }
                    avance = false;
                    retroceso = false;*/

                break;
        }
        return numEscena;
    }

}



