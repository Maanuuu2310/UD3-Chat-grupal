package net.salesianos.server;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import net.salesianos.server.threads.ClientHandler;
import net.salesianos.shared.constants.Ports;

public class ServerChat {
    public static void main(String[] args) throws Exception {
        ServerSocket serverSocket = new ServerSocket(Ports.SERVER_PORT);
        ArrayList<ObjectOutputStream> connectedObjOutputStream = new ArrayList<>();

        while (true) {
            System.out.println("Esperando conexión...");
            Socket clientSocket = serverSocket.accept();
            System.out.println("CONEXION ESTABLECIDA");
        
            ObjectOutputStream clientObjOutStream = new ObjectOutputStream(clientSocket.getOutputStream());
            connectedObjOutputStream.add(clientObjOutStream);
            
            ObjectInputStream clientObjInStream = new ObjectInputStream(clientSocket.getInputStream());
            ClientHandler clientHandler = new ClientHandler(clientObjInStream, clientObjOutStream, connectedObjOutputStream);
            clientHandler.start();
        }

        // serverSocket.close();
    }
}
