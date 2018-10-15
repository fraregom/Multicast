package tarea1.multicast;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.*;

public class Server {


    public static void main(String [] args){

        try {
            ServerSocket server = new ServerSocket(9999);

            Socket socket = server.accept();

            DataInputStream stream = new DataInputStream(socket.getInputStream());

            String message = stream.readUTF();

            System.out.println(message);

            socket.close();

        } catch (IOException e) {

            System.out.println(e.getMessage());
        }


    }

}
