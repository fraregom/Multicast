package tarea1.multicast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;
import java.util.regex.Pattern;

import java.io.BufferedWriter;
import java.io.FileWriter;

import static java.lang.Thread.interrupted;
import static java.lang.Thread.sleep;

public class Server {

    private static String Address = "224.0.0.1";
    private static HashMap<String, Integer> variables =
            new HashMap<String, Integer>() {{
                put("Temperature", 9001);
                put("Humidity", 9002);
                put("Pressure", 9003);
            }};


    public static void main(String[] args) {

        //  Se imprime en pantalla la solicitud si se desea setear un ip_adress
        Scanner terminal = new Scanner(System.in);
        String temporalAdress = null;
        System.out.print("Java server ");

        try {
            temporalAdress = terminal.next(Pattern.compile("^(?:(25[0-5]|2[0-4][0-9]|1[0-9][0-9]|[1-9][0-9]|[0-9])(\\.(?!$)|$)){4}$"));
        } catch (Exception e) {
            System.out.println("It has been established as IP-server: " + Address);
        }

        if (temporalAdress != null) Address = temporalAdress;
        System.out.println("IP-server: " + Address);


        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter("data"));


            writer.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }



        for (Map.Entry<String, Integer> var : variables.entrySet()) {
            Thread thread = new Thread(new SendingInAddress(var.getValue(), Address, var.getKey()));
            thread.start();
        }

    }

}

class SendingInAddress implements Runnable {

    private Integer Port;
    private String Address;
    private String Variable;

    public SendingInAddress(int port, String address, String variable) {
        this.Port = port;
        this.Address = address;
        this.Variable = variable;
    }

    @Override
    public void run() {
        try {

            //Se define el socket para multicast y se configura un grupo según la dirección pre-configurada.
            MulticastSocket multicastSocket = new MulticastSocket();
            multicastSocket.setReuseAddress(true);
            InetAddress group = InetAddress.getByName(Address);
            DatagramSocket socket = new DatagramSocket();

            int Id = 1;
            int value;
            byte[] data;

            //long t= System.currentTimeMillis();
            //long end = t+30000;

            while (!interrupted()) {//while (System.currentTimeMillis() < end) {//(dString = input.readLine()) != null){
                try {

                    switch (Variable) {
                        case "Pressure": {
                            Random rand = new Random();
                            value = rand.nextInt(10);
                            break;
                        }
                        case "Temperature": {
                            Random rand = new Random();
                            value = rand.nextInt(50);
                            break;
                        }
                        default: {
                            Random rand = new Random();
                            value = rand.nextInt(100);
                            break;
                        }
                    }

                    String row =  String.join(" ", String.valueOf(Id), Variable , String.valueOf(value),"\n");
                    BufferedWriter writer = new BufferedWriter(new FileWriter("data", true));
                    writer.append(row);
                    writer.close();



                    measurementBody Var = new measurementBody(Id, Variable, value);

                    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                    ObjectOutputStream os = new ObjectOutputStream(outputStream);
                    os.writeObject(Var);
                    data = outputStream.toByteArray();

                    DatagramPacket packet = new DatagramPacket(data, data.length, group, Port);
                    multicastSocket.send(packet);

                    System.out.print("Sending! at port: " + Port.toString() + "\n");
                    sleep(5000);

                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
                Id++;
            }

            socket.close();

        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
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

                    measurementBody Var = new measurementBody(Id, "Temperature:", value);

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

