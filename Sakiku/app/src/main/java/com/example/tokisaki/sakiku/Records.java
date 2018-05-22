package com.example.tokisaki.sakiku;

/**
 * Created by Tokisaki on 22/05/2018.
 */

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.MotionEvent;

/***
 * Escena que mostrará los 3 mejores resultados obtenidos en el juego
 */
public class Records extends Escenas{


    private String records; // Variable que recogerá "ayudaTexto" de strings.xml

    /***
     * Constructor de la clase
     * @param numEscena numero de la escena
     * @param context contexto de la aplicación
     * @param anchoPantalla ancho de la pantalla del dispositivo donde se ejecita la aplicación
     * @param altoPantalla alto de la pantalla del dispositivo donde se ejecita la aplicación
     * @param info Instancia de la clase Info
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public Records(int numEscena, Context context, int anchoPantalla, int altoPantalla, Info info) {
        super(numEscena, context, anchoPantalla, altoPantalla,info);
        inicializar();
    }

    /***
     * Inicializa los elementos a usar en la escena
     */
    private void inicializar(){
        p=new Paint();
        p.setColor(Color.RED);
        p.setTextSize(getPixels(50));
        p.setTypeface(faw);
        l=new Paint();
        l.setColor(Color.RED);
        l.setTextSize(getPixels(50));
        l.setTypeface(letras);
        records = context.getResources().getString(R.string.recordsTitulo);
    }

    /***
     * Dibujamos los elementos en pantalla
     * @param c Lienzo sobre el que dibujar
     */
    public void dibujar (Canvas c){
        c.drawColor(Color.BLACK);
        c.drawBitmap(fondo,0,0,null);
        c.drawText(records,anchoPantalla/2-getPixels(75),altoPantalla/2-getPixels(100),l);
        c.drawText("1º " + info.records.get(0),anchoPantalla/2-getPixels(50),altoPantalla/2-getPixels(25),l);
        c.drawText("3º " + info.records.get(2),anchoPantalla/2-getPixels(50),altoPantalla/2+getPixels(125),l);
        c.drawText("2º " + info.records.get(1),anchoPantalla/2-getPixels(50),altoPantalla/2+getPixels(50),l);
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
                return 1 ;
        }
        return numEscena;
    }
}

