package net.salesianos.server.threads;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;



public class ClientHandler extends Thread {

  private ObjectInputStream clientObjInStream;
  private ObjectOutputStream clientObjOutStream;
  private ArrayList<ObjectOutputStream> connectedObjOutputStreamList;

  public ClientHandler(ObjectInputStream clientObjInStream, ObjectOutputStream clientObjOutStream,
      ArrayList<ObjectOutputStream> connectedObjOutputStreamList) {
    this.clientObjInStream = clientObjInStream;
    this.clientObjOutStream = clientObjOutStream;
    this.connectedObjOutputStreamList = connectedObjOutputStreamList;
  }

  @Override
  public void run() {
    String username = "";
    boolean exit = true;
    try {

      username = this.clientObjInStream.readUTF();

      while (exit) {
        String personReceived = (String) this.clientObjInStream.readObject();
        System.out.println(username + " envía: " + personReceived.toString());

        if (personReceived.equals("bye")) {
          System.out.println("Entro");
          exit = false;
          this.connectedObjOutputStreamList.remove(this.clientObjOutStream);
          System.out.println("Cerrando conexion con " + username.toUpperCase());
          
        }
        for (ObjectOutputStream otherObjOutputStream : connectedObjOutputStreamList) {
          if (otherObjOutputStream != this.clientObjOutStream) {
            otherObjOutputStream.writeObject(personReceived);
          }
        }
      }

    } catch (EOFException eofException) {
      this.connectedObjOutputStreamList.remove(this.clientObjOutStream);
      System.out.println("CERRANDO CONEXIÓN CON " + username.toUpperCase());
    } catch (IOException | ClassNotFoundException e) {
      e.printStackTrace();
    }

  }
}
