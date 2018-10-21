package tarea1.multicast;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

public class Client {

    public static void main(String[] args) throws IOException {

        //clientFrame client = new clientFrame(9003);

        MulticastSocket socket = new MulticastSocket(9003);
        socket.setReuseAddress(true);

        InetAddress group = InetAddress.getByName("224.0.0.1");
        DatagramPacket packet;
        socket.joinGroup(group);

        while(true){
            byte[] buf = new byte[256];
            packet = new DatagramPacket(buf, buf.length);
            socket.receive(packet);

            if(packet.getLength() == 0) break;

            byte[] data = packet.getData();
            ObjectInputStream is = new ObjectInputStream(new ByteArrayInputStream(data));

            try {

                measurementBody Var = (measurementBody) is.readObject();
                System.out.println(Var.getId().toString() + ".-" + Var.getVariable() + " " + Var.getValue());

            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        socket.leaveGroup(group);
        socket.close();

    }
}

/*class clientFrame{
    InetAddress address;
    DatagramSocket socket = null;
    DatagramPacket packet;
    byte[] buf = new byte[256]; //Se definen los datos de donde el cliente envio la peticion.
    byte[] buf2 = new byte[1024];
    boolean check = false;

    public clientFrame(int port){
        try {
            socket = new DatagramSocket(); //Se crea un socket para enviar el paquete a crear.
            address = InetAddress.getLocalHost();
            packet = new DatagramPacket(buf, buf.length, address, port); //se define que la peticion se enviara al puerto 4445 del server
            socket.send(packet);

            packet = new DatagramPacket(buf2, buf2.length); //Esto imprimira lo que responda el servidor.
            socket.receive(packet);

            while(true) {
            packet = new DatagramPacket(buf2, buf2.length); //Esto imprimira lo que responda el servidor.
            socket.receive(packet);
            if(!check){
                System.out.println(packet.getPort());
                System.out.println(packet.getAddress());
                System.out.println(packet.getLength());
                System.out.println(packet.getOffset());
                System.out.println(packet.getSocketAddress());
                check = true;
            }

            byte[] data = packet.getData();
            ObjectInputStream is = new ObjectInputStream(new ByteArrayInputStream(data));

            try {

                measurementBody Var = (measurementBody) is.readObject();
                System.out.println(Var.getId().toString() + ".-" + Var.getVariable() + " " + Var.getValue());

            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}*/