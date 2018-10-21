package tarea1.multicast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.Random;

import static java.lang.Thread.interrupted;
import static java.lang.Thread.sleep;


public class Server {

    public static void main(String[] args) {

        //DatagramSocket socket = new DatagramSocket(9003); //Se define un socket a la escucha de peticiones en el puerto 9003

        try {

            MulticastSocket multicastSocket = new MulticastSocket();
            multicastSocket.setReuseAddress(true);
            //multicastSocket.setInterface(InetAddress.getByName("192.168.0.5"));
            InetAddress group = InetAddress.getLocalHost();

            DatagramSocket socket = new DatagramSocket();

            int Id = 1;
            //InetAddress adress = InetAddress.getLocalHost();
            byte[] data = new byte[0];
            int port = 9003;

            //long t= System.currentTimeMillis();
            //long end = t+30000;

            while (!interrupted()){//while (System.currentTimeMillis() < end) {//(dString = input.readLine()) != null){
                try {
                    Random rand = new Random();
                    int value = rand.nextInt(50);

                    VarBody Var = new VarBody(Id, "Temperature:", value);

                    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                    ObjectOutputStream os = new ObjectOutputStream(outputStream);
                    os.writeObject(Var);
                    data = outputStream.toByteArray();

                    //data = dString.getBytes(); //Se convierte en bytes una linea del texto.
                    DatagramPacket dgp = new DatagramPacket(data, data.length, group, port);
                    multicastSocket.send(dgp);

                    System.out.print("Sending!..\n");
                    sleep(5000);
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }

                Id++;

            }

            //DatagramPacket dgp = new DatagramPacket(data, 0, adress, port);
            //socket.send(dgp);
            socket.close();

        } catch (IOException e) {
            System.out.println(e.getMessage());
        }


        /*try {
            byte[] buf = new byte[256];

            DatagramPacket packet = new DatagramPacket(buf, buf.length);  //Se obtiene toda la informacion del cliente para enviar una respuesta.
            socket.receive(packet);

            // Agregar aqui la logica de revisar el paquete y designar los puertos para responder.

            ThreadServer server = new ThreadServer(packet.getAddress(), socket, packet.getPort()); //Se le designara un puerto segun lo solicitado, en este caso el 9000
            server.start();

        } catch (Exception e) {
            socket.setSoTimeout(100);
            e.printStackTrace();
        }*/
    }
}



/*class ThreadServer extends Thread {

    private InetAddress adress;
    private byte [] data;
    private int port;
    private DatagramSocket socket;


    public ThreadServer(InetAddress address, DatagramSocket Socket, int port){
        this.adress = address;
        this.port = port ;
        this.socket = Socket;
    }


    @Override
    public void run() {

        try{

            DatagramPacket firstResponse = new DatagramPacket(data, data.length, adress, port);
            socket.send(firstResponse);

            //BufferedReader input = new BufferedReader(
            //        new FileReader("D:\\Quest\\Escritorio\\Multicast\\tarea1\\multicast\\one-liners.txt")); //cambiar esto!!!
            //String dString;
            Integer Id = 1;

            while (Id < 10000){//(dString = input.readLine()) != null){

                try {
                    Random rand = new Random();
                    int value = rand.nextInt(50);

                    VarBody Var = new VarBody(Id, "Temperature:", value);

                    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                    ObjectOutputStream os = new ObjectOutputStream(outputStream);
                    os.writeObject(Var);
                    data = outputStream.toByteArray();

                    //data = dString.getBytes(); //Se convierte en bytes una linea del texto.
                    DatagramPacket dgp = new DatagramPacket(data, data.length, adress, port);
                    socket.send(dgp);

                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }

                Id++;

            }
            socket.close();

        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

    }

}*/

