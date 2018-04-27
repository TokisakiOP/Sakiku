package com.example.tokisaki.sakiku.Modelos;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.PointF;

import com.example.tokisaki.sakiku.R;

public class BloqueHielo extends Obstaculo {

    /**
     * Ininicializa uns instancia de la clase Obstaculo segun parametros recibidos
     *
     * @param context  Contexto de la aplicacion
     * @param posicion Posicion inicial
     */
    public BloqueHielo(Context context, PointF posicion) {
        super(context, posicion);

        frameActual = escalaAltura(BitmapFactory.decodeResource(context.getResources(), R.drawable.box), getPixels(40));
    }
}
