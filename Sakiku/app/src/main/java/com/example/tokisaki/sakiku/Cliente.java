package com.example.tokisaki.sakiku;


/**
 * Created by Tokisaki on 30/04/2018.
 */

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;

import org.apache.commons.io.IOUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.Socket;
import java.nio.charset.StandardCharsets;


/**
 * Clase para interactuar con el servidor
 * */
public class Cliente extends AsyncTask<String,Void,String> {

    /**
     * Puerto
     * */
    private static final int SERVERPORT = 4000;
    /**
     * HOST
     * */
    private static final String ADDRESS = "192.168.0.22";

        Escenas j;

        Jugar juego;

        Context context;

        public Cliente(Context context,Jugar j){
            juego =  j;
            this.context = context;
        }

        /**
         * Se conecta al servidor y trata resultado
         * */
        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        protected String doInBackground(String... values){

            try {
                //Se conecta al servidor
                InetAddress serverAddr = InetAddress.getByName(ADDRESS);
                Socket socket = new Socket(serverAddr, SERVERPORT);
                PrintStream output = new PrintStream(socket.getOutputStream());
                String request = values[0];
                output.println(request);
                String received = null;
                InputStream stream = null;
                Log.i("I/TCP Client", "Received data to server");
                while(juego.finCarrera == false) {
                    Log.i("prueba","@@@@@@@@@@@@");
                    stream = socket.getInputStream();
                    byte[] lenBytes = new byte[256];
                    stream.read(lenBytes, 0, 256);
                    received = new String(lenBytes, "UTF-8").trim();
                    if (received.equals("bola") || received.equals("bloque")){
                        juego.crearObstaculos(received);
                    }
                }
                //cierra conexion
                socket.close();
                return received;
            } catch (IOException ex) {
                Log.e("E/TCP Client", "" + ex.getMessage());
                return ex.getMessage();
            }
        }
}
