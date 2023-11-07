package com.mycompany.myserversockett;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

class cliente extends Thread {
    private Socket clientSocket;
    private PrintWriter out;

    public cliente(Socket socket) {
        this.clientSocket = socket;
    }

    public void run() {
        try {
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            String message;
            while (true) {
                message = in.readLine();
                if (message == null || "exit".equalsIgnoreCase(message)) {
                    break;
                }
                System.out.println("Mensaje: " + message);
                MyServerSockett.broadcastMessage(message, this); // Envía el mensaje al servidor
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                out.close();
                clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void sendMessage(String message) {
        out.println(message);
    }
}