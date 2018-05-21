package com.example.tokisaki.sakiku;

/**
 * Created by Tokisaki on 18/05/2018.
 */

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.MotionEvent;

/***
 * Escena que mostrará la configuración del juego
 */
public class Configuracion extends Escenas{

    /**
     * Variable que recogerá "musica" de strings.xml
     */
    private String musica;
    /**
     * Variable que recogerá "efectos" de strings.xml
     */
    private String efectos;
    /**
     * Variable que recogerá "vibracion" de strings.xml
     */
    private String vibracion;
    /**
     * icono de activo
     */
    private String onn;
    /**
     * icono de inactivo
     */
    private String off;
    /**
     * icono para salir
     */
    private String exit;
    /**
     * rectangulo e colision para el icono de vibrar
     */
    private Rect rVibrar;
    /**
     * rectangulo e colision para el icono de efectos
     */
    private Rect rEfectos;
    /**
     * rectangulo e colision para el icono de musica
     */
    private Rect rMusica;
    /**
     * rectangulo e colision para el icono de salir
     */
    private Rect salir;
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
     * Booleana que indica si ha habido algun cambio en la configuración
     */
    boolean cambio;

    /***
     * Constructor de la clase
     * @param numEscena numero de la escena
     * @param context contexto de la aplicación
     * @param anchoPantalla ancho de la pantalla del dispositivo donde se ejecita la aplicación
     * @param altoPantalla alto de la pantalla del dispositivo donde se ejecita la aplicación
     * @param info Instancia de la clase Info
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public Configuracion(int numEscena, Context context, int anchoPantalla, int altoPantalla, Info info) {
        super(numEscena, context, anchoPantalla, altoPantalla,info);
        cambio = false;
        inicializacion();
    }

    /***
     * Función que inicilaliza todos los elementos de la escena
     */
    private void inicializacion(){
        p=new Paint();
        p.setColor(Color.RED);
        p.setTextSize(getPixels(40));
        p.setTypeface(faw);
        l=new Paint();
        l.setColor(Color.RED);
        l.setTextSize(getPixels(40));
        l.setTypeface(letras);
        musica = context.getResources().getString(R.string.musica);
        efectos = context.getResources().getString(R.string.efectos);
        vibracion = context.getResources().getString(R.string.vibrar);
        onn = context.getResources().getString(R.string.sonido_on);
        off = context.getResources().getString(R.string.sonido_off);
        exit = context.getResources().getString(R.string.salir);
        posBx=posBf=poxBy= 0 + getPixels(30) ;
        posBx=anchoPantalla/2+getPixels(100);
        posBf=anchoPantalla/2+getPixels(150);
        rMusica = new Rect(posBx,poxBy,
                posBf,poxBy+getPixels(50));

        poxBy+=getPixels(50)*2;

        rEfectos = new Rect(posBx+0,poxBy+0,
                posBf+0,poxBy+getPixels(50));

        poxBy+=getPixels(50)*2;

        rVibrar = new Rect(posBx,poxBy,
                posBf,poxBy+getPixels(50));
        poxBy+=getPixels(50)*2;
        salir = new Rect(0,0,0+getPixels(50),0+getPixels(50));
    }

    /***
     * Dibujamos los elementos en pantalla
     * @param c Lienzo sobre el que dibujar
     */
    public void dibujar (Canvas c){
        c.drawBitmap(fondo,0,0,null);
        c.drawRect(rMusica,pBoton);
        c.drawRect(rEfectos,pBoton);
        c.drawRect(rVibrar,pBoton);
        c.drawRect(salir,pBoton);
        c.drawText(musica,0 + getPixels(100),rMusica.centerY()+getPixels(25),l);
        c.drawText(efectos,0 + getPixels(100),rEfectos.centerY()+getPixels(25),l);
        c.drawText(vibracion,0 + getPixels(100),rVibrar.centerY()+getPixels(25),l);
        c.drawText(exit, salir.centerX() - salir.width() / 2, salir.centerY() + salir.height() / 2, p);
        if(info.musica)c.drawText(onn,rMusica.centerX()-rMusica.width()/2,rMusica.centerY()+rMusica.height()/2,p);
        else c.drawText(off,rMusica.centerX()-rMusica.width()/2,rMusica.centerY()+rMusica.height()/2,p);
        if(info.efectos)c.drawText(onn,rEfectos.centerX()-rEfectos.width()/2,rEfectos.centerY()+rEfectos.height()/2,p);
        else c.drawText(off,rEfectos.centerX()-rEfectos.width()/2,rEfectos.centerY()+rEfectos.height()/2,p);
        if(info.vibrar)c.drawText(onn,rVibrar.centerX()-rVibrar.width()/2,rVibrar.centerY()+rVibrar.height()/2,p);
        else c.drawText(off,rVibrar.centerX()-rVibrar.width()/2,rVibrar.centerY()+rVibrar.height()/2,p);
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
                if(rMusica.contains((int)event.getX(),(int)event.getY())){
                    if(info.musica){
                        info.musica = false;
                        Inicio.mediaPlayer.pause();
                    } else{
                        info.musica = true;
                        Inicio.mediaPlayer.start();
                    }
                    cambio = true;
                }
                if(rVibrar.contains((int)event.getX(),(int)event.getY())){
                    if(info.vibrar) info.vibrar = false;
                    else info.vibrar = true;
                    cambio = true;
                }
                if(rEfectos.contains((int)event.getX(),(int)event.getY())){
                    if(info.efectos) info.efectos = false;
                    else info.efectos = true;
                    cambio = true;
                }
                if(salir.contains((int)event.getX(),(int)event.getY())){
                    if(cambio){
                        SharedPreferences preferencias = context.getSharedPreferences("saves",0);
                        SharedPreferences.Editor editor = preferencias.edit();
                        editor.putBoolean("efectos", info.efectos);
                        editor.putBoolean("musica", info.musica);
                        editor.putBoolean("vibrar", info.vibrar);
                        editor.apply();
                    }
                    return 1;
                }
        }
        return numEscena;
    }
}
