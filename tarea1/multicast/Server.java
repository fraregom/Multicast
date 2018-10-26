package tarea1.multicast;

import java.io.*;
import java.net.*;
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

    private static String MulticastAddress = "224.0.0.1";
    private static Integer RequestPort = 9000;
    private static HashMap<String, Integer> variables =
            new HashMap<String, Integer>() {{
                put("Temperature", 10001);
                put("Humidity", 10002);
                put("Pressure", 10003);
            }};


    public static void main(String[] args) {

        //  Se imprime en pantalla la solicitud si se desea setear un ip_adress
        Scanner terminal = new Scanner(System.in);
        String temporalAdress = null;
        System.out.print("Java server ");

        try {
            temporalAdress = terminal.next(Pattern.compile("^(?:(25[0-5]|2[0-4][0-9]|1[0-9][0-9]|[1-9][0-9]|[0-9])(\\.(?!$)|$)){4}$"));
        } catch (Exception e) {
            System.out.println("It has been established as IP-server: " + MulticastAddress);
        }

        if (temporalAdress != null) MulticastAddress = temporalAdress;
        System.out.println("IP-server: " + MulticastAddress);

        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter("data"));


            writer.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

        for (Map.Entry<String, Integer> var : variables.entrySet()) {
            Thread thread = new Thread(new SendingInAddress(var.getValue(), MulticastAddress, var.getKey()));
            thread.start();
        }

        Thread recoveryThread = new Thread(new RecoveryService(RequestPort));
        recoveryThread.start();

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

                    String row =  String.join(" ", String.valueOf(Id)+".-", Variable , String.valueOf(value),"\n");
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

class RecoveryService implements Runnable {
    int Port;

    public RecoveryService(int Port) {
        this.Port = Port;
    }

    @Override
    public void run() {
        try {
            ServerSocket serversocket = new ServerSocket(Port);
            Socket socket = serversocket.accept();
            DataInputStream request;
            BufferedReader output;
            DataOutputStream stream = null;
            String dString;

            while(!interrupted()) {
                request = new DataInputStream(socket.getInputStream());
                String msg_received = request.readUTF();
                if (msg_received.equals("GET")) {
                    try {
                        output = new BufferedReader(new FileReader("data")); //cambiar esto!!!
                        while ((dString = output.readLine()) != null) {

                            stream = new DataOutputStream(socket.getOutputStream());
                            stream.writeUTF(dString + "\n");

                        }
                    } catch (IOException e) {
                        System.out.println(e.getMessage());
                    }
                }
            }
            //stream.close();
            //socket.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}



