package tarea1.multicast;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.*;

public class Client {

    public static void main(String [] args){

        try {
            Socket socket = new Socket("127.0.1", 9999); //Localhost 127.0.0.1

            DataOutputStream stream = new DataOutputStream(socket.getOutputStream());

            stream.writeUTF("Hola mundo!");
            stream.close();

        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

    }

}
