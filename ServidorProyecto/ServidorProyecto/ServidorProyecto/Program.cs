using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Net;
using System.Net.Sockets;
using System.Text;
using System.Threading;
using System.Threading.Tasks;

namespace ServidorProyecto
{
    class Program
    {
        static List<string> usuarios = new List<string>();
        static int puerto = 4000;
        static List<StreamWriter> lista = new List<StreamWriter>();
        static readonly private object l = new object();
        static void Main(string[] args)
        {
            bool enEjecucion = true;
            IPEndPoint ie = new IPEndPoint(IPAddress.Any, puerto);
            Socket s = new Socket(AddressFamily.InterNetwork, SocketType.Stream, ProtocolType.Tcp);
            s.Bind(ie);
            s.Listen(10);
            Socket jugadores = null;

            while (enEjecucion)
            {
                jugadores = s.Accept();
                Thread hilo = new Thread(hiloJugadores);
                hilo.Start(jugadores);

            }
            s.Close();
        }
        static public void hiloJugadores(object jugador)
        {
            bool activo = true;
            string usuario = "mobil" + lista.Count; // USARLO PARA IDENTIFICAR AL HOST
            string mensaje = "";
            Socket player = (Socket)jugador;
            IPEndPoint endPoint = (IPEndPoint)player.RemoteEndPoint;
            NetworkStream ns = new NetworkStream(player);
            StreamReader sr = new StreamReader(ns);
            StreamWriter sw = new StreamWriter(ns);
            if (usuario != "" && usuario != null)
            {
                sw.WriteLine("Bienvenido al juego"); // activador
                sw.Flush();
                lock (l)
                {
                    usuarios.Add(usuario);
                    lista.Add(sw);
                }
                Console.WriteLine("Ha iniciado sesion " + usuario);

                while (activo)
                {
                    try
                    {
                        Console.WriteLine("hola");
                        mensaje = sr.ReadLine();
                    }
                    catch (IOException)
                    {
                        mensaje = "desconectado";
                    }
                    if (mensaje == null)
                    {
                        mensaje = "desconectado";
                    }
                    Console.WriteLine(mensaje);
                    if (mensaje == "conectado")
                    {
                        mensaje = lanzamientoObstaculos();
                        Console.WriteLine(mensaje);
                        try
                        {
                            sw.WriteLine(mensaje);
                            sw.Flush();
                            Thread.Sleep(1000);
                        }
                        catch (System.IO.IOException)
                        {
                            mensaje = "desconectado";
                        }
                    }
                    if (mensaje == "desconectado")
                    {
                        if (ns != null) ns.Close();
                        if (sr != null) sr.Close();
                        lock (l)
                        {
                            lista.RemoveAt(usuarios.IndexOf(usuario));
                            usuarios.Remove(usuario);
                        }
                        activo = false;
                        player.Close();
                    }
                    
                }
            }
        }
        static Random random = new Random();
        public static string lanzamientoObstaculos()
        {
            string mensaje = "";
            int randomNumber = random.Next(0, 100);
            if (randomNumber < 33)
            {
                mensaje = "bola";
                Console.WriteLine("1");
            }
            else if (randomNumber < 66)
            {
                mensaje = "bloque";
                Console.WriteLine("2");
            }
            else
            {
                mensaje = "nada";
                Console.WriteLine("3");
            }
            return mensaje;
        }
    }
}
