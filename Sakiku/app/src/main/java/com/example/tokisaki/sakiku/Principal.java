package com.example.tokisaki.sakiku;

/**
 * Created by Tokisaki on 15/04/2018.
 */

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.MotionEvent;

import static com.example.tokisaki.sakiku.Inicio.jugando;

/***
 * Escena que será el menu principal del juego
 */
public class Principal extends Escenas {

    /**
     * pincel para las fuentes externas
     */
    private Paint s;
    /**
     * Pincel que pinta la pantalla de negro
     */
    private Paint start;
   /**
     * icono para jugar
     */
    private String jugar;
    /**
     * icono para creditos
     */
    private String creditos;
    /**
     *  icono para ayuda
     */
    private String ayuda;
    /**
     * Variable que recogerá "app_name" de strings.xml
     */
    private String titulo;
    /**
     * String con la palabra "si" mostrado como respuesta a la pregunta si deseas salir
     */
    private String si;
    /**
     * String con la palabra "no" mostrado como respuesta a la pregunta si deseas salir
     */
    private String nop;
    /**
     * icono para salir
     */
    private String exit;
    /**
     * String que se muestra cuando pulsas exit y te pregunta si desas salir
     */
    private String pregunta;
    /**
     * Rectangulo de pulsación para jugar
     */
    private Rect play;
    /**
     * Rectangulo de pulsación para creditos
     */
    private Rect cre;
    /**
     * Rectangulo de pulsación para ayuda
     */
    private Rect help;
    /**
     * rectangulo e colision para el icono de salir
     */
    private Rect salir;
    /**
     * rectangulo que se dibuja sobre toda la pantalla al pulsar el botón de salir
     */
    private Rect parar;
    /**
     * Boton para salir del juego una vez pulsado salir
     */
    private Rect yes;
    /**
     * botón para regresar al juego despues de haber pulsado salir
     */
    private Rect no;
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
     * indica si se esta intentando salir del juego
     */
    protected static boolean saliendo = false;
    /**
     * POsicion del titulo
     */
    private Point posTitulo;
    /**
     * Posicion de jugar
     */
    private Point posJugar;
    /**
     * Posicion de creditos
     */
    private Point posCreditos;
    /**
     * Posicion salir
     */
    private Point posSalir;
    /**
     * Posicion pregunta salir
     */
    private Point posPregunta;
    /**
     * Posicion si
     */
    private Point posSi;
    /**
     * Posicion no
     */
    private Point posNo;
    /**
     * icono para configuración
     */
    private String configuracion;
    /**
     * Rectangulo de pulsación para configuración
     */
    private Rect confi;
    /**
     * Posicion ayuda
     */
    private Point posAyuda;
    /**
     * Posicion configuracion
     */
    private Point posConfi;
    /**
     * posicion lateral del boton de configuracion
     */
    private int posLatConfi_l;
    /**
     * posicion lateral del boton de configuracion
     */
    private int posLatConfi_r;
    /**
     * icono para configuración
     */
    private String personaje;
    /**
     * Rectangulo de pulsación para configuración
     */
    private Rect perso;
    /**
     * Posicion configuracion
     */
    private Point posPerso;
    /**
     * posicion lateral del boton de configuracion
     */
    private int posLatPerso_l;
    /**
     * posicion lateral del boton de configuracion
     */
    private int posLatPerso_r;
    /**
     * icono para records
     */
    private String records;
    /**
     * Rectangulo de pulsación para records
     */
    private Rect reco;
    /**
     * Posicion records
     */
    private Point posReco;
    /**
     * posicion lateral del boton de records
     */
    private int posLatReco_l;
    /**
     * posicion lateral del boton de records
     */
    private int posLatReco_r;


