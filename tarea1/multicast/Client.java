package tarea1.multicast;

import java.io.*;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Pattern;

import static java.lang.Thread.interrupted;

public class Client {

    private static String AddressMulticast = "224.0.0.1";
    private static String ServerAddress = null;
    private static Integer RequestPort = 9000;
    private static final Pattern IPV4_PATTERN = Pattern.compile("^(?:(25[0-5]|2[0-4][0-9]|1[0-9][0-9]|[1-9][0-9]|[0-9])(\\.(?!$)|$)){4}$");
    private static Pattern inf = Pattern.compile("^[10][10][10]");
    private static ArrayList<Integer> Ports = new ArrayList<Integer>() {{
        add(10001);
        add(10002);
        add(10003);
    }};
    //private static final Pattern BINARY_PATTERN = Pattern.compile("([1][01])|1");

    public static void main(String[] args) {

        String request = "";
        boolean acceptedAddress = false;
        System.out.print("Java client ");

        Scanner terminal = new Scanner(System.in).useDelimiter("[,\\s+]");
        String[] base = terminal.nextLine().split(" ");

        for (String aBase : base) {
            if (IPV4_PATTERN.matcher(aBase).matches()) {
                ServerAddress = aBase;
            }if (aBase.equals("-R")) {
                System.out.println( " -R funca" ); //Aplicar aqui la funcion para retornar el registro
                Thread thread = new Thread(new RequestThread(RequestPort, ServerAddress));
                thread.start();
            }if (inf.matcher(aBase).matches()){
                request = aBase;
            }
        }

        for (int i = 0, n = request.length(); i < n; i++)
            if (request.charAt(i) != '0') {
                Thread thread = new Thread(new BlindInAddress(AddressMulticast, Ports.get(i)));
                thread.start();
                acceptedAddress = true;
            }

        if(acceptedAddress) System.out.println("Connected in: " + AddressMulticast);

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

class RequestThread implements Runnable{
    private int Port;
    private String serverAddress;

    public RequestThread(int Port, String serverAddress){
        this.Port = Port;
        this.serverAddress = serverAddress;
    }

    @Override
    public void run() {
        try {
            Socket socket = new Socket(serverAddress, Port);
            DataOutputStream stream = new DataOutputStream(socket.getOutputStream());
            stream.writeUTF("GET");

            try{
                BufferedReader request = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                for (String line = request.readLine(); line != null; line = request.readLine()) {
                    System.out.println(line);
                }
                request.close();

            } catch(Exception e){
                System.err.println("Error: Target File Cannot Be Read");
            }

            socket.close();
            stream.close();

        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}