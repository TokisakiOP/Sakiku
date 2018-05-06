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
        static List<StreamWriter> swu = new List<StreamWriter>();
        static readonly private object l = new object();
        static void Main(string[] args)
        {
            bool enEjecucion = true;
            bool puertoInvalido = true;
            IPEndPoint ie = new IPEndPoint(IPAddress.Any, puerto);
            Socket s = new Socket(AddressFamily.InterNetwork, SocketType.Stream, ProtocolType.Tcp);
            while (puertoInvalido)
            {
                try
                {
                    s.Bind(ie);
                    puertoInvalido = false;
                }
                catch (SocketException)
                {
                    Console.WriteLine("Error asignando puerto {0} , probando el siguiente ", puerto);
                    puerto++;
                    ie = new IPEndPoint(IPAddress.Any, puerto);
                }
            }
            s.Listen(10);
            Socket sCliente = null;

            while (enEjecucion)
            {
                sCliente = s.Accept();
                Thread hilo = new Thread(hCliente);
                hilo.Start(sCliente);

            }
            s.Close();
        }
        static public void hCliente(object cliente)
        {
            bool activo = true;
            string usuario = "mobil";
            string mensaje = "";
            Socket sCliente = (Socket)cliente;
            IPEndPoint endPoint = (IPEndPoint)sCliente.RemoteEndPoint;
            NetworkStream ns = new NetworkStream(sCliente);
            StreamReader sr = new StreamReader(ns);
            StreamWriter sw = new StreamWriter(ns);


            while (usuario == "")
            {
                sw.WriteLine("Nombre de usuario");
                sw.Flush();
                usuario = sr.ReadLine();
            }
            if (usuario != "" && usuario != null)
            {
                usuario = usuario + "@" + endPoint.Address;
                sw.WriteLine("Bienvenido a chattirum - " + usuario);
                sw.Flush();
                lock (l)
                {
                    usuarios.Add(usuario);
                    swu.Add(sw);
                }
                Console.WriteLine("Ha iniciado sesion " + usuario);
                foreach (StreamWriter stream in swu)
                {
                    if (stream != sw)
                    {
                        stream.WriteLine("Se ha conectado : " + usuario);
                        stream.Flush();
                    }
                }

                while (activo)
                {
                    try
                    {
                        mensaje = sr.ReadLine();
                    }
                    catch (IOException)
                    {
                        mensaje = "#salir";
                    }
                    if (mensaje == null)
                    {
                        mensaje = "#salir";
                    }
                    switch (mensaje)
                    {
                        case "#lista":
                            sw.WriteLine("Usuarios online :");
                            sw.Flush();
                            foreach (string usu in usuarios)
                            {
                                if (usu != usuario)
                                {
                                    sw.WriteLine("Usuario : " + usu);
                                    sw.Flush();
                                }
                            }
                            break;
                        case "#salir":
                            foreach (StreamWriter stream in swu)
                            {
                                if (stream != sw && stream != null)
                                {
                                    stream.WriteLine(usuario + " ha salido");
                                    stream.Flush();
                                }
                            }

                            if (ns != null) ns.Close();
                            if (sr != null) sr.Close();
                            lock (l)
                            {
                                swu.RemoveAt(usuarios.IndexOf(usuario));
                                usuarios.Remove(usuario);
                            }
                            activo = false;
                            sCliente.Close();
                            break;
                        default:
                            foreach (StreamWriter stream in swu)
                            {
                                if (stream != sw)
                                {
                                    stream.WriteLine(usuario + " : " + mensaje);
                                    stream.Flush();
                                }
                            }
                            break;
                    }
                }
            }
        }
    }
}
