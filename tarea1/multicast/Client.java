package tarea1.multicast;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.*;

public class Client {

    public static void main(String[] args) {

        clientFrame client = new clientFrame();


        /*try {
            Socket socket = new Socket("127.0.1", 9999); //Localhost 127.0.0.1

            DataOutputStream stream = new DataOutputStream(socket.getOutputStream());

            stream.writeUTF("Hola mundo!");
            stream.close();

        } catch (IOException e) {
            System.out.println(e.getMessage());
        }*/
    }
}

class clientFrame{
    int port;
    InetAddress address;
    DatagramSocket socket = null;
    DatagramPacket packet;
    byte[] sendBuf = new byte[256];

    public clientFrame(){
        try {
            socket = new DatagramSocket(); //Se crea un socket para enviar el paquete a crear.

            byte[] buf = new byte[256]; //Se definen los datos de donde el cliente envio la peticion.
            InetAddress address = InetAddress.getLocalHost();
            DatagramPacket packet = new DatagramPacket(buf, buf.length, address, 4445); //se define que la peticion se enviara al puerto 4445 del server
            socket.send(packet);


            packet = new DatagramPacket(buf, buf.length); //Esto imprimira lo que responda el servidor.
            socket.receive(packet);
            String received = new String(packet.getData(), 0, packet.getLength());
            System.out.println("Quote of the Moment: " + received);


        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

    }

}



