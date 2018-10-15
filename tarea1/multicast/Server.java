package tarea1.multicast;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    public static void main(String [] args){

        try {
            ServerSocket serverSocket = new ServerSocket(9999);

        } catch (IOException e) {

            System.out.println(e.getMessage());
        }


    }

}
