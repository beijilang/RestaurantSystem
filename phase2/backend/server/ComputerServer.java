package backend.server;

import backend.event.EventManager;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;

public class ComputerServer {
  private Socket socket;
  private ServerSocket serverSocket;
  private ArrayList<ClientThread> clients;
  private ClientThread clientThread;
  private EventManager eventManager;

  private int port;
  private boolean isRunning;

  public ComputerServer(int port) {
    this.port = port;
    this.eventManager = eventManager;

    this.isRunning = true;
    this.clients = new ArrayList<>();

    this.run();
  }

  // Accept new clients
  private void run() {
    System.out.println("Waiting for client connection...");
    try {
      this.serverSocket = new ServerSocket(this.port);
    } catch (IOException e) {
      e.printStackTrace();
    }

    while (this.isRunning) {
      try {
        // Accepting the connection and adding to the existing client threads
        Socket connectionSocket = serverSocket.accept();
        ClientThread clientThread = new ClientThread(connectionSocket);
        this.clients.add(clientThread);

      } catch (SocketException se) {
        System.err.println("*** serverSocket has closed ***");
      } catch (IOException e) {
        System.err.println("*** Error connecting client to server ***");
        e.printStackTrace();
      }

    }

    System.out.println("This server is closing");
    try {
      this.serverSocket.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}