package com.example.tokisaki.sakiku;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

/***
 * Clase que inicia la aplicacion
 */
public class MainActivity extends AppCompatActivity {

    /**
     * instancia de la clase pantalla
     */
    private Inicio pantalla;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        pantalla = new Inicio(this);
        getSupportActionBar().hide();
        pantalla.setKeepScreenOn(true);
        setContentView(pantalla);
    }


    /***
     * Función que se llama cuando la aplicación queda en segundo plano
     */
    @Override
    protected void onPause() {
        super.onPause();
        pantalla.enPausa();
    }

    /***
     * Función que se llama cuando la aplicación vuelve del segundo plano
     */
    @Override
    protected void onResume() {
        super.onResume();
        pantalla.vuelta();
    }

    /**
     * funcion que se llama al pulsar el boton de retroceso de tu dispositivo
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBackPressed() {
        if (pantalla.escenaActual.getNumEscena() == 2){

        }else if(pantalla.escenaActual.getNumEscena() == 1){
            Principal.saliendo = true;
        }else{
            pantalla.eleccionEscena(1);
        }
    }
}


