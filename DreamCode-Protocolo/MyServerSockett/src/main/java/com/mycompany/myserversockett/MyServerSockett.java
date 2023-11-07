package com.mycompany.myserversockett;
import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

public class MyServerSockett {
    private static final int PORT = 6666;
    private static CopyOnWriteArrayList<cliente> clients = new CopyOnWriteArrayList<>();

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println(" conectado, conectate a: " + PORT);
            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Nuevo usuario");
                cliente client = new cliente(clientSocket);
                clients.add(client);
                client.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void broadcastMessage(String message, cliente sender) {
        for (cliente client : clients) {
            if (client != sender) {
                client.sendMessage(message);
            }
        }
    }
}