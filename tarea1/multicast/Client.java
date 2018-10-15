package tarea1.multicast;

import java.io.DataInputStream;
import java.io.IOException;

public class Client {

    public static void main(String [] args){

        try {
            Socket socket = new Socket("192.168.0.5", 9999); //Localhost 127.0.0.1

            DataInputStream inputStream = new DataInputStream(socket.getInputStream());
            inputStream.writeUTF("Hola mundo!");
            inputStream.close();

        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

    }

}
