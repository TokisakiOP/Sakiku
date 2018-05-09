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

        /**
         * Ventana que bloqueara la pantalla del movil hasta recibir respuesta del servidor
         * */
        ProgressDialog progressDialog;

        Escenas j;

        Jugar a;

        Context context;

        public Cliente(Escenas juego , Context context){
            String zz = "No";
            if( juego instanceof Jugar){
                zz = "SIIIIIIIIIIIIIIIIII";
            }
            Log.i("prueba"," " + zz );
            this.j =  juego;
            //a = (Jugar) j;
            this.context = context;
        }

        /**
         * muestra una ventana emergente
         * */
        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            progressDialog = new ProgressDialog(context);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setTitle("Connecting to server");
            progressDialog.setMessage("Please wait...");
//            progressDialog.show();
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
                Log.i("I/TCP Client", "Connecting...");
                Socket socket = new Socket(serverAddr, SERVERPORT);
                Log.i("I/TCP Client", "Connected to server");

                //envia peticion de cliente
                Log.i("I/TCP Client", "Send data to server");
                PrintStream output = new PrintStream(socket.getOutputStream());
                String request = values[0];
                output.println(request);

                //recibe respuesta del servidor y formatea a String
                String received = null;
                InputStream stream = null;
                Log.i("I/TCP Client", "Received data to server");
                while(Jugar.finCarrera == false) {
                    Log.i("prueba","@@@@@@@@@@@@");
                    stream = socket.getInputStream();
                    /*byte[] lenBytes = new byte[256];
                    stream.read(lenBytes, 0, 256);
                    received = new String(lenBytes, "UTF-8").trim();*/
                     //received = getStringFromInputStream(stream);
                    received = IOUtils.toString(stream, StandardCharsets.UTF_8);
                    Log.i("prueba", "Received1 :" + received);
                    Log.i("prueba", "Received prueba:" + received.equals("hola"));
                    Log.i("prueba", "Received bola:" + received.equals("bola"));
                    Log.i("prueba", "Received bloque:" + received.equals("bloque"));
                    if (received.equals("bola") || received.equals("bloque")){
                        Log.i("prueba", "Received :" + received);
                        j.crearObstaculos(received);
                    }
                    Log.i("prueba", "Received2 :" + received);
                    Log.i("prueba", "" + Jugar.finCarrera);
                }
                //cierra conexion
                socket.close();
                return received;
            } catch (IOException ex) {
                Log.e("E/TCP Client", "" + ex.getMessage());
                return ex.getMessage();
            }
        }

        /**
         * Oculta ventana emergente y muestra resultado en pantalla
         * */
        /*
        @Override
        protected void onPostExecute(String value){
            progressDialog.dismiss();
            //editText2.setText(value);
            Log.i("prueba",value);
        }
*/

        private static String getStringFromInputStream(InputStream is) {

            BufferedReader br = null;
            StringBuilder sb = new StringBuilder();

            String line;
            try {

                br = new BufferedReader(new InputStreamReader(is));
                while ((line = br.readLine()) != null) {
                    sb.append(line);
                }

            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (br != null) {
                    try {
                        br.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            return sb.toString();

        }

}
