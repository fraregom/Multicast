package tarea1.multicast;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.*;

public class Server{

    public static void main(String[] args) throws IOException {
        ThreadServer server = new ThreadServer();
    }

    /*public static void main(String [] args){

        try {
            ServerSocket server = new ServerSocket(9999);

            Socket socket = server.accept();

            DataInputStream stream = new DataInputStream(socket.getInputStream());

            String message = stream.readUTF();

            System.out.println(message);

            socket.close();

        } catch (IOException e) {

            System.out.println(e.getMessage());
        }


    }*/

}


class ThreadServer implements Runnable{

    public ThreadServer(){
        Thread thread = new Thread(this); //Inicializa un thread para realizar multiples funciones.
        thread.start();
    }


    @Override
    public void run() {

        try{
            BufferedReader input = null;
            String dString = null;
            DatagramSocket socket = new DatagramSocket(4445); //Se define un socket a la escucha de peticiones en el puerto 4445.
            input = new BufferedReader(new FileReader("/home/havok1177/Documents/Multicast/tarea1/multicast/one-liners.txt")); //cambiar esto!!!

            while ((dString = input.readLine()) != null){

                byte[] buf = new byte[256];
                int port;

                try {

                    DatagramPacket packet = new DatagramPacket(buf, buf.length);  //Se obtiene toda la informacion del cliente para enviar una respuesta.
                    socket.receive(packet);
                    InetAddress address = packet.getAddress();
                    port = packet.getPort();

                    buf = dString.getBytes(); //Se convierte en bytes una linea del texto.
                    packet = new DatagramPacket(buf, buf.length, address, port); //Se prepara el paquete a enviar al cliente.
                    socket.send(packet);

                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }

            }

            socket.close();

        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

    }

}
