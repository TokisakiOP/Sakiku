package com.example.tokisaki.sakiku;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.PointF;

/**
 * Created by Tokisaki on 29/04/2018.
 */

public class Obstaculo2 extends Obstaculos{

    /**
     * Constructor de la clase
     * @param context  Contexto de la aplicacion
     * @param posicion Posicion inicial
     */
    public Obstaculo2(Context context, PointF posicion) {
        super(context, posicion);
        frame = escalaAltura(BitmapFactory.decodeResource(context.getResources(), R.drawable.box), getPixels(40));
    }
}
