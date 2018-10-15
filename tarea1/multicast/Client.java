package tarea1.multicast;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.*;

public class Client {

    public static void main(String [] args){

        try {
            Socket socket = new Socket("127.0.0.1", 9999);

            DataInputStream inputStream = new DataInputStream(socket.getInputStream());
            


        } catch (IOException e) {
            System.out.println(e.getMessage());
        }


    }

}
