package tarea1.multicast;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Pattern;

import static java.lang.Thread.interrupted;

public class Client {

    private static String Address = "224.0.0.1";
    private static final Pattern IPV4_PATTERN = Pattern.compile("^(?:(25[0-5]|2[0-4][0-9]|1[0-9][0-9]|[1-9][0-9]|[0-9])(\\.(?!$)|$)){4}$");
    private static ArrayList<Integer> Ports = new ArrayList<Integer>() {{
        add(9001);
        add(9002);
        add(9003);
    }};
    //private static final Pattern BINARY_PATTERN = Pattern.compile("([1][01])|1");
    
    public static void main(String[] args) {

        String request = "";
        System.out.print("Java client ");

        Scanner terminal = new Scanner(System.in).useDelimiter("[,\\s+]");
        String[] base = terminal.nextLine().split(" ");

        for (String aBase : base) {
            if (IPV4_PATTERN.matcher(aBase).matches()) {
                Address = aBase;
            } else request = aBase;
        }

        for (int i = 0, n = request.length(); i < n; i++)
            if (request.charAt(i) != '0') {
                Thread thread = new Thread(new BlindInAddress(Address, Ports.get(i)));
                thread.start();
            }

        System.out.println("Connected in: " + Address);
    }
}

class BlindInAddress implements Runnable {

    private String Address;
    private Integer Port;

    public BlindInAddress(String Address, int Port) {
        this.Address = Address;
        this.Port = Port;
    }

    @Override
    public void run() {
        try {
            MulticastSocket socket = new MulticastSocket(Port);
            socket.setReuseAddress(true);

            InetAddress group = InetAddress.getByName(Address);
            DatagramPacket packet;
            socket.joinGroup(group);

            while (!interrupted()) {
                byte[] buf = new byte[256];
                packet = new DatagramPacket(buf, buf.length);
                socket.receive(packet);

                if (packet.getLength() == 0) break;

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

        } catch (IOException e) {
            e.printStackTrace();
        }

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