package com.example.tokisaki.sakiku;


/**
 * Created by Tokisaki on 30/04/2018.
 */

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.Log;

import org.apache.commons.io.IOUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.NetworkInterface;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;
import java.util.Locale;


/**
 * Clase para interactuar con el servidor
 * */
public class Cliente extends AsyncTask<String,Void,String> {

    /**
     * Puerto al que conectarse
     */
    private static final int SERVERPORT = 4000;
    /**
     * instancia de la clase jugar
     */
    Jugar juego;
    /**
     * Contexo de la aplicacion
     */
    Context context;
    /**
     * booleano que indica si la conexion es valida
     */
    boolean conexionInvalida;
    /**
     * ip a la que conectarse
     */
    String ip;
    /**
     * instancia de la clase info
     */
    Info info;

    /**
     * Constructor de la clase
     * @param context contexto de la aplicacion
     * @param j instancia de la clase Jugar que se esta usando en la aplicacion
     * @param info instancia de la clase info que se esta usando en la aplicacion
     */
        public Cliente(Context context,Jugar j,Info info){
            juego =  j;
            this.context = context;
            this.info = info;
            conexionInvalida = true;
        }


    /**
     * Se conecta con el servidor y lanza los obsculos recibidos por el mismo
     * @param values volores que se mandaran al servidor
     * @return mensaje recibido
     */
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected String doInBackground(String... values){
        try {
            Socket socket = pruebaPuerto();
            juego.conectado = true;
            PrintStream output = new PrintStream(socket.getOutputStream());
            String request = values[0];
            output.println(request);
            String received = null;
            InputStream stream = null;
            while(juego.finCarrera == false && juego.jugar) {
                output.println(request);
                stream = socket.getInputStream();
                byte[] lenBytes = new byte[256];
                stream.read(lenBytes, 0, 256);
                received = new String(lenBytes, "UTF-8").trim();
                if (received.equals("bola") || received.equals("bloque")){
                    juego.crearObstaculos(received);
                    }
            }
            output.println("desconectado");
            socket.close();
            return received;
        } catch (IOException ex) {
            juego.conectado = false;
            return ex.getMessage();
        }catch (NullPointerException e){
            juego.conectado = false;
            return e.getMessage();
        }
    }

    /**
     * funcion que buscará la ipv4 del dispositivo
     * @param id indica el tipo de conexion
     * @return la ip del dispositivo
     */
    @Nullable
    public static String getIPAddressIPv4(String id) {
        try {
            List<NetworkInterface> interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface intf : interfaces) {
                if (intf.getName().contains(id)) {
                    List<InetAddress> addrs = Collections.list(intf.getInetAddresses());
                    for (InetAddress addr : addrs) {
                        if (!addr.isLoopbackAddress()) {
                            String sAddr = addr.getHostAddress();
                            if (addr instanceof Inet4Address) {
                                return sAddr;
                            }
                        }

                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    /**
     * Función que intenta conectarse al servidor
     * @return la conexion con el servidor
     */
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public Socket pruebaPuerto(){
        boolean guardada = true;
        Socket socket = null;
        InetAddress serverAddr = null;
        try {
            serverAddr = InetAddress.getByName(info.ipServidor);
            socket = new Socket(serverAddr, SERVERPORT);
            conexionInvalida = false;
        } catch (IOException ex) {
            guardada = false;
        }
        if(!guardada) {
            int timeout = 1000;
            int cuartoNum = 1;
            String[] recorte = getIPAddressIPv4("wlan").split("[.]");
            ip = recorte[0] + "." + recorte[1] + "." + recorte[2] + ".";
            while (conexionInvalida && cuartoNum < 254 && juego.jugar) {
                cuartoNum++;
                try {
                    serverAddr = InetAddress.getByName(ip + cuartoNum);
                    if (pruebaConexion(ip + cuartoNum,SERVERPORT,timeout)) {
                        socket = new Socket(serverAddr, SERVERPORT);
                        conexionInvalida = false;
                    }
                } catch (IOException ex) {
                }
            }
            if(cuartoNum >= 254){
                juego.noEncuentra = true;
            }
            if(socket != null){
                info.ipServidor = ip + cuartoNum;
                SharedPreferences preferencias = context.getSharedPreferences("saves",0);
                SharedPreferences.Editor editor = preferencias.edit();
                editor.putString("ip",info.ipServidor);
                editor.apply();
            }
        }
        return socket;
    }

    /**
     * Funcion que busca la direccion donde esta alojado el servidor
     * @param address ip donde se busca
     * @param port puerto en el que se busca
     * @param timeout tiempo de espera
     * @return devuelve true si se ha podido conectar y false en el caso contrario
     */
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private static boolean pruebaConexion(String address, int port, int timeout) {
        try {

            try (Socket pruebaSocket = new Socket()) {
                pruebaSocket.connect(new InetSocketAddress(address, port), timeout);
            }
            return true;
        } catch (IOException exception) {
            return false;
        }
    }


}
