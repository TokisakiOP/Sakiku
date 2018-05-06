using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Net;
using System.Net.Sockets;
using System.Text;
using System.Threading.Tasks;

namespace ClienteProyecto
{
    class Program
    {
        static void Main(string[] args)
        {
            Console.WriteLine("Cliente");
            const string IP_SERVIDOR = "127.0.0.1";
            string mensaje;
            string mensajeUsuario;
            IPEndPoint ie = new IPEndPoint(IPAddress.Parse(IP_SERVIDOR), 4000);
            Console.WriteLine(
           "Iniciando cliente. Pulsa una tecla para iniciar la conexión");
            Console.ReadKey();
            Socket servidor = new Socket(AddressFamily.InterNetwork,
            SocketType.Stream, ProtocolType.Tcp);
            try
            {
                servidor.Connect(ie);
            }
            catch (SocketException e)
            {
                Console.WriteLine(
               "Error de conexión con servidor: {0}\nCódigo de error: {1}({2})",
                e.Message, (SocketError)e.ErrorCode, e.ErrorCode);
                Console.ReadKey();
                return;
            }
            NetworkStream ns = new NetworkStream(servidor);
            StreamReader sr = new StreamReader(ns);
            StreamWriter sw = new StreamWriter(ns);
            // Leemos mensaje de bienvenida
            mensaje = sr.ReadLine();
            Console.WriteLine(mensaje);
            while (true)
            {
                mensajeUsuario = Console.ReadLine();
                if (mensajeUsuario == "salir")
                    break;
                //Enviamos el mensaje de usuario al servidor
                sw.WriteLine(mensajeUsuario);
                sw.Flush();
                //Recibimos el mensaje del servidor
                mensaje = sr.ReadLine();
                Console.WriteLine(mensaje);
            }
            Console.WriteLine("Desconexión");
            sr.Close();
            sw.Close();
            ns.Close();
            //Indicamos fin de transmisión.
            servidor.Shutdown(SocketShutdown.Both);
            servidor.Close();
            Console.ReadKey();
        }
    }
}