    /***
     * Constructor de la clase
     * @param numEscena numero de la escena
     * @param context contexto de la aplicación
     * @param anchoPantalla ancho de la pantalla del dispositivo donde se ejecita la aplicación
     * @param altoPantalla alto de la pantalla del dispositivo donde se ejecita la aplicación
     * @param info Instancia de la clase Info
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public Principal(int numEscena, Context context, int anchoPantalla, int altoPantalla, Info info) {
        super(numEscena, context, anchoPantalla, altoPantalla,info);
        int size = getPixels(50);
        posBx = posBf = poxBy = altoPantalla / 2 - getPixels(75);
        posBx = anchoPantalla / 2 - getPixels(25);
        posBf = anchoPantalla / 2 + getPixels(25);
        inicializarRects();
        if(info.musica) {
            if (jugando) Inicio.musicaJuego.stop();
            Inicio.mediaPlayer.start();
        }

    }

    /***
     * funcion que inicializa los iconos
     */
    private void inicializarRects() {
        titulo = context.getResources().getString(R.string.app_name);
        jugar = context.getResources().getString(R.string.play);
        creditos = context.getResources().getString(R.string.creditos);
        ayuda = context.getResources().getString(R.string.help);
        exit = context.getResources().getString(R.string.salir);
        configuracion = context.getResources().getString(R.string.configuracion);
        personaje = context.getResources().getString(R.string.character);
        records = context.getResources().getString(R.string.records);
        play = new Rect(posBx, poxBy, posBf, poxBy + getPixels(50));
        posLatReco_l = posBx + getPixels(100) * 2;
        posLatReco_r = posBf + getPixels(100) * 2;
        reco = new Rect(posLatReco_l,poxBy,posLatReco_r,poxBy+getPixels(50));
        poxBy += getPixels(40) * 2;
        cre = new Rect(posBx, poxBy, posBf, poxBy + getPixels(50));
        poxBy += getPixels(40) * 2;
        help = new Rect(posBx,poxBy,posBf,poxBy+getPixels(50));
        posLatConfi_l = posBx - getPixels(100) * 2;
        posLatConfi_r = posBf - getPixels(100) * 2;
        confi = new Rect(posLatConfi_l,poxBy,posLatConfi_r,poxBy+getPixels(50));
        posLatPerso_l = posBx + getPixels(100) * 2;
        posLatPerso_r = posBf + getPixels(100) * 2;
        perso = new Rect(posLatPerso_l,poxBy,posLatPerso_r,poxBy+getPixels(50));
        salir = new Rect(0, 0, getPixels(50), getPixels(50));
        parar = new Rect(0, 0, anchoPantalla, altoPantalla);
        yes = new Rect(anchoPantalla / 2 - getPixels(100), altoPantalla / 2, anchoPantalla / 2 - getPixels(25), altoPantalla / 2 + getPixels(50));
        no = new Rect(anchoPantalla / 2 + getPixels(25), altoPantalla / 2, anchoPantalla / 2 + getPixels(100), altoPantalla / 2 + getPixels(50));
        pregunta = context.getResources().getString(R.string.preguntaSalir);
        si = context.getResources().getString(R.string.si);
        nop = context.getResources().getString(R.string.no);
        start = new Paint();
        start.setColor(Color.BLACK);
        p = new Paint();
        p.setColor(Color.RED);
        p.setTextSize(play.height());
        p.setTypeface(faw);
        l = new Paint();
        l.setColor(Color.RED);
        l.setTextSize(getPixels(50));
        l.setTypeface(letras);
        s = new Paint();
        s.setColor(Color.RED);
        s.setTextSize(getPixels(30));
        s.setTypeface(letras);

        posTitulo = new Point(anchoPantalla / 2 - getPixels(75), getPixels(75));
        posJugar = new Point(play.left + getPixels(3), play.bottom - getPixels(8));
        posCreditos = new Point(cre.left, cre.bottom - getPixels(8));
        posAyuda = new Point(help.left, help.bottom - getPixels(8));
        posConfi = new Point(confi.left, confi.bottom - getPixels(8));
        posPerso = new Point(perso.left, perso.bottom - getPixels(8));
        posReco = new Point(reco.left, reco.bottom - getPixels(8));
        posSalir = new Point(salir.left + getPixels(5), salir.bottom - getPixels(8));
        posPregunta = new Point(anchoPantalla / 3, getPixels(100));
        posSi = new Point(yes.left + yes.width() / 3, yes.bottom - getPixels(10));
        posNo = new Point(no.left + yes.width() / 4, no.bottom - getPixels(10));
    }

    /***
     * Dibujamos los elementos en pantalla
     * @param c Lienzo sobre el que dibujar
     */
    public void dibujar(Canvas c) {
        c.drawColor(Color.BLACK);
        if (!saliendo) {
            c.drawBitmap(fondo, 0, 0, null);
            c.drawText(titulo, posTitulo.x, posTitulo.y, l);
            c.drawText(jugar, posJugar.x, posJugar.y, p);
            c.drawText(creditos, posCreditos.x, posCreditos.y, p);
            c.drawText(ayuda, posAyuda.x, posAyuda.y, p);
            c.drawText(configuracion, posConfi.x, posConfi.y, p);
            c.drawText(personaje, posPerso.x, posPerso.y, p);
            c.drawText(records, posReco.x, posReco.y, p);
            c.drawText(exit, posSalir.x, posSalir.y, p);
        } else {
            c.drawRect(parar, start);
            c.drawText(pregunta, posPregunta.x, posPregunta.y, s);
            c.drawRect(yes, pBoton);
            c.drawText(si, posSi.x, posSi.y, s);
            c.drawRect(no, pBoton);
            c.drawText(nop, posNo.x, posNo.y, s);
        }
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
                if (!saliendo) {
                    if (play.contains(x, y)) {
                        return 2;
                    } else if (cre.contains(x, y)) {
                        return 3;
                    }else if(confi.contains((int) event.getX(), (int) event.getY())) {
                        return 4;
                    }else if (help.contains((int) event.getX(), (int) event.getY())) {
                        return 5;
                    }else if (perso.contains((int) event.getX(), (int) event.getY())) {
                        return 6;
                    }else if (reco.contains((int) event.getX(), (int) event.getY())) {
                        return 7;
                    } else if (salir.contains(x, y)) {
                        saliendo = true;
                    }
                } else {
                    if (yes.contains(x, y)) {
                        System.exit(0);
                    } else {
                        saliendo = false;
                    }
                }
        }
        return numEscena;
    }
}

